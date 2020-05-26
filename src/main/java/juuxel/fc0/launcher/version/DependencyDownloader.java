/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.version;

import juuxel.fc0.launcher.util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DependencyDownloader {
    private static final Path CACHE = Path.of(System.getProperty("user.home"), ".cache", "fc0-launcher");
    private static final String REPO = "https://repo.maven.apache.org/maven2/";

    private DependencyDownloader() {
    }

    private static Path downloadIfMissing(String pathString, URL url) throws IOException {
        Path path = CACHE.resolve(pathString.replace("/", File.separator));

        if (Files.notExists(path)) {
            Logger.info("Downloading " + url);

            Files.createDirectories(path.getParent());
            try (InputStream input = url.openStream()) {
                Files.copy(input, path);
            }
        }

        return path;
    }

    public static List<Path> download(Version version) throws IOException {
        List<Path> paths = new ArrayList<>();
        Path gamePath = downloadIfMissing("tk/valoeghese/2fc0f18-" + version.getName() + ".jar", new URL(version.getUrl()));
        paths.add(gamePath);

        for (Dependency dependency : version.getDependencies()) {
            String file = dependency.classifier != null
                ? String.format("%s-%s-%s.jar", dependency.name, dependency.version, dependency.classifier)
                : String.format("%s-%s.jar", dependency.name, dependency.version);

            String pathString = String.format("%s/%s/%s/%s", dependency.group.replace('.', '/'), dependency.name, dependency.version, file);
            Path path = downloadIfMissing(pathString, new URL(REPO + pathString));
            paths.add(path);
        }

        return paths;
    }
}
