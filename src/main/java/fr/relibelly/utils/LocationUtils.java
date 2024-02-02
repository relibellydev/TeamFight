package fr.relibelly.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static String loc2str(Location location) {
        return location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location str2loc(String str, String world) {
        String[] split = str.split(":");
        return new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
    }
}
