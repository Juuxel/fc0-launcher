/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher;

import juuxel.fc0.launcher.config.Config;
import juuxel.fc0.launcher.gui.Icons;
import juuxel.fc0.launcher.gui.LauncherGui;
import juuxel.fc0.launcher.util.Logger;
import org.pushingpixels.substance.api.skin.SubstanceNightShadeLookAndFeel;

import javax.swing.*;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceNightShadeLookAndFeel());
            } catch (Exception e) {
                Logger.error("Could not set look and feel", e);
            }

            JFrame frame = new JFrame("fc0 Launcher");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setContentPane(new LauncherGui(frame, Config.load()));
            frame.setIconImages(List.of(Icons.ICON_16, Icons.ICON_24, Icons.ICON_64));
            frame.setVisible(true);
        });
    }
}
