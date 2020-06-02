/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.launch;

import juuxel.fc0.launcher.gui.ErrorPane;
import juuxel.fc0.launcher.util.Logger;
import juuxel.fc0.launcher.util.Translations;

import javax.swing.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

/**
 * The launcher launches the game.
 */
public final class Launcher {
    private final ClassLoader classLoader;

    /**
     * Constructs a launcher.
     *
     * @param jars the JAR paths in descending order of preference
     */
    public Launcher(List<Path> jars) throws MalformedURLException {
        URL[] urls = new URL[jars.size()];

        for (int i = 0; i < jars.size(); i++) {
            urls[i] = jars.get(i).toUri().toURL();
        }

        this.classLoader = new URLClassLoader(urls, /* Use the JDK class loader */ String.class.getClassLoader());
    }

    /**
     * Launches the game.
     *
     * @param mainClassName the name of the main class
     * @throws Exception if launching the game fails
     */
    public void launch(String mainClassName) throws Exception {
        Class<?> mainClass = Class.forName(mainClassName, true, classLoader);
        Method main = mainClass.getMethod("main", String[].class);

        new Thread(() -> {
            try {
                main.invoke(null, (Object) new String[0]);
            } catch (Exception e) {
                Logger.error("2fc0f18 crashed!", e);
                SwingUtilities.invokeLater(() -> ErrorPane.show(null, Translations.get("error.crash"), null, e));
            }
        }, "Game thread").start();
    }
}
