/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class Icons {
    public static final Icon ADD = load("add");
    public static final Icon REMOVE = load("remove");
    public static final Icon MOVE_UP = load("move-up");
    public static final Icon MOVE_DOWN = load("move-down");
    public static final BufferedImage ICON_16 = loadImage("icon-16");
    public static final BufferedImage ICON_24 = loadImage("icon-24");
    public static final BufferedImage ICON_64 = loadImage("icon-64");

    private Icons() {
    }

    private static Icon load(String name) {
        return new ImageIcon(loadImage(name));
    }

    private static BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(Icons.class.getResourceAsStream("/fc0-launcher/" + name + ".png"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load icon " + name, e);
        }
    }
}
