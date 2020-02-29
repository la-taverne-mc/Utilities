package fr.neolithic.utilities.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import fr.neolithic.utilities.utils.FileManager;
import fr.neolithic.utilities.utils.StringUtils;
import net.milkbowl.vault.chat.Chat;

public class ChatListeners implements Listener {
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String DISPLAYNAME_PLACEHOLDER = "{displayname}";
    private static final String MESSAGE_PLACEHOLDER = "{message}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";
    private static final String SUFFIX_PLACEHOLDER = "{suffix}";
    private static final String DEFAULT_FORMAT = "<" + PREFIX_PLACEHOLDER + NAME_PLACEHOLDER + SUFFIX_PLACEHOLDER + "&r> " + MESSAGE_PLACEHOLDER;

    private String format;
    private Chat vaultChat = null;

    private FileManager saveFile;
    private FileConfiguration saveContent;

    public ChatListeners() {
        saveFile = new FileManager(Bukkit.getPluginManager().getPlugin("Utilities"), "chat_config.yml");
        saveFile.saveDefault();
        saveContent = saveFile.getContent();
        reloadConfigValues();
        refreshVault();

    }

    private void reloadConfigValues() {
        format = StringUtils.colorize(saveContent.getString("format", DEFAULT_FORMAT)
                .replace(DISPLAYNAME_PLACEHOLDER, "%1$s")
                .replace(MESSAGE_PLACEHOLDER, "%2$s"));
    }

    private void refreshVault() {
        Chat vaultChat = Bukkit.getServicesManager().load(Chat.class);
        if (vaultChat != this.vaultChat) {
            Bukkit.getLogger().info("New Vault Chat implementation registered: " + (vaultChat == null ? "null" : vaultChat.getName()));
        }
        this.vaultChat = vaultChat;
    }

    @EventHandler
    public void onServiceChange(ServiceRegisterEvent event) {
        if (event.getProvider().getService() == Chat.class) {
            refreshVault();
        }
    }

    @EventHandler
    public void onServiceChange(ServiceUnregisterEvent event) {
        if (event.getProvider().getService() == Chat.class) {
            refreshVault();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLow(AsyncPlayerChatEvent event) {
        event.setFormat(StringUtils.colorize(format));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHigh(AsyncPlayerChatEvent event) {
        String format = event.getFormat();
        if (vaultChat != null && format.contains("{prefix}")) {
            format = format.replace("{prefix}", StringUtils.colorize(vaultChat.getPlayerPrefix(event.getPlayer())));
        }
        if (vaultChat != null && format.contains("{suffix}")) {
            format = format.replace("{suffix}", StringUtils.colorize(vaultChat.getPlayerSuffix(event.getPlayer())));
        }
        format = format.replace("{name}", event.getPlayer().getName());
        event.setFormat(format);
        if (event.getPlayer().hasPermission("utilities.chatcolors")) {
            event.setMessage(StringUtils.colorize(event.getMessage()));
        }
    }
}
