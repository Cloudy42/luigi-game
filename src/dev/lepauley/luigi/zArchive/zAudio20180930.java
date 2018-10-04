package dev.lepauley.luigi.zArchive;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import dev.lepauley.luigi.audio.AudioManipulationGitHelper;
import dev.lepauley.luigi.utilities.EnumMusic;
import static dev.lepauley.luigi.utilities.Utilities.*;


/*
 * Handles all audio in game
 * Originally followed this stupid tutorial: https://www.youtube.com/watch?v=VMSTTg5EEnY
 * but I thought it was dumb and not clear. Heck the guy sounded confused at times. So ultimately
 * went with this: https://coderanch.com/t/572997/java/Stopping-looping-javax-sound-sampled
 * 
 * Note for Alan: Research distorting/slowing down frequency and speed when controlling time:
 *                https://docs.oracle.com/javase/tutorial/sound/controls.html
 */
public class zAudio20180930 {
	
	//Holds all sfx	& Music respectively
	Map<String, File> mapSFX = new HashMap<String, File>();
	Map<String, File> mapMusic = new HashMap<String, File>();
	
	//Tracks current SFX/Song
	private String currentMusic;
	
	//Sets Min and Max volume:
	//Note: Min = -80.0f | Max = 6.0206f
	//pulled from (FloatControl)gainControl.getMaximum()
	private final float MIN_VOLUME = -15.0f  
					  , MAX_VOLUME =   6.0f;
	
	//Sets Min and Max audio speed/rate:
	private final float MIN_RATE = -4.0f  
					  , MAX_RATE =  4.0f;
	
	//Tracks where a Thread is currently Running or not
	private boolean sfxThreadRunning = false
			      , musicThreadRunning = false;
	
	//Threads for individual audio channels
	private Thread sfxThread = null
			     , musicThread = null;

	private Runnable runnable = null;
	
	//current audio variables
	//Note: Player is usually able to raise/lower volume for SFX vs. music separately, hence why separate here
	private float currentVolumeSFX = 1.0f
			    , currentVolumeMusic = 1.0f
                , currentSpeed = 1.0f
                , currentPitch = 1.0f
                , currentRate = 1.0f;
	
	//Step 1 and 2 of setting audio (pull out CD then put CD in player, so to speak)
	private AudioInputStream audioSFX, audioMusic;
	private Clip clipSFX, clipMusic;
	private float currentSpeedMusic;
	
