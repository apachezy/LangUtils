package com.meowj.langutils.misc;

import com.meowj.langutils.LangUtils;
import org.jetbrains.annotations.NotNull;

public enum Named {

    PLAYER_HEAD_NAMED    ("%s's Head"              ),
    POTION_WITH_AMPLIFIER("%s %s"                  ),
    POTION_WITH_DURATION ("%s (%s)"                ),
    UNKNOWN_ENCHANTMENT  ("Unknown enchantment: %s"),
    UNKNOWN_ENTITY       ("Unknown entity: %s"     ),
    UNKNOWN_EFFECT       ("Unknown effect: %s"     ),;

    private final String localized;


    Named(String def) {
        this.localized = def;
    }

    @NotNull
    public String getLocalized(@NotNull String locale) {
        String result = LangUtils.namedStorage.getEntry(locale, this);
        if (result == null) {
            result = localized;
        }
        return result;
    }

}
