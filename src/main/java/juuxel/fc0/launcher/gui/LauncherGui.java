/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.gui;

import juuxel.fc0.launcher.config.Config;
import juuxel.fc0.launcher.config.Mod;
import juuxel.fc0.launcher.launch.Launcher;
import juuxel.fc0.launcher.util.Logger;
import juuxel.fc0.launcher.version.DependencyDownloader;
import juuxel.fc0.launcher.version.Version;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LauncherGui extends JPanel {
    private final JComboBox<Version> versionComboBox;
    private final JFrame frame;
    private final Config config;
    private final JFileChooser modChooser = new JFileChooser();

    public LauncherGui(JFrame frame, Config config) {
        super(new BorderLayout());
        this.frame = frame;
        this.config = config;

        modChooser.setFileFilter(new FileNameExtensionFilter("Mod files", "jar"));
        modChooser.setMultiSelectionEnabled(true);

        JPanel topPanel = new JPanel(new MigLayout("insets 0 20 0 20, fill"));
        versionComboBox = new JComboBox<>(Version.VERSIONS.toArray(new Version[0]));
        versionComboBox.setSelectedItem(config.selectedVersion);
        versionComboBox.addActionListener(event -> {
            config.selectedVersion = versionComboBox.getItemAt(versionComboBox.getSelectedIndex());
            config.save();
        });
        JLabel title = new JLabel("<html><h1>fc0 Launcher</h1>");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        ModList modList = new ModList(config);

        JButton addModButton = new JButton(Icons.ADD);
        addModButton.addActionListener(event -> {
            int result = modChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                for (File file : modChooser.getSelectedFiles()) {
                    Mod mod = new Mod(file.toPath());
                    config.mods.add(mod);
                }

                modList.refreshOrder();
                config.save();
            }
        });

        topPanel.add(title, "span, width 100%");
        topPanel.add(new JLabel("Game version"));
        topPanel.add(versionComboBox, "wrap");
        topPanel.add(new JLabel("<html><h2>Mods</h2>"));
        topPanel.add(addModButton, "gapleft min:push, wrap");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton launchButton = new JButton("Launch");
        launchButton.setFont(launchButton.getFont().deriveFont(14f));
        launchButton.addActionListener(event -> launch());

        bottomPanel.add(launchButton);

        add(topPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(modList), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }

    private void launch() {
        Version selectedVersion = versionComboBox.getItemAt(versionComboBox.getSelectedIndex());
        try {
            List<Path> jars = new ArrayList<>();

            for (Mod mod : config.mods) {
                if (mod.enabled) {
                    jars.add(mod.path);
                }
            }

            jars.addAll(DependencyDownloader.download(selectedVersion));

            new Launcher(jars).launch("tk.valoeghese.fc0.client.Main");

            frame.setVisible(false);
            frame.dispose();
        } catch (Exception e) {
            Logger.error("Could not launch game", e);
            ErrorPane.show(this, "Could not launch game", null, e);
        }
    }
}
