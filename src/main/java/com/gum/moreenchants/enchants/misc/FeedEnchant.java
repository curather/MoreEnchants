package com.gum.moreenchants.enchants.misc;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class FeedEnchant extends Enchantment {
    public FeedEnchant(Rarity rarity, EnchantmentType enchantType, EquipmentSlotType[] equipSlotType) {
        super(rarity, enchantType, equipSlotType);
    }

    public int getMinCost(int p_77321_1_) {
        return 1 + 10 * (p_77321_1_ - 1);
    }

    public int getMaxCost(int p_223551_1_) {
        return super.getMinCost(p_223551_1_) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return super.checkCompatibility(ench);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

}
