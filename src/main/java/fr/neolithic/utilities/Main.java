package fr.neolithic.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.neolithic.utilities.commands.HomesExecutor;
import fr.neolithic.utilities.commands.MiscellaneousExecutor;
import fr.neolithic.utilities.commands.SpawnExecutor;
import fr.neolithic.utilities.utilities.Database;
import fr.neolithic.utilities.utilities.FileManager;
import fr.neolithic.utilities.utilities.back.PlayersLastLocation;
import fr.neolithic.utilities.utilities.back.SerializedPlayerLastLocation;
import fr.neolithic.utilities.utilities.homes.Homes;
import fr.neolithic.utilities.utilities.homes.SerializedHome;

public class Main extends JavaPlugin {
    private Database db;

    private Homes homes;
    private PlayersLastLocation playersLastLocation = new PlayersLastLocation();

    private Location spawn = null;

    private FileManager saveFile;
    private FileConfiguration saveContent;

    @Override
    public void onEnable() {
        loadDatabase();

        homes = new Homes(db);
        loadHomes();
        loadPlayersLastLocations();

        loadSpawn();

        registerCommands();

        System.out.println("[Utilities] Successfully Enabled Utilities v1.0");
    }

    @Override
    public void onDisable() {
        savePlayersLastLocations();

        System.out.println("[Utilities] Successfully Disabled Utilities v1.0");
    }

    private void registerCommands() {
        HomesExecutor homesExecutor = new HomesExecutor(homes, playersLastLocation);
        getCommand("sethome").setExecutor(homesExecutor);
        getCommand("delhome").setExecutor(homesExecutor);
        getCommand("homes").setExecutor(homesExecutor);
        getCommand("home").setExecutor(homesExecutor);
        getCommand("bed").setExecutor(homesExecutor);

        SpawnExecutor spawnExecutor = new SpawnExecutor(spawn, playersLastLocation, db);
        getCommand("setspawn").setExecutor(spawnExecutor);
        getCommand("delspawn").setExecutor(spawnExecutor);
        getCommand("spawn").setExecutor(spawnExecutor);

        MiscellaneousExecutor miscellaneousExecutor = new MiscellaneousExecutor(playersLastLocation);
        getCommand("back").setExecutor(miscellaneousExecutor);
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
                SerializedHome serializedHome = new SerializedHome(resultSet.getString("uuid"), resultSet.getString("home"), resultSet.getString("worldUuid"),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
                serializedHomes.add(serializedHome);
            }

            homes.loadHomes(serializedHomes);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSpawn() {
        try {
            ResultSet resultSet = db.getSpawn();

            if (resultSet.next() && resultSet.getString("home").equalsIgnoreCase("spawn")) {
                spawn = new Location (Bukkit.getWorld(UUID.fromString(resultSet.getString("worldUuid"))),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"),
                    resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayersLastLocations() {
        try {
            ResultSet resultSet = db.getPlayersLastLocations();
            List<SerializedPlayerLastLocation> serializedPlayerLastLocations = Lists.newArrayList();

            while (resultSet.next()) {
                SerializedPlayerLastLocation serializedPlayerLastLocation = new SerializedPlayerLastLocation(resultSet.getString("playerUuid"), resultSet.getString("worldUuid"),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
                
                System.out.println(serializedPlayerLastLocation.toString());
                
                serializedPlayerLastLocations.add(serializedPlayerLastLocation);
            }

            if (!serializedPlayerLastLocations.isEmpty()) playersLastLocation.deserialize(serializedPlayerLastLocations);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void savePlayersLastLocations() {
        db.savePlayersLastLocations(playersLastLocation.serialize());
    }
}
