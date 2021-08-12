package com.gum.moreenchants.forgeEvents;

import com.gum.moreenchants.registers.EnchantmentRegister;
import com.gum.moreenchants.util.LogBlockUtils;
import com.gum.moreenchants.util.Utils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockHandler {

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null) return;
        int EnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.EXPERIENCE.get(), event.getPlayer().getMainHandItem());
        if (EnchantmentLevel > 0) {
            int exp = event.getExpToDrop();
            event.setExpToDrop((int) (exp * EnchantmentLevel * 1.15 + EnchantmentLevel * 3));
        }
    }
/*        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> returnValue = new ArrayList<>();
        ItemStack smelted;
        
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.VEIN.get(), event.getPlayer().getMainHandItem()) > 0) {
            List<BlockPos> Spots = new ArrayList<>();
            if (MathHelper.wrapDegrees(event.getPlayer().xRot) < -30 || MathHelper.wrapDegrees(event.getPlayer().xRot) > 30)
                Spots.addAll(Utils.getSurroundingBlockPositionsHorizontal(event.getPos()));
            else
                Spots.addAll(Utils.getSurroundingBlockPositionsVertical(event.getPos(), event.getPlayer().getDirection()));

            for (BlockPos currentPos : Spots) {
                playerDestroy(event.getWorld(), currentPos, event.getPos(), event.getPlayer());
            }

        }

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TREECAPITATOR.get(), event.getPlayer().getMainHandItem()) > 0) {
            LogBlockUtils.BreakAllLogs((World) event.getWorld(), event.getPos(), (LivingEntity) event.getPlayer());
            return;
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SMELTING_TOUCH.get(), event.getPlayer().getMainHandItem()) == 0) {
            if (event.getPlayer() != null) {
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), event.getPlayer().getMainHandItem()) > 0) {
                    List<ItemStack> overflowitems = new ArrayList<>();
                    for (int i = 0; i < returnValue.size(); i++) {
                        if (!event.getPlayer().inventory.add(returnValue.get(i))) {
                            overflowitems.add(returnValue.get(i));
                        }
                    }
                    returnValue = overflowitems;
                }
            }
            for (ItemStack item : returnValue) {
                Block.popResource((World) event.getWorld(),event.getPos(),item);
            }
            return;
        }
        for (ItemStack itemStack : returnValue) {
            Optional<FurnaceRecipe> recipe = ((World)event.getWorld()).getRecipeManager().getAllRecipesFor(IRecipeType.SMELTING).stream().filter((smeltingRecipe -> {
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
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), event.getPlayer().getMainHandItem()) == 0 || event.getPlayer() == null) {
            returnValue=items;
        } else {
            List<ItemStack> overflowitems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                if (!event.getPlayer().inventory.add(items.get(i))) {
                    overflowitems.add(items.get(i));
                }
            }
            returnValue=overflowitems;
        }
        for (ItemStack item : returnValue) {
            Block.popResource((World) event.getWorld(),event.getPos(),item);
        }
    }

    private static void playerDestroy(IWorld world, BlockPos currentPos, BlockPos pos, PlayerEntity player) {
            if (player == null) return;
            Block block = world.getBlockState(pos).getBlock();
            ItemStack item = block.asItem().getDefaultInstance();
            Boolean noTelekinesis = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), player.getMainHandItem()) == 0;
            int EnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.EXPERIENCE.get(), player.getMainHandItem());
            if (block == Blocks.AIR || block == Blocks.BEDROCK) return;

            if (EnchantmentLevel > 0)
                block.popExperience((ServerWorld) world, currentPos, (int) (5 * EnchantmentLevel * 1.15 + EnchantmentLevel * 3));

            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SMELTING_TOUCH.get(), player.getMainHandItem()) > 0) {
                item = Utils.Smelted((ServerWorld) world, item);
            }
            ItemEntity itemDrop = new ItemEntity((World) world, currentPos.getX(), currentPos.getY(), currentPos.getZ(), item);
            if (noTelekinesis) {
                world.destroyBlock(pos, false);
                world.addFreshEntity(itemDrop);
            } else {
                Boolean addedtoInventory = player.inventory.add(world.getBlockState(pos).getBlock().asItem().getDefaultInstance());
                world.destroyBlock(pos, false);
                if (!addedtoInventory)
                    world.addFreshEntity(itemDrop);
            }

        }*/
}
