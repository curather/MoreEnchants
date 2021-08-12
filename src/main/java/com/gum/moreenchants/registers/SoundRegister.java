package com.gum.moreenchants.registers;

import com.gum.moreenchants.MoreEnchants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegister {
    public static final DeferredRegister<SoundEvent> SOUNDEFFECTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            MoreEnchants.MODID);

    public static final RegistryObject<SoundEvent> JUMPSCARE = SOUNDEFFECTS.register("jumpscare", () ->
            new SoundEvent(new ResourceLocation(MoreEnchants.MODID, "jumpscare")));
    public static final RegistryObject<SoundEvent> JUMPSCARE2 = SOUNDEFFECTS.register("jumpscare2", () ->
            new SoundEvent(new ResourceLocation(MoreEnchants.MODID, "jumpscare2")));
    public static final RegistryObject<SoundEvent> JUMPSCARE3 = SOUNDEFFECTS.register("jumpscare3", () ->
            new SoundEvent(new ResourceLocation(MoreEnchants.MODID, "jumpscare3")));
    public static final RegistryObject<SoundEvent> SEEHEROBRINE = SOUNDEFFECTS.register("seeherobrine", () ->
            new SoundEvent(new ResourceLocation(MoreEnchants.MODID, "seeherobrine")));
}
