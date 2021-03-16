package me.kyllian.captcha.spigot.map;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.handlers.MapHandler;
import me.kyllian.captcha.spigot.utilities.HandUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapHandlerNew implements MapHandler {

    private CaptchaPlugin plugin;

    private File file;
    private FileConfiguration fileConfiguration;

    private Map<ItemStack, Boolean> mapsUsing;


    public MapHandlerNew(CaptchaPlugin plugin) {
        this.plugin = plugin;
        loadData();
    }

    public void loadData() {
        mapsUsing = new HashMap<>();
        file = new File(plugin.getDataFolder(), "maps.yml");
        if (!file.exists()) plugin.saveResource("maps.yml", false);
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        List<Integer> maps = fileConfiguration.getIntegerList("maps");
        int mapAmount = plugin.getConfig().getInt("captcha-settings.maps");
        int currentMapAmount = maps.size();
        if (mapAmount > currentMapAmount) {
            Bukkit.getLogger().info("Captcha didn't find existing, predefined maps. Generating them, this may take some time...");
            World world = Bukkit.getWorlds().get(0);
            for (int i = 0; i != mapAmount - currentMapAmount; i++) {
                MapView mapView = Bukkit.createMap(world);
                maps.add((int) mapView.getId());
            }
            fileConfiguration.set("maps", maps);
            world.save();
            try {
                fileConfiguration.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        maps.forEach(mapID -> {
            ItemStack map = new ItemStack(Material.valueOf("FILLED_MAP"));
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapId((int) mapID.shortValue());
            map.setItemMeta(meta);
            //map.setDurability(mapID.shortValue());
            mapsUsing.put(map, false);
        });
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
        mapView.getRenderers().clear();

        mapView.addRenderer(new MapRenderer() {
            boolean rendered = false;
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                if (rendered) return;
                mapCanvas.drawImage(0, 0, image);
                rendered = true;
            }
        });
        map.setItemMeta(mapMeta);
        HandUtils.setItemInHand(player, map);
    }

    public void resetMap(ItemStack map) {
        mapsUsing.put(map, false);
    }

    @Override
    public FileConfiguration getMaps() {
        return fileConfiguration;
    }
}
