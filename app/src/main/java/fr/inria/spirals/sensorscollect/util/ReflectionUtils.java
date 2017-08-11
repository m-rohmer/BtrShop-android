package fr.inria.spirals.sensorscollect.util;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {

    private static final String LOG_TAG = ReflectionUtils.class.getCanonicalName();

    public static Map<String, Object> mapOfFields(final Object object) {
        final Map<String, Object> fields = new HashMap<>();
        for (final Field field : getAllFields(object)) {
            try {
                field.setAccessible(true);
                fields.put(field.getName(), field.get(object));
            } catch (IllegalAccessException | SecurityException e) {
                Log.e(LOG_TAG, "Unable to access to field value", e);
            }
        }
        return fields;
    }

    public static List<Field> getAllFields(final Object object) {
        return getAllFields(object.getClass());
    }

    public static List<Field> getAllFields(final Class<?> clazz) {
        final List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (!Object.class.equals(clazz) && !Object.class.equals(clazz.getSuperclass())) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }

}
