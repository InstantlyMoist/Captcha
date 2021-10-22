package me.kyllian.captcha.spigot.utilities;

import org.bukkit.map.MapView;

import java.lang.reflect.Method;

public class MapUtils {

    public static int getMapId(MapView mapView) {
        try {
            Method method = mapView.getClass().getMethod("getId");
            Object mapId = method.invoke(mapView);
            if (mapId instanceof Short) {
                return ((Short) mapId).intValue();
            } else {
                return ((Integer) mapId).intValue();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
