/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.version;

import org.organicdesign.fp.collections.ImList;
import org.organicdesign.fp.collections.ImMap;
import org.organicdesign.fp.collections.PersistentHashSet;
import org.organicdesign.fp.tuple.Tuple2;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static org.organicdesign.fp.StaticImports.vec;

public final class Version {
    private static final String LWJGL_NATIVES = "natives-" + getOs();
    private static final String LWJGL_VERSION = "3.2.3";
    // The same deps are currently used for each version, so we can just list them here.
    private static final Builder TEMPLATE = builder()
        // LWJGL
        .dependency("org.lwjgl", "lwjgl", LWJGL_VERSION)
        .dependency("org.lwjgl", "lwjgl-assimp", LWJGL_VERSION)
        .dependency("org.lwjgl", "lwjgl-opengl", LWJGL_VERSION)
        .dependency("org.lwjgl", "lwjgl-glfw", LWJGL_VERSION)
        .dependency("org.lwjgl", "lwjgl-openal", LWJGL_VERSION)
        .dependency("org.lwjgl", "lwjgl-stb", LWJGL_VERSION)
        // Natives
        .dependency("org.lwjgl", "lwjgl", LWJGL_VERSION, LWJGL_NATIVES)
        .dependency("org.lwjgl", "lwjgl-assimp", LWJGL_VERSION, LWJGL_NATIVES)
        .dependency("org.lwjgl", "lwjgl-opengl", LWJGL_VERSION, LWJGL_NATIVES)
        .dependency("org.lwjgl", "lwjgl-glfw", LWJGL_VERSION, LWJGL_NATIVES)
        .dependency("org.lwjgl", "lwjgl-openal", LWJGL_VERSION, LWJGL_NATIVES)
        .dependency("org.lwjgl", "lwjgl-stb", LWJGL_VERSION, LWJGL_NATIVES)
        // Other deps
        .dependency("org.joml", "joml", "1.9.24")
        .dependency("it.unimi.dsi", "fastutil", "8.3.1");

    public static final Version VERSION_0_1_3_BUILD_2 = TEMPLATE.copy().name("0.1.3+build.2").build();
    public static final Version VERSION_0_1_4 = TEMPLATE.copy().name("0.1.4").build();
    public static final Version VERSION_0_1_5 = TEMPLATE.copy().name("0.1.5").build();
    public static final Version VERSION_0_2_0 = TEMPLATE.copy().name("0.2.0").build();
    public static final Version LATEST = VERSION_0_2_0;

    public static final ImList<Version> VERSIONS = vec(VERSION_0_2_0, VERSION_0_1_5, VERSION_0_1_4, VERSION_0_1_3_BUILD_2);
    public static final ImMap<String, Version> BY_NAME = VERSIONS.toImMap(version -> Tuple2.of(version.getName(), version));

    private final String name;
    private final Set<Dependency> dependencies;
    private final String url;

    private Version(String name, Set<Dependency> dependencies, String url) {
        this.name = name;
        this.dependencies = PersistentHashSet.of(dependencies);
        this.url = url;
    }

    private static String getOs() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("mac") || os.contains("darwin")) {
            return "macos";
        } else {
            return "linux";
        }
    }

    public String getName() {
        return name;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return name.equals(version.name) &&
            dependencies.equals(version.dependencies) &&
            url.equals(version.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dependencies, url);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Set<Dependency> dependencies = new HashSet<>();
        private String name;
        private String url;

        private Builder() {
        }

        public Builder dependency(String group, String name, String version) {
            dependencies.add(new Dependency(group, name, version));
            return this;
        }

        public Builder dependency(String group, String name, String version, String classifier) {
            dependencies.add(new Dependency(group, name, version, classifier));
            return this;
        }

        public Builder name(String name) {
            if (this.name != null) {
                throw new IllegalStateException("The version name has already been set!");
            }

            this.name = name;
            this.url = "https://github.com/valoeghese/2fc-early-releases/raw/master/2fc0f18-" + name + ".jar";
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder copy() {
            Builder copy = new Builder();
            copy.name = this.name;
            copy.dependencies.addAll(this.dependencies);
            copy.url = this.url;

            return copy;
        }

        public Version build() {
            if (this.name == null) {
                throw new IllegalStateException("The version name has not been set!");
            }
            if (this.url == null) {
                throw new IllegalStateException("The version URL has not been set!");
            }

            return new Version(name, dependencies, url);
        }
    }
}
