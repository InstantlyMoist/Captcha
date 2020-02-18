package me.kyllian.captcha.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

import java.awt.image.BufferedImage;

public class MapHandler {

    public void sendMap(Player player, BufferedImage image) {
        ItemStack map = new ItemStack(Material.FILLED_MAP);

        MapView view = Bukkit.createMap(player.getWorld());
        for (MapRenderer mapRenderer : view.getRenderers()) view.removeRenderer(mapRenderer);

        view.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                mapCanvas.drawImage(0, 0, image);
            }
        });

        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setMapView(view);
        map.setItemMeta(mapMeta);

        player.getInventory().setItemInMainHand(map);
    }
}
