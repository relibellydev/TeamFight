package fr.relibelly.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }

    public ItemBuilder(Material m, int amount, short meta) {
        this.is = new ItemStack(m, amount, meta);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilder setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(name);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta((ItemMeta)im);
        } catch (ClassCastException classCastException) {}
        return this;
    }

    public ItemBuilder setSkullTexture(String texture) {
        try {
            SkullMeta skullM = (SkullMeta)this.is.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", texture));
            Field profileField = null;
            try {
                profileField = skullM.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullM, profile);
            } catch (IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e) {
                e.printStackTrace();
            }
            this.is.setItemMeta((ItemMeta)skullM);
        } catch (ClassCastException classCastException) {}
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        ItemMeta meta = this.is.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder flag(ItemFlag itemFlag) {
        ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(new ItemFlag[] { itemFlag });
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLoreWithList(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.is.getItemMeta();
        for (String string : lore)
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setWoolColor(DyeColor color) {
        this.is.setDurability((short)color.getData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta((ItemMeta)im);
        } catch (ClassCastException classCastException) {}
        return this;
    }

    public ItemStack build(boolean showItemInfo) {
        ItemMeta im = this.is.getItemMeta();
        if (!showItemInfo) {
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
            im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
            this.is.setItemMeta(im);
        }
        return this.is;
    }
}
