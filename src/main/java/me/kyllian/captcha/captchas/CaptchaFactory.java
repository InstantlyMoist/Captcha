package me.kyllian.captcha.captchas;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.entity.Player;

public class CaptchaFactory {

    private CaptchaPlugin plugin;

    public CaptchaFactory(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    public Captcha getCaptcha(Player player) {
        //TODO: Randomize this when more captcha's are added.
        if (true) return new TextCaptcha(plugin, player);
        return null;
    }
}
