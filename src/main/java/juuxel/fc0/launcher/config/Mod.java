/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.config;

import java.nio.file.Path;

public final class Mod {
    public final Path path;
    public boolean enabled = true;

    public Mod(Path path) {
        this.path = path;
    }

    // For Jankson
    Mod() {
        this.path = null;
    }
}
