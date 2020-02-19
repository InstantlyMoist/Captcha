package me.kyllian.captcha.captchas;

public interface Captcha {

    void send();
    CaptchaType getType();
    String getAnswer();

}
