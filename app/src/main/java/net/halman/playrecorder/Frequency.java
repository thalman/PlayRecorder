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

    private int sample_rate = 8000;
    private short[] buffer;
    private double[] buffer_real;
    private double[] buffer_img;
    private double[] hann_window = null;
    private int bufferSize;
    private AudioRecord audioInput;

    private int FFT_SIZE = 8192;
    private int FFT_EXP = 13;
    private int[] fft_bitreverse = null;
    private Handler messageHandler;
    int buffer_recording_step = 1;
    int buffer_recording_step_max = 4;

    public Frequency(Handler h)
    {
        messageHandler = h;
        int minSize = AudioRecord.getMinBufferSize(sample_rate, AudioFormat.CHANNEL_IN_MONO,  AudioFormat.ENCODING_PCM_16BIT);
        buffer_recording_step_max = FFT_SIZE / minSize;
        if (buffer_recording_step_max > 4) {
            buffer_recording_step_max = 4;
        }

        if (buffer_recording_step_max < 1) {
            buffer_recording_step_max = 1;
        }

        bufferSize = minSize < FFT_SIZE ? FFT_SIZE : minSize;
        buffer = new short[bufferSize];
        buffer_real = new double[bufferSize];
        buffer_img = new double[bufferSize];
        buildHannWindow();
        initFFT();
        audioInput = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
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
        hann_window = new double[bufferSize];
        for (int i = 0; i < bufferSize; ++i) {
            hann_window[i] = .5 * (1 - Math.cos(2 * Math.PI * i / (bufferSize - 1.0)));
        }
    }

    private void recordSample() {
        switch(buffer_recording_step) {
            default:
            case 1:
                audioInput.read(buffer, 0, bufferSize);
                break;
            case 2:
                System.arraycopy(buffer, bufferSize / 2, buffer, 0, bufferSize / 2);
                audioInput.read(buffer, bufferSize / 2, bufferSize / 2);
                break;
            case 4:
                System.arraycopy(buffer, bufferSize / 4, buffer, 0, bufferSize * 3 / 4);
                audioInput.read(buffer, bufferSize * 3 / 4, bufferSize / 4);
                break;
        }
    }

    private void prepareBuffers(boolean last_quarter)
    {
        Arrays.fill(buffer_img, 0.0);
        Arrays.fill(buffer_real, 0.0);

        int idx = 0;
        if (last_quarter) {
            idx = bufferSize * 3 / 4;
        }

        for(int i = idx; i < bufferSize; ++i) {
            buffer_real[i] = buffer[i];
        }
    }

    private void lowPass()
    {
        double lastInput = 0;

        for(int i = 0; i < bufferSize; ++i) {
            double output = (lastInput + buffer_real[i]) / 2.0;
            lastInput = buffer_real[i];
            buffer_real[i] = output;
        }
    }

    private void applyWindow()
    {
        for(int i = 0; i < bufferSize; ++i) {
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

        return (sample_rate * maxIndex) / (double)( FFT_SIZE );
    }

    public void run() {
        long time_elapsed;
        int freq100;
        int freq100_low_precision;

        try {
            audioInput.startRecording();
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

                if (messageHandler != null) {
                    messageHandler.sendMessage(messageHandler.obtainMessage(MSG_FREQUENCY, freq100, freq100_low_precision));
                }
            }
        } catch (Exception e) {
        }

        if (messageHandler != null) {
            messageHandler.sendMessage(messageHandler.obtainMessage(MSG_FREQUENCY, 0, 0));
        }
        audioInput.stop();
    }
}
