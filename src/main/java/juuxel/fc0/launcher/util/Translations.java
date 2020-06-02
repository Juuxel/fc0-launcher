package juuxel.fc0.launcher.util;

import java.util.ResourceBundle;

public final class Translations {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("fc0-launcher.translations");

    private Translations() {
    }

    public static String get(String key) {
        if (BUNDLE.containsKey(key)) {
            return BUNDLE.getString(key);
        } else {
            Logger.warn("Trying to get unknown resource bundle key '" + key + "'");
            return key;
        }
    }

    public static String get(String key, Object... parameters) {
        return String.format(get(key), parameters);
    }
}
