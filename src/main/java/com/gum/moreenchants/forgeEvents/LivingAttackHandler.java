package com.gum.moreenchants.forgeEvents;

import com.gum.moreenchants.registers.EnchantmentRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingAttackHandler {

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (event.getSource().getEntity());
            if (player == null) return;
            int ThunderEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.THUNDER.get(), player.getMainHandItem());
            int LifeStealEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.LIFESTEAL.get(), player.getMainHandItem());
            float healAmount = (float) Math.ceil(event.getAmount() * LifeStealEnchantmentLevel * 0.20);

            if (ThunderEnchantmentLevel > 0) {
                BlockPos pos = new BlockPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY() - 3, event.getEntity().blockPosition().getZ());
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(player.level);
                lightningboltentity.moveTo(Vector3d.atBottomCenterOf(pos));
                lightningboltentity.setVisualOnly(true);
                player.level.addFreshEntity(lightningboltentity);
                event.getEntityLiving().setSecondsOnFire(8);
                event.getEntityLiving().hurt(DamageSource.LIGHTNING_BOLT, 5.0F);
                healAmount += (float) Math.ceil(5 * LifeStealEnchantmentLevel * 0.20);
            }

            if (LifeStealEnchantmentLevel > 0) {
                player.heal(healAmount);
            }
        }

    }

}
