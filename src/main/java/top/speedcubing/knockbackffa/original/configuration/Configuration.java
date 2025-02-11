package top.speedcubing.knockbackffa.original.configuration;

import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;

public class Configuration {
    public static double centerX = 0;
    public static double centerY = 150;
    public static double centerZ = 0;
    public static double offsetY = 100;
    public static float yaw = 0;
    public static double holoX = 0;
    public static double holoZ = 0;
    public static int spawnY = 150;
    public static int spawnprotectY = spawnY - 4;

    public static void reload() {
        Configuration.centerX = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("x").getAsDouble();
        Configuration.centerZ = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("z").getAsDouble();
        Configuration.offsetY = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("y").getAsDouble();
        Configuration.yaw = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("yaw").getAsFloat();
        Configuration.holoX = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("holox").getAsDouble();
        Configuration.holoZ = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("holoz").getAsDouble();
        Configuration.spawnprotectY = ServerConfig.getConfig().getAsJsonObject("defaultkbffa").get("spawnproty").getAsInt();
    }

    @CubingEventHandler(priority = 30)
    public void configReloadEvent(ConfigReloadEvent e) {
        //reload();
    }
}
