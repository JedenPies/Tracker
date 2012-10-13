package pl.jedenpies.web.tracker.beans;

import java.awt.image.BufferedImage;

import nl.captcha.Captcha;
import nl.captcha.text.producer.DefaultTextProducer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class CaptchaHandler {

	private static final int CAPTCHA_WIDTH = 200;
	private static final int CAPTCHA_HEIGHT = 50;
	private static final char[] 
			CAPTCHA_CHARACTERS = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789".toCharArray();
	private static final int CAPTCHA_LENGTH = 7;
	private Captcha captcha;
		
	public void create() {
		captcha = new Captcha.Builder(CAPTCHA_WIDTH, CAPTCHA_HEIGHT)
			.addText(new DefaultTextProducer(CAPTCHA_LENGTH, CAPTCHA_CHARACTERS))			
			.addNoise()
			.addNoise()
			.build();
	}
	
	public boolean isCorrect(String text) {
		return captcha.isCorrect(text);
	}
	
	public void clear() {
		this.captcha = null;
	}
	
	public BufferedImage getImage() {
		return captcha.getImage();
	}
}
