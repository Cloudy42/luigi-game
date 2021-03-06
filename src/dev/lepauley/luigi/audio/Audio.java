package dev.lepauley.luigi.audio;

import static dev.lepauley.luigi.utilities.Utilities.print;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.utilities.EnumMusic;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.Utilities;


/*
 * Handles all audio in game
 */
public class Audio {
	
	//Holds all SFX	& Music paths in a user-friendly easy to read way
	//For example. If we type mapSFX.Coin it will pull the file path for the Coin sound effect ("res/audio/sfx/smb_coin.wav") 
	private Map<String, File> mapSFX = new HashMap<String, File>()
	                        , mapMusic = new HashMap<String, File>();
	
	//Tracks current SFX/Song
	private String currentSFX
	             , currentMusic;
	
	//Sets Min and Max volume
	//Note: 0 = muted | negative volumes seem to not matter and are treated like positive? I didn't have min and it was crazy before.
	public final float MIN_VOLUME = 0.0f  
					  , MAX_VOLUME = 4.0f;
	
	//Sets Min and Max audio speed (How fast/slow the song/SFX can play):
	//STOP_SPEED_ADJUST = How much speed is gained/lost with "Stop" Mode
	public final float MIN_SPEED = -4.0f  
					  , MAX_SPEED =  4.0f
					  , STOP_SPEED_ADJUST = 0.25f;
	
	//Sets Min and Max audio pitch
	public final float MIN_PITCH = 0.3f  
					  , MAX_PITCH =  4.0f;

	//Sets Min and Max audio rate
	public final float MIN_RATE = 0.4f  
					  , MAX_RATE =  4.0f;

	//Threads for individual audio channels
	//Needed for simultaneous audio using audio manipulation because it takes a lot of processing to simultaneously read & write audio.
	private Thread sfxThread = null
			     , musicThread = null;

	//Needed for threads (but don't need a unique one per thread? Dunno. A bit of an enigma to me tbh but is currently working with one)
	private Runnable runnable = null;
	
	//Tracks whether a Thread is currently Running or not (helps so we don't step over toes or have null pointer errors)
	private boolean sfxThreadRunning = false
			      , musicThreadRunning = false;
	
	//current audio variables
	//Note: Player is usually able to raise/lower volume for SFX vs. music separately, hence why separate here (other audio modifiers are shared)
	//Haven't touched Pitch or Rate yet to know what those are/if we want to manipulate them. I think we also can adjust echo?
	private float currentVolumeSFX
			    , currentVolumeMusic
                , currentSpeed
                , currentPitch
                , currentRate;
	
	//Sets all default audio values
	public final float DEFAULT_CURRENT_VOLUME_SFX = 1.0f
			         , DEFAULT_CURRENT_VOLUME_MUSIC = 1.0f
			         , DEFAULT_CURRENT_SPEED = 1.0f
			         , DEFAULT_CURRENT_PITCH = 1.0f
			         , DEFAULT_CURRENT_RATE = 1.0f; 
	
	//Tracks how many seconds (and subSeconds) have elapsed in song 
	//(Needed for pausing and resuming, but more importantly, for transitioning between audio as we manipulate it, otherwise starts over every time):
	private int secondsToSkip, subSecondsToSkip;
	
	//Constructor to set defaults, get default audio loaded to hashmaps, and loads up audio to gets threads going
	public Audio(){

		//Sets default audio values for variables
		resetDefaults();

		//Add all SFX and Music to hashmaps
		populateHashMaps();

		//Sets default audio for each audioType (just to get audio loaded to start Threads)
		currentSFX = EnumSFX.Coin.toString();
		currentMusic = EnumMusic.RunningAbout.toString();

		//Starts up some audio to get the ball rolling
		playAudioStagingArea("SFX", EnumSFX.Coin.toString());
		playAudioStagingArea("MUSIC", EnumMusic.RunningAbout.toString());

		//Pauses audio threads that were just created (doesn't destroy them)
		//Note: Only pausing "MUSIC" since I like the "boot up" coin sound in "SFX". Reminds me of the chime from some old games when booting up.
		pauseAudioStagingArea("MUSIC");
	}
	
