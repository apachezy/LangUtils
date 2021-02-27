/*
 * Copyright (c) 2015 Jerrell Fang
 *
 * This project is Open Source and distributed under The MIT License (MIT)
 * (http://opensource.org/licenses/MIT)
 *
 * You should have received a copy of the The MIT License along with
 * this project.   If not, see <http://opensource.org/licenses/MIT>.
 */
package com.meowj.langutils;

import com.meowj.langutils.misc.LangInfo;
import com.meowj.langutils.storages.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class LangUtils extends JavaPlugin {

    public static final String EN_US = "en_us";

    // @formatter:off
    public static final MaterialStorage         materialStorage         = new MaterialStorage        (EN_US);
    public static final EnchantStorage          enchantStorage          = new EnchantStorage         (EN_US);
    public static final PotionStorage           potionStorage           = new PotionStorage          (EN_US);
    public static final PotionStorage           splashPotionStorage     = new PotionStorage          (EN_US);
    public static final PotionStorage           lingeringPotionStorage  = new PotionStorage          (EN_US);
    public static final PotionStorage           tippedArrowStorage      = new PotionStorage          (EN_US);
    public static final NamedStorage            namedStorage            = new NamedStorage           (EN_US);
    public static final BiomeStorage            biomeStorage            = new BiomeStorage           (EN_US);
    public static final EntityStorage           entityStorage           = new EntityStorage          (EN_US);
    public static final PotionEffectStorage     effectStorage           = new PotionEffectStorage    (EN_US);
    public static final TropicalFishTypeStorage tropicalFishTypeStorage = new TropicalFishTypeStorage(EN_US);
    public static final IntegerStrage           tropicalFishNameStorage = new IntegerStrage          (EN_US);
    public static final DyeColorStorage         dyeColorStorage         = new DyeColorStorage        (EN_US);
    public static final IntegerStrage           villagerLevelStorage    = new IntegerStrage          (EN_US);
    public static final ProfessionStorage       professionStorage       = new ProfessionStorage      (EN_US);
    // @formatter:on

    @NotNull
    public static String fixLocale(@NotNull String original) {
        return original.toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.reload(null);
    }

    private void reload(CommandSender sender) {
        reloadConfig();

        String fallback = getConfig().getString("FallbackLanguage");
        fallback = fallback == null || fallback.isEmpty() ? EN_US : fixLocale(fallback);

        boolean loadAll = false;

        Set<String> codes = new HashSet<>();
        List<String> list = getConfig().getStringList("LoadLanguage");

        for (String s : list) {
            if (s.equalsIgnoreCase("all")) {
                loadAll = true;
            }
            codes.add(fixLocale(s));
        }
        codes.add(fallback);

        clearStorages();

        // @formatter:off
        materialStorage         .setFallbackLocale(fallback);
        enchantStorage          .setFallbackLocale(fallback);
        potionStorage           .setFallbackLocale(fallback);
        splashPotionStorage     .setFallbackLocale(fallback);
        lingeringPotionStorage  .setFallbackLocale(fallback);
        tippedArrowStorage      .setFallbackLocale(fallback);
        namedStorage            .setFallbackLocale(fallback);
        biomeStorage            .setFallbackLocale(fallback);
        entityStorage           .setFallbackLocale(fallback);
        effectStorage           .setFallbackLocale(fallback);
        tropicalFishTypeStorage .setFallbackLocale(fallback);
        tropicalFishNameStorage .setFallbackLocale(fallback);
        dyeColorStorage         .setFallbackLocale(fallback);
        villagerLevelStorage    .setFallbackLocale(fallback);
        professionStorage       .setFallbackLocale(fallback);
        // @formatter:on

        try (JarFile jar = new JarFile(getFile())) {
            loadResources(jar, codes, loadAll);
        } catch (IOException e) {
            getLogger().severe("An exception occurred while loading language resources:");
            getLogger().severe(e.getLocalizedMessage());
            e.printStackTrace();
            return;
        }

        if (sender != null) {
            sender.sendMessage("Reload success.");
        }
    }

    private void loadResources(JarFile jarFile, Collection<String> codes, boolean loadAll) {
        int count = 0;
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String localePath = jarEntry.getName();

            if (!localePath.startsWith("lang/") || !localePath.endsWith(".yml")) {
                continue;
            }

            try (InputStream is = jarFile.getInputStream(jarEntry);
                 Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                Configuration cfg = YamlConfiguration.loadConfiguration(rd);
                LangInfo langInfo = LangInfo.load(cfg.getConfigurationSection("language"));

                if (langInfo == null) {
                    getLogger().severe("Invalid language resource: " + localePath + ".");
                }
                else if (loadAll || codes.contains(langInfo.code)) {
                    // @formatter:off
                    materialStorage         .load(langInfo.code, cfg, "material"                );
                    enchantStorage          .load(langInfo.code, cfg, "enchantment"             );
                    potionStorage           .load(langInfo.code, cfg, "potion"                  );
                    splashPotionStorage     .load(langInfo.code, cfg, "splash_potion"           );
                    lingeringPotionStorage  .load(langInfo.code, cfg, "lingering_potion"        );
                    tippedArrowStorage      .load(langInfo.code, cfg, "tipped_arrow"            );
                    namedStorage            .load(langInfo.code, cfg, "named"                   );
                    biomeStorage            .load(langInfo.code, cfg, "biome"                   );
                    entityStorage           .load(langInfo.code, cfg, "entity"                  );
                    effectStorage           .load(langInfo.code, cfg, "effect"                  );
                    tropicalFishTypeStorage .load(langInfo.code, cfg, "tropical_fish_type"      );
                    tropicalFishNameStorage .load(langInfo.code, cfg, "predefined_tropical_fish");
                    dyeColorStorage         .load(langInfo.code, cfg, "dye_color"               );
                    villagerLevelStorage    .load(langInfo.code, cfg, "merchant_level"          );
                    professionStorage       .load(langInfo.code, cfg, "villager_profession"     );
                    // @formatter:on

                    count++;
                    if (!loadAll) {
                        getLogger().info(langInfo.toString() + " has been loaded.");
                    }
                }
            } catch (IOException e) {
                getLogger().severe("Fail to load language resource " + localePath);
                e.printStackTrace();
            }
        }

        if (loadAll) {
            getLogger().info(count + " languages loaded.");
        }
    }

    private void clearStorages() {
        // @formatter:off
        materialStorage          .clear();
        enchantStorage           .clear();
        potionStorage            .clear();
        splashPotionStorage      .clear();
        lingeringPotionStorage   .clear();
        tippedArrowStorage       .clear();
        namedStorage             .clear();
        biomeStorage             .clear();
        entityStorage            .clear();
        effectStorage            .clear();
        tropicalFishTypeStorage  .clear();
        dyeColorStorage          .clear();
        villagerLevelStorage     .clear();
        professionStorage        .clear();
        tropicalFishNameStorage  .clear();
        // @formatter:on
    }

    @Override
    public void onDisable() {
        clearStorages();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                this.reload(sender);
                return true;
            }

            if (args.length >= 3 && "test".equalsIgnoreCase(args[0])) {
                Material material = Material.matchMaterial(args[2]);
                if (material == null) {
                    sender.sendMessage("Material '" + args[2] + "' not found.");
                    return true;
                }

                String localized = materialStorage.getEntry(args[1], material);
                sender.sendMessage(args[2] + " -> " + localized);
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>(1);

        if (!this.isEnabled()) {
            return result;
        }

        switch (args.length) {
            case 1:
                if ("reload".startsWith(args[0])) result.add("reload");
                if ("test"  .startsWith(args[0])) result.add("test"  );
                break;

            case 2:
                if ("test".equalsIgnoreCase(args[0]) && args[1].length() > 0) {
                    return materialStorage.getLocales(s -> s.startsWith(args[1]));
                }
                break;

            case 3:
                if ("test".equalsIgnoreCase(args[0]) && args[2].length() > 0) {
                    return Arrays.stream(Material.values())
                            .map(material -> material.name().toLowerCase(Locale.ROOT))
                            .filter(s -> s.startsWith(args[2]))
                            .collect(Collectors.toList());
                }
                break;
        }

        return result;
    }

}
