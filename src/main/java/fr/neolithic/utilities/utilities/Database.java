package fr.neolithic.utilities.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.back.SerializedPlayerLastLocation;
import fr.neolithic.utilities.utilities.homes.SerializedHome;

public class Database {
    private Plugin plugin;

    private Connection connection;
    private String host, database, username, password;
    private int port;

    private Statement statement;

    public Database(@NotNull Plugin plugin, @NotNull String host, @NotNull int port, @NotNull String database, @NotNull String username, @NotNull String password) {
        this.plugin = plugin;
        
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        try {
            openConnection();
            statement = connection.createStatement();
            createDefaultTable();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) return;

        synchronized (this) {
            if (connection != null && !connection.isClosed()) return;

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        }
    }

    private void createDefaultTable() {
        try {
            statement.addBatch(String.join("",
                "CREATE TABLE IF NOT EXISTS `utilities_homes` (",
                "`id` int(11) NOT NULL AUTO_INCREMENT, ",
                "`uuid` varchar(36) NOT NULL, ",
                "`home` varchar(16) NOT NULL, ",
                "`worldUuid` varchar(36) NOT NULL, ",
                "`x` double NOT NULL, ",
                "`y` double NOT NULL, ",
                "`z` double NOT NULL, ",
                "`yaw` float NOT NULL, ",
                "`pitch` float NOT NULL, ",
                "PRIMARY KEY (`id`)",
                ") CHARSET=latin1;"
            ));

            statement.addBatch(String.join("",
                "CREATE TABLE IF NOT EXISTS `utilities_last_locations` (",
                "`playerUuid` varchar(36) NOT NULL, ",
                "`worldUuid` varchar(36) NOT NULL, ",
                "`x` double NOT NULL, ",
                "`y` double NOT NULL, ",
                "`z` double NOT NULL, ",
                "`yaw` float NOT NULL, ",
                "`pitch` float NOT NULL, ",
                "PRIMARY KEY (`playerUuid`)",
                ") CHARSET=latin1;"
            ));

            statement.addBatch(String.join("",
                "CREATE TABLE IF NOT EXISTS `utilities_max_homes` (",
                "`playerUuid` varchar(36) NOT NULL, ",
                "`maxHomes` int(11) NOT NULL, ",
                "`isRelative` tinyint(1) NOT NULL, ",
                "PRIMARY KEY (`playerUuid`)",
                ") CHARSET=latin1;"
            ));

            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            statement.execute(String.join("",
                "INSERT INTO `utilities_homes`(`id`, `uuid`, `home`, `worldUuid`, `x`, `y`, `z`, `yaw`, `pitch`) ",
                "VALUES (1,'00000000-0000-0000-0000-000000000000','notspawn','00000000-0000-0000-0000-000000000000',0,0,0,0,0) ",
                "ON DUPLICATE KEY UPDATE `id` = id"
            ));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMaxHome(@NotNull String playerUuid, @NotNull int amount) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "INSERT INTO `utilities_max_homes`(`playerUuid`, `maxHomes`, `isRelative`) VALUES ('",
                        playerUuid + "', ",
                        amount + ", ",
                        "true) ",
                        "ON DUPLICATE KEY UPDATE ",
                        "`maxHomes` = maxHomes + " + amount + ", ",
                        "`isRelative` = true"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public void subtractMaxHome(@NotNull String playerUuid, @NotNull int amount) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "INSERT INTO `utilities_max_homes`(`playerUuid`, `maxHomes`, `isRelative`) VALUES ('",
                        playerUuid + "', ",
                        "-" + amount + ", ",
                        "true) ",
                        "ON DUPLICATE KEY UPDATE ",
                        "`maxHomes` = maxHomes - " + amount + ", ",
                        "`isRelative` = true"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public void setMaxHome(@NotNull String playerUuid, @NotNull int amount) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "INSERT INTO `utilities_max_homes`(`playerUuid`, `maxHomes`, `isRelative`) VALUES ('",
                        playerUuid + "', ",
                        amount + ", ",
                        "true) ",
                        "ON DUPLICATE KEY UPDATE ",
                        "`maxHomes` = maxHomes + " + amount + ", ",
                        "`isRelative` = false"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public void resetMaxHome(@NotNull String playerUuid) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "DELETE FROM `utilities_max_homes` WHERE ",
                        "`playerUuid` = '" + playerUuid + "'"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public @Nullable ResultSet getMaxHomes() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `utilities_max_homes`");
            return resultSet;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addHome(SerializedHome serializedHome) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "INSERT INTO `utilities_homes`(`uuid`, `home`, `worldUuid`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('",
                        serializedHome.uuid() + "','" + serializedHome.home() + "','" + serializedHome.worldUuid() + "',",
                        serializedHome.x() + "," + serializedHome.y() + "," + serializedHome.z() + ",",
                        serializedHome.yaw() + "," + serializedHome.pitch() + ");"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public void deleteHome(@NotNull String playerUuid, @NotNull String home) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "DELETE FROM `utilities_homes` WHERE ",
                        "`uuid` = '" + playerUuid + "' AND ",
                        "`home` = '" + home + "'"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public @Nullable ResultSet getHomes() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `utilities_homes` WHERE NOT `id` = 1");
            return resultSet;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSpawn(@NotNull String worldUuid, @NotNull double x, @NotNull double y, @NotNull double z, @NotNull float yaw, @NotNull float pitch) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "UPDATE `utilities_homes` SET ",
                        "`home` = 'spawn', ",
                        "`worldUuid` = '" + worldUuid + "', ",
                        "`x` = " + x + ", ",
                        "`y` = " + y + ", ",
                        "`z` = " + z + ", ",
                        "`yaw` = " + yaw + ", ",
                        "`pitch` = " + pitch + " ",
                        "WHERE `id` = 1"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public @Nullable ResultSet getSpawn() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `utilities_homes` WHERE `id` = 1");
            return resultSet;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteSpawn() {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "UPDATE `utilities_homes` SET ",
                        "`home` = 'notspawn' ",
                        "WHERE `id` = 1"
                    ));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        runnable.runTaskAsynchronously(plugin);
    }

    public @Nullable ResultSet getPlayersLastLocations() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `utilities_last_locations`");
            return resultSet;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void savePlayersLastLocations(List<SerializedPlayerLastLocation> serializedPlayerLastLocations) {
        try {
            if (serializedPlayerLastLocations != null) {
                for (SerializedPlayerLastLocation serializedPlayerLastLocation : serializedPlayerLastLocations) {
                    statement.addBatch(String.join("",
                        "INSERT INTO `utilities_last_locations`(`playerUuid`, `worldUuid`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('",
                        serializedPlayerLastLocation.playerUuid() + "', '",
                        serializedPlayerLastLocation.worldUuid() + "', ",
                        serializedPlayerLastLocation.x() + ", ",
                        serializedPlayerLastLocation.y() + ", ",
                        serializedPlayerLastLocation.z() + ", ",
                        serializedPlayerLastLocation.yaw() + ", ",
                        serializedPlayerLastLocation.pitch() + ") ",
                        "ON DUPLICATE KEY UPDATE ",
                        "`worldUuid` = '" + serializedPlayerLastLocation.worldUuid() + "', ",
                        "`x` = " + serializedPlayerLastLocation.x() + ", ",
                        "`y` = " + serializedPlayerLastLocation.y() + ", ",
                        "`z` = " + serializedPlayerLastLocation.z() + ", ",
                        "`yaw` = " + serializedPlayerLastLocation.yaw() + ", ",
                        "`pitch` = " + serializedPlayerLastLocation.pitch()
                    ));
                }
    
                statement.executeBatch();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
