package caponera.uned.tfm.lizardclips.utils;

import java.util.ResourceBundle;

public class I18NUtils {
    private static final String RESOURCE_BUNDLE_NAME = "TextosAplicacion";

    private static ResourceBundle textosAplicacion;

    public static String getString(String key) {
        if (textosAplicacion == null) {
            textosAplicacion = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
        }
        return textosAplicacion.getString(key);
    }
}
