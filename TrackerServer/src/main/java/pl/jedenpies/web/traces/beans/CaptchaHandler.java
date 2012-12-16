package pl.jedenpies.web.traces.beans;

import java.awt.image.BufferedImage;

import nl.captcha.Captcha;
import nl.captcha.text.producer.DefaultTextProducer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Creates and keeps captcha generated for given session.
 * @author Patryk Dobrowolski
 *
 */
@Component
@Scope("session")
public class CaptchaHandler {

	private static final int CAPTCHA_WIDTH = 200;
	private static final int CAPTCHA_HEIGHT = 50;
	
	/**
	 * Characters allowed in captcha 
	 */
	private static final char[] 
			CAPTCHA_CHARACTERS = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789".toCharArray();
	
	/**
	 * Length of captcha
	 */
	private static final int CAPTCHA_LENGTH = 7;
	
	private Captcha captcha;

	/**
	 * Creates new captcha.
	 * Saves it in session to later use.
	 */
	public void create() {
		captcha = new Captcha.Builder(CAPTCHA_WIDTH, CAPTCHA_HEIGHT)
			.addText(new DefaultTextProducer(CAPTCHA_LENGTH, CAPTCHA_CHARACTERS))			
			.addNoise()
			.addNoise()
			.build();
	}
	
	/**
	 * Checks if answer for current captcha is correct.
	 * @param text answer to check
	 * @return true if answer is correct, false otherwise
	 */
	public boolean isCorrect(String text) {
		return captcha == null ? false : captcha.isCorrect(text);
	}
	
	/**
	 * Clears captcha.
	 */
	public void clear() {
		this.captcha = null;
	}
	
	/**
	 * Returns current captcha as image.
	 * @return image of captcha, null if captcha has not been created.
	 */
	public BufferedImage getImage() {
		return captcha == null ? null : captcha.getImage();
	}
}
