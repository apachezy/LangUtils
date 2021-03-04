package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

public class EntityStorage extends Storage<EntityType> {

    public EntityStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {
        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries == null) {
            return null;
        }

        for (EntityType ent : EntityType.values()) {
            if (ent != EntityType.UNKNOWN) {

                String entryName = ent.getKey().getKey();
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "EntityType name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, ent, localized, remaper);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull EntityType entityType,
                         @NotNull String localized, Remaper remaper) {
        locale = LangUtils.fixLocale(locale);
        Map<EntityType, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(EntityType.class));
        pairMap.put(entityType, localized);

        remapping(locale, pairMap, remaper);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull EntityType entityType) {
        String result = super.getEntry(locale, entityType);
        return result == null ? entityType.getKey().toString() : result;
    }

}