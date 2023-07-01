package dev.jqstln.chatindiscord.listeners;

import dev.jqstln.chatindiscord.ChatInDiscord;
import dev.jqstln.chatindiscord.discord.DiscordWebhook;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
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
        String message = e.getMessage();
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        boolean useEmbed = plugin.getConfig().getBoolean("EmbedOptions.Enabled");

        if (webhookURLs == null || webhookURLs.isEmpty()) {
            if (player.hasPermission("chatindiscord.showerror")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GRAY + error);
            }
            return;
        }

        if (message.matches(".*@(\\S+).*")) {
            player.sendMessage(ChatColor.RED + notallowed);
            e.setCancelled(true);
            return;
        }

        for (String webhookURL : webhookURLs) {
            DiscordWebhook webhook = new DiscordWebhook(webhookURL);

            if (useEmbed) {
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setDescription(message)
                        .setAuthor(player.getName(), "", "https://crafatar.com/avatars/" + playerUUID + "?size=512&overlay")
                        .setColor(Color.decode(plugin.getConfig().getString("EmbedOptions.Color"))));
            } else {
                webhook.setContent(player.getName() + ": " + message);
            }

            try {
                webhook.execute();
            } catch (IOException ex) {
                // Bukkit.getLogger().severe(Arrays.toString(ex.getStackTrace()));
            }
        }
    }
}