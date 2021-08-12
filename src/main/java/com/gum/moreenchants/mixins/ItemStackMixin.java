package com.gum.moreenchants.mixins;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract void setDamageValue(int p_196085_1_);

    @Inject(method = "Lnet/minecraft/item/ItemStack;hurt(ILjava/util/Random;Lnet/minecraft/entity/player/ServerPlayerEntity;)Z", cancellable = true, at = @At(value = "HEAD"))
    public void hurt(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        // preventing damage if item has the enchantment
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, (ItemStack) (Object) this);
        if (i > 0) {
            setDamageValue(-2147483648); // setting items that didn't have unbreaking before and lost durability have full durability
            cir.setReturnValue(false); // making sure that it doesn't attempt to damage
            // if preventDamage is false then use the normal ItemStack's behaviour

        }
    }
}
