package dev.lepauley.luigi.zArchive.tempaudioPlayground_20180929;

import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.util.Date;
//https://stackoverflow.com/questions/5760128/increase-playback-speed-of-sound-file-in-java
public class AcceleratedPlayback {

    public static void main(String[] args) throws Exception {
        int playBackSpeed = 2;
        System.out.println("Playback Rate: " + playBackSpeed);

        //URL url = new URL("http://www.talkingwav.com/wp-content/uploads/2017/10/mario_01.wav");
        //File url = new File("res/audio/music/05 - Swimming Around.wav");
        File url = new File("res/audio/sfx/smb_powerup.wav");
        System.out.println("URL: " + url);
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        AudioFormat af = ais.getFormat();

        int frameSize = af.getFrameSize();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[2^16];
        int read = 1;
        while( read>-1 ) {
            read = ais.read(b);
            if (read>0) {
                baos.write(b, 0, read);
            }
        }
        System.out.println("End entire: \t" + new Date());
        byte[] b1 = baos.toByteArray();
        byte[] b2 = new byte[b1.length/frameSize];
        /*
        for (int ii=0; ii<b2.length/frameSize; ii++) {
            for (int jj=0; jj<frameSize; jj++) {
                b2[(ii*frameSize)+jj] = b1[(ii*frameSize*playBackSpeed)+jj];
            }
        }
*/
        System.out.println("End sub-sample: \t" + new Date());
        System.out.println("b2.length: " + b2.length);
        System.out.println("frameSize: " + frameSize);
        System.out.println("b2.length/frameSize: " + b2.length/frameSize);
        System.out.println("21811 * 10905 = " + 21811 * 10905);

        for (int x=0; x<b2.length/frameSize; x++) {
            for (int y=0; y<frameSize; y++) {
                for (int z=0; z<2; z++) {
	         //       System.out.println("x: " + x + " | frameSize: " + frameSize + " | y: " + y + " | (x*frameSize)+y: " + (x*frameSize)+y);
	         //       System.out.println("x: " + x + " | frameSize: " + frameSize + " | playBackSpeed: " + playBackSpeed + " | y: " + y + " | (x*frameSize*playBackSpeed)+y: " + (x*frameSize*playBackSpeed)+y);
	                b2[(x*frameSize)+y] = b1[(x*frameSize*playBackSpeed)+y];
                }
            }
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(b2);
        AudioInputStream aisAccelerated = new AudioInputStream(bais, af, b2.length);
        Clip clip = AudioSystem.getClip();
        clip.open(aisAccelerated);
        clip.loop(5);
        clip.start();

        JOptionPane.showMessageDialog(null, "Exit?");
    }
}