	//Used as a staging Area for playing Audio so the "ugly" try/catch code only needs to go there, not in general code
	public void playAudioStagingArea(String audioType, String targetAudio) {
		try {
			playAudio(audioType, targetAudio);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Plays SFX or Music, separate so can run both simultaneously.
	//Note: May want to consider further channel splits if we need to track enemy sound effects as well
	 //Integrates AudioManipulation (custom git code that allows manipulation of audio) with our SFX and Music.
	 //Plays audio at end in separate thread
	//Note: @@SuppressWarnings("deprecation") related to the Thread.stop() methods. Highlight methods to read about deprecation.
	 @SuppressWarnings("deprecation")
	public void playAudio(String audioType, String targetAudio) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException
	 {
		 boolean emulateChordPitch = true;
         int quality = 10;
         
         //Tracks whether audio about to be played is new or not 
         //(used purely for debug purposes atm not wanting to keep displaying same song with all audio modifications)
         boolean newAudio = false;
         
        //Pauses current Audio
        pauseAudioStagingArea(audioType);
        
        //Loads current Song
        AudioInputStream stream;
        
        //Based on audioType, populated map[SFX/Music] with targetAudio
        if(audioType.equals("SFX")) {
        	stream = AudioSystem.getAudioInputStream(mapSFX.get(targetAudio));
        } else if(audioType.equals("MUSIC")) {
        	stream = AudioSystem.getAudioInputStream(mapMusic.get(targetAudio));
        } else {
        	stream = null;
	    	 print("Error! AudioType doesn't match in playAudio!");
        } 
            
        //Music-related code (from online)
        AudioFormat format = stream.getFormat();
        int sampleRate = (int)format.getSampleRate();
        int numChannels = format.getChannels(); 
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format,((int)stream.getFrameLength()*format.getFrameSize()));
        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);

	    //Find out how many bytes you have to skip, this depends on bytes per frame (a.k.a. frameSize). Seems to be working pretty good! Yay math!
        //Note: I'm actually not sure how much it even matters/would come into play during the game and is likely just a debugging 
        //      issue to get it "audio perfect" but alas, I decided to tackle it anyways. IF it's off right now (hard to
        //      tell), then it's BARELY off so I'm happy with how it is. 
        //Calculation: basically takes the base calculation times 1 = the amount it would increase each second
        //             Then divides that by current FPS to get how much should change per frame
        //             Then multiplies that value by however many subseconds (frames) have elapsed since last full second increase
        long subSecondCalculation = (format.getFrameSize() * ((int)format.getFrameRate()) * 1) / GVar.FPS * subSecondsToSkip;
    	long bytesToSkip = format.getFrameSize() * ((int)format.getFrameRate()) * secondsToSkip + subSecondCalculation;
    	//Note: This is the original when I wasn't using subSeconds. Definitely can tell it sounds spottier
    	//bytesToSkip = format.getFrameSize() * ((int)format.getFrameRate()) * secondsToSkip;
        //print("bytesToSkip: " + bytesToSkip + " | SubSeconds: " + subSecondsToSkip + " | subSecondCalculation: " + subSecondCalculation);
        
        //Checks if sfxThread is currently Running. If so, stop it:
        if(sfxThreadRunning && audioType.equals("SFX")) {
			 sfxThread.stop();
			 sfxThreadRunning = false;
		 }

        //Checks if musicThread is currently Running. If so, stop it:
        if(musicThreadRunning && audioType.equals("MUSIC")) {
			 musicThread.stop();
			 musicThreadRunning = false;
		 }
        
        //Opens line (audio) and starts (plays) it
        line.open(stream.getFormat());
        line.start();
		 
        //Creates thread so all processing isn't waiting for this and halting all other code
        if(audioType.equals("SFX")) {
        	sfxThread = new Thread(runnable) {
			    @Override
			    public void run() {
					try {
						//Runs AudioManipulation Code
					    runAudioManipulation(stream, line, emulateChordPitch, quality, sampleRate, numChannels, audioType, targetAudio, bytesToSkip, stream);
						} catch(Exception ex) { ex.printStackTrace(); }
				}
			};
			
			//Starts thread
			sfxThread.start();
			
			//Helper to let us know thread is running
			sfxThreadRunning = true;

        	//Checks if audioType = SFX and currentSFX audio is NOT equal to audio about to play, then this is a new SFX.
	         //(used purely for debug purposes atm not wanting to keep displaying same song with all audio modifications)
			if(audioType.equals("SFX") && !currentSFX.equals(targetAudio)) {
        		newAudio = true;
			}

        	//Sets current SFX
			setCurrentSFX(targetAudio);

        	//Displays current SFX played if in Debug Mode AND it's a new SFX
        	if(GVar.getDebug() && newAudio)
        		print("currentSFX: " + currentSFX); 			
        }

        //Creates thread so all processing isn't waiting for this and halting all other code
        if(audioType.equals("MUSIC")) {
        	musicThread = new Thread(runnable) {
			    @Override
			    public void run() {
					try {
						//Runs AudioManipulation Code
						runAudioManipulation(stream, line, emulateChordPitch, quality, sampleRate, numChannels, audioType, targetAudio, bytesToSkip, stream);
						} catch(Exception ex) { ex.printStackTrace(); }
				}
			};
			
			//Starts thread
			musicThread.start();
			
			//Helper to let us know thread is running
			musicThreadRunning = true;
        	//Checks if audioType = Music and currentMusic audio is NOT equal to audio about to play, then this is a new Music.
	         //(used purely for debug purposes atm not wanting to keep displaying same song with all audio modifications)
			if(audioType.equals("MUSIC") && !currentMusic.equals(targetAudio))
				newAudio = true;

			//Sets current Music
			setCurrentMusic(targetAudio);
			
        	//Displays current Music played if in Debug Mode AND it's a new Song
        	if(GVar.getDebug() && newAudio)
        		print("currentMusic: " + currentMusic); 			
        }

        //Every time audio is adjusted, I have it update the settings file so that upon reloading, it's still saved
        //Note: Surrounded with try-catch since this isn't initialized at the start of the game.
        if(Game.getLoaded()) {
	         try {
				Utilities.writeSettingsFile();
			} catch (Exception e) {
				//Commenting out since not initialized at beginning of game
				e.printStackTrace();
			}
        }
        
	 }

