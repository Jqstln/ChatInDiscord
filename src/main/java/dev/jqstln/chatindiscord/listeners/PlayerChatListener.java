package dev.jqstln.chatindiscord.listeners;

import dev.jqstln.chatindiscord.ChatInDiscord;
import dev.jqstln.chatindiscord.discord.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class PlayerChatListener implements Listener {
    private final ChatInDiscord plugin;

    public PlayerChatListener(ChatInDiscord plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String webhookURL = this.plugin.getConfig().getString("IntegrationUrl");
        String prefix = this.plugin.getConfig().getString("Prefix");
        String error = this.plugin.getConfig().getString("Messages.ShowError");
        String message = e.getMessage();
        String player = e.getPlayer().getName();
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);

        boolean useEmbed = this.plugin.getConfig().getBoolean("DiscordEmbed");

        if (webhookURL == null || webhookURL.isEmpty()) {
            Player sender = e.getPlayer();
            if (sender.hasPermission("chatindiscord.showerror")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GRAY + error);
            }
            return;
        }

        if (useEmbed) {
            webhook.addEmbed((new DiscordWebhook.EmbedObject()).setDescription(player + ": " + message).setColor(Color.CYAN));
        } else {
            webhook.setContent(player + ": " + message);
        }

        try {
            webhook.execute();
        } catch (IOException var7) {
            Bukkit.getLogger().severe(Arrays.toString(var7.getStackTrace()));
        }
    }

}
