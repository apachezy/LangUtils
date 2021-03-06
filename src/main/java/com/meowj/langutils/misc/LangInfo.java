package com.meowj.langutils.misc;

import com.meowj.langutils.LangUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class LangInfo {

    @NotNull public final String code;
    @NotNull public final String name;
    @NotNull public final String region;

    public LangInfo(@NotNull String code, @NotNull String name, @NotNull String region) {
        this.code   = LangUtils.fixLocale(code);
        this.name   = name;
        this.region = region;
    }

    public static LangInfo load(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        String code = section.getString("code");
        if (code == null || code.isEmpty() || code.length() > 30) {
            return null;
        }

        String name = section.getString("name");
        if (name == null || name.isEmpty() || name.length() > 30) {
            return null;
        }

        String region = section.getString("region");
        if (region == null || region.isEmpty() || region.length() > 30) {
            return null;
        }

        return new LangInfo(code, name, region);
    }

    public void save(ConfigurationSection section) {
        if (section != null) {
            section.set("code",   this.code  );
            section.set("name",   this.name  );
            section.set("region", this.region);
        }
    }

    @Override
    public String toString() {
        return name + " (" + region + ") - " + code;
    }

}
