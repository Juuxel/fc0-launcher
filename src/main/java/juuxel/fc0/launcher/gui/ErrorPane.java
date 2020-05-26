/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.Component;

public final class ErrorPane {
    public static void show(@Nullable Component parent, String title, @Nullable String message, Throwable error) {
        JOptionPane.showMessageDialog(parent, message != null ? message : error.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
}
