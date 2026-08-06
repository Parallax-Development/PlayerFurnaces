package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.util.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageManager {

    private final PlayerFurnacesPlugin plugin;
    private File messagesFile;
    private FileConfiguration messagesConfig;
    private String prefix = "";

    public MessageManager(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    public void loadMessages() {
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        this.prefix = colorize(messagesConfig.getString("prefix", "&8[&ePlayerFurnaces&8] "));
    }

    public void reloadMessages() {
        loadMessages();
    }

    public String getRawMessage(String key) {
        if (messagesConfig == null) return "";
        return messagesConfig.getString(key, "&cMessage missing: " + key);
    }

    public String getMessage(String key, boolean withPrefix, String... replacements) {
        String msg = getRawMessage(key);
        msg = applyReplacements(msg, replacements);
        msg = colorize(msg);
        return withPrefix ? prefix + msg : msg;
    }

    public void sendMessage(CommandSender sender, String key, String... replacements) {
        sender.sendMessage(getMessage(key, true, replacements));
    }

    public void sendRawMessage(CommandSender sender, String key, String... replacements) {
        sender.sendMessage(getMessage(key, false, replacements));
    }

    public void sendListMessage(CommandSender sender, String key, String... replacements) {
        for (String line : getMessageList(key, replacements)) {
            sender.sendMessage(line);
        }
    }

    public List<String> getMessageList(String key, String... replacements) {
        List<String> result = new java.util.ArrayList<>();
        if (messagesConfig == null) return result;
        List<String> lines = messagesConfig.getStringList(key);
        for (String line : lines) {
            String processed = applyReplacements(line, replacements);
            result.add(colorize(processed));
        }
        return result;
    }

    private String applyReplacements(String text, String... replacements) {
        if (replacements == null || replacements.length < 2) {
            return text;
        }
        for (int i = 0; i < replacements.length - 1; i += 2) {
            String target = replacements[i];
            String replacement = replacements[i + 1];
            if (target != null && replacement != null) {
                text = text.replace(target, replacement);
            }
        }
        return text;
    }

    private String colorize(String text) {
        return ColorUtils.colorize(text);
    }
}
