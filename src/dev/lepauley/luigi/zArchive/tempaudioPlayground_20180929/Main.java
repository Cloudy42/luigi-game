package dev.lepauley.luigi.zArchive.tempaudioPlayground_20180929;

import javax.sound.sampled.LineUnavailableException;

import dev.lepauley.luigi.zArchive.tempaudioPlayground_20180929.Player;

import java.net.MalformedURLException;
import java.net.URL;

//All of this code came from here: https://stackoverflow.com/questions/32724143/unsupported-control-type-sample-rate/52572811#52572811

public class Main {
@SuppressWarnings("static-access")
public static void main(String[] args) throws MalformedURLException, LineUnavailableException, InterruptedException {
    URL url = ClassLoader.getSystemClassLoader().getSystemResource("res/audio/music/05 - Swimming Around.wav");
    Player player = new Player(url);
    player.play();
    Thread.sleep(2000);
    for(int i=0;i<60;i++) {
        player.setPlayRate(1f+(float)(i)*0.1f);
        System.out.println(1f+(float)(i)*0.1f + "x");
        Thread.sleep(1000);
    }
    player.stop();
}
}