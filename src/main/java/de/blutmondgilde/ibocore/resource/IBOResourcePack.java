package de.blutmondgilde.ibocore.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.blutmondgilde.ibocore.IBOCore;
import de.blutmondgilde.ibocore.block.BlockList;
import de.blutmondgilde.ibocore.item.ItemList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class IBOResourcePack {
    private static final Logger logger = LogManager.getLogger("IBO-Core/ResourceLoader");

    public static void create() {
        for (String modId : IBOCore.registeredModIds) {
            final File folder = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve(modId).resolve("textures").resolve("item").toFile();
            if (folder.mkdirs()) {
                logger.info("Generated Folder: " + folder.getPath());
            }
        }

        ItemList.getItems().forEach((name, registryObject) -> {
            final String modId = Objects.requireNonNull(registryObject.get().getRegistryName()).getNamespace();
            final File folder = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve(modId).resolve("models").resolve("item").toFile();
            if (folder.mkdirs()) {
                logger.info("Generated Folder: " + folder.getPath());
            }
            if (BlockList.isBlock(registryObject)) {
                final ResourceLocation location = registryObject.get().getRegistryName();
                writeBlockModelFile(location);
                writeBlockStateFile(location);
            } else {
                writeItemModelFile(Objects.requireNonNull(registryObject.get().getRegistryName()));
            }
        });

        if (!FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("pack.mcmeta").toFile().exists()) {
            writePackMetaData();
        }

        if (addResourcePackToSettings()) {
            logger.info("Enabled IBO-Core Resource Pack");
        }

        for (ClientResourcePackInfo info : Minecraft.getInstance().getResourcePackList().getAllPacks()) {
            logger.info("Resource Pack: " + info.getName());
        }

        reloadResources();
    }

    private static void writeBlockModelFile(final ResourceLocation resourceLocation) {
        final File file = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve(resourceLocation.getNamespace()).resolve("models").resolve("block").resolve(resourceLocation.getPath() + ".json").toFile();
        if (file.exists()) return;
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cube_all");
        JsonObject textues = new JsonObject();
        textues.addProperty("all", resourceLocation.getNamespace() + ":block/" + resourceLocation.getPath());
        root.add("textures", textues);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(root.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBlockStateFile(final ResourceLocation resourceLocation) {
        final File file = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve(resourceLocation.getNamespace()).resolve("blockstates").resolve(resourceLocation.getPath() + ".json").toFile();
        if (file.exists()) return;
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject list = new JsonObject();
        list.addProperty("model", resourceLocation.getNamespace() + ":block/" + resourceLocation.getPath());
        variants.add("", list);
        root.add("variants", variants);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(root.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeItemModelFile(final ResourceLocation resourceLocation) {
        final File file = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve(resourceLocation.getNamespace()).resolve("models").resolve("item").resolve(resourceLocation.getPath() + ".json").toFile();
        if (file.exists()) return;
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textues = new JsonObject();
        textues.addProperty("layer0", resourceLocation.getNamespace() + ":item/" + resourceLocation.getPath());
        root.add("textures", textues);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(root.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writePackMetaData() {
        JsonObject jsonObject = new JsonObject();
        JsonElement pack_format = new JsonPrimitive(5);
        JsonElement description = new JsonPrimitive("Dynamic Generated IBO-Core Resource Pack");
        JsonObject pack = new JsonObject();
        pack.add("pack_format", pack_format);
        pack.add("description", description);
        jsonObject.add("pack", pack);

        try {
            FileOutputStream out = new FileOutputStream(FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("pack.mcmeta").toFile());
            out.write((jsonObject.toString() + '\n').getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("Created pack.mcmeta file");
        }
    }

    private static boolean addResourcePackToSettings() {
        File file = FMLPaths.GAMEDIR.get().resolve("options.txt").toFile();


        try {
            Scanner scanner = new Scanner(file);
            StringBuilder fileOut = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("resourcePacks:[")) {
                    if (!line.contains("ibo-core")) {
                        line = line.substring(0, line.length() - 1) + ",\"file/ibo-core\"]";
                    } else {
                        return false;
                    }
                }

                fileOut.append(line);
                fileOut.append("\n");
            }

            FileOutputStream out = new FileOutputStream(file);
            out.write(fileOut.toString().getBytes());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void reloadResources() {
        Minecraft.getInstance().getResourcePackList().reloadPacksFromFinders();
        final Collection<String> enabled = new ArrayList<>();

        for (final ClientResourcePackInfo info : Minecraft.getInstance().getResourcePackList().getEnabledPacks()) {
            enabled.add(info.getName());
        }

        if (!enabled.contains("file/ibo-core")) {
            enabled.add("file/ibo-core");
            Minecraft.getInstance().getResourcePackList().setEnabledPacks(enabled);
        }
    }
}
