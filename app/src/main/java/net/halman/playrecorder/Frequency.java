/*
 * This file is part of PlayRecorder.
 *
 * PlayRecorder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlayRecorder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.halman.playrecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;

/*
 * This code detects sound frequency (AKA pitch detection) using fast Fourier transformation
 * Credit to the other sources and authors
 *
 * android example of recorder https://stackoverflow.com/questions/8499042/android-audiorecord-example
 * pitch detection blog http://blog.bjornroche.com/2012/07/frequency-detection-using-fft-aka-pitch.html
 * c source for article above https://github.com/bejayoharen/guitartuner/blob/master/src/main.c
 */

public class Frequency implements Runnable {
    public static final int MSG_FREQUENCY = 1;

    private final int sample_rate = 8000;
    private final int FFT_SIZE = 8192;
    private final int FFT_EXP = 13;
    private short[] buffer;
    private double[] buffer_real;
    private double[] buffer_img;
    private double[] hann_window = null;
    private int buffer_size;
    private AudioRecord audio_input;

    private int[] fft_bitreverse = null;
    private Handler message_handler;
    private int buffer_recording_step = 1;

    public Frequency(Handler h, int highest_frequency_100)
    {
        message_handler = h;
        int minSize = 4 * AudioRecord.getMinBufferSize(sample_rate, AudioFormat.CHANNEL_IN_MONO,  AudioFormat.ENCODING_PCM_16BIT);

        buffer_size = minSize < FFT_SIZE ? FFT_SIZE : minSize;
        buffer = new short[buffer_size];
        buffer_real = new double[buffer_size];
        buffer_img = new double[buffer_size];
        buildHannWindow();
        initFFT();
        computeSecondOrderLowPassParameters(highest_frequency_100 / 100.0);
        audio_input = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffer_size);
    }

    private void initFFT()
    {
        fft_bitreverse = new int[FFT_SIZE];

        for (int i = (1 << FFT_EXP) - 1; i >= 0; --i) {
            int k = 0;
            for (int j = 0; j < FFT_EXP; ++j) {
                k *= 2;
                if ((i & ( 1 << j )) != 0) {
                    k += 1;
                }
            }
            fft_bitreverse[i] = k;
        }
    }

    private void applyFFT()
    {

        int n, n2, i, k, kn2, l, p;
        double ang, s, c, tr, ti;
        for(i = 0; i < FFT_SIZE; ++i) {
            buffer_img[i] = 0.0;
        }

        n = 1 << FFT_EXP;
        n2 = n / 2;


        for (l = 0; l < FFT_EXP; ++l) {
            for (k = 0; k < n; k += n2) {
                for(i = 0; i < n2; ++i, ++k) {
                    p = fft_bitreverse[k / n2];
                    ang = Math.PI * 2 * p / n;
                    c = Math.cos(ang);
                    s = Math.sin(ang);

                    kn2 = k + n2;
                    // if (inversion_transformation) { s = -s; }

                    tr = buffer_real[kn2] * c + buffer_img[kn2] * s;
                    ti = buffer_img[kn2] * c - buffer_real[kn2] * s;
                    buffer_real[kn2] = buffer_real[k] - tr;
                    buffer_img[kn2] = buffer_img[k] - ti;
                    buffer_real[k] += tr;
                    buffer_img[k] += ti;
                }
            }
            n2 /= 2;
        }

        for ( k = 0; k < n; ++k ) {
            i = fft_bitreverse[k];
            if ( i <= k )
                continue;
            tr = buffer_real[k];
            ti = buffer_img[k];
            buffer_real[k] = buffer_real[i];
            buffer_img[k] = buffer_img[i];
            buffer_real[i] = tr;
            buffer_img[i] = ti;
        }

        // Finally, multiply each value by 1/n, if this is the forward transform.
        // if (! inversion_transformation)
        {
            double f;

            f = 1.0 / n;
            for( i = 0; i < n ; ++i ) {
                buffer_real[i] *= f;
                buffer_img[i] *= f;
            }
        }
    }

    private void buildHannWindow()
    {
        hann_window = new double[buffer_size];
        for (int i = 0; i < buffer_size; ++i) {
            hann_window[i] = .5 * (1 - Math.cos(2 * Math.PI * i / (buffer_size - 1.0)));
        }
    }

    private void recordSample() {
        switch(buffer_recording_step) {
            default:
            case 1:
                audio_input.read(buffer, 0, buffer_size);
                break;
            case 2:
                System.arraycopy(buffer, buffer_size / 2, buffer, 0, buffer_size / 2);
                audio_input.read(buffer, buffer_size / 2, buffer_size / 2);
                break;
            case 4:
                System.arraycopy(buffer, buffer_size / 4, buffer, 0, buffer_size * 3 / 4);
                audio_input.read(buffer, buffer_size * 3 / 4, buffer_size / 4);
                break;
        }
    }

    private void prepareBuffers(boolean last_quarter)
    {
        Arrays.fill(buffer_img, 0.0);
        Arrays.fill(buffer_real, 0.0);

        int idx = 0;
        if (last_quarter) {
            idx = buffer_size * 3 / 4;
        }

        for(int i = idx; i < buffer_size; ++i) {
            buffer_real[i] = buffer[i];
        }
    }

    private double[] low_pass_parameter_a = {0, 0};
    private double[] low_pass_parameter_b = {0, 0, 0};

    private void computeSecondOrderLowPassParameters(double highest_freq)
    {
        double a0;
        double w0 = 2 * Math.PI * highest_freq / sample_rate;
        double cosw0 = Math.cos(w0);
        double sinw0 = Math.sin(w0);
        //double alpha = sinw0/2;
        double alpha = sinw0 / 2 * Math.sqrt(2);

        a0   = 1 + alpha;
        low_pass_parameter_a[0] = (-2 * cosw0) / a0;
        low_pass_parameter_a[1] = (1 - alpha) / a0;
        low_pass_parameter_b[0] = ((1 - cosw0) / 2) / a0;
        low_pass_parameter_b[1] = (1 - cosw0) / a0;
        low_pass_parameter_b[2] = low_pass_parameter_b[0];
    }

    private double processSecondOrderFilter(double x, double[] mem)
    {
        double ret = low_pass_parameter_b[0] * x + low_pass_parameter_b[1] * mem[0] + low_pass_parameter_b[2] * mem[1]
                - low_pass_parameter_a[0] * mem[2] - low_pass_parameter_a[1] * mem[3] ;

        mem[1] = mem[0];
        mem[0] = x;
        mem[3] = mem[2];
        mem[2] = ret;

        return ret;
    }

    private void lowPass()
    {
        double[] mem1 = {0, 0, 0, 0};
        double[] mem2 = {0, 0, 0, 0};

        for(int i = 0; i < buffer_size; ++i) {
            buffer_real[i] = processSecondOrderFilter(buffer_real[i], mem1);
            buffer_real[i] = processSecondOrderFilter(buffer_real[i], mem2);
        }
    }

    private void applyWindow()
    {
        for(int i = 0; i < buffer_size; ++i) {
            buffer_real[i] *= hann_window[i];
        }
    }

    private double peak()
    {
        //find the peak
        double maxVal = -1;
        int maxIndex = -1;
        for( int j=0; j<FFT_SIZE/2; ++j ) {
            double v = buffer_real[j] * buffer_real[j] + buffer_img[j] * buffer_img[j] ;
            if( v > maxVal ) {
                maxVal = v;
                maxIndex = j;
            }
        }

        Log.d("FREQUENCY", "strength " + maxVal);
        if (maxVal < 100) {
            // filter out very week signal
            return 0;
        }

        return (sample_rate * maxIndex) / (double)( FFT_SIZE );
    }

    public void run() {
        long time_elapsed;
        int freq100;
        int freq100_low_precision;

        try {
            audio_input.startRecording();
            while (!Thread.currentThread().isInterrupted()) {
                recordSample();
                time_elapsed = System.currentTimeMillis();

                // calculate FFT on whole 1second sample to get good precision for tunning
                prepareBuffers(false);
                lowPass();
                applyWindow();
                applyFFT();
                freq100 = (int) (peak() * 100);

                // calculate FFT on last 0.25second sample to get better reaction on sound change
                prepareBuffers(true);
                lowPass();
                applyWindow();
                applyFFT();
                freq100_low_precision = (int) (peak() * 100);
                time_elapsed = System.currentTimeMillis() - time_elapsed;

                // check whether we can calculate FFT 4 x per second
                if (time_elapsed < 100) {
                    buffer_recording_step = 4;
                } else if (time_elapsed < 200) {
                    buffer_recording_step = 2;
                } else {
                    buffer_recording_step = 1;
                }
                Log.d("FREQUENCY","freq100 " + freq100 + " freq100lp " + freq100_low_precision);
                Log.d("FREQUENCY","calculation time " + time_elapsed);

                if (message_handler != null) {
                    message_handler.sendMessage(message_handler.obtainMessage(MSG_FREQUENCY, freq100, freq100_low_precision));
                }
            }
        } catch (Exception e) {
        }

        if (message_handler != null) {
            message_handler.sendMessage(message_handler.obtainMessage(MSG_FREQUENCY, 0, 0));
        }
        audio_input.stop();
    }
}
