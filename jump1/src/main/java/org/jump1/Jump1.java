package org.jump1;

import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.File;

public class Jump1 extends JavaPlugin implements CommandExecutor {

    private JumpListener jumpListener;
    private double speed;
    private double height;
    private Material mat;
    private String name;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig(); // Сохраняет config.yml из ресурсов, если его нет
        loadConfigValues();
        getCommand("jumph").setExecutor(this);
        getCommand("launch").setExecutor(this);
        jumpListener = new JumpListener(this);
        getServer().getPluginManager().registerEvents(jumpListener, this);

        getServer().getLogger().info("Плагин успешно активирован!");
        getServer().getLogger().info("====================================================");
        getServer().getLogger().info("=     ......  ..  ....    ..   ......  ..  ......  =");
        getServer().getLogger().info("=       ..        .   .  .  .     ..  .  .    ..   =");
        getServer().getLogger().info("=       ..    ..  ...    .  .    ..   .  .   ..    =");
        getServer().getLogger().info("=       ..    ..  .  .   .  .   ..    .  .  ..     =");
        getServer().getLogger().info("=       ..    ..  .   .   ..   ..      ..  ..      =");
        getServer().getLogger().info("====================================================");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("Плагин деактивирован!");
        // Plugin shutdown logic
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        speed = config.getDouble("speed");
        height = config.getDouble("height");
        String materialName = config.getString("material.mat");
        mat = Material.getMaterial(materialName);
        name = config.getString("material.name");
    }

    private class JumpListener implements org.bukkit.event.Listener {
        private final JavaPlugin plugin;

        public JumpListener(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @org.bukkit.event.EventHandler
        public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
            Player player = event.getPlayer();
            Block block = player.getLocation().getBlock().getRelative(0, -1, 0);

            if (block.getType() == mat) {
                if (block.getState() instanceof Beacon) {
                    Beacon beacon = (Beacon) block.getState();
                    if (beacon.getCustomName() != null && ChatColor.stripColor(beacon.getCustomName()).equals(name)) {
                        performJump(player);
                    }
                } else {
                    // Handle other block types if necessary
                    performJump(player);
                }
            }
        }

        private void performJump(Player player) {
            World world = player.getWorld();
            Location north = player.getLocation().setDirection(new Vector(0, 0, -10));
            north.setPitch(0);

            Vector boostVector = new Vector(north.getDirection().getX() * speed, height, north.getDirection().getZ() * speed);
            player.setVelocity(boostVector);
        }
    }
}
