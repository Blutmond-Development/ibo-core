package de.blutmondgilde.ibocore.item;

import de.blutmondgilde.ibocore.IBOCore;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.function.Supplier;

public class ItemList {
    private static final HashMap<String, DeferredRegister<Item>> REGISTRIES = new HashMap<>();
    private static final HashMap<String, RegistryObject<Item>> ITEMS = new HashMap<>();

    public static void register() {
        //Registers all Registries to FORGE
        REGISTRIES.forEach((name, registry) -> {
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            LogManager.getLogger("IBO-Core/Items").info("Registered Items for Mod-ID: " + name);
        });

        LogManager.getLogger("IBO-Core/Items").info("All Items Registered");
    }

    /**
     * Creates a new Item Registry with given Mod ID
     *
     * @param modId to create the Item Registry
     */
    private static void addRegistry(final String modId) {
        REGISTRIES.put(modId, DeferredRegister.create(ForgeRegistries.ITEMS, modId));
        IBOCore.registeredModIds.add(modId);
    }

    /**
     * Registers Item with the given parameter.
     *
     * @param modId the item should be registered to
     * @param name  of the Item (will be used as registry name)
     * @param item  supplier of the class used to create the item
     */
    public static void registerItem(final String modId, final String name, final Supplier<? extends Item> item) {
        if (!REGISTRIES.containsKey(modId)) {
            addRegistry(modId);
        }
        final RegistryObject<Item> itemObj = REGISTRIES.get(modId).register(name, item);
        ITEMS.put(modId + "/" + name, itemObj);
    }

    /**
     * Uses the Mod Id and the Item Name to identify a registered Item and return it
     *
     * @param modId    the Item is registered to
     * @param itemName the Item is registered with
     * @return found Item
     */
    public static Item getRegisteredItem(final String modId, final String itemName) {
        return ITEMS.get(modId + "/" + itemName).get();
    }

    public static HashMap<String, RegistryObject<Item>> getItems() {
        return ITEMS;
    }
}
