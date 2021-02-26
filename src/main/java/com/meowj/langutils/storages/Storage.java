package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.lang.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Storage<T> {

    protected final LangUtils                   plugin;
    protected final Map<String, Map<T, String>> storage;
    protected final String                      fallbackLocale;

    public Storage(@NotNull String fallbackLocale) {
        this.storage        = new HashMap<>();
        this.plugin         = LangUtils.getInstance();
        this.fallbackLocale = fallbackLocale;
    }

    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection result = langConfig.getConfigurationSection(node);
        if (result == null || result.getValues(false).size() == 0) {
            // plugin.error("Invalid language resource: " + locale + ", missing '" + node + "'.");
            return null;
        }
        return result;
    }

    public void addEntry(@NotNull String locale, @NotNull T t, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<T, String> pairMap = storage.computeIfAbsent(locale, s -> new HashMap<>());
        pairMap.put(t, localized);
        remapping(locale, pairMap);
     }

    protected void remapping(@NotNull String locale, Map<T, String> pairMap) {
        String remapped = Remaper.remap(locale);
        if (remapped != null) {
            if (storage.get(remapped) == pairMap) {
                return;
            }
            storage.put(remapped, pairMap);
        }
    }

    public String getEntry(@NotNull String locale, @NotNull T t) {
        locale = LangUtils.fixLocale(locale);
        Map<T, String> convMap = storage.get(locale);
        if (convMap == null) {
            convMap = storage.get(Remaper.getLang(locale));
            if (convMap == null) {
                convMap = storage.get(fallbackLocale);
            }
        }
        if (convMap == null) {
            return null;
        }
        String result = convMap.get(t);
        return result == null || result.isEmpty() ? null : result;
    }

    public String getFallbackLocale() {
        return fallbackLocale;
    }

    @NotNull
    public Set<String> getLocales() {
        return storage.keySet();
    }

    @Nullable
    public Set<T> getOriginals(String locale) {
        Map<T, String> map = storage.get(locale);
        if (map == null) {
            return null;
        }
        return map.keySet();
    }

    public void clear() {
        storage.clear();
    }

}
