package com.meowj.langutils.misc;

import com.meowj.langutils.lang.LanguageHelper;
import com.meowj.langutils.nms.NMS;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Util {

    private Util() {
    }

    @NotNull
    public static String getShortTime(int duration) {
        int m = duration / 20 / 60;
        int s = duration / 20 % 60;
        return String.format("%d:%02d", m, s);
    }

    @NotNull
    public static PotionType getPotionType(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta) meta).getBasePotionData().getType();
        }
        throw new UnsupportedOperationException("The given item is not a potion.");
    }

    @Nullable
    public static String getPlayerHeadOwnerName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            OfflinePlayer ofp = ((SkullMeta) meta).getOwningPlayer();
            if (ofp != null) {
                return ofp.getName();
            }
        }
        return null;
    }

    @Nullable
    public static Integer getTropicalFishVariant(@NotNull TropicalFishBucketMeta meta) {
        if (meta.hasVariant()) {
            TropicalFish.Pattern pattern = meta.getPattern();

            // https://minecraft.gamepedia.com/Tropical_Fish#Entity_data

            int type = pattern.ordinal() > 5 ? 1 : 0;
            int patt = pattern.ordinal() % 6;
            int bcol = meta.getBodyColor().ordinal();
            int pcol = meta.getPatternColor().ordinal();

            return pcol << 24 | bcol << 16 | patt << 8 | type;
        }
        return null;
    }

    @NotNull
    public static Integer getPatternMixedCode(@NotNull Pattern pattern) {
        int p = pattern.getPattern().ordinal();
        int c = pattern.getColor().ordinal();
        return p << 16 | c;
    }

    public static void testWithPlayer(CommandSender sender, String lang) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String locale = lang == null ? player.getLocale() : lang;

            sendBiome         (player, locale);
            sendChunkEntities (player, locale);

            ItemStack item = player.getInventory().getItemInMainHand();

            sendItemName      (item, player, locale);
            sendShiedPatterns (item, player, locale);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }

            sendEnchantment   (meta, player, locale);
            sendBannerPatterns(meta, player, locale);
            sendFishBucket    (meta, player, locale);
            sendPotionEffect  (meta, player, locale);
        }
    }

    private static void sendBiome(Player player, String locale) {
        Biome biom = player.getLocation().getBlock().getBiome();
        String msg = LanguageHelper.getBiomeName(biom, locale);

        player.sendMessage("Biome: " + msg);
    }

    private static void sendChunkEntities(Player player, String locale) {
        player.sendMessage("Entities in the current chunk:");

        Chunk chunk = player.getLocation().getBlock().getChunk();
        for (Entity entity : chunk.getEntities()) {
            String name = LanguageHelper.getEntityDisplayName(entity, locale);
            player.sendMessage("  " + name);
        }
    }

    private static void sendItemName(ItemStack item, Player player, String locale) {
        player.sendMessage(LanguageHelper.getItemName(item, locale));
    }

    private static void sendShiedPatterns(ItemStack item, Player player, String locale) {
        if (item.getType() != Material.SHIELD) {
            return;
        }

        List<Pattern> patterns = NMS.getShiedPatterns(item);
        for (Pattern pattern : patterns) {
            player.sendMessage(
                    ChatColor.GRAY
                            + "  "
                            + LanguageHelper.getBannerPatternName(pattern, locale));
        }
    }

    private static void sendBannerPatterns(ItemMeta meta, Player player, String locale) {
        if (meta instanceof BannerMeta) {
            BannerMeta bannerMeta = (BannerMeta) meta;

            for (org.bukkit.block.banner.Pattern pattern : bannerMeta.getPatterns()) {
                player.sendMessage(
                        ChatColor.GRAY + "  "
                                + LanguageHelper.getBannerPatternName(pattern, locale)
                                + " ("
                                + pattern.getPattern().name()
                                + " - " + pattern.getColor().name()
                                + ")");
            }
        }
    }

    private static void sendFishBucket(ItemMeta meta, Player player, String locale) {
        if (meta instanceof TropicalFishBucketMeta) {
            TropicalFishBucketMeta fishBucketMeta = (TropicalFishBucketMeta) meta;

            String message = LanguageHelper.getPredefinedTropicalFishName(fishBucketMeta, locale);
            if (message == null) {
                message = LanguageHelper.getDyeColorName(fishBucketMeta.getBodyColor(), locale)
                        + "-"
                        + LanguageHelper.getDyeColorName(fishBucketMeta.getPatternColor(), locale)
                        + " "
                        + LanguageHelper.getTropicalFishTypeName(fishBucketMeta.getPattern(), locale);
            }

            player.sendMessage(ChatColor.GRAY + "  " + ChatColor.ITALIC + message);
        }
    }

    private static void sendPotionEffect(ItemMeta meta, Player player, String locale) {
        if (meta instanceof PotionMeta) {

            PotionMeta potionMeta = (PotionMeta) meta;
            PotionData potionData = potionMeta.getBasePotionData();

            String message = ChatColor.GRAY + "  ";
            message += LanguageHelper.getPotionBaseEffectName(potionData.getType(), locale);
            if (potionData.isUpgraded()) {
                message += "II";
            }
            player.sendMessage(message);

            for (PotionEffect eff : potionMeta.getCustomEffects()) {
                player.sendMessage(
                        ChatColor.GRAY
                                + "  "
                                + LanguageHelper.getPotionEffectDisplay(eff, locale));
            }
        }
    }

    private static void sendEnchantment(ItemMeta meta, Player player, String locale) {
        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            String message = LanguageHelper.getEnchantmentDisplayName(entry, locale);
            player.sendMessage(ChatColor.GRAY + "  " + message);
        }
    }

}
