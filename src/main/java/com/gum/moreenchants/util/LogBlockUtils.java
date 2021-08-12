package com.gum.moreenchants.util;

import com.gum.moreenchants.registers.EnchantmentRegister;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;

public class LogBlockUtils {

    private static Boolean isNaturalLeaf(BlockState val) {
        if (!(val.hasProperty(LeavesBlock.PERSISTENT))) return false;
        return val.getValue(LeavesBlock.PERSISTENT) /*&& !this.get(LeavesBlock.PERSISTENT) */ || (val.getBlock() instanceof FungusBlock);
    }

    private static Boolean isChoppable(BlockState val) {
        return val.getBlock() instanceof RotatedPillarBlock && (val.getMaterial() == Material.WOOD || val.getMaterial() == Material.NETHER_WOOD);
    }

    public static boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY >= 0 && minY < 256/*world.get getMaxHeight()*/) {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (!world.hasChunk(x, z)) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    private static LinkedList<BlockPos> findAllLogsAbove(World world, BlockPos originalBlockPos) {
        LinkedList<BlockPos> logQueue = new LinkedList<BlockPos>();
        byte range = 4;
        byte max = 32;
        int off = range + 1;
        int x = originalBlockPos.getX();
        int y = originalBlockPos.getY();
        int z = originalBlockPos.getZ();

        logQueue.push(originalBlockPos);

        if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
            int offX;
            int offY;
            int offZ;
            int type;

            for (offX = -range; offX <= range; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        BlockPos tempPos = new BlockPos(x + offX, y + offY, z + offZ);
                        if (isChoppable(world.getBlockState(tempPos))) {
                            logQueue.push(tempPos);
                        }
                    }
                }
            }
        }
        return logQueue;
    }

    private static LinkedList<BlockPos> findAllLeavesAbove(World world, BlockPos originalBlockPos) {
        LinkedList<BlockPos> logQueue = new LinkedList<BlockPos>();
        byte range = 4;
        byte max = 32;
        int off = range + 1;
        int x = originalBlockPos.getX();
        int y = originalBlockPos.getY();
        int z = originalBlockPos.getZ();

        logQueue.push(originalBlockPos);

        if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
            int offX;
            int offY;
            int offZ;
            int type;

            for (offX = -range; offX <= range; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        BlockPos tempPos = new BlockPos(x + offX, y + offY, z + offZ);
                        if (isNaturalLeaf(world.getBlockState(tempPos))) {
                            logQueue.push(tempPos);
                        }
                    }
                }
            }
        }
        return logQueue;
    }

    public static void BreakAllLogs(World world, BlockPos blockPos, LivingEntity livingEntity) {
        LinkedList<BlockPos> logs = findAllLogsAbove(world, blockPos);
        Boolean noTelekinesis = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.TELEKINESIS.get(), livingEntity.getMainHandItem()) == 0;
        PlayerEntity player = world.getPlayerByUUID(livingEntity.getUUID());
        if (player == null) return;

        for (BlockPos log : logs) {
            if (noTelekinesis) {
                world.destroyBlock(log, true);
            } else {
                Boolean addedtoInventory = player.inventory.add(world.getBlockState(log).getBlock().asItem().getDefaultInstance());
                world.destroyBlock(log, !addedtoInventory);
            }
        }

        logs = findAllLeavesAbove(world, blockPos);

        for (BlockPos leaves : logs) {
            world.destroyBlock(leaves, true);
        }

    }


}
