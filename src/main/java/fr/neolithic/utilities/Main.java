package fr.neolithic.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.neolithic.utilities.commands.HomesExecutor;
import fr.neolithic.utilities.utilities.Database;
import fr.neolithic.utilities.utilities.FileManager;
import fr.neolithic.utilities.utilities.Homes;
import fr.neolithic.utilities.utilities.SerializedHome;

public class Main extends JavaPlugin {
    private Homes homes;

    private FileManager saveFile;
    private FileConfiguration saveContent;

    Database db;

    @Override
    public void onEnable() {
        loadDatabase();

        homes = new Homes(db);
        loadHomes();

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

    private void loadHomes() {
        try {
            ResultSet resultSet = db.getHomes();
            List<SerializedHome> serializedHomes = Lists.newArrayList();

            while (resultSet.next()) {
                SerializedHome serializedHome = new SerializedHome(resultSet.getString("uuid"), resultSet.getString("home"), resultSet.getString("world"),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
                serializedHomes.add(serializedHome);
            }

            homes.loadHomes(serializedHomes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
