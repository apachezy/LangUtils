package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class EnchantStorage extends Storage<Enchantment> {

    public EnchantStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (Enchantment enchant : Enchantment.values()) {

                String entryName = enchant.getKey().getKey();
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "Enchantment name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, enchant, localized, remaper);
            }
        }

        return entries;
    }

}