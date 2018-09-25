package dev.lepauley.luigi.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dev.lepauley.luigi.utilities.EnumMusic;

/*
 * Handles all audio in game
 * Originally followed this stupid tutorial: https://www.youtube.com/watch?v=VMSTTg5EEnY
 * but I thought it was dumb and not clear. Heck the guy sounded confused at times. So ultimately
 * went with this: https://coderanch.com/t/572997/java/Stopping-looping-javax-sound-sampled
 */
public class Audio {
	
	//Holds all sfx	& Music respectively
	Map<String, File> mapSFX = new HashMap<String, File>();
	Map<String, File> mapMusic = new HashMap<String, File>();
	
	//Tracks current Song
	private String currentSong;

	//Step 1 and 2 of setting audio (pull out CD then put CD in player, so to speak)
	private AudioInputStream audioSFX, audioMusic;
	private Clip clipSFX, clipMusic;
	
	//Constructor to get default audio loaded
	public Audio() {
		//Add all SFX and Music to hashmaps
		populateHashMaps();
		try {
	        clipSFX = AudioSystem.getClip();
	        audioSFX = AudioSystem.getAudioInputStream(mapSFX.get("Coin"));
	        clipSFX.open(audioSFX);
	        clipMusic = AudioSystem.getClip();
	        audioMusic = AudioSystem.getAudioInputStream(mapMusic.get("Running Around"));
	        clipMusic.open(audioMusic);
        }        
        catch (IOException ex){
            System.out.println("Sorry but there has been a problem reading your file.");
            ex.printStackTrace();
        }
        catch (UnsupportedAudioFileException ex1){
            System.out.println("Sorry but the audio file format you are using is not supported.");
            ex1.printStackTrace();
        }
        catch (LineUnavailableException ex2){
            System.out.println("Sorry but there are audio line problems.");
            ex2.printStackTrace();
        } 
	}
	
	//Plays SFX or Music, separate so can run both simultaneously.
	//Note: May want to consider further channel splits if we need to track enemy sound effects as well
	public void playAudio(String audioType, String s){
		try {
			if(audioType.toLowerCase().equals("sfx") && !clipSFX.isRunning()) {
				clipSFX.stop();
				clipSFX = AudioSystem.getClip();
		        audioSFX = AudioSystem.getAudioInputStream(mapSFX.get(s));
		        clipSFX.open(audioSFX);
		        clipSFX.start();
			}
			else if(audioType.toLowerCase().equals("music") && !clipMusic.isRunning()) {
				clipMusic.stop();
				clipMusic = AudioSystem.getClip();
		        audioMusic = AudioSystem.getAudioInputStream(mapMusic.get(s));
		        clipMusic.open(audioMusic);
		        clipMusic.start();
		        //Loop BGM continuously
		        clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
		        currentSong = s;
			}
        }        
        catch (IOException ex){
            System.out.println("Sorry but there has been a problem reading your file.");
            ex.printStackTrace();
        }
        catch (UnsupportedAudioFileException ex1){
            System.out.println("Sorry but the audio file format you are using is not supported.");
            ex1.printStackTrace();
        }
        catch (LineUnavailableException ex2){
            System.out.println("Sorry but there are audio line problems.");
            ex2.printStackTrace();
        } 
    }

	//Resume audio
	public void resumeAudio(String audioType){
			if(audioType.toLowerCase().equals("sfx")) {
		        clipSFX.start();
			}
			else if(audioType.toLowerCase().equals("music")) {
		        clipMusic.start();
		        //Loop BGM continuously
		        clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
			}
    }
	
	//Abruptly pauses audio
	public void pauseAudio(String audioType){
		if(audioType.toLowerCase().equals("sfx")) {
			clipSFX.stop();
		}
		if(audioType.toLowerCase().equals("music")) {
			clipMusic.stop();
		}
		if(audioType.toLowerCase().equals("all")) {
			clipSFX.stop();
			clipMusic.stop();
		}
	}
	
	//Close audio (I ran into the following error when overdoing,
	//So trying the following to see if this cleans things up. So
	//far it's working and I haven't hit the error nor the sound
	//dropping since. *fingers crossed*:
	/*
	 * Exception in thread "Thread-3" java.lang.OutOfMemoryError: Java heap space
	 * at com.sun.media.sound.DirectAudioDevice$DirectClip.open(DirectAudioDevice.java:1135)
	 * at dev.lepauley.luigi.audio.Audio.playAudio(Audio.java:69)
	 * at dev.lepauley.luigi.Game.tick(Game.java:102)
	 * at dev.lepauley.luigi.Game.run(Game.java:240)
	 * at java.lang.Thread.run(Thread.java:745)
	 */
	public void closeAudio(String audioType){
			if(audioType.toLowerCase().equals("sfx") || audioType.toLowerCase().equals("all"))
		        clipSFX.close();
			if(audioType.toLowerCase().equals("music") || audioType.toLowerCase().equals("all"))
		        clipMusic.close();			
    }
	
