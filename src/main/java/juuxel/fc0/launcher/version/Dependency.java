/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.version;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Dependency {
    public final String group;
    public final String name;
    public final String version;
    @Nullable
    public final String classifier;

    public Dependency(String group, String name, String version) {
        this(group, name, version, null);
    }

    public Dependency(String group, String name, String version, @Nullable String classifier) {
        this.group = Objects.requireNonNull(group, "group");
        this.name = Objects.requireNonNull(name, "name");
        this.version = Objects.requireNonNull(version, "version");
        this.classifier = classifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return group.equals(that.group) &&
            name.equals(that.name) &&
            version.equals(that.version) &&
            Objects.equals(classifier, that.classifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, name, version, classifier);
    }

    @Override
    public String toString() {
        return group + ":" + name + ":" + version + (classifier != null ? (":" + classifier) : "");
    }
}
