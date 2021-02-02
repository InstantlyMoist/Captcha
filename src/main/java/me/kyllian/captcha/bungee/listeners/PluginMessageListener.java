package me.kyllian.captcha.bungee.listeners;

import me.kyllian.captcha.bungee.CaptchaPlugin;
import me.kyllian.captcha.shared.data.StateData;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class PluginMessageListener implements Listener {

    private CaptchaPlugin plugin;

    public PluginMessageListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("kyllian:captcha")) return;
        try {
            byte b[] = event.getData();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            StateData obj = (StateData) si.readObject();
            plugin.inCaptchaMap.put(obj.getUUID(), obj.isInCaptcha());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
