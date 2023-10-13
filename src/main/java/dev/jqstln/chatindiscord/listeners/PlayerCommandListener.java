package dev.jqstln.chatindiscord.listeners;

import dev.jqstln.chatindiscord.ChatInDiscord;
import dev.jqstln.chatindiscord.discord.DiscordWebhook;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class PlayerCommandListener implements Listener {
    private final ChatInDiscord plugin;

    public PlayerCommandListener(ChatInDiscord plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandExecute(PlayerCommandPreprocessEvent e) {
        List<String> webhookURLs = plugin.getConfig().getStringList("IntegrationUrls");
        String prefix = plugin.getConfig().getString("Prefix");
        String error = plugin.getConfig().getString("Messages.ShowError");
        String notallowed = plugin.getConfig().getString("Messages.NotAllowed");
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        if (webhookURLs == null || webhookURLs.isEmpty()) {
            sendErrorMessage(player, prefix, error);
            return;
        }

        String command = e.getMessage();

        if (containsMention(command)) {
            sendErrorMessage(player, ChatColor.RED + notallowed);
            e.setCancelled(true);
            return;
        }

        boolean useEmbed = plugin.getConfig().getBoolean("EmbedOptions.Enabled");

        for (String webhookURL : webhookURLs) {
            DiscordWebhook webhook = new DiscordWebhook(webhookURL);

            if (useEmbed) {
                String avatarURL = "https://crafatar.com/avatars/" + playerUUID + "?size=512&overlay";
                Color embedColor = Color.decode(plugin.getConfig().getString("EmbedOptions.Color"));

                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
                        .setDescription(player.getName() + " executed command: " + command)
                        .setAuthor(player.getName(), "", avatarURL)
                        .setColor(embedColor);

                webhook.addEmbed(embedObject);
            } else {
                String chatMessage = player.getName() + " executed command: " + command;
                webhook.setContent(chatMessage);
            }

            try {
                webhook.execute();
            } catch (IOException ex) {
                // Handle the exception (e.g., log it)
            }
        }
    }

    private void sendErrorMessage(Player player, String message) {
        player.sendMessage(message);
    }

    private void sendErrorMessage(Player player, String prefix, String message) {
        sendErrorMessage(player, ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GRAY + message);
    }

    private boolean containsMention(String message) {
        return message.matches(".*@(\\S+).*");
    }
}