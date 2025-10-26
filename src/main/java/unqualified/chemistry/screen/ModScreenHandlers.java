package unqualified.chemistry.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.screen.custom.BeakerScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<BeakerScreenHandler> BEAKER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    Identifier.of(Unqualified_Chemistry.MOD_ID, "beaker_screen_handler"),
                    new ExtendedScreenHandlerType<>(BeakerScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        Unqualified_Chemistry.LOGGER.info("Registering Screen Handlers for " + Unqualified_Chemistry.MOD_ID);
    }
}