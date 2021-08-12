package com.gum.moreenchants.mixins;

import com.gum.moreenchants.registers.EnchantmentRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract ResourceLocation getLootTable();

    @Shadow
    public abstract LootContext.Builder createLootContext(boolean p_213363_1_, DamageSource p_213363_2_);

    @Shadow
    public PlayerEntity lastHurtByPlayer;

    @Inject(
            method = "Lnet/minecraft/entity/LivingEntity;dropFromLootTable(Lnet/minecraft/util/DamageSource;Z)V",
            at = @At("HEAD"),
            cancellable = true)
    protected void dropFromLootTable(DamageSource p_213354_1_, boolean p_213354_2_, CallbackInfo ci) {

        //look at possibly hooking this into spawnAtLocation instead
        if (lastHurtByPlayer != null) {
            PlayerEntity player = lastHurtByPlayer;
            if (player == null) return;
            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), player.getMainHandItem()) > 0) {
                ResourceLocation resourcelocation = this.getLootTable();
                LootTable loottable = lastHurtByPlayer.getServer().getLootTables().get(resourcelocation);
                LootContext.Builder lootcontext$builder = this.createLootContext(p_213354_2_, p_213354_1_);
                LootContext ctx = lootcontext$builder.create(LootParameterSets.ENTITY);
                loottable.getRandomItems(ctx).forEach(player.inventory::add);//this::spawnAtLocation);
                ci.cancel();
            }
        }
    }
}
