package de.blutmondgilde.ibocore.block;

import de.blutmondgilde.ibocore.IBOCore;
import de.blutmondgilde.ibocore.item.ItemList;
import de.blutmondgilde.ibocore.item.templates.DefaultItemBlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.Supplier;

public class BlockList {
    private static final HashMap<String, DeferredRegister<Block>> REGISTRIES = new HashMap<>();
    private static final HashMap<String, RegistryObject<Block>> BLOCKS = new HashMap<>();
    private static final Logger logger = LogManager.getLogger("IBO-Core/Blocks");

    public static void register() {
        REGISTRIES.forEach((name, registry) -> {
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            logger.info("Registered Blocks for Mod-ID: " + name);
        });

        logger.info("All Blocks Registered");
    }

    private static void addRegistry(final String modId) {
        REGISTRIES.put(modId, DeferredRegister.create(ForgeRegistries.BLOCKS, modId));
        IBOCore.registeredModIds.add(modId);
    }

    public static void registerBlock(final String modId, final String name, final Supplier<? extends Block> block, final Item.Properties itemProbs) {
        if (!REGISTRIES.containsKey(modId)) {
            addRegistry(modId);
        }
        final RegistryObject<Block> blockObj = REGISTRIES.get(modId).register(name, block);
        BLOCKS.put(modId + "/" + name, blockObj);
        ItemList.registerItem(modId, name, () -> new DefaultItemBlockTemplate(block, itemProbs));
    }

    public static Supplier<? extends Block> getBlock(final String modId, final String registryName) {
        return BLOCKS.get(modId + "/" + registryName);
    }

    public static boolean isBlock(RegistryObject<Item> itemRegistryObject) {
        final ResourceLocation location = itemRegistryObject.get().getRegistryName();
        final String modId = location.getNamespace();
        final String name = location.getPath();
        return BLOCKS.containsKey(modId + "/" + name);
    }
}
