package com.gum.moreenchants.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Utils {
    public static Random rand = new Random();

    public static void applyAttributeModifierSafely(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        ModifiableAttributeInstance instance = entity.getAttribute(attribute);

        if (instance != null && !instance.hasModifier(modifier))
            instance.addTransientModifier(modifier);
    }

    public static void removeAttributeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        ModifiableAttributeInstance instance = entity.getAttribute(attribute);

        if (instance != null && instance.hasModifier(modifier))
            instance.removeModifier(modifier);
    }

    public static Item getEquipmentForSlot(EquipmentSlotType slot, int p_184636_1_) {
        switch (slot) {
            case MAINHAND:
                if (p_184636_1_ == 0) {
                    return Items.WOODEN_SWORD;//4
                } else if (p_184636_1_ == 1) {
                    return Items.GOLDEN_SWORD;//4
                } else if (p_184636_1_ == 2) {
                    return Items.STONE_SWORD;//5
                } else if (p_184636_1_ == 3) {
                    return Items.IRON_SWORD;//6
                } else if (p_184636_1_ == 4) {
                    return Items.DIAMOND_SWORD;//7
                }
        }
        return null;
    }

    public static ItemStack Smelted(ServerWorld world, ItemStack temp) {
        ItemStack smelted;
        Optional<FurnaceRecipe> recipe = world.getRecipeManager().getAllRecipesFor(IRecipeType.SMELTING).stream().filter((smeltingRecipe -> {
            return smeltingRecipe.getIngredients().get(0).test(temp);
        })).findFirst();
        if (recipe.isPresent()) {
            smelted = recipe.get().getResultItem();
            smelted.setCount(temp.getCount());

            return smelted;

        } else {
            return temp;
        }
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static ArrayList<Vector3d> AroundSpot(int low, int high) {
        ArrayList<Vector3d> temp = new ArrayList<>();
        for (int x = low; x <= high; x++)
            for (int y = low; y <= high; y++)
                for (int z = low; z <= high; z++)
                    temp.add(new Vector3d(x, y, z));
        return temp;
    }

    public static void printLogger(String value) {
        System.out.println(value);
    }

    public static int random(int min, int max) {

        return (rand.nextInt(max - min) + min);
    }

    public static int random(int number) {

        return (rand.nextInt(number));
    }

    public static void addOrDrop(ServerWorld currentWorld, PlayerEntity player, ItemStack item) {
        ItemEntity itementity = new ItemEntity(currentWorld, player.getX(), player.getY(), player.getZ(), item);
        if (!player.inventory.add(item))
            currentWorld.addFreshEntity(itementity);
    }

    public static BlockState ReplaceSet(ServerWorld currentWorld, int x, int y, int z, BlockState state, BlockState checkagainst) {
        if (currentWorld.getBlockState(new BlockPos(x, y, z)) == checkagainst)
            return CheckSet(currentWorld, x, y, z, state, Blocks.BEDROCK);
        return null;
    }

    public static BlockState CheckSet(ServerWorld currentWorld, int x, int y, int z, BlockState state, Block checkagainst) {
        if (currentWorld.getBlockState(new BlockPos(x, y, z)) == state) return state;
        if (currentWorld.getBlockState(new BlockPos(x, y, z)).getBlock() != checkagainst) {
            currentWorld.setBlock(new BlockPos(x, y, z), state, 3);
            return currentWorld.getBlockState(new BlockPos(x, y, z));
        }
        return null;
    }

    public static int GroundPosition(ServerWorld currentWorld, Vector3d startPos, int limit) {
        for (int tmp = 255; tmp > (limit + 1); tmp--) {
            BlockPos tmpBlock = new BlockPos(startPos.x, tmp, startPos.z);
            //Utils.printLogger("Block POS: "+tmpBlock);
            //Utils.printLogger("Block: "+currentWorld.getBlockState(tmpBlock).getBlock());
            if (!currentWorld.getBlockState(tmpBlock).getBlockState().getMaterial().isLiquid() && currentWorld.getBlockState(tmpBlock).getBlock() != Blocks.AIR && currentWorld.getBlockState(tmpBlock).getBlock() != Blocks.BEDROCK) {
                return tmp;
            }
        }
        return -1;
    }

    public static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public static void renderScare(MatrixStack matrixStack, ResourceLocation imageLocation, float tickdelta) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();
        Minecraft.getInstance().textureManager.bind(imageLocation);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void playSound(ServerPlayerEntity player, ResourceLocation audioLocation, SoundCategory category, Vector3d location, float volumeLevel, float pitchLevel) {
        player.connection.send(new SPlaySoundPacket(audioLocation, category, location, volumeLevel, pitchLevel));
    }

    public static String stringToRainbow(String parString, boolean parReturnToBlack) {
        int stringLength = parString.length();
        if (stringLength < 1) {
            return "";
        }
        String outputString = "";
        TextFormatting[] colorChar =
                {
                        TextFormatting.RED,
                        TextFormatting.GOLD,
                        TextFormatting.YELLOW,
                        TextFormatting.GREEN,
                        TextFormatting.AQUA,
                        TextFormatting.BLUE,
                        TextFormatting.LIGHT_PURPLE,
                        TextFormatting.DARK_PURPLE
                };
        for (int i = 0; i < stringLength; i++) {
            outputString = outputString + colorChar[i % 8] + parString.charAt(i);
        }
        // return color to a common one after (most chat is white, but for other GUI might want black)
        if (parReturnToBlack) {
            return outputString + TextFormatting.BLACK;
        }
        return outputString + TextFormatting.WHITE;
    }

    public static String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack) {
        int stringLength = parString.length();
        if (stringLength < 1) {
            return "";
        }
        String outputString = "";
        for (int i = 0; i < stringLength; i++) {
            if ((i + parShineLocation + System.currentTimeMillis() / 20) % 88 == 0) {
                outputString = outputString + TextFormatting.WHITE + parString.charAt(i);
            } else if ((i + parShineLocation + System.currentTimeMillis() / 20) % 88 == 1) {
                outputString = outputString + TextFormatting.YELLOW + parString.charAt(i);
            } else if ((i + parShineLocation + System.currentTimeMillis() / 20) % 88 == 87) {
                outputString = outputString + TextFormatting.YELLOW + parString.charAt(i);
            } else {
                outputString = outputString + TextFormatting.GOLD + parString.charAt(i);
            }
        }
        // return color to a common one after (most chat is white, but for other GUI might want black)
        if (parReturnToBlack) {
            return outputString + TextFormatting.BLACK;
        }
        return outputString + TextFormatting.WHITE;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderVanillaWeather(Minecraft mc, float partialTicks, double cameraX, double cameraY, double cameraZ, LightTexture lightmapIn, float[] rainSizeX, float[] rainSizeZ, ResourceLocation rainTexture, ResourceLocation snowTexture, int ticks) {
        float rainStrength = mc.level.getRainLevel(partialTicks);
        if (!(rainStrength <= 0.0F)) {
            lightmapIn.turnOnLightLayer();
            World world = mc.level;
            int x = MathHelper.floor(cameraX);
            int y = MathHelper.floor(cameraY);
            int z = MathHelper.floor(cameraZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            RenderSystem.enableAlphaTest();
            RenderSystem.disableCull();
            RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableDepthTest();
            int weatherRenderDistanceInBlocks = 5;
            if (Minecraft.useFancyGraphics()) {
                weatherRenderDistanceInBlocks = 10;
            }

            RenderSystem.depthMask(Minecraft.useShaderTransparency());// isFabulousGraphicsEnabled());
            int i1 = -1;
            float ticksAndPartialTicks = (float) ticks + partialTicks;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int dz = z - weatherRenderDistanceInBlocks; dz <= z + weatherRenderDistanceInBlocks; ++dz) {
                for (int dx = x - weatherRenderDistanceInBlocks; dx <= x + weatherRenderDistanceInBlocks; ++dx) {
                    int index = (dz - z + 16) * 32 + dx - x + 16;
                    double rainX = (double) rainSizeX[index] * 0.5D;
                    double rainZ = (double) rainSizeZ[index] * 0.5D;
                    mutable.set(dx, y, dz);
                    Biome biome = world.getBiome(mutable);

                    if (biome.getPrecipitation() != Biome.RainType.NONE) {
                        int motionBlockingHeight = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, mutable).getY();
                        int belowCameraYWeatherRenderDistance = y - weatherRenderDistanceInBlocks;
                        int aboveCameraYWeatherRenderDistance = y + weatherRenderDistanceInBlocks;
                        if (belowCameraYWeatherRenderDistance < motionBlockingHeight) {
                            belowCameraYWeatherRenderDistance = motionBlockingHeight;
                        }

                        if (aboveCameraYWeatherRenderDistance < motionBlockingHeight) {
                            aboveCameraYWeatherRenderDistance = motionBlockingHeight;
                        }

                        int atAboveHeightY = Math.max(motionBlockingHeight, y);

                        if (belowCameraYWeatherRenderDistance != aboveCameraYWeatherRenderDistance) {
                            Random random = new Random(dx * dx * 3121 + dx * 45238971 ^ dz * dz * 418711 + dz * 13761);
                            mutable.set(dx, belowCameraYWeatherRenderDistance, dz);
                            float biomeTemperature = biome.getTemperature(mutable);
                            if (biomeTemperature >= 0.15F) {
                                i1 = renderRain(mc, partialTicks, cameraX, cameraY, cameraZ, rainTexture, ticks, rainStrength, world, tessellator, bufferbuilder, (float) weatherRenderDistanceInBlocks, i1, mutable, dz, dx, rainX, rainZ, belowCameraYWeatherRenderDistance, aboveCameraYWeatherRenderDistance, atAboveHeightY, random);
                            } else {
                                i1 = renderSnow(mc, partialTicks, cameraX, cameraY, cameraZ, snowTexture, ticks, rainStrength, world, tessellator, bufferbuilder, (float) weatherRenderDistanceInBlocks, i1, ticksAndPartialTicks, mutable, dz, dx, rainX, rainZ, belowCameraYWeatherRenderDistance, aboveCameraYWeatherRenderDistance, atAboveHeightY, random);
                            }
                        }
                    }
                }
            }

            if (i1 >= 0) {
                tessellator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.disableAlphaTest();
            lightmapIn.turnOffLightLayer();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static int renderSnow(Minecraft mc, float partialTicks, double x, double y, double z, ResourceLocation snowTexture, int ticks, float rainStrength, World world, Tessellator tessellator, BufferBuilder bufferbuilder, float graphicsQuality, int i1, float f1, BlockPos.Mutable mutable, int dz, int dx, double d0, double d1, int j2, int k2, int atOrAboveY, Random random) {
        if (i1 != 1) {
            if (i1 >= 0) {
                tessellator.end();
            }

            i1 = 1;
            mc.getTextureManager().bind(snowTexture);
            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE);
        }

        float f6 = -((float) (ticks & 511) + partialTicks) / 512.0F;
        float f7 = (float) (random.nextDouble() + (double) f1 * 0.01D * (double) ((float) random.nextGaussian()));
        float f8 = (float) (random.nextDouble() + (double) (f1 * (float) random.nextGaussian()) * 0.001D);
        double d3 = (double) ((float) dx + 0.5F) - x;
        double d5 = (double) ((float) dz + 0.5F) - z;
        float f9 = MathHelper.sqrt(d3 * d3 + d5 * d5) / graphicsQuality;
        float alpha = ((1.0F - f9 * f9) * 0.3F + 0.5F) * rainStrength;
        mutable.set(dx, atOrAboveY, dz);
        int combinedLight = WorldRenderer.getLightColor(world, mutable);
        int l3 = combinedLight >> 16 & '\uffff';
        int i4 = (combinedLight & '\uffff') * 3;
        int j4 = (l3 * 3 + 240) / 4;
        int k4 = (i4 * 3 + 240) / 4;
        bufferbuilder.vertex((double) dx - x - d0 + 0.5D, (double) k2 - y, (double) dz - z - d1 + 0.5D).uv(0.0F + f7, (float) j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, alpha).uv2(k4, j4).endVertex();
        bufferbuilder.vertex((double) dx - x + d0 + 0.5D, (double) k2 - y, (double) dz - z + d1 + 0.5D).uv(1.0F + f7, (float) j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, alpha).uv2(k4, j4).endVertex();
        bufferbuilder.vertex((double) dx - x + d0 + 0.5D, (double) j2 - y, (double) dz - z + d1 + 0.5D).uv(1.0F + f7, (float) k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, alpha).uv2(k4, j4).endVertex();
        bufferbuilder.vertex((double) dx - x - d0 + 0.5D, (double) j2 - y, (double) dz - z - d1 + 0.5D).uv(0.0F + f7, (float) k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, alpha).uv2(k4, j4).endVertex();
        return i1;
    }

    @OnlyIn(Dist.CLIENT)
    private static int renderRain(Minecraft mc, float partialTicks, double x, double y, double z, ResourceLocation rainTexture, int ticks, float rainStrength, World world, Tessellator tessellator, BufferBuilder bufferbuilder, float l, int i1, BlockPos.Mutable mutable, int dz, int dx, double d0, double d1, int j2, int k2, int atOrAboveY, Random random) {
        if (i1 != 0) {
            if (i1 >= 0) {
                tessellator.end();
            }

            i1 = 0;
            mc.getTextureManager().bind(rainTexture);
            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE);
        }

        int i3 = ticks + dx * dx * 3121 + dx * 45238971 + dz * dz * 418711 + dz * 13761 & 31;
        float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
        double d2 = (double) ((float) dx + 0.5F) - x;
        double d4 = (double) ((float) dz + 0.5F) - z;
        float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / l;
        float alpha = ((1.0F - f4 * f4) * 0.5F + 0.5F) * rainStrength;
        mutable.set(dx, atOrAboveY, dz);
        int combinedLight = WorldRenderer.getLightColor(world, mutable);
        bufferbuilder.vertex((double) dx - x - d0 + 0.5D, (double) k2 - y, (double) dz - z - d1 + 0.5D).uv(0.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, alpha).uv2(combinedLight).endVertex();
        bufferbuilder.vertex((double) dx - x + d0 + 0.5D, (double) k2 - y, (double) dz - z + d1 + 0.5D).uv(1.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, alpha).uv2(combinedLight).endVertex();
        bufferbuilder.vertex((double) dx - x + d0 + 0.5D, (double) j2 - y, (double) dz - z + d1 + 0.5D).uv(1.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, alpha).uv2(combinedLight).endVertex();
        bufferbuilder.vertex((double) dx - x - d0 + 0.5D, (double) j2 - y, (double) dz - z - d1 + 0.5D).uv(0.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, alpha).uv2(combinedLight).endVertex();
        return i1;
    }

    public static double GRAVITY_METRES_PER_SEC = 20;
    public static double GRAVITY_VELOCITY = 0.05;
    public static BlockPos NULL_POS = new BlockPos(0, -100, 0);

    public static void drawBoundedText(MatrixStack matrices, int posx, int posy, int bound, String s) {
        StringBuilder str = new StringBuilder(s);
        for (int a = 0; a < s.length(); a++) {
            if (a % bound == 0) {
                if (String.valueOf(str.charAt(a)).equals(" ")) {
                    str.insert(a, "=");
                } else {

                    int k = a;
                    while (!String.valueOf(str.charAt(k)).equals(" ")) {
                        k--;
                        if (k == -1) {
                            break;
                        }
                    }
                    if (k != -1) {
                        str.insert(k + 1, "=");
                    }

                }
            }
        }

        String[] string = str.toString().split("=");

        int y = 0;
        for (String strings : string) {
            AbstractGui.drawString(matrices, Minecraft.getInstance().font, strings, posx, posy + y, 0xffffff);
            y += 10;
        }

    }


    public static void drawLine(MatrixStack stack, int x1, int y1, int x2, int y2, float red, float green, float blue) {
        GL11.glLineWidth(2F);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(red, green, blue, 255);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x2, y2);

        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static Chunk[] getSurroundingChunks(ServerWorld level, BlockPos worldPosition) {
        return new Chunk[]{level.getChunkAt(worldPosition), level.getChunkAt(worldPosition.offset(16, 0, 0)), level.getChunkAt(worldPosition.offset(0, 0, 16)),
                level.getChunkAt(worldPosition.offset(-16, 0, 0)), level.getChunkAt(worldPosition.offset(0, 0, -16)), level.getChunkAt(worldPosition.offset(16, 0, 16)),
                level.getChunkAt(worldPosition.offset(-16, 0, -16)), level.getChunkAt(worldPosition.offset(16, 0, -16)), level.getChunkAt(worldPosition.offset(-16, 0, 16))};
    }


    public static boolean isReachable(ServerWorld world, BlockPos pos1, BlockPos pos2, int radius) {
        Vector3d vec1 = new Vector3d(pos1.getX() + 0.5f, pos1.getY() + 0.5f, pos1.getZ() + 0.5f);
        Vector3d vec2 = new Vector3d(pos2.getX() + 0.5f, pos2.getY() + 0.5f, pos2.getZ() + 0.5f);
        Vector3d vector = new Vector3d(vec2.x - vec1.x, vec2.y - vec1.y, vec2.z - vec1.z);
        RayTraceContext ctx = new RayTraceContext(vec1.add(vector.normalize()), vec2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
        BlockRayTraceResult result = world.clip(ctx);


        return world.getBlockState(result.getBlockPos()).getBlock() == world.getBlockState(pos2).getBlock() && result.getBlockPos().equals(pos2) && vector.length() <= radius;
    }


    public static double blocksPerSecondToVelocity(double a) {
        return a * 0.05;
    }


    public static Collection<BlockPos> getSurroundingBlockPositionsVertical(BlockPos pos, Direction dir) {
        List<BlockPos> positions = new ArrayList<>();
        positions.add(pos.above());
        positions.add(pos.below());
        if (dir.equals(Direction.WEST) || dir.equals(Direction.EAST)) {
            positions.add(pos.offset(0, 1, 1));
            positions.add(pos.offset(0, 1, -1));
            positions.add(pos.offset(0, 0, 1));
            positions.add(pos.offset(0, 0, -1));
            positions.add(pos.offset(0, -1, 1));
            positions.add(pos.offset(0, -1, -1));

        }
        if (dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH)) {
            positions.add(pos.offset(1, 1, 0));
            positions.add(pos.offset(-1, 1, 0));
            positions.add(pos.offset(1, 0, 0));
            positions.add(pos.offset(-1, 0, 0));
            positions.add(pos.offset(1, -1, 0));
            positions.add(pos.offset(-1, -1, 0));
        }


        return positions;
    }

    public static Collection<BlockPos> getSurroundingBlockPositionsHorizontal(BlockPos pos) {
        List<BlockPos> positions = new ArrayList<>();
        positions.add(pos.west());
        positions.add(pos.east());
        positions.add(pos.north());
        positions.add(pos.south());
        positions.add(pos.offset(1, 0, 1));
        positions.add(pos.offset(1, 0, -1));
        positions.add(pos.offset(-1, 0, 1));
        positions.add(pos.offset(-1, 0, -1));
        return positions;
    }

    public static boolean isEntityReachable(ServerWorld world, BlockPos pos1, BlockPos pos2) {
        Vector3d vec1 = new Vector3d(pos1.getX() + 0.5f, pos1.getY() + 0.5f, pos1.getZ() + 0.5f);
        Vector3d vec2 = new Vector3d(pos2.getX() + 0.5f, pos2.getY() + 1.25f, pos2.getZ() + 0.5f);
        Vector3d vector = new Vector3d(vec2.x - vec1.x, vec2.y - vec1.y, vec2.z - vec1.z);
        RayTraceContext ctx = new RayTraceContext(vec2, vec1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
        BlockRayTraceResult result = world.clip(ctx);

        return result.getBlockPos().equals(pos1);
    }


    public static Vector3d calculateVelocity(Vector3d pos1, Vector3d pos2) {
        return new Vector3d(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z).normalize();
    }

    public static Vector3d getBlockCenter(BlockPos pos) {
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static List<BlockPos> getBlockPositionsByDirection(Direction dir, BlockPos mainpos, int count) {
        List<BlockPos> pos = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            if (dir.equals(Direction.UP)) {
                pos.add(mainpos.offset(0, i, 0));
            } else if (dir.equals(Direction.DOWN)) {
                pos.add(mainpos.offset(0, -i, 0));
            } else if (dir.equals(Direction.NORTH)) {
                pos.add(mainpos.offset(0, 0, -i));
            } else if (dir.equals(Direction.SOUTH)) {
                pos.add(mainpos.offset(0, 0, i));
            } else if (dir.equals(Direction.WEST)) {
                pos.add(mainpos.offset(-i, 0, 0));
            } else {
                pos.add(mainpos.offset(i, 0, 0));
            }
        }

        return pos;
    }

    public static boolean equalsBlockPos(BlockPos pos1, BlockPos pos2) {
        return (pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY()) && (pos1.getZ() == pos2.getZ());
    }

    public static Direction getRandomHorizontalDirection(boolean exclude, Direction whatToExclude, Random rnd) {
        Direction[] horizontal = {Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST};

        if (exclude) {
            Direction direction = horizontal[rnd.nextInt(4)];
            while (direction.equals(whatToExclude)) {
                direction = horizontal[rnd.nextInt(4)];
            }
            return direction;
        }
        return horizontal[rnd.nextInt(4)];
    }

    public static class HashMapConstructor<T, E> {
        private final Map<T, E> MAP = new HashMap<>();

        public HashMapConstructor() {
        }

        public HashMapConstructor<T, E> addEntry(T key, E entry) {
            MAP.put(key, entry);
            return this;
        }

        public Map<T, E> build() {
            return MAP;
        }

    }

    public static List<Integer> oldIdToIntArray(String oldid) {
        String oldidfull = oldid.replace("-", "");

        List<Integer> parts = getIntegerParts(oldidfull, 8);
        return parts;
        //return getConvertedUUID(parts.toArray(new Integer[parts.size()]));
    }

    private static List<Integer> getIntegerParts(String string, int partitionSize) {
        List<Integer> parts = new ArrayList<Integer>();
        int len = string.length();
        for (int i = 0; i < len; i += partitionSize) {
            parts.add(partToDecimalValue(string.substring(i, Math.min(len, i + partitionSize))));
        }
        return parts;
    }

    private static int partToDecimalValue(String hex) {
        int decimal = Long.valueOf(hex, 16).intValue();
        return decimal;
    }

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.blockPosition().getX() - pos.getX();
        double deltaY = entity.blockPosition().getY() - pos.getY();
        double deltaZ = entity.blockPosition().getZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    public static ItemStack getTexturedHead(String texture, String oldid, Integer amount) {
        ItemStack texturedhead = new ItemStack(Items.PLAYER_HEAD, amount);

        List<Integer> intarray = Utils.oldIdToIntArray(oldid);

        CompoundNBT skullOwner = new CompoundNBT();
        skullOwner.putIntArray("Id", intarray);

        CompoundNBT properties = new CompoundNBT();
        ListNBT textures = new ListNBT();
        CompoundNBT tex = new CompoundNBT();
        tex.putString("Value", texture);
        textures.add(tex);

        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        texturedhead.addTagElement("SkullOwner", skullOwner);
        return texturedhead;
    }
    public static Entity getEntityLookedAt(Entity e) {
        Entity foundEntity = null;

        final double finalDistance = 32;
        double distance = finalDistance;
        RayTraceResult pos = raycast(e, finalDistance);

        Vector3d positionVector = e.position();
        if(e instanceof PlayerEntity)
            positionVector = positionVector.add(0, e.getEyeHeight(), 0);

        if(pos != null)
            distance = pos.getLocation().distanceTo(positionVector);

        Vector3d lookVector = e.getLookAngle();
        Vector3d reachVector = positionVector.add(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance);

        Entity lookedEntity = null;
        List<Entity> entitiesInBoundingBox = e.level.getEntities(e, e.getBoundingBox().inflate(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance).inflate(1F, 1F, 1F));
        double minDistance = distance;

        for(Entity entity : entitiesInBoundingBox) {
            if(entity.canBeCollidedWith()) {
                double collisionBorderSize = entity.getBoundingBox().getSize();
                AxisAlignedBB hitbox = entity.getBoundingBox().inflate(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                Optional<Vector3d> interceptPosition = hitbox.clip(positionVector, reachVector);
                Vector3d interceptVec = interceptPosition.orElse(null);

                if(hitbox.contains(positionVector)) {
                    if(0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if(interceptVec != null) {
                    double distanceToEntity = positionVector.distanceTo(interceptVec);

                    if(distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if(lookedEntity != null && (minDistance < distance || pos == null))
                foundEntity = lookedEntity;
        }

        return foundEntity;
    }

    @OnlyIn(Dist.CLIENT)
    public static RayTraceResult raycast(Entity e, double len) {
        Vector3d vec = new Vector3d(e.getX(), e.getY(), e.getZ());
        if(e instanceof PlayerEntity)
            vec = vec.add(new Vector3d(0, e.getEyeHeight(), 0));

        Vector3d look = e.getLookAngle();
        if(look == null)
            return null;

        return raycast(e.level, vec, look, e, len);
    }

    @OnlyIn(Dist.CLIENT)
    public static RayTraceResult raycast(World world, Vector3d origin, Vector3d ray, Entity e, double len) {
        Vector3d end = origin.add(ray.normalize().scale(len));
        RayTraceResult pos = world.clip(new RayTraceContext(origin, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, e));
        return pos;
    }

    public static boolean canSeeVisual(PlayerEntity Player, Entity p_70685_1_) {
        Vector3d vector3d = new Vector3d(Player.getX(), Player.getEyeY(), Player.getZ());
        Vector3d vector3d1 = new Vector3d(p_70685_1_.getX(), p_70685_1_.getEyeY(), p_70685_1_.getZ());
        return Player.level.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, Player)).getType() == RayTraceResult.Type.MISS;
    }
    public static boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

}