	//Run sonic: Audio manipulation
	//Original Source: https://github.com/waywardgeek/sonic
	//I didn't write code, so won't be commenting it other than the changes I made. Otherwise, untouched.
	 private void runAudioManipulation(
	     AudioInputStream audioStream
	     ,SourceDataLine line
	     ,boolean emulateChordPitch
	     ,int quality
	     ,int sampleRate
	     ,int numChannels
	     
	     //Added these 3 variables because:
	     //audioType is needed to differentiate SFX vs. Music, and 
	     //the other two needed to start audio x seconds into file
	     ,String audioType
	     ,String targetAudio
	     ,long bytesToSkip
	     ,AudioInputStream stream
	     ) throws IOException
	 {
	     AudioManipulationGitHelper audioManipulation = new AudioManipulationGitHelper(sampleRate, numChannels);
	     int bufferSize = line.getBufferSize();
	     byte inBuffer[] = new byte[bufferSize];
	     byte outBuffer[] = new byte[bufferSize];
	     int numRead, numWritten;

	     //Uses our global "current" variables
	     audioManipulation.setSpeed(currentSpeed);
	     audioManipulation.setPitch(currentPitch);
	     audioManipulation.setRate(currentRate);

	     //if SFX, use the SFX volume (since can be separate), if Music, use music, 
	     //else if neither, print error out! (shouldn't happen but if it does could be useful to debug?)
	     if(audioType.equals("SFX")) {
	    	 audioManipulation.setVolume(currentVolumeSFX);
	     }else if(audioType.equals("MUSIC")) {
	    	 audioManipulation.setVolume(currentVolumeMusic);
	     }else {
	    	 audioManipulation.setVolume(0F);
	    	 print("Error! AudioType doesn't match in runAudioManipulation!");
	     }

	     audioManipulation.setChordPitch(emulateChordPitch);
	     audioManipulation.setQuality(quality);
	     
	     //From Stack Overflow: https://stackoverflow.com/questions/52595473/java-start-audio-playback-at-x-position/52596824
	     //Skip based on seconds elapsed so the correct number of bytes have been skipped
	     long justSkipped = 0;
	     
	     //If SFX, DON'T skip! Sound effects are so short this feels unnecessary. 
	     //IF we need to do other logic down the road, we can reassess.
	     if(audioType.equals("SFX"))
	    	 bytesToSkip = 0;
	     while (bytesToSkip > 0 && (justSkipped = stream.skip(bytesToSkip)) > 0) {
	         bytesToSkip -= justSkipped;
	     }

	     //Original code I didn't write/touch, but looks like it reads and writes audio based on input.
	     do {
	    	 numRead = audioStream.read(inBuffer, 0, bufferSize);
             if(numRead <= 0) {
            	 audioManipulation.flushStream();
             } else {
            	 audioManipulation.writeBytesToStream(inBuffer, numRead);
             }
             do {
            	 numWritten = audioManipulation.readBytesFromStream(outBuffer, bufferSize);
            	 if(numWritten > 0) {
            		 line.write(outBuffer, 0, numWritten);
            	 }
             } while(numWritten > 0);
	     } while(numRead > 0);
	     
	     //Loop audio IF done reading audio (end of song) and audioType = "Music"
	     if(numRead <= 0 && audioType.equals("MUSIC")) {

	    	 //If we just play song again, the audio will resume where it last left off, which would be 
	    	 //the end of the song in this case. So we reset secondstoSkip prior which will effectively restart song.
        	 secondsToSkip = 0;

        	 //Song is over, so set musicThreadRunning = false
	    	 musicThreadRunning = false;

	    	 //if debugMode = true, print loop
	    	 if(GVar.getDebug())
	    		 print("Loop Audio: " + targetAudio);

	    	 //play audio again from the start
	    	 playAudioStagingArea(audioType, targetAudio);
	     }
 	}
	 
