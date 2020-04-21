package me.kyllian.captcha;

import me.kyllian.captcha.commands.CaptchaCommand;
import me.kyllian.captcha.handlers.*;
import me.kyllian.captcha.listeners.*;
import me.kyllian.captcha.map.MapHandlerFactory;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptchaPlugin extends JavaPlugin {

    private CaptchaHandler captchaHandler;
    private MapHandler mapHandler;
    private MessageHandler messageHandler;
    private PlayerDataHandler playerDataHandler;
    private UpdateHandler updateHandler;

    @Override
    public void onEnable() {
        super.onEnable();
        Metrics metrics = new Metrics(this, 75891);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        saveResource("background.png", false);

        captchaHandler = new CaptchaHandler(this);
        mapHandler = new MapHandlerFactory(this).getMapHandler();
        messageHandler = new MessageHandler(this);
        playerDataHandler = new PlayerDataHandler(this);
        updateHandler = new UpdateHandler(this);

        loadListeners();

        getCommand("captcha").setExecutor(new CaptchaCommand(this));

        Bukkit.getOnlinePlayers().forEach(playerDataHandler::loadPlayerDataFromPlayer);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        captchaHandler.removeAllCaptchas();
    }

    public void loadListeners() {
        new InventoryClickListener(this);
        new PlayerChatListener(this);
        new PlayerCommandPreprocessListener(this);
        new PlayerDropItemListener(this);
        new PlayerInteractListener(this);
        new PlayerItemHeldListener(this);
        new PlayerJoinListener(this);
        new PlayerMoveListener(this);
        new PlayerQuitListener(this);
        new PlayerSwapHandItemsListener(this);

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

    public UpdateHandler getUpdateHandler() {
        return updateHandler;
    }
}
