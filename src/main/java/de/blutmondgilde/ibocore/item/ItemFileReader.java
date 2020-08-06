package de.blutmondgilde.ibocore.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blutmondgilde.ibocore.item.builder.ItemBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ItemFileReader {
    private static final Logger LOGGER = LogManager.getLogger("IBO-Core/ItemFileReader");

    public static void readFiles() {
        File folder = FMLPaths.GAMEDIR.get().resolve("ibo-core").resolve("items").toFile();
        if (folder.mkdirs()) {
            LOGGER.info("Generated Folder: " + folder.getName());
        }

        File[] files = folder.listFiles();
        if (files == null) return;

        for (final File file : files) {
            readFile(file);
        }
    }

    private static void readFile(final File file) {
        JsonParser parser = new JsonParser();
        String name;
        String tab = null;
        String modId = null;
        int stackSize = -1;
        try {
            Object object = parser.parse(new FileReader(file));
            JsonObject rootElement = (JsonObject) object;

            if (!rootElement.has("name")) {
                throw new IllegalStateException("The file " + file.getName() + " is invalid. Please define at least a name value");
            }

            name = rootElement.get("name").getAsString();
            if (rootElement.has("tab")) {
                tab = rootElement.get("tab").getAsString();
            }
            if (rootElement.has("maxStack")) {
                stackSize = rootElement.get("maxStack").getAsInt();
            }

            if (rootElement.has("modId")) {
                modId = rootElement.get("modId").getAsString();
            }

            generateItems(name, tab, modId, stackSize);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generateItems(final String name, String tab, String modId, int stackSize) {
        boolean hasTab = !(tab == null);
        boolean hasModId = !(modId == null);
        boolean hasStackSize = !(stackSize == -1);
        //true true true
        if (hasTab && hasModId && hasStackSize) {
            ItemBuilder.create(name)
                    .setCreativeTab(ItemGroupList.getItemGroup(tab))
                    .setModId(modId)
                    .setMaxStackSize(stackSize)
                    .build();
        }
        //false true true
        if (!hasTab && hasModId && hasStackSize) {
            ItemBuilder.create(name)
                    .setModId(modId)
                    .setMaxStackSize(stackSize)
                    .build();
        }
        //false false true
        if (!hasTab && !hasModId && hasStackSize) {
            ItemBuilder.create(name)
                    .setMaxStackSize(stackSize)
                    .build();
        }
        //false false false
        if (!hasTab && !hasModId && !hasStackSize) {
            ItemBuilder.create(name).build();
        }
        //true false false
        if (hasTab && !hasModId && !hasStackSize) {
            ItemBuilder.create(name)
                    .setCreativeTab(ItemGroupList.getItemGroup(tab))
                    .build();
        }
        //false true false
        if (!hasTab && hasModId && !hasStackSize) {
            ItemBuilder.create(name)
                    .setModId(modId)
                    .build();
        }
        //true true false
        if (hasTab && hasModId && !hasStackSize) {
            ItemBuilder.create(name)
                    .setCreativeTab(ItemGroupList.getItemGroup(tab))
                    .setModId(modId)
                    .build();
        }
        //true false true
        if (hasTab && !hasModId && hasStackSize) {
            ItemBuilder.create(name)
                    .setCreativeTab(ItemGroupList.getItemGroup(tab))
                    .setMaxStackSize(stackSize)
                    .build();
        }

        LOGGER.info("Generated Item " + name + " from json file.");
    }

}
