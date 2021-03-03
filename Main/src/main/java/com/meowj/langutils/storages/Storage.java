package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Storage<T> {

    protected final Map<String, Map<T, String>> pairStorage;
    protected String fallbackLocale;

    protected Storage(@NotNull String fallbackLocale) {
        this.pairStorage = new HashMap<>();
        this.fallbackLocale = fallbackLocale;
    }

    @Nullable
    protected ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                        @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection result = langConfig.getConfigurationSection(config);

        if (result == null || result.getValues(false).size() == 0) {
            return null;
        }

        return result;
    }

    public void addEntry(@NotNull String locale, @NotNull T t, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<T, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new HashMap<>());
        pairMap.put(t, localized);

        remapping(locale, pairMap);
    }

    protected void remapping(@NotNull String locale, Map<T, String> pairMap) {
        String remapped = Remaper.remap(locale);
        if (remapped != null) {
            if (pairStorage.get(remapped) == pairMap) {
                return;
            }
            pairStorage.put(remapped, pairMap);
        }
    }

    @Nullable
    public String getEntry(@NotNull String locale, @NotNull T t) {
        locale = LangUtils.fixLocale(locale);
        Map<T, String> pair = pairStorage.get(locale);

        if (pair == null) {
            pair = pairStorage.get(Remaper.getLang(locale));
            if (pair == null) {
                pair = pairStorage.get(fallbackLocale);
            }
        }

        if (pair == null) {
            return null;
        }

        String result = pair.get(t);
        return result == null || result.isEmpty() ? null : result;
    }

    public String getFallbackLocale() {
        return fallbackLocale;
    }

    public void setFallbackLocale(@NotNull String fallbackLocale) {
        this.fallbackLocale = fallbackLocale;
    }

    @NotNull
    public List<String> getLocales(Predicate<String> filter) {
        if (filter == null) {
            return new ArrayList<>(pairStorage.keySet());
        }
        return pairStorage.keySet().stream().filter(filter).collect(Collectors.toList());
    }

    public void clear() {
        pairStorage.clear();
    }

}
