package dev.lepauley.luigi.audio;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
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

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.utilities.EnumMusic;
import dev.lepauley.luigi.utilities.EnumSFX;


/*
 * Handles all audio in game
 * Originally followed this stupid tutorial: https://www.youtube.com/watch?v=VMSTTg5EEnY
 * but I thought it was dumb and not clear. Heck the guy sounded confused at times. So ultimately
 * went with this: https://coderanch.com/t/572997/java/Stopping-looping-javax-sound-sampled
 * 
 * Note for Alan: Research distorting/slowing down frequency and speed when controlling time:
 *                https://docs.oracle.com/javase/tutorial/sound/controls.html
 */
public class Audio {
	
	//Holds all sfx	& Music respectively
	Map<String, File> mapSFX = new HashMap<String, File>();
	Map<String, File> mapMusic = new HashMap<String, File>();
	
	//Tracks current SFX/Song
	private String currentSFX
	             , currentMusic;

	//Sets Min and Max volume:
	private final float MIN_VOLUME = 0.0f  
					  , MAX_VOLUME = 4.0f;
	
	//Sets Min and Max audio speed/rate:
	private final float MIN_RATE = -4.0f  
					  , MAX_RATE =  4.0f;
	
	//Tracks where a Thread is currently Running or not
	private boolean sfxThreadRunning = false
			      , musicThreadRunning = false;
	
	//Threads for individual audio channels
	private Thread sfxThread = null
			     , musicThread = null;

	//Track line for audio
    SourceDataLine lineSFX = null
                 , lineMusic = null;
	
	private Runnable runnable = null;
	
	//current audio variables
	//Note: Player is usually able to raise/lower volume for SFX vs. music separately, hence why separate here
	private float currentVolumeSFX = 1.0f
			    , currentVolumeMusic = 1.0f
                , currentSpeed = 1.0f
                , currentPitch = 1.0f
                , currentRate = 1.0f;
	
	//Tracks how many seconds have elapsed in song:
	int secondsToSkip = 0;
	
