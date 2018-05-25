package org.enginehub.util.forge;

import java.io.File;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ForgeUtils.MODID, name = "Forge Utils", version = "%VERSION%", acceptableRemoteVersions = "*")
public class ForgeUtils {

    public static final String MODID = "forgeutils";

    private File blockData = new File("blocks.json");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            if (mod.getModId().equals("fastasyncworldedit")) {
                FMLLog.info("Detected FAWE");
                File dir = new File(configDir, "FastAsyncWorldEdit");
                if (!dir.exists() && !dir.mkdirs()) {
                    FMLLog.warning("Unable to create FAWE config dir.");
                    return;
                }
                blockData = new File(dir, "extrablocks.json");
                break;
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FMLLog.info("ForgeUtils loading.");

        try {
            FMLLog.info("Dumping block data to: " + blockData);
            new BlockRegistryDumper(blockData).run();
        } catch (Exception e) {
            FMLLog.severe("Error running block registry dumper: " +  e);
            e.printStackTrace();
        }
    }
}
