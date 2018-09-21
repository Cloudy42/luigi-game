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

/*
 * Handles all audio in game
 * Followed this stupid tutorial: https://www.youtube.com/watch?v=VMSTTg5EEnY
 */
public class Audio {
	
	//Holds all sfx	& Music respectively
	Map<String, File> mapSFX = new HashMap<String, File>();
	Map<String, File> mapMusic = new HashMap<String, File>();	

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
	
}
