package de.blutmondgilde.ibocore;

import de.blutmondgilde.ibocore.block.BlockList;
import de.blutmondgilde.ibocore.block.builder.BlockBuilder;
import de.blutmondgilde.ibocore.config.Config;
import de.blutmondgilde.ibocore.item.ItemFileReader;
import de.blutmondgilde.ibocore.item.ItemList;
import de.blutmondgilde.ibocore.lang.LangFileGenerator;
import de.blutmondgilde.ibocore.resource.IBOResourcePack;
import de.blutmondgilde.ibocore.util.Ref;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


@Mod(Ref.MOD_ID)
public class IBOCore {
    public static final List<String> registeredModIds = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger("IBO-Core/Main");

    public IBOCore() {
        setupFeatures();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private static void setupFeatures() {
        BlockBuilder.create("test_block").build();

        //Items from file
        ItemFileReader.readFiles();
        //Items from Mods
        ItemList.register();
        //Blocks from Mods
        BlockList.register();
        //Lang generator
        LangFileGenerator.generateEnglishLangFile();
    }

    private void clientSetup(final FMLClientSetupEvent e) {
        IBOResourcePack.create();
    }
}
