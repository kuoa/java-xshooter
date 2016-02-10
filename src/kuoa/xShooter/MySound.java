package kuoa.xShooter;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class MySound {

	private Clip clip;
	
	public MySound (String s){
		
		try {
		    //File yourFile = new File(s);
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    InputStream is = getClass().getResourceAsStream(s);
		    
		    stream = AudioSystem.getAudioInputStream(
		    		new BufferedInputStream(is));
		    
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);		  		    
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public void play(){
		if (clip == null){
			return;
		}
	
		if (clip.isRunning()){
			clip.stop();
			
		}
		clip.setFramePosition(0);
		
		while (!clip.isRunning()){
			clip.start();
			
		}			
	}
	
	public void stop(){
		if (clip == null){
			return;
		}
		
		if (clip.isRunning()){
			clip.stop();
		}
	}
	
	public void loop(){
		
		if (clip == null){
			return;
		}
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	
}
