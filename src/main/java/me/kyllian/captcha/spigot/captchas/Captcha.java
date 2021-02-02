package me.kyllian.captcha.spigot.captchas;

public interface Captcha {

    void send();
    CaptchaType getType();
    String getAnswer();

}
