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
import org.bukkit.ChatColor;
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

public class LangUtils extends JavaPlugin {

    private final static String DEFAULT_FALLBACK = "en_us";

    private static LangUtils instance;

    // @formatter:off
    private static MaterialStorage          materialStorage;
    private static EnchantStorage           enchantStorage;
    private static PotionStorage            potionStorage;
    private static PotionStorage            splashPotionStorage;
    private static PotionStorage            lingeringPotionStorage;
    private static PotionStorage            tippedArrowStorage;
    private static NamedStorage             namedStorage;
    private static BiomeStorage             biomeStorage;
    private static EntityStorage            entityStorage;
    private static PotionEffectStorage      effectStorage;
    private static TropicalFishTypeStorage  tropicalFishTypeStorage;
    private static DyeColorStorage          dyeColorStorage;
    private static IntegerStrage            villagerLevelStorage;
    private static ProfessionStorage        professionStorage;
    // todo: TropicalFish.predefined
    // @formatter:on


    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        this.reload(null);
    }

    private void reload(CommandSender sender) {
        reloadConfig();

        String fallback = getConfig().getString("FallbackLanguage");
        if (fallback == null || fallback.isEmpty()) {
            fallback = DEFAULT_FALLBACK;
        } else {
            fallback = fixLocale(fallback);
        }

        boolean loadAll = false;
        int count = 0;

        Set<String> loads = new HashSet<>();
        List<String> list = getConfig().getStringList("LoadLanguage");

        for (String s : list) {
            if (s.equalsIgnoreCase("all")) {
                loadAll = true;
            }
            loads.add(fixLocale(s));
        }
        loads.add(fallback);

        // @formatter:off
        materialStorage         = new MaterialStorage        (fallback);
        enchantStorage          = new EnchantStorage         (fallback);
        potionStorage           = new PotionStorage          (fallback);
        splashPotionStorage     = new PotionStorage          (fallback);
        lingeringPotionStorage  = new PotionStorage          (fallback);
        tippedArrowStorage      = new PotionStorage          (fallback);
        namedStorage            = new NamedStorage           (fallback);
        biomeStorage            = new BiomeStorage           (fallback);
        entityStorage           = new EntityStorage          (fallback);
        effectStorage           = new PotionEffectStorage    (fallback);
        tropicalFishTypeStorage = new TropicalFishTypeStorage(fallback);
        dyeColorStorage         = new DyeColorStorage        (fallback);
        villagerLevelStorage    = new IntegerStrage          (fallback);
        professionStorage       = new ProfessionStorage      (fallback);
        // @formatter:on

        try (JarFile jar = new JarFile(getFile())) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String path = jarEntry.getName();

                if (!path.startsWith("lang/") || !path.endsWith(".yml")) {
                    continue;
                }

                try (InputStream is = jar.getInputStream(jarEntry);
                     Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                    Configuration c = YamlConfiguration.loadConfiguration(rd);
                    LangInfo lf = LangInfo.load(c.getConfigurationSection("language"));

                    if (lf == null) {
                        error("Invalid language resource: " + path + ".");
                        continue;
                    }

                    if (loadAll || loads.contains(lf.code)) {

                        // @formatter:off
                        materialStorage          .load(lf.code, c, "material"           );
                        enchantStorage           .load(lf.code, c, "enchantment"        );
                        potionStorage            .load(lf.code, c, "potion"             );
                        splashPotionStorage      .load(lf.code, c, "splash_potion"      );
                        lingeringPotionStorage   .load(lf.code, c, "lingering_potion"   );
                        tippedArrowStorage       .load(lf.code, c, "tipped_arrow"       );
                        namedStorage             .load(lf.code, c, "named"              );
                        biomeStorage             .load(lf.code, c, "biome"              );
                        entityStorage            .load(lf.code, c, "entity"             );
                        effectStorage            .load(lf.code, c, "effect"             );
                        tropicalFishTypeStorage  .load(lf.code, c, "tropical_fish_type" );
                        dyeColorStorage          .load(lf.code, c, "dye_color"          );
                        villagerLevelStorage     .load(lf.code, c, "merchant_level"     );
                        professionStorage        .load(lf.code, c, "villager_profession");
                        // @formatter:on

                        count ++;
                        if (!loadAll) {
                            info(lf.toString() + " has been loaded.");
                        }
                    }
                } catch (IOException e) {
                    error("Fail to load language resource " + path);
                    e.printStackTrace();
                }
            }

            if (loadAll) {
                info(count + " languages loaded.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sender != null) {
            sender.sendMessage("Reload success.");
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
                String localized = LangUtils.getMaterialStorage().getEntry(args[1], material);
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
        if (this.isEnabled()) {
            if (args.length == 1) {
                if ("reload".startsWith(args[0])) {
                    result.add("reload");
                } else if ("test".startsWith(args[0])) {
                    result.add("test");
                }
            } else if (args.length == 2) {
                if ("test".equalsIgnoreCase(args[0]) && !args[1].isEmpty()) {
                    for (String locale : materialStorage.getLocales()) {
                        if (locale.startsWith(args[1])) {
                            result.add(locale);
                        }
                    }
                }
            } else if (args.length == 3) {
                if ("test".equalsIgnoreCase(args[0]) && !args[2].isEmpty()) {
                    String fallback = materialStorage.getFallbackLocale();
                    Set<Material> materials = materialStorage.getOriginals(fallback);
                    if (materials == null) {
                        return result;
                    }
                    String name;
                    for (Material material : materials) {
                        name = material.getKey().getKey();
                        if (name.startsWith(args[2])) {
                            result.add(name);
                        }
                    }
                }
            }
        }

        return result;
    }

    public void info(@NotNull String msg) {
        getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
    }

    @SuppressWarnings("unused")
    public void warning(@NotNull String msg) {
        getLogger().warning(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void error(@NotNull String msg) {
        getLogger().severe(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static LangUtils getInstance() {
        return instance;
    }

    @NotNull
    public static MaterialStorage getMaterialStorage() {
        return materialStorage == null
                ? new MaterialStorage(DEFAULT_FALLBACK)
                : materialStorage;
    }

    @NotNull
    public static EnchantStorage getEnchantStorage() {
        return enchantStorage == null
                ? new EnchantStorage(DEFAULT_FALLBACK)
                : enchantStorage;
    }

    public static PotionStorage getPotionStorage() {
        return potionStorage == null
                ? new PotionStorage(DEFAULT_FALLBACK)
                : potionStorage;
    }

    public static PotionStorage getSplashPotionStorage() {
        return splashPotionStorage == null
                ? new PotionStorage(DEFAULT_FALLBACK)
                : splashPotionStorage;
    }

    public static PotionStorage getLingeringPotionStorage() {
        return lingeringPotionStorage == null
                ? new PotionStorage(DEFAULT_FALLBACK)
                : lingeringPotionStorage;
    }

    public static PotionStorage getTippedArrowStorage() {
        return tippedArrowStorage == null
                ? new PotionStorage(DEFAULT_FALLBACK)
                : tippedArrowStorage;
    }

    public static NamedStorage getNamedStorage() {
        return namedStorage == null
                ? new NamedStorage(DEFAULT_FALLBACK)
                : namedStorage;
    }

    public static BiomeStorage getBiomeStorage() {
        return biomeStorage == null
                ? new BiomeStorage(DEFAULT_FALLBACK)
                : biomeStorage;
    }

    public static EntityStorage getEntityStorage() {
        return entityStorage == null
                ? new EntityStorage(DEFAULT_FALLBACK)
                : entityStorage;
    }

    public static PotionEffectStorage getEffectStorage() {
        return effectStorage == null
                ? new PotionEffectStorage(DEFAULT_FALLBACK)
                : effectStorage;
    }

    public static TropicalFishTypeStorage getTropicalFishTypeStorage() {
        return tropicalFishTypeStorage == null
                ? new TropicalFishTypeStorage(DEFAULT_FALLBACK)
                : tropicalFishTypeStorage;
    }

    public static DyeColorStorage getDyeColorStorage() {
        return dyeColorStorage == null
                ? new DyeColorStorage(DEFAULT_FALLBACK)
                : dyeColorStorage;
    }

    public static IntegerStrage getVillagerLevelStorage() {
        return villagerLevelStorage == null
                ? new IntegerStrage(DEFAULT_FALLBACK)
                : villagerLevelStorage;
    }

    public static ProfessionStorage getProfessionStorage() {
        return professionStorage == null
                ? new ProfessionStorage(DEFAULT_FALLBACK)
                : professionStorage;
    }

    @NotNull
    public static String fixLocale(@NotNull String original) {
        return original.toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

}
