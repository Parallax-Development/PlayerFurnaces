package dev.darkblade.playerfurnaces.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .build();

    /**
     * Colorizes a string supporting MiniMessage tags, &#RRGGBB, <#RRGGBB>, and & legacy codes.
     */
    public static String colorize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // Convert &#RRGGBB format to <#RRGGBB>
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<#" + matcher.group(1) + ">");
        }
        matcher.appendTail(sb);
        String processed = sb.toString();

        // Convert legacy ampersand codes to legacy section symbols
        processed = ChatColor.translateAlternateColorCodes('&', processed);

        if (processed.contains("<") && processed.contains(">")) {
            try {
                Component component = MINI_MESSAGE.deserialize(processed);
                return LEGACY_SERIALIZER.serialize(component);
            } catch (Exception ignored) {
            }
        }

        return processed;
    }

    /**
     * Colorizes a list of strings.
     */
    public static List<String> colorize(List<String> list) {
        if (list == null) return new ArrayList<>();
        List<String> colored = new ArrayList<>();
        for (String line : list) {
            colored.add(colorize(line));
        }
        return colored;
    }

    /**
     * Applies a base64 texture string to a SkullMeta instance via reflection.
     */
    public static void applySkullTexture(SkullMeta skullMeta, String texture) {
        if (skullMeta == null || texture == null || texture.isEmpty()) return;
        try {
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");

            Object profile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), "");
            Object property = propertyClass.getConstructor(String.class, String.class).newInstance("textures", texture);

            Object propertiesMap = gameProfileClass.getMethod("getProperties").invoke(profile);
            propertiesMap.getClass().getMethod("put", Object.class, Object.class).invoke(propertiesMap, "textures", property);

            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, profile);
        } catch (Exception ignored) {
        }
    }
}
