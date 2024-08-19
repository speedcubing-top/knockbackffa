package top.speedcubing.knockbackffa.utils;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public class GsonSerializer {
    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

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
