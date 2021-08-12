package com.gum.moreenchants.forgeEvents;

import com.gum.moreenchants.registers.EnchantmentRegister;
import com.gum.moreenchants.util.Utils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class LivingEquipmentChangeHandler {
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (event.getEntity());
            int growthenchantmentLevelfrom = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.GROWTH.get(), event.getFrom());
            int growthenchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.GROWTH.get(), event.getTo());

            int speedenchantmentLevelfrom = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SUGARRUSH.get(), event.getFrom());
            int speedenchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SUGARRUSH.get(), event.getTo());
            if (growthenchantmentLevelfrom > 0) {
                final AttributeModifier BOOTS_DEBUFF = new AttributeModifier(UUID.fromString("139baab5-47f7-469c-a6bb-56e55f6ebb5c"), "GrowthBoots", 2.0d * growthenchantmentLevelfrom, AttributeModifier.Operation.ADDITION);
                final AttributeModifier LEGS_DEBUFF = new AttributeModifier(UUID.fromString("7d5c12b7-962b-4133-8744-d435c80b586a"), "GrowthLegs", 2.0d * growthenchantmentLevelfrom, AttributeModifier.Operation.ADDITION);
                final AttributeModifier CHESTPLATE_DEBUFF = new AttributeModifier(UUID.fromString("2535b128-17d7-4e01-938e-6143ce3618c1"), "GrowthBody", 2.0d * growthenchantmentLevelfrom, AttributeModifier.Operation.ADDITION);
                final AttributeModifier HELMET_DEBUFF = new AttributeModifier(UUID.fromString("48574fe5-4208-489c-9d4c-e577c86dae02"), "GrowthHelmet", 2.0d * growthenchantmentLevelfrom, AttributeModifier.Operation.ADDITION);

                player.setHealth(player.getHealth() - 2.0f * growthenchantmentLevelfrom);
                switch (event.getSlot()) {
                    case FEET:
                        Utils.removeAttributeModifier(event.getEntityLiving(), Attributes.MAX_HEALTH, BOOTS_DEBUFF);
                        break;
                    case LEGS:
                        Utils.removeAttributeModifier(event.getEntityLiving(), Attributes.MAX_HEALTH, LEGS_DEBUFF);
                        break;
                    case CHEST:
                        Utils.removeAttributeModifier(event.getEntityLiving(), Attributes.MAX_HEALTH, CHESTPLATE_DEBUFF);
                        break;
                    case HEAD:
                        Utils.removeAttributeModifier(event.getEntityLiving(), Attributes.MAX_HEALTH, HELMET_DEBUFF);
                        break;
                }

            }
            if (growthenchantmentLevel > 0) {
                final AttributeModifier BOOTS_BUFF = new AttributeModifier(UUID.fromString("139baab5-47f7-469c-a6bb-56e55f6ebb5c"), "GrowthBoots", 2.0d * growthenchantmentLevel, AttributeModifier.Operation.ADDITION);
                final AttributeModifier LEGS_BUFF = new AttributeModifier(UUID.fromString("7d5c12b7-962b-4133-8744-d435c80b586a"), "GrowthLegs", 2.0d * growthenchantmentLevel, AttributeModifier.Operation.ADDITION);
                final AttributeModifier CHESTPLATE_BUFF = new AttributeModifier(UUID.fromString("2535b128-17d7-4e01-938e-6143ce3618c1"), "GrowthBody", 2.0d * growthenchantmentLevel, AttributeModifier.Operation.ADDITION);
                final AttributeModifier HELMET_BUFF = new AttributeModifier(UUID.fromString("48574fe5-4208-489c-9d4c-e577c86dae02"), "GrowthHelmet", 2.0d * growthenchantmentLevel, AttributeModifier.Operation.ADDITION);
                switch (event.getSlot()) {
                    case FEET:
                        Utils.applyAttributeModifierSafely(event.getEntityLiving(), Attributes.MAX_HEALTH, BOOTS_BUFF);
                        break;
                    case LEGS:
                        Utils.applyAttributeModifierSafely(event.getEntityLiving(), Attributes.MAX_HEALTH, LEGS_BUFF);
                        break;
                    case CHEST:
                        Utils.applyAttributeModifierSafely(event.getEntityLiving(), Attributes.MAX_HEALTH, CHESTPLATE_BUFF);
                        break;
                    case HEAD:
                        Utils.applyAttributeModifierSafely(event.getEntityLiving(), Attributes.MAX_HEALTH, HELMET_BUFF);
                        break;
                }
                player.setHealth(player.getHealth() + 2.0f * growthenchantmentLevel);
            }
            if (speedenchantmentLevelfrom > 0) {
                final AttributeModifier BOOTS_DEBUFF = new AttributeModifier(UUID.fromString("8a88b6e1-e68d-4178-9b96-b6328f96c3d4"), "SpeedBoots", .05d * speedenchantmentLevelfrom, AttributeModifier.Operation.ADDITION);
                if (event.getSlot() == EquipmentSlotType.FEET) {
                    Utils.removeAttributeModifier(event.getEntityLiving(), Attributes.MOVEMENT_SPEED, BOOTS_DEBUFF);
                }
            }
            if (speedenchantmentLevel > 0) {
                final AttributeModifier BOOTS_BUFF = new AttributeModifier(UUID.fromString("8a88b6e1-e68d-4178-9b96-b6328f96c3d4"), "SpeedBoots", .05d * speedenchantmentLevel, AttributeModifier.Operation.ADDITION);
                if (event.getSlot() == EquipmentSlotType.FEET) {
                    Utils.applyAttributeModifierSafely(event.getEntityLiving(), Attributes.MOVEMENT_SPEED, BOOTS_BUFF);
                }
            }
        }
    }

}
