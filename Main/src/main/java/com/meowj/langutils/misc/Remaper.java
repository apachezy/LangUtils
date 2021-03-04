package com.meowj.langutils.misc;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.stream.Collectors;

public class Remaper {

    private final Map<String, String> map;

    private Remaper(@NotNull Map<String, String> map) {
        this.map = map;
    }

    @Nullable
    public static Remaper init(@NotNull JavaPlugin plugin, @NotNull String resource) {
        try (InputStream stream = plugin.getResource(resource)) {
            if (stream != null) {
                Map<String, String> map = loadMap(stream);
                if (map != null) {
                    return new Remaper(map);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private static Map<String, String> loadMap(InputStream stream) {
        try (Reader reader = new InputStreamReader(stream)) {

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(reader);
            Map<String, String> rt = yaml.getValues(false)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, t -> t.getValue().toString()));

            return rt.isEmpty() ? null : rt;

        } catch (IOException e) {
            return null;
        }
    }

    public String remap(String locale) {
        return map.get(locale);
    }

    @NotNull
    public static String getLang(@NotNull String locale) {
        int i = locale.indexOf('_');
        if (i > 0) {
            return locale.substring(0, i);
        }
        return locale;
    }

}
