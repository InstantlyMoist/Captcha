package me.kyllian.captcha.spigot.listeners.login;

import fr.xephi.authme.events.LoginEvent;
import me.kyllian.captcha.spigot.CaptchaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LoginListener implements Listener {

    private CaptchaPlugin plugin;

    public LoginListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataHandler().loadPlayerDataFromPlayer(player);
        plugin.getCaptchaHandler().login(player);
    }
}
