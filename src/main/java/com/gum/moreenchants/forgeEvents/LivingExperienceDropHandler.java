package com.gum.moreenchants.forgeEvents;

import com.gum.moreenchants.registers.EnchantmentRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingExperienceDropHandler {

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getAttackingPlayer() != null) {
            int EnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.MOBEXPERIENCE.get(), event.getAttackingPlayer().getMainHandItem());
            if (EnchantmentLevel > 0) {
                int exp = event.getDroppedExperience();
                event.setDroppedExperience((int) Math.ceil(exp * EnchantmentLevel * 1.35 + EnchantmentLevel * 3));
            }
            int FeedEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.FEED.get(), event.getAttackingPlayer().getMainHandItem());
            if (FeedEnchantmentLevel > 0) {
                int foodlev = event.getAttackingPlayer().getFoodData().getFoodLevel() + FeedEnchantmentLevel * 3;
                if (foodlev > 20) foodlev = 20;
                float satlev = Math.min(event.getAttackingPlayer().getFoodData().getSaturationLevel() + (float) foodlev * 4.0F, (float) foodlev);
                if (satlev > 20) satlev = 20;
                event.getAttackingPlayer().getFoodData().eat(foodlev,satlev);

            }
        }
    }

}
