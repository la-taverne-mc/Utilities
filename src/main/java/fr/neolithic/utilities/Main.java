package fr.neolithic.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.neolithic.utilities.commands.GamemodeExecutor;
import fr.neolithic.utilities.commands.HomesExecutor;
import fr.neolithic.utilities.commands.MiscellaneousExecutor;
import fr.neolithic.utilities.commands.SpawnExecutor;
import fr.neolithic.utilities.utils.Database;
import fr.neolithic.utilities.utils.FileManager;
import fr.neolithic.utilities.utils.back.PlayersLastLocation;

public class Main extends JavaPlugin {
    private Database db;

    private PlayersLastLocation playersLastLocation;

    private FileManager saveFile;
    private FileConfiguration saveContent;

    @Override
    public void onEnable() {
        loadDatabase();

        playersLastLocation = new PlayersLastLocation(db);

        registerCommands();

        System.out.println("[Utilities] Successfully Enabled Utilities v1.0");
    }

    @Override
    public void onDisable() {
        db.savePlayersLastLocations(playersLastLocation.serialize());

        System.out.println("[Utilities] Successfully Disabled Utilities v1.0");
    }

    private void registerCommands() {
        HomesExecutor homesExecutor = new HomesExecutor(db, playersLastLocation);
        getCommand("addmaxhomes").setExecutor(homesExecutor);
        getCommand("subtractmaxhomes").setExecutor(homesExecutor);
        getCommand("setmaxhomes").setExecutor(homesExecutor);
        getCommand("resetmaxhomes").setExecutor(homesExecutor);
        getCommand("sethome").setExecutor(homesExecutor);
        getCommand("delhome").setExecutor(homesExecutor);
        getCommand("homes").setExecutor(homesExecutor);
        getCommand("home").setExecutor(homesExecutor);
        getCommand("bed").setExecutor(homesExecutor);

        SpawnExecutor spawnExecutor = new SpawnExecutor(db, playersLastLocation);
        getCommand("setspawn").setExecutor(spawnExecutor);
        getCommand("delspawn").setExecutor(spawnExecutor);
        getCommand("spawn").setExecutor(spawnExecutor);

        MiscellaneousExecutor miscellaneousExecutor = new MiscellaneousExecutor(playersLastLocation);
        getCommand("back").setExecutor(miscellaneousExecutor);
        getCommand("fly").setExecutor(miscellaneousExecutor);
        getCommand("flyspeed").setExecutor(miscellaneousExecutor);
        getCommand("god").setExecutor(miscellaneousExecutor);
        getCommand("heal").setExecutor(miscellaneousExecutor);
        getCommand("feed").setExecutor(miscellaneousExecutor);

        GamemodeExecutor gamemodeExecutor = new GamemodeExecutor();
        getCommand("gm").setExecutor(gamemodeExecutor);
        getCommand("gms").setExecutor(gamemodeExecutor);
        getCommand("gmc").setExecutor(gamemodeExecutor);
        getCommand("gma").setExecutor(gamemodeExecutor);
        getCommand("gmsp").setExecutor(gamemodeExecutor);
    }

    private void loadDatabase() {
        saveFile = new FileManager(this, "database.yml");
        saveFile.saveDefault();
        saveContent = saveFile.getContent();

        String host = saveContent.getString("host");
        Integer port = saveContent.getInt("port");
        String database = saveContent.getString("database");
        String username = saveContent.getString("username");
        String password = saveContent.getString("password");

        if (isNullOrEmpty(host) || isNullOrEmpty(database) || isNullOrEmpty(username) || isNullOrEmpty(password) || port == null)
            return;

        db = new Database(this, host, port, database, username, password);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
