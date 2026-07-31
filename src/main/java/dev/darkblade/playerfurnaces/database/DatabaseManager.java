package dev.darkblade.playerfurnaces.database;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DatabaseManager {

    private final PlayerFurnacesPlugin plugin;
    private HikariDataSource dataSource;
    private ExecutorService executor;

    public DatabaseManager(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        String dbType = plugin.getConfig().getString("database.type", "SQLITE").toUpperCase();
        String dbFile = plugin.getConfig().getString("database.file", "furnaces.db");
        File file = new File(plugin.getDataFolder(), dbFile);

        HikariConfig config = new HikariConfig();
        if (dbType.equals("H2")) {
            config.setJdbcUrl("jdbc:h2:" + file.getAbsolutePath().replace(".db", "") + ";MODE=MySQL");
            config.setDriverClassName("org.h2.Driver");
        } else {
            config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
        }
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
        this.executor = Executors.newFixedThreadPool(4, r -> {
            Thread t = new Thread(r, "PlayerFurnaces-DB");
            t.setDaemon(true);
            return t;
        });

        createTables();
    }

    private void createTables() {
        String query = """
            CREATE TABLE IF NOT EXISTS player_furnaces (
                owner_uuid VARCHAR(36) NOT NULL,
                furnace_id INT NOT NULL,
                custom_name VARCHAR(64),
                input_item BLOB,
                fuel_item BLOB,
                output_item BLOB,
                cook_time INT DEFAULT 0,
                total_cook_time INT DEFAULT 200,
                burn_time INT DEFAULT 0,
                total_burn_time INT DEFAULT 0,
                last_updated BIGINT NOT NULL,
                PRIMARY KEY (owner_uuid, furnace_id)
            );
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not initialize database table", e);
        }
    }

    public CompletableFuture<List<VirtualFurnace>> loadFurnaces(UUID ownerUuid) {
        if (executor == null || executor.isShutdown()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return CompletableFuture.supplyAsync(() -> {
            List<VirtualFurnace> list = new ArrayList<>();
            if (dataSource == null || dataSource.isClosed()) {
                return list;
            }
            String query = "SELECT * FROM player_furnaces WHERE owner_uuid = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, ownerUuid.toString());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int furnaceId = rs.getInt("furnace_id");
                    VirtualFurnace furnace = new VirtualFurnace(ownerUuid, furnaceId);
                    furnace.setCustomName(rs.getString("custom_name"));

                    byte[] inputBytes = rs.getBytes("input_item");
                    byte[] fuelBytes = rs.getBytes("fuel_item");
                    byte[] outputBytes = rs.getBytes("output_item");

                    furnace.setInputItem(ItemSerializer.deserialize(inputBytes));
                    furnace.setFuelItem(ItemSerializer.deserialize(fuelBytes));
                    furnace.setOutputItem(ItemSerializer.deserialize(outputBytes));

                    furnace.setCookTime(rs.getInt("cook_time"));
                    furnace.setTotalCookTime(rs.getInt("total_cook_time"));
                    furnace.setBurnTime(rs.getInt("burn_time"));
                    furnace.setTotalBurnTime(rs.getInt("total_burn_time"));
                    furnace.setLastUpdatedTimestamp(rs.getLong("last_updated"));

                    list.add(furnace);
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error loading furnaces for " + ownerUuid, e);
            }
            return list;
        }, executor);
    }

    public CompletableFuture<Void> saveFurnace(VirtualFurnace furnace) {
        if (executor == null || executor.isShutdown()) {
            saveFurnaceSync(furnace);
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> saveFurnaceSync(furnace), executor);
    }

    public void saveFurnaceSync(VirtualFurnace furnace) {
        if (dataSource == null || dataSource.isClosed()) {
            return;
        }
        String query = """
            REPLACE INTO player_furnaces (
                owner_uuid, furnace_id, custom_name, input_item, fuel_item, output_item,
                cook_time, total_cook_time, burn_time, total_burn_time, last_updated
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, furnace.getOwnerUuid().toString());
            stmt.setInt(2, furnace.getFurnaceId());
            stmt.setString(3, furnace.getCustomName());
            stmt.setBytes(4, ItemSerializer.serialize(furnace.getInputItem()));
            stmt.setBytes(5, ItemSerializer.serialize(furnace.getFuelItem()));
            stmt.setBytes(6, ItemSerializer.serialize(furnace.getOutputItem()));
            stmt.setInt(7, furnace.getCookTime());
            stmt.setInt(8, furnace.getTotalCookTime());
            stmt.setInt(9, furnace.getBurnTime());
            stmt.setInt(10, furnace.getTotalBurnTime());
            stmt.setLong(11, furnace.getLastUpdatedTimestamp());

            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error saving furnace #" + furnace.getFurnaceId() + " for " + furnace.getOwnerUuid(), e);
        }
    }

    public void close() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
