package dev.jqstln.chatindiscord.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatInDiscordCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public ChatInDiscordCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = showPrefix(this.plugin.getConfig().getString("Prefix"));
        String reload = this.plugin.getConfig().getString("Messages.ReloadCommand");
        String nopermission = this.plugin.getConfig().getString("Messages.NoPermission");
        String invalidusage = this.plugin.getConfig().getString("Messages.InvalidUsage");

        if (args.length == 0) {
            if(sender.hasPermission("chatindiscord.use")) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin("ChatInDiscord");
                String version = plugin.getDescription().getVersion();

                sender.sendMessage(prefix + "ChatInDiscord Help:");
                sender.sendMessage(prefix + ChatColor.AQUA + "/chatindiscord reload " + ChatColor.WHITE + "- Reloads the ChatInDiscord plugin.");
                sender.sendMessage(prefix);
                sender.sendMessage(prefix + ChatColor.GREEN + "Version: " + ChatColor.YELLOW + version);
            } else {
                sender.sendMessage(prefix + ChatColor.RED + nopermission);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("chatindiscord.reload")) {
                reloadConfiguration();
                sender.sendMessage(prefix + ChatColor.GREEN + reload);
            } else {
                sender.sendMessage(prefix + ChatColor.RED + nopermission);
            }
            return true;
        } else {
            sender.sendMessage(prefix + ChatColor.RED + invalidusage);
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("reload");
            return completions;
        } else {
            return Collections.emptyList();
        }
    }

    private void reloadConfiguration() {
        plugin.reloadConfig();
    }

    public String showPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}