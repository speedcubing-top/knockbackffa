package top.speedcubing.knockbackffa.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MapManager {
    //Add other map you need modify getMapCommandByMapName
    private static final String NETHER_LAND = "https://i.imgur.com/xdA4XEy.png";
    private static final String FLOATING_CASTLE = "https://i.imgur.com/TpKmqwO.png";
    private static final String SANDSTONE_ROADS_1 = "https://i.imgur.com/p4qEziJ.png";
    private static final String PP_LAND = "https://i.imgur.com/ek1zZSk.png";
    private static final String GIFT_BOXES = "https://i.imgur.com/fmlU2ei.png";
    private static final String SANDSTONE_ROAD_2 = "https://i.imgur.com/9zoiZGM.png";
    private static final String SEASONS = "https://i.imgur.com/sSGtu6O.png";
    private static final String FOOTBALL_FIELD = "https://i.imgur.com/s4DyC1X.png";
    private static final String ZABER_THE_BUILDER = "https://i.imgur.com/kCPJtjD.png";
    //private static final String FROZEN_EARTH = "https://i.imgur.com/MtlkSAa.png";
    private static final String HOT_AIR_BALLOON = "https://i.imgur.com/eQwAkqj.png";

    public static String getMapByName(String mapName) {
        try {
            Field field = MapManager.class.getDeclaredField(mapName);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static List<String> getAllMapUrls() {
        List<String> mapUrls = new ArrayList<>();
        Field[] fields = MapManager.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                String fieldValue;
                try {
                    fieldValue = (String) field.get(null);
                    mapUrls.add(fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapUrls;
    }

    public static List<String> getAllMapNames() {
        List<String> mapNames = new ArrayList<>();
        Field[] fields = MapManager.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                String fieldName = field.getName();
                mapNames.add(fieldName);
            }
        }
        return mapNames;
    }

    public static String getMapCommandByMapName(String mapName) {
        switch (mapName) {
            case "NETHER_LAND":
                return "map 2000 0 50 135 2002.5 -2.5";
            case "FLOATING_CASTLE":
                return "map 1000 -1000 50 0 1003.5 -996.5";
            case "SANDSTONE_ROADS_1":
                return "map 1000 1000 50 -180 997.5 997.5";
            case "PP_LAND":
                return "map 0 0 100 -90 3.5 -2.5";
            case "GIFT_BOXES":
                return "map -1000 -1000 100 0 -997.5 -997.5";
            case "SANDSTONE_ROAD_2":
                return "map 10000 10000 100 180 126.5 38.5";
            case "SEASONS":
                return "map -1000 1000 100 -90 -996.5 1009.5";
            case "FOOTBALL_FIELD":
                return "map 4000 4000 100 180 3997.5 3997.5";
            case "ZABER_THE_BUILDER":
                return "map 20000 20000 100 180 19998.5 19998.5";
//            case "FROZEN_EARTH":
//                return "map 2000 1000 25 -99 2002.5 998.5";
            case "HOT_AIR_BALLOON":
                return "map 2023 1225 50 -45 2026.5 1226.5";
            default:
                System.out.println("Occurred Error, could not found map command");
                return null;
        }
    }

    public static void open(Player player) {
        Inventory mapInv = Bukkit.createInventory(null, 36, "Map Vote");
        AtomicInteger count = new AtomicInteger(0);

        MapManager.getAllMapNames().forEach(mapName -> {
            MapView mapView = Bukkit.createMap(player.getWorld());
            ItemStack map = new ItemStack(Material.MAP, 1, mapView.getId());
            ItemMeta meta = map.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add("§eLeft Click to vote!");
            lore.add("§eRight Click to view!");
            lore.add("§eVote counts: " + VoteManager.getVoteCountByMapName(mapName));

            meta.setDisplayName("§b" + mapName);
            meta.setLore(lore);
            map.setItemMeta(meta);
            mapInv.setItem(count.getAndIncrement(), map);
        });

        player.openInventory(mapInv);
    }

}
