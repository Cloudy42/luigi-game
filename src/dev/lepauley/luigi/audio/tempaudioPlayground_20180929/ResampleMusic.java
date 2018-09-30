package dev.lepauley.luigi.audio.tempaudioPlayground_20180929;

import java.io.File;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;

public class ResampleMusic {

	private static final Encoding PCM_SIGNED = null;
	//source file
	final File file1 = new File("res/audio/sfx/smb_powerup.wav");
	final File file2 = new File("res/audio/sfx/smb_powerup_alt.wav");
	//destination file
	
	//audio stream of file1
	final AudioInputStream in1 = AudioSystem.getAudioInputStream(file1);
	
	//get audio format for targetted sound
	final AudioFormat inFormat = getOutFormat(in1.getFormat());
	//change the frequency of Audio format
	private AudioFormat getOutFormat(AudioFormat inFormat) {
			int ch = inFormat.getChannels();
			float rate = inFormat.getSampleRate();	
			return new AudioFormat(PCM_SIGNED, 72000, 16, ch, ch * 2, rate,
					inFormat.isBigEndian());
	}
	
	
	//get the target file audio stream using file format
	final AudioInputStream in2 = AudioSystem.getAudioInputStream(inFormat, in1);
	//write the audio file in targeted pitch file
	AudioSystem.write(in2, AudioFileFormat.Type.WAVE, file2);
}
