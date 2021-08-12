package com.gum.moreenchants;

import com.gum.moreenchants.forgeEvents.*;
import com.gum.moreenchants.registers.*;
import com.gum.moreenchants.util.randomItemGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MoreEnchants.MODID)
public class MoreEnchants {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "moreenchants";
    public static randomItemGenerator randomItems = new randomItemGenerator();
    public static boolean isDevelopment = !FMLEnvironment.production;
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public MoreEnchants() {

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        //Register enchantments
        //MinecraftForge.EVENT_BUS.register(new RegistryEvents());
        //modEventBus.register(RegistryEvents.class);
        EnchantmentRegister.ENCHANTMENTS.register(modEventBus);
        SoundRegister.SOUNDEFFECTS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(BlockHandler.class);
        MinecraftForge.EVENT_BUS.register(LivingExperienceDropHandler.class);
        MinecraftForge.EVENT_BUS.register(LivingHurtHandler.class);
        MinecraftForge.EVENT_BUS.register(LivingAttackHandler.class);
        MinecraftForge.EVENT_BUS.register(LivingEquipmentChangeHandler.class);

        //MinecraftForge.EVENT_BUS.register(new EntityViewRenderHandler());
        //MinecraftForge.EVENT_BUS.register(new PlaySoundHandler());

        //MinecraftForge.EVENT_BUS.register(new RenderLivingHandler());
        //MinecraftForge.EVENT_BUS.register(new RightClickItemHandler());
    }

}
