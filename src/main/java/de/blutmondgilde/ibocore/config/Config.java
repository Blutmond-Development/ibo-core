package de.blutmondgilde.ibocore.config;

import de.blutmondgilde.ibocore.util.Ref;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    protected static final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    public static final ClientConfig CLIENT = specPair.getLeft();
    public static final ForgeConfigSpec CLIENT_SPEC = specPair.getRight();

    public static boolean isEditModeEnabled;


    public static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue isEditModeEnabled;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            isEditModeEnabled = builder
                    .comment("Is Edit-Mode for Items, Blocks and Ores enabled?")
                    .define("enableEditmode", true);
            builder.push("General");
            builder.pop();
        }
    }

    public static void bakeConfig() {
        isEditModeEnabled = CLIENT.isEditModeEnabled.get();
    }

    @SubscribeEvent
    public static void onConfigEvent(final ModConfig.ModConfigEvent e) {
        if (e.getConfig().getSpec() == Config.CLIENT_SPEC) {
            bakeConfig();
        }
    }
}
