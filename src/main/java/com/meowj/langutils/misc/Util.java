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
import java.util.Map.Entry;

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
            sendMusicDiskDesc (item, player, locale);
            sendPatternDesc   (item, player, locale);

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

    private static void sendItemName(ItemStack i, Player p, String l) {
        p.sendMessage(LanguageHelper.getItemName(i, l));
    }

    private static void sendShiedPatterns(ItemStack i, Player p, String l) {
        if (i.getType() != Material.SHIELD) {
            return;
        }

        List<Pattern> patterns = NMS.getShiedPatterns(i);
        for (Pattern pt : patterns) {
            p.sendMessage(
                ChatColor.GRAY
                    + "  "
                    + LanguageHelper.getBannerPatternName(pt, l));
        }
    }

    private static void sendMusicDiskDesc(ItemStack i, Player p, String l) {
        String s = LanguageHelper.getMusicDiskDesc(i.getType(), l);
        if (s != null) {
            p.sendMessage(ChatColor.GRAY + "  " + s);
        }
    }

    private static void sendPatternDesc(ItemStack i, Player p, String l) {
        String s = LanguageHelper.getNewBannerPatternDesc(i.getType(), l);
        if (s != null) {
            p.sendMessage(ChatColor.GRAY + "  " + s);
        }
    }

    private static void sendBannerPatterns(ItemMeta m, Player p, String l) {
        if (m instanceof BannerMeta) {
            BannerMeta bm = (BannerMeta) m;

            for (Pattern pt : bm.getPatterns()) {
                p.sendMessage(
                    ChatColor.GRAY + "  "
                        + LanguageHelper.getBannerPatternName(pt, l)
                        + " ("
                        + pt.getPattern().name()
                        + " - " + pt.getColor().name()
                        + ")");
            }
        }
    }

    private static void sendFishBucket(ItemMeta m, Player p, String l) {
        if (m instanceof TropicalFishBucketMeta) {
            TropicalFishBucketMeta tm = (TropicalFishBucketMeta) m;

            String msg = LanguageHelper.getPredefinedTropicalFishName(tm, l);
            if (msg == null) {
                msg = LanguageHelper.getDyeColorName(tm.getBodyColor(), l)
                    + "-"
                    + LanguageHelper.getDyeColorName(tm.getPatternColor(), l)
                    + " "
                    + LanguageHelper.getTropicalFishTypeName(tm.getPattern(), l);
            }

            p.sendMessage(ChatColor.GRAY + "  " + ChatColor.ITALIC + msg);
        }
    }

    private static void sendPotionEffect(ItemMeta m, Player p, String l) {
        if (m instanceof PotionMeta) {
            PotionMeta pm = (PotionMeta) m;
            PotionData pd = pm.getBasePotionData();

            String message = ChatColor.GRAY + "  ";
            message += LanguageHelper.getPotionBaseEffectName(pd.getType(), l);
            if (pd.isUpgraded()) {
                message += "II";
            }

            p.sendMessage(message);

            for (PotionEffect eff : pm.getCustomEffects()) {
                p.sendMessage(
                    ChatColor.GRAY
                        + "  "
                        + LanguageHelper.getPotionEffectDisplay(eff, l));
            }
        }
    }

    private static void sendEnchantment(ItemMeta m, Player p, String l) {
        for (Entry<Enchantment, Integer> e : m.getEnchants().entrySet()) {
            String msg = LanguageHelper.getEnchantmentDisplayName(e, l);
            p.sendMessage(ChatColor.GRAY + "  " + msg);
        }
    }

}
