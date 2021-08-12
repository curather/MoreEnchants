package com.gum.moreenchants.util;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class randomItemGenerator {
    public static ArrayList<Item> toolEntries = new ArrayList<Item>();
    public static ArrayList<Item> weaponEntries = new ArrayList<Item>();
    public static ArrayList<Item> foodEntries = new ArrayList<Item>();
    public static ArrayList<Item> blockEntries = new ArrayList<Item>();
    public static ArrayList<Item> miscEntries = new ArrayList<Item>();
    public static ArrayList<Item> armorEntries = new ArrayList<Item>();


    public randomItemGenerator() {
        Registry.ITEM.stream().filter(item -> item.getItemCategory() == ItemGroup.TAB_TOOLS).forEach((item) -> toolEntries.add(item));
        Registry.ITEM.stream().filter(item -> item.getItemCategory() == ItemGroup.TAB_COMBAT).forEach((item) -> weaponEntries.add(item));
        Registry.ITEM.stream().filter(item -> item.getItemCategory() == ItemGroup.TAB_FOOD).forEach((item) -> foodEntries.add(item));
        Registry.ITEM.stream().filter(item -> item.getItemCategory() == ItemGroup.TAB_BUILDING_BLOCKS).forEach((item) -> blockEntries.add(item));
        Registry.ITEM.stream().filter(item -> item.getItemCategory() == ItemGroup.TAB_MISC).forEach((item) -> miscEntries.add(item));
        Registry.ITEM.stream().filter(item -> item instanceof ArmorItem).forEach((item) -> armorEntries.add(item));
    }

    public static ArrayList<Item> returnSlot(EquipmentSlotType type) {
        ArrayList<Item> Items = (type == EquipmentSlotType.MAINHAND || type == EquipmentSlotType.OFFHAND) ? toolEntries : armorEntries;
        ArrayList<Item> temp = new ArrayList<>();
        if (type == EquipmentSlotType.MAINHAND || type == EquipmentSlotType.OFFHAND) {
            Items.addAll(weaponEntries);
            Items.stream().filter(item -> item instanceof SwordItem || item instanceof ToolItem).forEach((item) -> temp.add(item));
        } else {
            Items.stream().filter(item -> item instanceof ArmorItem && ((ArmorItem) item).getSlot() == type).forEach((item) -> temp.add(item));
        }

        return temp;
    }
}
