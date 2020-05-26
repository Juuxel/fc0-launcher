/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.api.SyntaxError;
import juuxel.fc0.launcher.util.Logger;
import juuxel.fc0.launcher.version.Version;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Config {
    private static final Path CONFIG_PATH = Path.of(System.getProperty("user.home"), ".config", "fc0-launcher.json");
    public final List<Mod> mods = new ArrayList<>();
    public Version selectedVersion = Version.LATEST;

    public static Config load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                return fromJson(CONFIG_PATH);
            } catch (Exception e) {
                Logger.error("Could not load config, using default...", e);
                return new Config();
            }
        } else {
            Config config = new Config();
            config.save();
            return config;
        }
    }

    private static Jankson createJankson() {
        return Jankson.builder()
            .registerTypeFactory(Config.class, Config::new)
            .registerTypeFactory(Mod.class, Mod::new)
            .registerSerializer(Path.class, (path, m) -> new JsonPrimitive(path.toString()))
            .registerSerializer(Version.class, (version, m) -> new JsonPrimitive(version.getName()))
            .registerDeserializer(JsonPrimitive.class, Path.class, (primitive, m) -> Path.of(primitive.asString()))
            .registerDeserializer(JsonPrimitive.class, Version.class, (primitive, m) -> {
                Version version = Version.BY_NAME.get(primitive.asString());

                if (version == null) {
                    throw new DeserializationException("Unknown version: " + primitive.asString());
                }

                return version;
            })
            .registerDeserializer(JsonObject.class, Config.class, (json, m) -> {
                Config result = new Config();

                if (json.containsKey("selectedVersion")) {
                    Version v = m.marshallCarefully(Version.class, json.get("selectedVersion"));
                    if (v != null) {
                        result.selectedVersion = v;
                    }
                }

                if (json.containsKey("mods")) {
                    JsonElement mods = json.get("mods");

                    if (mods instanceof JsonArray) {
                        for (JsonElement mod : (JsonArray) mods) {
                            result.mods.add(m.marshallCarefully(Mod.class, mod));
                        }
                    } else {
                        throw new DeserializationException("'mods' is not a JSON array!");
                    }
                }

                return result;
            })
            .build();
    }

    public static Config fromJson(Path path) throws IOException, SyntaxError, DeserializationException {
        Jankson jankson = createJankson();

        try (InputStream in = Files.newInputStream(path)) {
            return jankson.fromJsonCarefully(jankson.load(in), Config.class);
        }
    }

    public void save() {
        Jankson jankson = createJankson();
        String json = jankson.toJson(this).toJson(false, true);

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Logger.error("Could not save config", e);
        }
    }
}
