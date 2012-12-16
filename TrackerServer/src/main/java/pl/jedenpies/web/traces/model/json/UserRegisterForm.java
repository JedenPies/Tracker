package pl.jedenpies.web.traces.model.json;

import javax.swing.text.html.ImageView;

import org.springframework.util.StringUtils;
import org.springframework.validation.ValidationUtils;

import pl.jedenpies.web.traces.validation.SyntaxValidator;

public class UserRegisterForm {

	private String username;
	private String email;
	private String password;
	private String password2;
	private ImageView captcha;
	private String captchaAnswer;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getCaptchaAnswer() {
		return captchaAnswer;
	}
	public void setCaptchaAnswer(String captchaAnswer) {
		this.captchaAnswer = captchaAnswer;
	}
	public ImageView getCaptcha() {
		return captcha;
	}
	public void setCaptcha(ImageView captcha) {
		this.captcha = captcha;
	}

}