	//Resumes audio 
	//(but ACTUALLY just destroys old audio and creates new audio, even if the same song. 
    //I haven't figured out how to unpause these threads and resume audio correctly, so this is working for now, though feels like it must be inefficient)
	public void resumeAudio(String audioType){

		//If audioType = SFX, load up SFX to play
		if(audioType.equals("SFX")) {
			playAudioStagingArea(audioType, currentSFX);
			
			//IFF we figure out how to unpause threads, will want to uncomment this boolean
	        //sfxThreadRunning = true;
		}

		//If audioType = Music, load up Music to play
		else if(audioType.equals("MUSIC")) {
			playAudioStagingArea(audioType, currentMusic);

			//IFF we figure out how to unpause threads, will want to uncomment this boolean
			//musicThreadRunning = true;
		}
    }
	
	//Used as a staging Area for playing Audio so the "ugly" try/catch code only needs to go there, not in general code
	public void pauseAudioStagingArea(String audioType) {
		try {
			pauseAudio(audioType);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Abruptly pauses audio (though used to be more abrupt. I'd like to see if I can make it more abrupt again... </3)
	//Checks audioType AND that thread is currently running so we don't get null pointer errors
    //Note: Need to do "synchronized" when pausing audio, but not currently pausing so don't NEED, but I don't think hurting, so keeping for now
	//Note: @@SuppressWarnings("deprecation") related to the Thread.stop() methods. Highlight methods to read about deprecation.
	@SuppressWarnings("deprecation")
	public void pauseAudio(String audioType) throws InterruptedException{

		//If audioType = SFX and the SFX thread is currently running, stop thread and running boolean
		if(audioType.equals("SFX") && sfxThreadRunning) {
			synchronized(sfxThread) { 
				sfxThread.stop();
		        sfxThreadRunning = false;
			}
		}
		
		//If audioType = Music and the Music thread is currently running, stop thread and running boolean
		if(audioType.equals("MUSIC") && musicThreadRunning) {
			synchronized(musicThread) { 
				musicThread.stop();
		        musicThreadRunning = false;
			}
		}
		
		//Broke out both separately since sometimes both aren't running and didn't want to proc an error. Also it will not work if lumped.
		//If audioType = SFX and the SFX thread is currently running, stop thread and running boolean
		if(audioType.equals("ALL") && sfxThreadRunning) {
			synchronized(sfxThread) {
				sfxThread.stop();
				sfxThreadRunning = false;
			}
		}

		//If audioType = Music and the Music thread is currently running, stop thread and running boolean
		if(audioType.equals("ALL") && musicThreadRunning) {
			synchronized(musicThread) { 
				musicThread.stop();
		        musicThreadRunning = false;
			}
		}
	}

	//Populate Hashmaps as a convenient way to get file path in an easy to read/code way
	public void populateHashMaps() {

		//Holds all SFX
		//Note: we almost definitely want to split out between playerSFX vs. enemy SFX 
		//      (and possibly game SFX like Pause? that one may be excessive since I don't think a lot)  
		mapSFX.put("1-Up", new File("res/audio/sfx/smb_1-up.wav"));
		mapSFX.put("BowserFalls", new File("res/audio/sfx/smb_bowserfalls.wav"));
		mapSFX.put("BowserFire", new File("res/audio/sfx/smb_bowserfire.wav"));
		mapSFX.put("BreakBlock", new File("res/audio/sfx/smb_breakblock.wav"));
		mapSFX.put("Bump", new File("res/audio/sfx/smb_bump.wav"));
		mapSFX.put("Coin", new File("res/audio/sfx/smb_coin.wav"));
		mapSFX.put("Fireball", new File("res/audio/sfx/smb_fireball.wav"));
		mapSFX.put("Fireworks", new File("res/audio/sfx/smb_fireworks.wav"));
		mapSFX.put("Flagpole", new File("res/audio/sfx/smb_flagpole.wav"));
		mapSFX.put("GameOver", new File("res/audio/sfx/smb_gameover.wav"));
		mapSFX.put("Jump(Small)", new File("res/audio/sfx/smb_jump-small.wav"));
		mapSFX.put("Jump(Big)", new File("res/audio/sfx/smb_jump-super.wav"));
		mapSFX.put("Kick", new File("res/audio/sfx/smb_kick.wav"));
		mapSFX.put("LuigiDie", new File("res/audio/sfx/smb_mariodie.wav"));
		mapSFX.put("Pause", new File("res/audio/sfx/smb_pause.wav"));
		mapSFX.put("Pipe", new File("res/audio/sfx/smb_pipe.wav"));
		mapSFX.put("PowerUp", new File("res/audio/sfx/smb_powerup.wav"));
		mapSFX.put("StageClear", new File("res/audio/sfx/smb_stage_clear.wav"));
		mapSFX.put("Stomp", new File("res/audio/sfx/smb_stomp.wav"));
		mapSFX.put("Vine", new File("res/audio/sfx/smb_vine.wav"));
		mapSFX.put("Warning", new File("res/audio/sfx/smb_warning.wav"));
		mapSFX.put("WorldClear", new File("res/audio/sfx/smb_world_clear.wav"));

		//Holds all music
		mapMusic.put("RunningAbout", new File("res/audio/music/01 - Running About.wav"));
		mapMusic.put("RunningAbout(Hurry!)", new File("res/audio/music/02 - Running About (Hurry!).wav"));
		mapMusic.put("Underground", new File("res/audio/music/03 - Underground.wav"));
		mapMusic.put("Underground(Hurry!)", new File("res/audio/music/04 - Underground (Hurry!).wav"));
		mapMusic.put("SwimmingAround", new File("res/audio/music/05 - Swimming Around.wav"));
		mapMusic.put("SwimmingAround(Hurry!)", new File("res/audio/music/06 - Swimming Around (Hurry!).wav"));
		mapMusic.put("Bowser'sCastle", new File("res/audio/music/07 - Bowser's Castle.wav"));
		mapMusic.put("Bowser'sCastle(Hurry!)", new File("res/audio/music/08 - Bowser's Castle (Hurry!).wav"));
		mapMusic.put("Invincible", new File("res/audio/music/09 - Invincible.wav"));
		mapMusic.put("Invincible(Hurry!)", new File("res/audio/music/10 - Invincible (Hurry!).wav"));
		mapMusic.put("LevelComplete", new File("res/audio/music/11 - Level Complete-small.wav"));
		mapMusic.put("Bowser'sCastleComplete", new File("res/audio/music/12 - Bowser's Castle Complete.wav"));
		mapMusic.put("YouHaveDied", new File("res/audio/music/13 - You Have Died.wav"));
		mapMusic.put("GameOver", new File("res/audio/music/14 - Game Over.wav"));
		mapMusic.put("GameOver(Alternative)", new File("res/audio/music/15 - Game Over (Alternative).wav"));
		mapMusic.put("IntoThePipe", new File("res/audio/music/16 - Into The Pipe.wav"));
		mapMusic.put("IntoThePipe(Hurry!)", new File("res/audio/music/17 - Into The Pipe (Hurry!).wav"));
		mapMusic.put("SavedThePrincess", new File("res/audio/music/18 - Saved The Princess.wav"));
	}
	
	//Cycles through songs, just to shake it up when debugging. 
	//We shouldn't need this for the full game, which is good since the try catches make it gross.
	//IFF we decided we needed this long term, I'd probably opt for us putting in an array so we could just do x + 1 rather than all of these checks.
	//    This one only meant as a quick and dirty solution.
	public void nextSong() {
		
		//If player is NOT dead, allow them to change song
		if(!Game.gameHeader.getDead()) {

			//Resets Song Time Elapsed to 0 since changing song.
			secondsToSkip = 0;
	
			//Pauses Song (if playing)
			pauseAudioStagingArea("MUSIC");
	
			//A series of checks that cycle through songs and loop to start at the end
			if(currentMusic.equals(EnumMusic.RunningAbout.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.Underground.toString());
			} else if(currentMusic.equals(EnumMusic.RunningAbout_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.Underground_Hurry.toString());
			} else if(currentMusic.equals(EnumMusic.Underground.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.SwimmingAround.toString());
			} else if(currentMusic.equals(EnumMusic.Underground_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.SwimmingAround_Hurry.toString());
			} else if(currentMusic.equals(EnumMusic.SwimmingAround.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.BowserCastle.toString());
			} else if(currentMusic.equals(EnumMusic.SwimmingAround_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.BowserCastle_Hurry.toString());
			} else if(currentMusic.equals(EnumMusic.BowserCastle.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.Invincible.toString());
			} else if(currentMusic.equals(EnumMusic.BowserCastle_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.Invincible_Hurry.toString());
			} else if(currentMusic.equals(EnumMusic.Invincible.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.IntoThePipe.toString());
			} else if(currentMusic.equals(EnumMusic.Invincible_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.IntoThePipe_Hurry.toString());
			} else if(currentMusic.equals(EnumMusic.IntoThePipe.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.RunningAbout.toString());
			} else if(currentMusic.equals(EnumMusic.IntoThePipe_Hurry.toString())) {
				playAudioStagingArea("MUSIC", EnumMusic.RunningAbout_Hurry.toString());
			}
		}
	}

	/*************** GETTERS and SETTERS ***************/
	
	//Gets current SFX playing
	public String getCurrentSFX() {
		return currentSFX;
	}

	//Sets current SFX playing
	public void setCurrentSFX(String audio){
		currentSFX = audio;
	}

	//Gets current Music playing
	public String getCurrentMusic() {
		return currentMusic;
	}

	//Sets current Music playing
	public void setCurrentMusic(String audio){
		currentMusic = audio;
	}

	//Sets current Audio Speed
	public void setCurrentSpeed(float f) {
		
		//Uses FPS cap check since we want this to be linked with FPS. ONLY adjust speed if can still change FPS
		//OR if less than and not paused which implies we're entering "Stop" mode, so will be
		//decreasing audio speed considerably.
		if((GVar.FPS > GVar.FPS_MIN && GVar.FPS < GVar.FPS_MAX) ||
		   (GVar.FPS <= GVar.FPS_MIN && !GVar.getStop()))
			currentSpeed += f;

		//Only change if within the MAX/MIN range for allowable speed values 
		if(currentSpeed > MAX_SPEED)
			currentSpeed = MAX_SPEED;
		//Only reset this IF in "Stop" Mode
		if(currentSpeed < MIN_SPEED && GVar.getStop())
			currentSpeed = MIN_SPEED;
		
		//Prints Current Audio Speed IF in Debug mode
		if(GVar.getDebug())
			print("Current Audio Speed: " + currentSpeed);
	}
	
	//Gets current Audio Speed
	public float getCurrentSpeed() {
		return currentSpeed;
	}
	
	//Sets current Audio pitch
	public void setCurrentPitch(float f) {
		currentPitch += f;

		//Then checks if adjustment is within the range
		if(currentPitch < MIN_PITCH)
			currentPitch = MIN_PITCH;
		if(currentPitch > MAX_PITCH)
			currentPitch = MAX_PITCH;			
	}
	
	//Gets current Audio pitch (currently not using)
	public float getCurrentPitch() {
		return currentPitch;
	}
	
	//Sets current Audio Rate
	//Not sure of difference with Speed TBH
	public void setCurrentRate(float f) {
		currentRate += f;

		//Then checks if adjustment is within the range
		if(currentRate < MIN_RATE)
			currentRate = MIN_RATE;
		if(currentRate > MAX_RATE)
			currentRate = MAX_RATE;			
	}
	
	//Gets current Audio pitch (currently not using)
	//Not sure of difference with Speed TBH
	public float getCurrentRate() {
		return currentRate;
	}
	
	//Sets current Audio Volume. Treat SFX and Music separately
	//This needs to be refactored likely to account for different volumes.
	public void setCurrentVolume(String audioType, float adjust) {

		//First gets adjustment with the current value
		if(audioType.equals("SFX")) {
			adjust += currentVolumeSFX;
		}else if(audioType.equals("MUSIC")) {
			adjust += currentVolumeMusic;
		}else if(audioType.equals("ALL")) {
			adjust += currentVolumeSFX;
		}else {
			print("Error! AudioType doesn't match in setCurrentVolume!");
		}
		
		//Then checks if adjustment is within the range
		if(adjust < MIN_VOLUME)
			adjust = MIN_VOLUME;
		if(adjust > MAX_VOLUME)
			adjust = MAX_VOLUME;			
		
		//Then sets volume per audioType
		if(audioType.equals("SFX")) {
			currentVolumeSFX = adjust;
		}else if(audioType.equals("MUSIC")) {
			currentVolumeMusic = adjust;

		//This needs to be refactored likely to account for different volumes.
		}else if(audioType.equals("ALL")) {
			currentVolumeSFX = adjust;
			currentVolumeMusic = adjust;
		}		
	}
	
	//Gets current Audio volume based on audioType
	public float getCurrentVolume(String audioType) {
		if(audioType.equals("SFX")) {
			return currentVolumeSFX;
		}else if(audioType.equals("MUSIC")) {
			return currentVolumeMusic;
		}else {
			print("Error! AudioType doesn't match in setCurrentVolume!");
			return 0f;
		}		
	}
	
	//Increments How many seconds have elapsed for current Song (and resets subSeconds)
	public void incrementSecondsToSkip() {
		secondsToSkip++;
		subSecondsToSkip = 0;
	}

	//Sets How many seconds have elapsed for current Song (and resets subSeconds)
	public void setSecondsToSkip(int i) {
		secondsToSkip = i;
		subSecondsToSkip = 0;
	}

	//Gets How many seconds have elapsed for current Song
	public int getSecondsToSkip() {
		return secondsToSkip;
	}
	
	//Increments How many subSeconds have elapsed for current Song
	public void incrementSubSecondsToSkip() {
		subSecondsToSkip++;
	}

	//Sets How many subSeconds have elapsed for current Song
	public void setSubsecondsToSkip(int i) {
		subSecondsToSkip = i;
	}

	//Gets How many subSeconds have elapsed for current Song
	public int getSubSecondsToSkip() {
		return subSecondsToSkip;
	}
	
	//Resets default values. Useful when creating Audio object at beginning as well as resetting game.
	//Note that when we eventually save settings, we can't have volume in here since that can be set custom to other values.
	public void resetDefaults() {
		
		//If no settings file exists or game isn't loaded, use these defaults
		if(!GVar.settingsExists() || !Game.getLoaded()){
			currentVolumeSFX = DEFAULT_CURRENT_VOLUME_SFX;
		    currentVolumeMusic = DEFAULT_CURRENT_VOLUME_MUSIC;
	        currentSpeed = DEFAULT_CURRENT_SPEED;
	        currentPitch = DEFAULT_CURRENT_PITCH;
	        currentRate = DEFAULT_CURRENT_RATE;
		}
        secondsToSkip = 0;
	}
	
	//Resets the audio. 
	//Note this doesn't include speed, because it is tied to FPS. 
	//If you don't do this, can run into issues with audio not matching FPS and "feel" is off.
	public void manuallyResetDefaults() {
		
		currentVolumeSFX = DEFAULT_CURRENT_VOLUME_SFX;
		currentVolumeMusic = DEFAULT_CURRENT_VOLUME_MUSIC;
	    currentPitch = DEFAULT_CURRENT_PITCH;
	    currentRate = DEFAULT_CURRENT_RATE;
	}
	
	//Manually resets speed 
	//Note this isn't included in manuallyResetDefaults, because speed is tied to FPS. 
	//If you don't do this, can run into issues with audio not matching FPS and "feel" is off.
	public void manuallyResetSpeedDefault() {
    	currentSpeed = DEFAULT_CURRENT_SPEED;
	}
	
}	