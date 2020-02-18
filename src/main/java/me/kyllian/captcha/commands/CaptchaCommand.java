package me.kyllian.captcha.commands;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaptchaCommand implements CommandExecutor {

    private CaptchaPlugin plugin;

    public CaptchaCommand(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //TODO: Write command structure
        Player player = (Player) commandSender;
        try {
            plugin.getCaptchaHandler().assignCaptcha(player);
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
        }
        return true;
    }
}
