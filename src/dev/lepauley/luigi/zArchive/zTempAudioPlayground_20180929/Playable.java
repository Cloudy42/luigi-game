
package dev.lepauley.luigi.zArchive.zTempAudioPlayground_20180929;

import javax.sound.sampled.LineUnavailableException;

public interface Playable {
public boolean play() throws LineUnavailableException;
public boolean stop();
public boolean setPlayRate(float playRate);
}