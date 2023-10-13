package dev.jqstln.chatindiscord.listeners;

import dev.jqstln.chatindiscord.ChatInDiscord;
import dev.jqstln.chatindiscord.discord.DiscordWebhook;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class PlayerChatListener implements Listener {
    private final ChatInDiscord plugin;

    public PlayerChatListener(ChatInDiscord plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        List<String> webhookURLs = plugin.getConfig().getStringList("IntegrationUrls");
        String prefix = plugin.getConfig().getString("Prefix");
        String error = plugin.getConfig().getString("Messages.ShowError");
        String notallowed = plugin.getConfig().getString("Messages.NotAllowed");
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        boolean useEmbed = plugin.getConfig().getBoolean("EmbedOptions.Enabled");

        if (webhookURLs == null || webhookURLs.isEmpty()) {
            if (player.hasPermission("chatindiscord.showerror")) {
                player.sendMessage(formatMessage(prefix, ChatColor.GRAY + error));
            }
            return;
        }

        String message = e.getMessage();

        if (message.matches(".*@(\\S+).*")) {
            player.sendMessage(ChatColor.RED + notallowed);
            e.setCancelled(true);
            return;
        }

        for (String webhookURL : webhookURLs) {
            DiscordWebhook webhook = new DiscordWebhook(webhookURL);

            if (useEmbed) {
                String avatarURL = "https://crafatar.com/avatars/" + playerUUID + "?size=512&overlay";
                Color embedColor = Color.decode(plugin.getConfig().getString("EmbedOptions.Color"));

                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
                        .setDescription(message)
                        .setAuthor(player.getName(), "", avatarURL)
                        .setColor(embedColor);

                webhook.addEmbed(embedObject);
            } else {
                String chatMessage = player.getName() + ": " + message;
                webhook.setContent(chatMessage);
            }

            try {
                webhook.execute();
            } catch (IOException ex) {
                // Handle the exception (e.g., log it)
            }
        }
    }

    private String formatMessage(String prefix, String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix) + message;
    }
}