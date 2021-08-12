package com.gum.moreenchants.forgeEvents;

import com.gum.moreenchants.registers.EnchantmentRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingHurtHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (event.getSource().getEntity());
            if (player == null) return;

            int EnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.CRITICAL.get(), player.getMainHandItem());
            if (EnchantmentLevel > 0) {
                float exp = event.getAmount();
                event.setAmount((float) (exp * EnchantmentLevel * 1.15 + EnchantmentLevel * (Math.random() * 2 + 1)));
            }

        }
    }
}
