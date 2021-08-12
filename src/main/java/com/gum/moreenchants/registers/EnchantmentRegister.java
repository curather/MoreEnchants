package com.gum.moreenchants.registers;

import com.gum.moreenchants.enchants.armor.GrowthEnchant;
import com.gum.moreenchants.enchants.armor.SugarRushEnchant;
import com.gum.moreenchants.enchants.misc.ExperienceEnchant;
import com.gum.moreenchants.enchants.misc.FeedEnchant;
import com.gum.moreenchants.enchants.misc.MobExperienceEnchant;
import com.gum.moreenchants.enchants.tools.SmeltingTouchEnchantment;
import com.gum.moreenchants.enchants.tools.TelekinesisEnchantment;
import com.gum.moreenchants.enchants.tools.TreeCapitatorEnchant;
import com.gum.moreenchants.enchants.tools.VeinEnchant;
import com.gum.moreenchants.MoreEnchants;
import com.gum.moreenchants.enchants.swords.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegister {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MoreEnchants.MODID);
    private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    //Enchants
    public static final RegistryObject<Enchantment> FROST = ENCHANTMENTS.register("frost", () ->
            new FrostEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> SMELTING_TOUCH = ENCHANTMENTS.register("smelting_touch", () ->
            new SmeltingTouchEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> TELEKINESIS = ENCHANTMENTS.register("telekinesis", () ->
            new TelekinesisEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> TREECAPITATOR = ENCHANTMENTS.register("treecapitator", () ->
            new TreeCapitatorEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> VEIN = ENCHANTMENTS.register("vein", () ->
            new VeinEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> EXPERIENCE = ENCHANTMENTS.register("experience", () ->
            new ExperienceEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> MOBEXPERIENCE = ENCHANTMENTS.register("mobexperience", () ->
            new MobExperienceEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> CRITICAL = ENCHANTMENTS.register("critical", () ->
            new CriticalEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> FEED = ENCHANTMENTS.register("feed", () ->
            new FeedEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> THUNDER = ENCHANTMENTS.register("thunder", () ->
            new ThunderEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));

    public static final RegistryObject<Enchantment> GROWTH = ENCHANTMENTS.register("growth", () ->
            new GrowthEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.ARMOR,
                    ARMOR_SLOTS));

    public static final RegistryObject<Enchantment> SUGARRUSH = ENCHANTMENTS.register("sugarrush", () ->
            new SugarRushEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentType.ARMOR,
                    new EquipmentSlotType[]{EquipmentSlotType.FEET}));

    public static final RegistryObject<Enchantment> LIFESTEAL = ENCHANTMENTS.register("lifesteal", () ->
            new LifeStealEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));


}
