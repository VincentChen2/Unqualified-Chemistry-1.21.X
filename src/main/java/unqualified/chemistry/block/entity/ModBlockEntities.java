package unqualified.chemistry.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.block.ModBlocks;
import unqualified.chemistry.block.entity.custom.BeakerBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<BeakerBlockEntity> BEAKER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(Unqualified_Chemistry.MOD_ID, "beaker_be"),
                    FabricBlockEntityTypeBuilder.create(BeakerBlockEntity::new, ModBlocks.BEAKER_BLOCK).build());

    public static void registerBlockEntities() {
        Unqualified_Chemistry.LOGGER.info("Registering Block Entities for " + Unqualified_Chemistry.MOD_ID);
    }
}