	//Constructor to get default audio loaded to hashmaps
	public Audio(){
			currentSFX = EnumSFX.Coin.toString();
			currentMusic = EnumMusic.RunningAround.toString();
		//Add all SFX and Music to hashmaps
		populateHashMaps();
		try {
			playAudio("SFX", EnumSFX.Coin.toString());
			playAudio("MUSIC", EnumMusic.RunningAround.toString());
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
		try {
			//Only pausing music since I like the "boot up" coin sound. Reminds me of the chime from some old games when booting up.
			pauseAudio("MUSIC");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Plays SFX or Music, separate so can run both simultaneously.
	//Note: May want to consider further channel splits if we need to track enemy sound effects as well
	
	// Run sonic: Audio manipulation
	// Source: https://github.com/waywardgeek/sonic
	//I didn't write code, so won't be commenting it other tahn saying I swapped our global "current" variables in. Otherwise, untouched.
	 private void runSonic(
	     AudioInputStream audioStream
	     ,SourceDataLine line
	     ,boolean emulateChordPitch
	     ,int quality
	     ,int sampleRate
	     ,int numChannels
	     ,String audioType
	     ,long bytesToSkip
	     ,AudioInputStream stream
	     ) throws IOException
	 {
	     Sonic sonic = new Sonic(sampleRate, numChannels);
	     int bufferSize = line.getBufferSize();
	     byte inBuffer[] = new byte[bufferSize];
	     byte outBuffer[] = new byte[bufferSize];
	     int numRead, numWritten;

	     //Uses our global "current" variables
	     sonic.setSpeed(currentSpeed);
	     sonic.setPitch(currentPitch);
	     sonic.setRate(currentRate);
	     if(audioType.equals("SFX")) {
	    	 sonic.setVolume(currentVolumeSFX);
	     }else if(audioType.equals("MUSIC")) {
	    	 sonic.setVolume(currentVolumeMusic);
	     }else {
	    	 sonic.setVolume(0F);
	    	 System.out.println("Error! AudioType doesn't match in runSonic!");
	     }

	     sonic.setChordPitch(emulateChordPitch);
	     sonic.setQuality(quality);
	     
	     //From SO: https://stackoverflow.com/questions/52595473/java-start-audio-playback-at-x-position/52596824
	     // now skip until the correct number of bytes have been skipped
	     long justSkipped = 0;
	     if(audioType.equals("SFX"))
	    	 bytesToSkip = 0;
	     while (bytesToSkip > 0 && (justSkipped = stream.skip(bytesToSkip)) > 0) {
	         bytesToSkip -= justSkipped;
	     }
	     // then proceed with writing to your line like you have done before
	     
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
	 public void playAudio(String audioType, String targetAudio) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException
	 {
		 boolean emulateChordPitch = false;
         int quality = 0;

        //Pauses current Audio
        pauseAudio(audioType);
        
        //Loads current Song
        AudioInputStream stream;
        
        if(audioType.equals("SFX")) {
        	stream = AudioSystem.getAudioInputStream(mapSFX.get(targetAudio));
        } else if(audioType.equals("MUSIC")) {
        	stream = AudioSystem.getAudioInputStream(mapMusic.get(targetAudio));
        } else {
        	stream = null;
        } 
            
        //Music-related code
        AudioFormat format = stream.getFormat();
        int sampleRate = (int)format.getSampleRate();
        int numChannels = format.getChannels(); 
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format,((int)stream.getFrameLength()*format.getFrameSize()));
        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);

	     // find out how many bytes you have to skip, this depends on bytes per frame (a.k.a. frameSize)
	     long bytesToSkip = format.getFrameSize() * ((int)format.getFrameRate()) * secondsToSkip;

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
        	lineSFX = line;
	        sfxThread = new Thread(runnable) {
			    @Override
			    public void run() {
					try {
						//Runs sonic Code
					    runSonic(stream, lineSFX, emulateChordPitch, quality, sampleRate, numChannels, audioType,bytesToSkip,stream);
						} catch(Exception ex) { ex.printStackTrace(); }
				}
			};
			//Starts thread
			sfxThread.start();
			
			//Helper to let us know thread is running
			sfxThreadRunning = true;

			//Sets current SFX
        	setCurrentSFX(targetAudio);
        }
        if(audioType.equals("MUSIC")) {
        	lineMusic = line;
	        musicThread = new Thread(runnable) {
			    @Override
			    public void run() {
					try {
						//Runs sonic Code
					    runSonic(stream, lineMusic, emulateChordPitch, quality, sampleRate, numChannels, audioType,bytesToSkip,stream);
						} catch(Exception ex) { ex.printStackTrace(); }
				}
			};
			//Starts thread
			musicThread.start();
			
			//Helper to let us know thread is running
			musicThreadRunning = true;

			//Sets current Music
			setCurrentMusic(targetAudio);
        }
	 }

	//Resumes audio
	public void resumeAudio(String audioType){
			if(audioType.equals("SFX")) {
				try {
					playAudio(audioType, EnumMusic.RunningAround.toString());
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
		        sfxThreadRunning = true;
			}
			else if(audioType.equals("MUSIC")) {
					System.out.println("Music: resumed!");
					try {
						playAudio(audioType, EnumMusic.RunningAround.toString());
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
					musicThreadRunning = true;
			}
    }
	
	//Abruptly pauses audio
    //Note: Need to do "synchronized" when pausing audio
	public void pauseAudio(String audioType) throws InterruptedException{
		if(audioType.equals("SFX") && sfxThreadRunning) {
			synchronized(sfxThread) { 
				sfxThread.stop();
				//lineSFX.wait();
		        sfxThreadRunning = false;
			}
		}
		
		if(audioType.equals("MUSIC") && musicThreadRunning) {
			synchronized(musicThread) { 
				musicThread.stop();
				//lineMusic.wait();
		        musicThreadRunning = false;
			}
		}
		
		//Broke out both separately since sometimes both aren't running and didn't want to proc an error.
		if(audioType.equals("ALL") && sfxThreadRunning) {
			synchronized(sfxThread) {
				sfxThread.stop();
				//lineSFX.wait();
				sfxThreadRunning = false;
			}
		}
		if(audioType.equals("ALL") && musicThreadRunning) {
			synchronized(musicThread) { 
				musicThread.stop();
				//lineMusic.wait();
		        musicThreadRunning = false;
			}
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
		secondsToSkip = 0;
		try {
			pauseAudio("MUSIC");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentMusic.equals(EnumMusic.RunningAround.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.Underground.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.RunningAround_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.Underground_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.Underground.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.SwimmingAround.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.Underground_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.SwimmingAround_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e8) {
				// TODO Auto-generated catch block
				e8.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.SwimmingAround.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.BowserCastle.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e7) {
				// TODO Auto-generated catch block
				e7.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.SwimmingAround_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.BowserCastle_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e6) {
				// TODO Auto-generated catch block
				e6.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.BowserCastle.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.Invincible.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.BowserCastle_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.Invincible_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.Invincible.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.IntoThePipe.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.Invincible_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.IntoThePipe_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.IntoThePipe.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.RunningAround.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if(currentMusic.equals(EnumMusic.IntoThePipe_Hurry.toString()))
			try {
				try {
					playAudio("MUSIC", EnumMusic.RunningAround_Hurry.toString());
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/*************** GETTERS and SETTERS ***************/
	
	//Gets current SFX in thread
	public String getCurrentSFX() {
		return currentSFX;
	}

	//Sets current SFX in thread
	public void setCurrentSFX(String s) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		currentSFX = s;
	}

	//Gets current Music in thread
	public String getCurrentMusic() {
		return currentMusic;
	}

	//Sets current Music in thread
	public void setCurrentMusic(String s) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		currentMusic = s;
	}

	public void setCurrentSpeed(float f) {
		if(GVar.FPS > GVar.FPS_MIN && GVar.FPS < GVar.FPS_MAX)
			currentSpeed += f;
	}
	
	public void setCurrentPitch(float f) {
		currentPitch += f;
	}
	
	public void setCurrentRate(float f) {
		currentRate += f;
	}
	
	public void setCurrentVolume(String audioType, float adjust) {
		if(audioType.equals("SFX")) {
			adjust += currentVolumeSFX;
		}else if(audioType.equals("MUSIC")) {
			adjust += currentVolumeMusic;
		}else if(audioType.equals("ALL")) {
			adjust += currentVolumeSFX;
		}else {
			System.out.println("Error! AudioType doesn't match in setCurrentVolume!");
		}
		
		if(adjust < MIN_VOLUME)
			adjust = MIN_VOLUME;
		if(adjust > MAX_VOLUME)
			adjust = MAX_VOLUME;			
		
		if(audioType.equals("SFX")) {
			currentVolumeSFX = adjust;
		}else if(audioType.equals("MUSIC")) {
			currentVolumeMusic = adjust;
		}else if(audioType.equals("ALL")) {
			currentVolumeSFX = adjust;
			currentVolumeMusic = adjust;
		}		
	}
	
	public float getCurrentVolume(String audioType) {
		if(audioType.equals("SFX")) {
			return currentVolumeSFX;
		}else if(audioType.equals("MUSIC")) {
			return currentVolumeMusic;
		}else {
			System.out.println("Error! AudioType doesn't match in setCurrentVolume!");
			return 0f;
		}		
	}
	
	//Increments How many seconds have elapsed for current Song
	public void incrementSecondsToSkip() {
		secondsToSkip++;
	}

	//Sets How many seconds have elapsed for current Song
	public void setSecondsToSkip(int i) {
		secondsToSkip = i;
	}

	//Gets How many seconds have elapsed for current Song
	public int getSecondsToSkip() {
		return secondsToSkip;
	}
	
	public void resetDefaults() {
		currentVolumeSFX = 1.0f;
	    currentVolumeMusic = 1.0f;
        currentSpeed = 1.0f;
        currentPitch = 1.0f;
        currentRate = 1.0f;
        secondsToSkip = 0;
	}
	
}	