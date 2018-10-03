package dev.lepauley.luigi.zArchive.zTempAudioPlayground_20180929;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Player implements Playable {
 private AudioFormat format;
 private SourceDataLine sourceDataLine;
 private DataLine.Info info;
 private final URL soundUrl;
 private final boolean[] stopped;
 private float playRate;
 private int bytesWritten;
 private int bytesIgnorred;

public Player(URL soundUrl) throws LineUnavailableException, MalformedURLException {
    this.format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100, 16, 2 , 4, 44100,false);
    this.info = new DataLine.Info(SourceDataLine.class, format);
    this.sourceDataLine =  (SourceDataLine) AudioSystem.getLine(info);

    this.soundUrl = soundUrl;
    this.stopped = new boolean[1];
    this.stopped[0] = false;
    this.playRate = 1;
    this.bytesWritten =0;
    this.bytesIgnorred = 0;
}
@Override
public boolean play() throws LineUnavailableException {
    sourceDataLine.open();
    final Thread playerThread = new Thread(){

        @Override
        public void run() {
            int numberOfBitesRead = 0;
            AudioInputStream auis = null;
            try {
                auis = AudioSystem.getAudioInputStream(soundUrl);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[4];
            sourceDataLine.start();
            try {
                auis.mark(auis.available());

            while(!stopped[0]) {

                numberOfBitesRead = auis.read(bytes);
                if(numberOfBitesRead == -1) {
                    auis.reset();
                }
                if(bytesWritten < (10)) {
                    sourceDataLine.write(bytes, 0, bytes.length);
                    bytesWritten+=4;
                } else {
                    if(bytesIgnorred < ((int)(playRate*10-10))) {
                        bytesIgnorred+=4;
                    } else {
                        bytesWritten =0;
                        bytesIgnorred = 0;
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    playerThread.start();

    return false;
}

@Override
public boolean stop() {
    this.stopped[0] = true;
    this.sourceDataLine.stop();
    this.sourceDataLine.close();
    return false;
}

@Override
public boolean setPlayRate(float playRate) {
    this.playRate = playRate;
    return false;
}
}