package dev.jqstln.chatindiscord;

import dev.jqstln.chatindiscord.commands.ChatInDiscordCommand;
import dev.jqstln.chatindiscord.listeners.PlayerChatListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatInDiscord extends JavaPlugin {
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getLogger().info("ChatInDiscord has been enabled!");

        registerCommands();
        registerListeners();

        // bStats
        int pluginId = 18452;
        Metrics metrics = new Metrics(this, pluginId);
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