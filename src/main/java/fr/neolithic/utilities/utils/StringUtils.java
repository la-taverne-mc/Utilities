package fr.neolithic.utilities.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String colorize(String str) {
        return str == null ? null : ChatColor.translateAlternateColorCodes('&', str);
    }
}
