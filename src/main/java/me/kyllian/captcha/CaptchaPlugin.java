package me.kyllian.captcha;

import me.kyllian.captcha.commands.CaptchaCommand;
import me.kyllian.captcha.handlers.CaptchaHandler;
import me.kyllian.captcha.handlers.MapHandler;
import me.kyllian.captcha.handlers.PlayerDataHandler;
import me.kyllian.captcha.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptchaPlugin extends JavaPlugin {

    private CaptchaHandler captchaHandler;
    private MapHandler mapHandler;
    private PlayerDataHandler playerDataHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        captchaHandler = new CaptchaHandler(this);
        mapHandler = new MapHandler();
        playerDataHandler = new PlayerDataHandler();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        getCommand("captcha").setExecutor(new CaptchaCommand(this));
    }

    public CaptchaHandler getCaptchaHandler() {
        return captchaHandler;
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerDataHandler;
    }
}
