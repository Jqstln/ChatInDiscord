package dev.jqstln.chatindiscord;

import dev.jqstln.chatindiscord.commands.ChatInDiscordCommand;
import dev.jqstln.chatindiscord.listeners.PlayerChatListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatInDiscord extends JavaPlugin {
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getLogger().info(ChatColor.AQUA + "ChatInDiscord has been enabled!");

        registerCommands();
        registerListeners();

        // bStats
        int pluginId = 18452;
        Metrics metrics = new Metrics(this, pluginId);

        new UpdateChecker(this, 109793).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info(ChatColor.RED + "There is no new update available.");
            } else {
                getLogger().info(ChatColor.GREEN + "There is a new update available. Download the new update at https://www.spigotmc.org/resources/chatindiscord.109793/");
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatInDiscord has been disabled!");
    }

    public void registerCommands() {
        getCommand("chatindiscord").setExecutor(new ChatInDiscordCommand(this));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
    }
}