package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager.Career;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VillagerCareerStorage extends Storage<Career> {

    public VillagerCareerStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public @Nullable ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                                  @NotNull String config, @Nullable Remaper remaper) {

        return super.load(locale, langConfig, config, remaper);
    }

    @Override
    public @Nullable String getEntry(@NotNull String locale, @NotNull Career career) {
        // todo:
        //    村民专业比较特殊，只在1.13版本中存在，1.14已经变为职业“Profession”
        return super.getEntry(locale, career);
    }

}
