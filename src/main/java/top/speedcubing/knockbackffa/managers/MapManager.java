package top.speedcubing.knockbackffa.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

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
    private static final String FROZEN_EARTH = "https://i.imgur.com/MtlkSAa.png";
    private static final String HOT_AIR_BALLOON = "https://i.imgur.com/eQwAkqj.png";
    private static final String INVASION = "";
    private static final String BOX_CITY = "";
    private static final String GAMING = "";
    private static final String ROMAN_ARENA = "";
    private static final String CRUSHERS = "";

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
                return "map 2000 0 50 135 2002.5 -2.5 150";
            case "FLOATING_CASTLE":
                return "map 1000 -1000 50 0 1003.5 -996.5 150";
            case "SANDSTONE_ROADS_1":
                return "map 1000 1000 50 -180 997.5 997.5 150";
            case "PP_LAND":
                return "map 0 0 100 -90 3.5 -2.5 150";
            case "GIFT_BOXES":
                return "map -1000 -1000 100 0 -997.5 -997.5 150";
            case "SANDSTONE_ROAD_2":
                return "map 10000 10000 100 180 126.5 38.5 150";
            case "SEASONS":
                return "map -1000 1000 100 -90 -996.5 1009.5 150";
            case "FOOTBALL_FIELD":
                return "map 4000 4000 100 180 3997.5 3997.5 150";
            case "ZABER_THE_BUILDER":
                return "map 20000 20000 100 180 19998.5 19998.5 150";
            case "FROZEN_EARTH":
                return "map 5000 5000 100 90 5000.5 4997.5";
            case "HOT_AIR_BALLOON":
                return "map 2023 1225 50 -45 2026.5 1226.5 150";
            case "INVASION":
                return "map 8964 8964 60 0 8967.5 8967.5 150";
            case "BOX_CITY":
                return "map 1204 1204 93 -180 1201.5 1201.5 150";
            case "GAMING":
                return "map 40000 40000 84 0 40000.5 40008.5 150";
            case "ROMAN_ARENA":
                return "map 6900 6900 108 0 6897.5 6900.5 150";
            case "CRUSHERS":
                return "map 30000.5 19996.5 70 0 29995.5 20000.5 150";

            default:
                System.out.println("Occurred Error, could not found map command");
                return null;
        }
    }

    public static void open(Player player) {
        Inventory mapInv = Bukkit.createInventory(null, 36, "Map Vote");
        AtomicInteger count = new AtomicInteger(0);

        getAllMapNames().forEach(mapName -> {
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
