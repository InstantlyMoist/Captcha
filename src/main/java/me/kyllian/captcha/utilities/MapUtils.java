package me.kyllian.captcha.utilities;

import org.bukkit.map.MapView;

import java.lang.reflect.Method;

public class MapUtils {

    public static int getMapId(MapView mapView) {
        try {
            Method method = mapView.getClass().getMethod("getId");
            return ((Short)method.invoke(mapView)).intValue();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
