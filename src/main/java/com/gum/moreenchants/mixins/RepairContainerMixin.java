package com.gum.moreenchants.mixins;

import net.minecraft.inventory.container.RepairContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepairContainer.class)
public class RepairContainerMixin {

    @Inject(
            method = "Lnet/minecraft/inventory/container/RepairContainer;calculateIncreasedRepairCost(I)I",
            at = @At("RETURN"),
            cancellable = true)
    private static void calculateIncreasedRepairCost(int p_216977_0_, CallbackInfoReturnable<Integer> cir) {
            cir.setReturnValue(p_216977_0_);
    }
}