	//Constructor to get default audio loaded
	public zAudio20180930() {
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
            print("Sorry but there has been a problem reading your file.");
            ex.printStackTrace();
        }
        catch (UnsupportedAudioFileException ex1){
            print("Sorry but the audio file format you are using is not supported.");
            ex1.printStackTrace();
        }
        catch (LineUnavailableException ex2){
            print("Sorry but there are audio line problems.");
            ex2.printStackTrace();
        } 
	}
	
	//Plays SFX or Music, separate so can run both simultaneously.
	//Note: May want to consider further channel splits if we need to track enemy sound effects as well
	//2018.09.30: Added Close audio because I ran into the following error when overdoing,
		//and doing the following seems to clean things up. So
		//far it's working and I haven't hit the error nor the sound
		//dropping since. *fingers crossed*:
		/*
		 * Exception in thread "Thread-3" java.lang.OutOfMemoryError: Java heap space
		 * at com.sun.media.sound.DirectAudioDevice$DirectClip.open(DirectAudioDevice.java:1135)
		 * at dev.lepauley.luigi.audio.Audio.playAudio(Audio.java:69)
		 * at dev.lepauley.luigi.Game.tick(Game.java:102)
		 * at dev.lepauley.luigi.Game.run(Game.java:240)
		 * at java.lang.Thread.run(Thread.java:745)
		 */public void playAudio(String audioType, String s){
			try {
				if(audioType.toLowerCase().equals("sfx") && !clipSFX.isRunning()) {
					clipSFX.stop();
					clipSFX.close();
					clipSFX = AudioSystem.getClip();
			        audioSFX = AudioSystem.getAudioInputStream(mapSFX.get(s));
			        clipSFX.open(audioSFX);
			        clipSFX.start();
				}
				else if(audioType.toLowerCase().equals("music") && !clipMusic.isRunning()) {
					clipMusic.stop();
					clipMusic.close();
					clipMusic = AudioSystem.getClip();
			        audioMusic = AudioSystem.getAudioInputStream(mapMusic.get(s));
			        clipMusic.open(audioMusic);
			        clipMusic.start();
			        //Loop BGM continuously
			        clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
			        currentMusic = s;
				}
	        }        
	        catch (IOException ex){
	            print("Sorry but there has been a problem reading your file.");
	            ex.printStackTrace();
	        }
	        catch (UnsupportedAudioFileException ex1){
	            print("Sorry but the audio file format you are using is not supported.");
	            ex1.printStackTrace();
	        }
	        catch (LineUnavailableException ex2){
	            print("Sorry but there are audio line problems.");
	            ex2.printStackTrace();
	        }
			//adjustVolume("all",0F);
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
	
	//Adjust volumne. Base code from here: https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
	//Modifications by Alan
	public void adjustVolume(String audioType, float adjust) {
		
		FloatControl gainControl = null;
		float gain = 0F;

		//Loop twice to check for both without needing to duplicate code
		for(int i = 0; i < 2; i++) {
			//If SFX or "all" adjust SFX volume
			if((audioType.equals("sfx") || audioType.equals("all")) && i == 0) {
				gainControl = (FloatControl) clipSFX.getControl(FloatControl.Type.MASTER_GAIN);
				gain = currentVolumeSFX;
			}
			//If Music or "all" adjust SFX volume
	        if((audioType.equals("music") || audioType.equals("all")) && i == 1) {
					gainControl = (FloatControl) clipMusic.getControl(FloatControl.Type.MASTER_GAIN);
					gain = currentVolumeMusic;
	        }
	        
			gain += adjust;

			//Only Print this is greater than the minimum or is less than the maximum
			if(gain >= MIN_VOLUME && gain <= MAX_VOLUME)
				print("Current Volume [SFX: " + currentVolumeSFX + "|Music: " + currentVolumeMusic + "]");

			//Add volume and check caps
		        if(gain > MAX_VOLUME)
		        	gain = MAX_VOLUME;
		        if(gain < MIN_VOLUME)
		        	gain = MIN_VOLUME;
		    
		    //Update master volume for each 
			if((audioType.equals("sfx") || audioType.equals("all")) && i == 0)
		        currentVolumeSFX = gain;
	        if((audioType.equals("music") || audioType.equals("all")) && i == 1)
		        currentVolumeMusic = gain;

	        //set volume
	        gainControl.setValue(gain);
		}
	}
	
	 //Adjusts Playback Speed
 	 //From Stack Overflow in response to my inquiry: https://stackoverflow.com/questions/52572517/java-adjust-playback-speed-of-a-wav-file/52580516#52580516
	 //I ALSO needed to create a new thread to play this audio, otherwise it freezes game until song ends:
	 //https://stackoverflow.com/questions/21373231/cannot-do-anything-while-writing-to-a-sourcedataline
	 /*
	  * I want to save this for some reason: 
   		AudioInputStream din = null;
       	AudioInputStream in = audioInputStream;
       	AudioFormat baseFormat = in.getFormat();
   		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
       	din = AudioSystem.getAudioInputStream(decodedFormat, in);
       	RadioUtils.rawplay(decodedFormat, din, 6f);
	  */
	 @SuppressWarnings("deprecation")
	public void adjustSpeed(String audioType, float adjust) {
		 float tempCalc = currentSpeedMusic + adjust; 
		 if(tempCalc > MAX_RATE)
			 tempCalc = MAX_RATE;
		 if(tempCalc < MIN_RATE)
			 tempCalc = MIN_RATE;

		 //for whatever reason, gain has to be final for the below, but I also need to do the 
		 //checks above, so alas, this is where I end up, with tempCalc above and gain below...
		 final float gain = tempCalc;
		 
		 if(musicThreadRunning) {
			 musicThread.stop();
			 musicThreadRunning = false;
		 }
		 pauseAudio(audioType);
		 musicThread = new Thread(runnable) {
			    @Override
			    public void run() {
				    try {
				    	
					      File fileIn = mapMusic.get(currentMusic);
					      AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(fileIn);
					      AudioFormat formatIn=audioInputStream.getFormat();
					      AudioFormat format;
					      
					      if(gain < 0) {
					    	  format = new AudioFormat((int)(formatIn.getSampleRate()/-gain), formatIn.getSampleSizeInBits(), formatIn.getChannels(), true, formatIn.isBigEndian());
					      } else if(gain > 0) {
					    	  format = new AudioFormat((int)(formatIn.getSampleRate()*gain), formatIn.getSampleSizeInBits(), formatIn.getChannels(), true, formatIn.isBigEndian());
					      } else {
					    	print("Go back to normal audio");
					    	format = new AudioFormat((int)(formatIn.getSampleRate()), formatIn.getSampleSizeInBits(), formatIn.getChannels(), true, formatIn.isBigEndian());
					      }
					          print(formatIn.toString());
					          print(format.toString());
					      byte[] data=new byte[1024];
					      DataLine.Info dinfo=new DataLine.Info(SourceDataLine.class, format);
					      SourceDataLine line=(SourceDataLine)AudioSystem.getLine(dinfo);
					      if(line!=null) {
					        line.open(format);
					        line.start();
					        while(true) {
					          int k=audioInputStream.read(data, 0, data.length);
					          if(k<0) break;
					          line.write(data, 0, k);
					        }
					        line.stop();
					        line.close();
					      }
					    }
					    catch(Exception ex) { ex.printStackTrace(); }
			    }
			};
			currentSpeedMusic = gain;
			print("currentSpeedMusic: " +  currentSpeedMusic + " | gain: " + gain);
			musicThread.start();
			musicThreadRunning = true;
		  }

		// Run sonic: Audio manipulation
		// Source: https://github.com/waywardgeek/sonic
		//I didn't write code, so won't be commenting it other tahn saying I swapped our global "current" variables in. Otherwise, untouched.
		 private void runSonic(
		     AudioInputStream audioStream,
		     SourceDataLine line,
		     boolean emulateChordPitch,
		     int quality,
		     int sampleRate,
		     int numChannels) throws IOException
		 {
		     AudioManipulationGitHelper sonic = new AudioManipulationGitHelper(sampleRate, numChannels);
		     int bufferSize = line.getBufferSize();
		     byte inBuffer[] = new byte[bufferSize];
		     byte outBuffer[] = new byte[bufferSize];
		     int numRead, numWritten;

		     //Uses our global "current" variables
		     sonic.setSpeed(currentSpeed);
		     sonic.setPitch(currentPitch);
		     sonic.setRate(currentRate);
		     sonic.setVolume(currentVolumeMusic);

		     sonic.setChordPitch(emulateChordPitch);
		     sonic.setQuality(quality);
		     do {
		    	 numRead = audioStream.read(inBuffer, 0, bufferSize);
		            if(numRead <= 0) {
		                sonic.flushStream();
		            } else {
		                sonic.writeBytesToStream(inBuffer, numRead);
		            }
		            do {
		                numWritten = sonic.readBytesFromStream(outBuffer, bufferSize);
		                if(numWritten > 0) {
		                    line.write(outBuffer, 0, numWritten);
		                }
		            } while(numWritten > 0);
		        } while(numRead > 0);
		 }
		 
		 //Integrates Sonic (custom git code that allows manipulation of audio) with our SFX and Music.
		 //Plays audio at end in separate thread
		 @SuppressWarnings("deprecation")
		public void playSonic(String audioType) throws UnsupportedAudioFileException, IOException, LineUnavailableException
		 {
			 boolean emulateChordPitch = false;
	         int quality = 0;

	        //Pauses current Audio
	        pauseAudio(audioType);
	        
	        //Loads current Song
	        AudioInputStream stream;
	        
	        //May need to do currentSFX to account for cases like this:
	        if(audioType.equals("sfx")) {
	        	stream = AudioSystem.getAudioInputStream(mapSFX.get(currentMusic));
	        } else if(audioType.equals("music")) {
	        	stream = AudioSystem.getAudioInputStream(mapMusic.get(currentMusic));
	        } else {
	        	stream = null;
	        } 
	            
	        //Music-related code
	        AudioFormat format = stream.getFormat();
	        int sampleRate = (int)format.getSampleRate();
	        int numChannels = format.getChannels(); 
	        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format,((int)stream.getFrameLength()*format.getFrameSize()));
	        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
	        
	        //Checks if sfxThread is currently Running. If so, stop it:
	        if(sfxThreadRunning && audioType.equals("sfx")) {
				 sfxThread.stop();
				 sfxThreadRunning = false;
			 }

	        //Checks if musicThread is currently Running. If so, stop it:
	        if(musicThreadRunning && audioType.equals("music")) {
				 musicThread.stop();
				 musicThreadRunning = false;
			 }
	        
	        //Opens line (audio) and starts (plays) it
	        line.open(stream.getFormat());
	        line.start();
			 
	        //Creates thread so all processing isn't waiting for this and halting all other code
	        if(audioType.equals("sfx")) {
		        sfxThread = new Thread(runnable) {
				    @Override
				    public void run() {
						try {
							//Runs sonic Code
						    runSonic(stream, line, emulateChordPitch, quality, sampleRate, numChannels);
							} catch(Exception ex) { ex.printStackTrace(); }
					}
				};
				//Starts thread
				sfxThread.start();
				
				//Helper to let us know thread is running
				sfxThreadRunning = true;
	        }
	        if(audioType.equals("music")) {
		        musicThread = new Thread(runnable) {
				    @Override
				    public void run() {
						try {
							//Runs sonic Code
						    runSonic(stream, line, emulateChordPitch, quality, sampleRate, numChannels);
							} catch(Exception ex) { ex.printStackTrace(); }
					}
				};
				//Starts thread
				musicThread.start();
				
				//Helper to let us know thread is running
				musicThreadRunning = true;
	        }
		 }
	 
	//Populate Hashmaps as a convenient way to get file path in an easy to read/code way
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
		pauseAudio("music");
		if(currentMusic.equals(EnumMusic.RunningAround.toString())) playAudio("music", EnumMusic.Underground.toString());
		else if(currentMusic.equals(EnumMusic.RunningAround_Hurry.toString())) playAudio("music", EnumMusic.Underground_Hurry.toString());
		else if(currentMusic.equals(EnumMusic.Underground.toString())) playAudio("music", EnumMusic.SwimmingAround.toString());
		else if(currentMusic.equals(EnumMusic.Underground_Hurry.toString())) playAudio("music", EnumMusic.SwimmingAround_Hurry.toString());
		else if(currentMusic.equals(EnumMusic.SwimmingAround.toString())) playAudio("music", EnumMusic.BowserCastle.toString());
		else if(currentMusic.equals(EnumMusic.SwimmingAround_Hurry.toString())) playAudio("music", EnumMusic.BowserCastle_Hurry.toString());
		else if(currentMusic.equals(EnumMusic.BowserCastle.toString())) playAudio("music", EnumMusic.Invincible.toString());
		else if(currentMusic.equals(EnumMusic.BowserCastle_Hurry.toString())) playAudio("music", EnumMusic.Invincible_Hurry.toString());
		else if(currentMusic.equals(EnumMusic.Invincible.toString())) playAudio("music", EnumMusic.IntoThePipe.toString());
		else if(currentMusic.equals(EnumMusic.Invincible_Hurry.toString())) playAudio("music", EnumMusic.IntoThePipe_Hurry.toString());
		else if(currentMusic.equals(EnumMusic.IntoThePipe.toString())) playAudio("music", EnumMusic.RunningAround.toString());
		else if(currentMusic.equals(EnumMusic.IntoThePipe_Hurry.toString())) playAudio("music", EnumMusic.RunningAround_Hurry.toString());
	}

	/*************** GETTERS and SETTERS ***************/
	
	public String getCurrentSong() {
		return currentMusic;
	}
	
	public void setCurrentSpeed(float f) {
		currentSpeed += f;
	}
	
	public void setCurrentPitch(float f) {
		currentPitch += f;
	}
	
	public void setCurrentRate(float f) {
		currentRate += f;
	}
	
}	