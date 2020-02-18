package fr.neolithic.utilities;

import org.bukkit.plugin.java.JavaPlugin;

import fr.neolithic.utilities.commands.HomesExecutor;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        registerCommands();

        System.out.println("[Utilities] Successfully Enabled Utilities v1.0");
    }

    @Override
    public void onDisable() {
        System.out.println("[Utilities] Successfully Disabled Utilities v1.0");
    }

    private void registerCommands() {
        HomesExecutor homesExecutor = new HomesExecutor();
        getCommand("bed").setExecutor(homesExecutor);
    }
}
