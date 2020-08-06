package de.blutmondgilde.ibocore.handler;

import de.blutmondgilde.ibocore.config.Config;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber
public class GuiHandler {
    @SubscribeEvent
    public static void onGuiInit(final GuiScreenEvent.InitGuiEvent e) {
        if (!Config.isEditModeEnabled) return;

        if (e.getGui().getClass().getName().equals(OptionsScreen.class.getName())) {
            //e.addWidget(new AccessButton());
        }
    }

    public static class AccessButton extends Button {
        public AccessButton() {
            super(50, 50, 150, 20, new TranslationTextComponent("ibo.button.open"), (action) -> AccessButton.onPressButton());
        }

        public static void onPressButton() {
            LogManager.getLogger("IBO-Core/Button").debug("Pressed");
        }
    }
}
