package fr.neolithic.utilities;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("[Utilities] Successfully Enabled Utilities v1.0");
    }

    @Override
    public void onDisable() {
        System.out.println("[Utilities] Successfully Disabled Utilities v1.0");
    }
}
