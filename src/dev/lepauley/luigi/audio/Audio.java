package dev.lepauley.luigi.audio;

import java.io.*;
import javax.sound.sampled.*;

/*
 * Handles all audio in game
 * Followed this stupid tutorial: https://www.youtube.com/watch?v=VMSTTg5EEnY
 */
public class Audio {
	
	//Holds all sfx
	private File [] fileSFX = { new File("res/audio/sfx/smb_1-up.wav")
			                  , new File("res/audio/sfx/smb_bowserfalls.wav")
			                  , new File("res/audio/sfx/smb_bowserfire.wav")
			                  , new File("res/audio/sfx/smb_breakblock.wav")
			                  , new File("res/audio/sfx/smb_bump.wav")
			                  , new File("res/audio/sfx/smb_coin.wav")
			                  , new File("res/audio/sfx/smb_fireball.wav")
			                  , new File("res/audio/sfx/smb_fireworks.wav")
			                  , new File("res/audio/sfx/smb_flagpole.wav")
			                  , new File("res/audio/sfx/smb_gameover.wav")
			                  , new File("res/audio/sfx/smb_jump-small.wav")
			                  , new File("res/audio/sfx/smb_jump-super.wav")
			                  , new File("res/audio/sfx/smb_kick.wav")
			                  , new File("res/audio/sfx/smb_mariodie.wav")
			                  , new File("res/audio/sfx/smb_pause.wav")
			                  , new File("res/audio/sfx/smb_pipe.wav")
			                  , new File("res/audio/sfx/smb_powerup.wav")
			                  , new File("res/audio/sfx/smb_stage_clear.wav")
			                  , new File("res/audio/sfx/smb_stomp.wav")
			                  , new File("res/audio/sfx/smb_vine.wav")
			                  , new File("res/audio/sfx/smb_warning.wav")
			                  , new File("res/audio/sfx/smb_world_clear.wav")};

	//Holds all music - Note: I need to convert mp3's to wav's so am temporarily using SFX
	private File [] fileMusic = {  new File("res/audio/sfx/smb_1-up.wav")
					             , new File("res/audio/sfx/smb_bowserfalls.wav")
					             , new File("res/audio/sfx/smb_bowserfire.wav")
					             , new File("res/audio/sfx/smb_breakblock.wav")
					             , new File("res/audio/sfx/smb_bump.wav")
					             , new File("res/audio/sfx/smb_coin.wav")
					             , new File("res/audio/sfx/smb_fireball.wav")
					             , new File("res/audio/sfx/smb_fireworks.wav")
					             , new File("res/audio/sfx/smb_flagpole.wav")
					             , new File("res/audio/sfx/smb_gameover.wav")
					             , new File("res/audio/sfx/smb_jump-small.wav")
					             , new File("res/audio/sfx/smb_jump-super.wav")
					             , new File("res/audio/sfx/smb_kick.wav")
					             , new File("res/audio/sfx/smb_mariodie.wav")
					             , new File("res/audio/sfx/smb_pause.wav")
					             , new File("res/audio/sfx/smb_pipe.wav")
					             , new File("res/audio/sfx/smb_powerup.wav")
					             , new File("res/audio/sfx/smb_stage_clear.wav")
					             , new File("res/audio/sfx/smb_stomp.wav")
					             , new File("res/audio/sfx/smb_vine.wav")
					             , new File("res/audio/sfx/smb_warning.wav")
					             , new File("res/audio/sfx/smb_world_clear.wav")
					             , new File("res/audio/music/1 - Running About.mp3")};

	//Step 1 and 2 of setting audio (pull out CD then put CD in player, so to speak)
	private AudioInputStream audioSFX, audioMusic;
	private Clip clipSFX, clipMusic;
	
	//Constructor to get default audio loaded
	public Audio() {
		try {
	        clipSFX = AudioSystem.getClip();
	        audioSFX = AudioSystem.getAudioInputStream(fileSFX[0]);
	        clipSFX.open(audioSFX);
	        clipMusic = AudioSystem.getClip();
	        audioMusic = AudioSystem.getAudioInputStream(fileMusic[0]);
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
	public void playAudio(String audioType, int i){
		try {
			if(audioType.toLowerCase().equals("sfx") && !clipSFX.isRunning()) {
				clipSFX.stop();
				clipSFX = AudioSystem.getClip();
		        audioSFX = AudioSystem.getAudioInputStream(fileSFX[i]);
		        clipSFX.open(audioSFX);
		        clipSFX.start();
			}
			else if(audioType.toLowerCase().equals("music") && !clipMusic.isRunning()) {
				clipMusic.stop();
				clipMusic = AudioSystem.getClip();
		        audioMusic = AudioSystem.getAudioInputStream(fileMusic[i]);
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
	
}
