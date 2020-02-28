package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MapHandler {

    private CaptchaPlugin plugin;

    private File file;
    private FileConfiguration fileConfiguration;

    private Map<ItemStack, Boolean> mapsUsing;

    public MapHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        mapsUsing = new HashMap<>();
        file = new File(plugin.getDataFolder(), "maps.yml");
        if (!file.exists()) plugin.saveResource("maps.yml", false);
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        List<Integer> maps = fileConfiguration.getIntegerList("maps");
        if (maps.isEmpty()) {
            //TODO: Gen new maps
            Bukkit.getLogger().info("Captcha didn't find existing, predefined maps. Generating them, this may take some time...");
            World world = Bukkit.getWorld("world");
            for (int i = 0; i != 5; i++) {
                MapView mapView = Bukkit.createMap(world);
                maps.add(mapView.getId());
                Bukkit.getLogger().info("Added " + mapView.getId());
            }
            fileConfiguration.set("maps", maps);
            try {
                fileConfiguration.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        maps.forEach(mapID -> {
            //TODO: Load the itemstack for later usage.
            ItemStack map = new ItemStack(Material.FILLED_MAP);

            MapMeta mapMeta = (MapMeta) map.getItemMeta();
            mapMeta.setMapId(mapID);
            map.setItemMeta(mapMeta);

            mapsUsing.put(map, false);
        });
        Bukkit.getLogger().info("Loaded maps " + mapsUsing);

        //TODO: Check if plugin has loaded maps already, maps.yml?
    }

    public void sendMap(Player player, BufferedImage image) {
        ItemStack map = mapsUsing.entrySet()
                .stream()
                .filter(mapValue -> !mapValue.getValue())
                .findFirst()
                .get()
                .getKey();

        mapsUsing.put(map, true);

        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mapView = mapMeta.getMapView();

        for (MapRenderer mapRenderer : mapView.getRenderers()) mapView.removeRenderer(mapRenderer);

        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                mapCanvas.drawImage(0, 0, image);
            }
        });

        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);

        player.getInventory().setItemInMainHand(map);
    }

    public void resetMap(ItemStack map) {
        mapsUsing.put(map, false);
    }
}
