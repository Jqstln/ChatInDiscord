package dev.jqstln.chatindiscord;

import dev.jqstln.chatindiscord.commands.ChatInDiscordCommand;
import dev.jqstln.chatindiscord.listeners.PlayerChatListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatInDiscord extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        this.getLogger().info("ChatInDiscord has been enabled!");

        // Commands && Listeners
        this.registerCommands();

        // bStats
        int pluginId = 18452; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
    }
    @Override
    public void onDisable() {
        this.getLogger().info("ChatInDiscord has been disabled!");
    }

    public void registerCommands() {
        getCommand("chatindiscord").setExecutor(new ChatInDiscordCommand(this));
    }
}
