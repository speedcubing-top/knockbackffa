package top.speedcubing.knockbackffa.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SerializeUtils {
    private static final Gson gson = new Gson();

    public static String serializeObject(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserializeObject(String str, Class<T> clazz) {
        return gson.fromJson(str, clazz);
    }

    public static <T> T deserializeObject(String str, Type typeOfT) {
        return gson.fromJson(str, typeOfT);
    }
}
