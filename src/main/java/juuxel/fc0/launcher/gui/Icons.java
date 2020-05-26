/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

final class Icons {
    public static final Icon ADD = load("add");
    public static final Icon REMOVE = load("remove");
    public static final Icon MOVE_UP = load("move-up");
    public static final Icon MOVE_DOWN = load("move-down");

    private Icons() {
    }

    private static Icon load(String name) {
        try {
            return new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/fc0-launcher/" + name + ".png")));
        } catch (IOException e) {
            throw new RuntimeException("Could not load icon " + name, e);
        }
    }
}
