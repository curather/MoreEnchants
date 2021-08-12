package com.gum.moreenchants.mixins;

import com.gum.moreenchants.registers.EnchantmentRegister;
import com.gum.moreenchants.util.LogBlockUtils;
import com.gum.moreenchants.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {
    private static void playerDestroy(ServerWorld world, BlockPos pos, BlockPos oldPos, Entity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player == null) return;
            Block block = world.getBlockState(pos).getBlock();
            ItemStack item = block.asItem().getDefaultInstance();
            Boolean noTelekinesis = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), player.getMainHandItem()) == 0;
            int EnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.EXPERIENCE.get(), stack);
            if (block == Blocks.AIR || block == Blocks.BEDROCK) return;

            if (EnchantmentLevel > 0)
                block.popExperience(world, oldPos, (int) (5 * EnchantmentLevel * 1.15 + EnchantmentLevel * 3));

            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SMELTING_TOUCH.get(), stack) > 0) {
                item = Utils.Smelted(world, item);
            }
            ItemEntity itementity = new ItemEntity(world, oldPos.getX(), oldPos.getY(), oldPos.getZ(), item);
            if (noTelekinesis) {
                world.destroyBlock(pos, false);
                world.addFreshEntity(itementity);
            } else {
                Boolean addedtoInventory = player.inventory.add(world.getBlockState(pos).getBlock().asItem().getDefaultInstance());
                world.destroyBlock(pos, false);
                if (!addedtoInventory)
                    world.addFreshEntity(itementity);
            }

        }
    }

    @Inject(
            method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true)
    private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, TileEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> returnValue = cir.getReturnValue();
        ItemStack smelted;

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.VEIN.get(), stack) > 0) {
            List<BlockPos> Spots = new ArrayList<>();
            if (MathHelper.wrapDegrees(entity.xRot) < -30 || MathHelper.wrapDegrees(entity.xRot) > 30)
                Spots.addAll(Utils.getSurroundingBlockPositionsHorizontal(pos));
            else
                Spots.addAll(Utils.getSurroundingBlockPositionsVertical(pos, entity.getDirection()));

            for (BlockPos currentPos : Spots) {
                playerDestroy(world, currentPos, pos, entity, stack);
            }

        }

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TREECAPITATOR.get(), stack) > 0) {
            LogBlockUtils.BreakAllLogs(world, pos, (LivingEntity) entity);
            return;
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SMELTING_TOUCH.get(), stack) == 0) {
            if (entity == null) {
                cir.setReturnValue(returnValue);
            } else {
                PlayerEntity player = world.getPlayerByUUID(entity.getUUID());
                if (player == null) {
                    cir.setReturnValue(returnValue);
                    return;
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), stack) > 0) {
                    List<ItemStack> overflowitems = new ArrayList<>();
                    for (int i = 0; i < returnValue.size(); i++) {
                        if (!player.inventory.add(returnValue.get(i))) {
                            overflowitems.add(returnValue.get(i));
                        }
                    }
                    cir.setReturnValue(overflowitems);
                } else
                    cir.setReturnValue(returnValue);
            }
            return;
        }
        for (ItemStack itemStack : returnValue) {
            Optional<FurnaceRecipe> recipe = world.getRecipeManager().getAllRecipesFor(IRecipeType.SMELTING).stream().filter((smeltingRecipe -> {
                return smeltingRecipe.getIngredients().get(0).test(itemStack);
            })).findFirst();
            if (recipe.isPresent()) {
                smelted = recipe.get().getResultItem();
                smelted.setCount(itemStack.getCount());

                items.add(smelted);

            } else {
                items.add(itemStack);
            }
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), stack) == 0 || entity == null) {
            cir.setReturnValue(items);
        } else {
            PlayerEntity player = world.getPlayerByUUID(entity.getUUID());
            if (player == null) {
                cir.setReturnValue(returnValue);
            }
            List<ItemStack> overflowitems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                if (!player.inventory.add(items.get(i))) {
                    overflowitems.add(items.get(i));
                }
            }
            cir.setReturnValue(overflowitems);
        }
    }

}

