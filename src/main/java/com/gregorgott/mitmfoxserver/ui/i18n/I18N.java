package com.gregorgott.mitmfoxserver.ui.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for Internationalization (i18n).
 */
public class I18N {
    private static ResourceBundle bundle;

    public static String getString(String key) {
        try {
            return getBundle().getString(key);
        } catch (MissingResourceException e) {
            System.err.println("RESOURCE NOT FOUND. KEY: " + key);
            return "---";
        }
    }

    public static String getString(String key, Object... arguments) {
        final String pattern = getString(key);
        return MessageFormat.format(pattern, arguments);
    }

    public static synchronized ResourceBundle getBundle() {
        if (bundle == null) {
            final String packageName = I18N.class.getPackage().getName();
            bundle = ResourceBundle.getBundle(
                    packageName + ".mitmfox-client",
                    Locale.getDefault(),
                    I18N.class.getClassLoader()
            );
        }

        return bundle;
    }
}
