package fr.neolithic.utilities;

import org.bukkit.plugin.java.JavaPlugin;

import fr.neolithic.utilities.commands.HomesExecutor;
import fr.neolithic.utilities.utilities.Homes;

public class Main extends JavaPlugin {
    private Homes homes = new Homes();

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
        HomesExecutor homesExecutor = new HomesExecutor(homes);
        getCommand("sethome").setExecutor(homesExecutor);
        getCommand("delhome").setExecutor(homesExecutor);
        getCommand("homes").setExecutor(homesExecutor);
        getCommand("home").setExecutor(homesExecutor);
        getCommand("bed").setExecutor(homesExecutor);
    }
}
