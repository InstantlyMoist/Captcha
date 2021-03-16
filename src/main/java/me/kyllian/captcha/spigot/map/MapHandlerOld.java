package me.kyllian.captcha.spigot.map;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.handlers.MapHandler;
import me.kyllian.captcha.spigot.utilities.HandUtils;
import me.kyllian.captcha.spigot.utilities.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapHandlerOld implements MapHandler {

    private CaptchaPlugin plugin;

    private File file;
    private FileConfiguration fileConfiguration;

    private Map<ItemStack, Boolean> mapsUsing;

    public MapHandlerOld(CaptchaPlugin plugin) {
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
                maps.add(MapUtils.getMapId(mapView));
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
            ItemStack map = new ItemStack(Material.MAP);
            map.setDurability(mapID.shortValue());
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

        MapView finalView = null;
        Class bukkitClass = null;

        try {
            bukkitClass = Class.forName("org.bukkit.Bukkit");
            Method getMapInt = bukkitClass.getMethod("getMap", int.class);
            finalView = (MapView) getMapInt.invoke(bukkitClass, new Object[] {map.getDurability()});
        } catch (Exception exception) {
            try {
                Method getMapShort = bukkitClass.getMethod("getMap", short.class);
                finalView = (MapView) getMapShort.invoke(bukkitClass, new Object[] {map.getDurability()});
            } catch (Exception otherException) {
                otherException.printStackTrace();
            }
        }
        finalView.getRenderers().clear();

        finalView.addRenderer(new MapRenderer() {
            boolean rendered = false;
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                if (rendered) return;
                mapCanvas.drawImage(0, 0, image);
                rendered = true;
            }
        });
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
