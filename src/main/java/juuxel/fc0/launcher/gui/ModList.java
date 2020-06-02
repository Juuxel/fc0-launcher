/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.gui;

import juuxel.fc0.launcher.config.Config;
import juuxel.fc0.launcher.config.Mod;
import juuxel.fc0.launcher.util.Translations;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

final class ModList extends JPanel {
    private final Config config;

    ModList(Config config) {
        this.config = config;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        refreshOrder();
    }

    void refreshOrder() {
        removeAll();

        if (config.mods.isEmpty()) {
            add(new JLabel(Translations.get("mods.no-mods")));
        } else {
            for (int i = 0; i < config.mods.size(); i++) {
                add(new ModEntry(i, config.mods.get(i)));
            }
        }

        revalidate();
        repaint();
    }

    final class ModEntry extends JPanel {
        ModEntry(int index, Mod mod) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

            JCheckBox enabledCheckBox = new JCheckBox();
            enabledCheckBox.setSelected(mod.enabled);
            enabledCheckBox.addActionListener(event -> {
                mod.enabled = enabledCheckBox.isSelected();
                config.save();
            });

            JButton removeButton = new JButton(Icons.REMOVE);
            removeButton.addActionListener(event -> {
                config.mods.remove(mod);
                config.save();
                refreshOrder();
            });

            add(new JLabel(mod.path.getFileName().toString()));
            add(Box.createHorizontalGlue());
            add(enabledCheckBox);

            if (index > 0) {
                JButton moveUpButton = new JButton(Icons.MOVE_UP);
                moveUpButton.addActionListener(event -> {
                    setVisible(false);
                    config.mods.remove(mod);
                    config.mods.add(index - 1, mod);

                    config.save();
                    refreshOrder();
                });

                add(moveUpButton);
            }

            if (index < config.mods.size() - 1) {
                JButton moveDownButton = new JButton(Icons.MOVE_DOWN);
                moveDownButton.addActionListener(event -> {
                    config.mods.remove(mod);
                    config.mods.add(index + 1, mod);

                    config.save();
                    refreshOrder();
                });

                add(moveDownButton);
            }

            add(removeButton);
        }
    }
}
