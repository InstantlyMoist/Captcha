package me.kyllian.captcha;

import me.kyllian.captcha.commands.CaptchaCommand;
import me.kyllian.captcha.handlers.CaptchaHandler;
import me.kyllian.captcha.handlers.MapHandler;
import me.kyllian.captcha.handlers.MessageHandler;
import me.kyllian.captcha.handlers.PlayerDataHandler;
import me.kyllian.captcha.listeners.PlayerChatListener;
import me.kyllian.captcha.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptchaPlugin extends JavaPlugin {

    private CaptchaHandler captchaHandler;
    private MapHandler mapHandler;
    private MessageHandler messageHandler;
    private PlayerDataHandler playerDataHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        captchaHandler = new CaptchaHandler(this);
        mapHandler = new MapHandler(this);
        messageHandler = new MessageHandler(this);
        playerDataHandler = new PlayerDataHandler();

        loadListeners();


        getCommand("captcha").setExecutor(new CaptchaCommand(this));
    }

    public void loadListeners() {
        new PlayerChatListener(this);
        new PlayerJoinListener(this);
    }

    public CaptchaHandler getCaptchaHandler() {
        return captchaHandler;
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerDataHandler;
    }
}