	//Populate Hashmaps
	public void populateHashMaps() {
		mapSFX.put("1-Up", new File("res/audio/sfx/smb_1-up.wav"));
		mapSFX.put("Bowser Falls", new File("res/audio/sfx/smb_bowserfalls.wav"));
		mapSFX.put("Bowser Fire", new File("res/audio/sfx/smb_bowserfire.wav"));
		mapSFX.put("Break Block", new File("res/audio/sfx/smb_breakblock.wav"));
		mapSFX.put("Bump", new File("res/audio/sfx/smb_bump.wav"));
		mapSFX.put("Coin", new File("res/audio/sfx/smb_coin.wav"));
		mapSFX.put("Fireball", new File("res/audio/sfx/smb_fireball.wav"));
		mapSFX.put("Fireworks", new File("res/audio/sfx/smb_fireworks.wav"));
		mapSFX.put("Flagpole", new File("res/audio/sfx/smb_flagpole.wav"));
		mapSFX.put("Game Over", new File("res/audio/sfx/smb_gameover.wav"));
		mapSFX.put("Jump (Small)", new File("res/audio/sfx/smb_jump-small.wav"));
		mapSFX.put("Jump (Big)", new File("res/audio/sfx/smb_jump-super.wav"));
		mapSFX.put("Kick", new File("res/audio/sfx/smb_kick.wav"));
		mapSFX.put("Luigi Die", new File("res/audio/sfx/smb_mariodie.wav"));
		mapSFX.put("Pause", new File("res/audio/sfx/smb_pause.wav"));
		mapSFX.put("Pipe", new File("res/audio/sfx/smb_pipe.wav"));
		mapSFX.put("Power Up", new File("res/audio/sfx/smb_powerup.wav"));
		mapSFX.put("Stage Clear", new File("res/audio/sfx/smb_stage_clear.wav"));
		mapSFX.put("Stomp", new File("res/audio/sfx/smb_stomp.wav"));
		mapSFX.put("Vine", new File("res/audio/sfx/smb_vine.wav"));
		mapSFX.put("Warning", new File("res/audio/sfx/smb_warning.wav"));
		mapSFX.put("World Clear", new File("res/audio/sfx/smb_world_clear.wav"));

		//Holds all music - Note: I need to convert mp3's to wav's so am temporarily using SFX
		mapMusic.put("Running Around", new File("res/audio/music/01 - Running About.wav"));
		mapMusic.put("Running Around (Hurry!)", new File("res/audio/music/02 - Running About (Hurry!).wav"));
		mapMusic.put("Underground", new File("res/audio/music/03 - Underground.wav"));
		mapMusic.put("Underground (Hurry!)", new File("res/audio/music/04 - Underground (Hurry!).wav"));
		mapMusic.put("Swimming Around", new File("res/audio/music/05 - Swimming Around.wav"));
		mapMusic.put("Swimming Around (Hurry!)", new File("res/audio/music/06 - Swimming Around (Hurry!).wav"));
		mapMusic.put("Bowser's Castle", new File("res/audio/music/07 - Bowser's Castle.wav"));
		mapMusic.put("Bowser's Castle (Hurry!)", new File("res/audio/music/08 - Bowser's Castle (Hurry!).wav"));
		mapMusic.put("Invincible", new File("res/audio/music/09 - Invincible.wav"));
		mapMusic.put("Invincible (Hurry!)", new File("res/audio/music/10 - Invincible (Hurry!).wav"));
		mapMusic.put("Level Complete", new File("res/audio/music/11 - Level Complete-small.wav"));
		mapMusic.put("Bowser's Castle Complete", new File("res/audio/music/12 - Bowser's Castle Complete.wav"));
		mapMusic.put("You Have Died", new File("res/audio/music/13 - You Have Died.wav"));
		mapMusic.put("Game Over", new File("res/audio/music/14 - Game Over.wav"));
		mapMusic.put("Game Over (Alternative)", new File("res/audio/music/15 - Game Over (Alternative).wav"));
		mapMusic.put("Into The Pipe", new File("res/audio/music/16 - Into The Pipe.wav"));
		mapMusic.put("Into The Pipe (Hurry!)", new File("res/audio/music/17 - Into The Pipe (Hurry!).wav"));
		mapMusic.put("Saved The Princess", new File("res/audio/music/18 - Saved The Princess.wav"));
	}
	
	//Cycles through songs, just to shake it up when debugging
	public void nextSong() {
		closeAudio("music");
		if(currentSong.equals(EnumMusic.RunningAround.toString())) playAudio("music", EnumMusic.Underground.toString());
		else if(currentSong.equals(EnumMusic.RunningAround_Hurry.toString())) playAudio("music", EnumMusic.Underground_Hurry.toString());
		else if(currentSong.equals(EnumMusic.Underground.toString())) playAudio("music", EnumMusic.SwimmingAround.toString());
		else if(currentSong.equals(EnumMusic.Underground_Hurry.toString())) playAudio("music", EnumMusic.SwimmingAround_Hurry.toString());
		else if(currentSong.equals(EnumMusic.SwimmingAround.toString())) playAudio("music", EnumMusic.BowserCastle.toString());
		else if(currentSong.equals(EnumMusic.SwimmingAround_Hurry.toString())) playAudio("music", EnumMusic.BowserCastle_Hurry.toString());
		else if(currentSong.equals(EnumMusic.BowserCastle.toString())) playAudio("music", EnumMusic.Invincible.toString());
		else if(currentSong.equals(EnumMusic.BowserCastle_Hurry.toString())) playAudio("music", EnumMusic.Invincible_Hurry.toString());
		else if(currentSong.equals(EnumMusic.Invincible.toString())) playAudio("music", EnumMusic.IntoThePipe.toString());
		else if(currentSong.equals(EnumMusic.Invincible_Hurry.toString())) playAudio("music", EnumMusic.IntoThePipe_Hurry.toString());
		else if(currentSong.equals(EnumMusic.IntoThePipe.toString())) playAudio("music", EnumMusic.RunningAround.toString());
		else if(currentSong.equals(EnumMusic.IntoThePipe_Hurry.toString())) playAudio("music", EnumMusic.RunningAround_Hurry.toString());
	}

	/*************** GETTERS and SETTERS ***************/
	
	public String getCurrentSong() {
		return currentSong;
	}
}
