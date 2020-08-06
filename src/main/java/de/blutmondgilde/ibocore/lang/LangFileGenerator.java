package de.blutmondgilde.ibocore.lang;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blutmondgilde.ibocore.util.Ref;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LangFileGenerator {
    private static final Logger logger = LogManager.getLogger("IBO-Core/LangFileGenerator");
    private static final HashMap<String, String> langStrings = new HashMap<>();

    public static void addString(final String modId, final String translationString) {
        langStrings.put(translationString, modId);
    }

    public static void addString(final String translationString) {
        langStrings.put(translationString, Ref.MOD_ID);
    }

    public static void generateEnglishLangFile() {
        final File folder = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve("ibocore").resolve("lang").toFile();
        final File file = FMLPaths.GAMEDIR.get().resolve("resourcepacks").resolve("ibo-core").resolve("assets").resolve("ibocore").resolve("lang").resolve("en_us.json").toFile();

        try {
            if (folder.mkdirs()) {
                logger.info("Generated folder: " + folder.getName());
            }

            JsonObject rootElement;

            if (file.exists()) {
                JsonParser parser = new JsonParser();
                Object object = parser.parse(new FileReader(file));
                rootElement = (JsonObject) object;
            } else {
                rootElement = new JsonObject();
            }

            logger.info("Loaded Lang File with: " + rootElement.toString());

            langStrings.forEach((k, v) -> {
                if (!rootElement.has(k)) {
                    rootElement.addProperty(k, k);
                    logger.info("Added Translation String " + k);
                }
            });

            FileOutputStream out = new FileOutputStream(file);
            out.write(rootElement.toString().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("Generated Langfile");
        }
    }
}
