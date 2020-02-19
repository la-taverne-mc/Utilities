package fr.neolithic.utilities.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            statement.execute(String.join("",
                "CREATE TABLE IF NOT EXISTS `utilities_homes` (",
                "`id` int(11) NOT NULL AUTO_INCREMENT, ",
                "`uuid` varchar(36) NOT NULL, ",
                "`home` varchar(16) NOT NULL, ",
                "`world` varchar(36) NOT NULL, ",
                "`x` double NOT NULL, ",
                "`y` double NOT NULL, ",
                "`z` double NOT NULL, ",
                "`yaw` float NOT NULL, ",
                "`pitch` float NOT NULL, ",
                "PRIMARY KEY (`id`)",
                ") CHARSET=latin1;"
            ));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHome(SerializedHome serializedHome) {
        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    statement.execute(String.join("",
                        "INSERT INTO `utilities_homes`(`uuid`, `home`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('",
                        serializedHome.uuid() + "','" + serializedHome.home() + "','" + serializedHome.world() + "',",
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
}
