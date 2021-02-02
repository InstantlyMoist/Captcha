package me.kyllian.captcha.bungee.listeners;

import me.kyllian.captcha.bungee.CaptchaPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    private CaptchaPlugin plugin;

    public ChatListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (!plugin.inCaptchaMap.get(player.getUniqueId().toString())) return;
        if (event.getMessage().startsWith("/")) event.setCancelled(true);
    }
}
