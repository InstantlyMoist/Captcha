package me.kyllian.captcha.bungee;

import me.kyllian.captcha.bungee.listeners.ChatListener;
import me.kyllian.captcha.bungee.listeners.PluginMessageListener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class CaptchaPlugin extends Plugin {

    public Map<String, Boolean> inCaptchaMap; // For easy data structure

    @Override
    public void onEnable() {
        inCaptchaMap = new HashMap<>();

        getProxy().registerChannel("kyllian:captcha");

        new PluginMessageListener(this);
        new ChatListener(this);
    }
}
