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

    private int sampleRate = 8000;
    private int freq100 = 0;
    private short[] buffer;
    private double[] buffer_real;
    private double[] buffer_img;
    private double[] hannWindow = null;
    private int bufferSize;
    private AudioRecord audioInput;

    private int FFT_SIZE = 8192;
    private int FFT_EXP = 13;
    private int[] fft_bitreverse = null;
    private Handler messageHandler;

    public Frequency(Handler h)
    {
        messageHandler = h;
        int minSize = 2 * AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,  AudioFormat.ENCODING_PCM_16BIT);
        bufferSize = minSize < FFT_SIZE ? FFT_SIZE : minSize;
        buffer = new short[bufferSize];
        buffer_real = new double[bufferSize];
        buffer_img = new double[bufferSize];
        buildHannWindow();
        initFFT();
        audioInput = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
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
        hannWindow = new double[bufferSize];
        for (int i = 0; i < bufferSize; ++i) {
            hannWindow[i] = .5 * (1 - Math.cos(2 * Math.PI * i / (bufferSize - 1.0)));
        }
    }

    private void recordSample() {
        // keep half of the recording to make the update smoother
        System.arraycopy(buffer, bufferSize/2, buffer, 0, bufferSize/2);
        audioInput.read(buffer, bufferSize/2, bufferSize/2);
    }

    private void lowPass()
    {
        int lastInput = 0;

        for(int i = 0; i < bufferSize; ++i) {
            double output = (lastInput + buffer[i]) / 2.0;
            lastInput = buffer[i];
            buffer_real[i] = output;
            buffer_img[i] = 0.0;
        }
    }

    private void applyWindow()
    {
        for(int i = 0; i < bufferSize; ++i) {
            buffer_real[i] *= hannWindow[i];
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

        return (sampleRate * maxIndex) / (double)( FFT_SIZE );
    }

    public void run() {
        try {
            audioInput.startRecording();
            recordSample();
            while (!Thread.currentThread().isInterrupted()) {
                recordSample();
                lowPass();
                applyWindow();
                applyFFT();
                int newFreq100 = (int) (peak() * 100);

                freq100 = newFreq100;
                if (messageHandler != null) {
                    messageHandler.sendMessage(messageHandler.obtainMessage(MSG_FREQUENCY, freq100, 0));
                }
            }
        } catch (Exception e) {
            freq100 = 0;
        }

        if (messageHandler != null) {
            messageHandler.sendMessage(messageHandler.obtainMessage(MSG_FREQUENCY, 0, 0));
        }
        audioInput.stop();
    }
}
