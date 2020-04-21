package me.kyllian.captcha.commands;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CaptchaCommand implements CommandExecutor {

    private CaptchaPlugin plugin;

    public CaptchaCommand(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!commandSender.hasPermission("captcha.reload")) {
                commandSender.sendMessage(plugin.getMessageHandler().getMessage("no-permission"));
                return true;
            }
            plugin.getCaptchaHandler().removeAllCaptchas();
            plugin.reloadConfig();
            plugin.getMessageHandler().reload();
            plugin.getMapHandler().loadData();
            plugin.getPlayerDataHandler().reloadPlayerData();
            commandSender.sendMessage(plugin.getMessageHandler().getMessage("reload"));
            return true;
        }
        commandSender.sendMessage(plugin.getMessageHandler().getMessage("help"));
        return true;
    }
}
