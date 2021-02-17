package me.kyllian.captcha.spigot.commands;

import me.kyllian.captcha.spigot.CaptchaPlugin;
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
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
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
            if (args[0].equalsIgnoreCase("setsafearea")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(plugin.getMessageHandler().getMessage("player-only"));
                    return true;
                }
                if (!commandSender.hasPermission("captcha.setsafearea")) {
                    commandSender.sendMessage(plugin.getMessageHandler().getMessage("no-permission"));
                    return true;
                }
                Player player = (Player) commandSender;
                plugin.getSafeArea().setSafeLocation(player.getLocation());
                player.sendMessage(plugin.getMessageHandler().getMessage("safe-area-set"));
                return true;
            }
        }
        commandSender.sendMessage(plugin.getMessageHandler().getMessage("help"));
        return true;
    }
}
