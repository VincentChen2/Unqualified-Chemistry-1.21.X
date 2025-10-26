package unqualified.chemistry.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.block.custom.BeakerBlock;

public class ModBlocks {
    public static final Block GRAPHITE_BLOCK = registerBlock("graphite_block",
            AbstractBlock.Settings.create().
                    strength(1.2f).
                    requiresTool().
                    sounds(BlockSoundGroup.STONE));

    public static final Block BEAKER_BLOCK = registerBlock("beaker_block",
            AbstractBlock.Settings.create().
                    strength(0.5f).
                    nonOpaque().
                    sounds(BlockSoundGroup.GLASS));

    private static void registerBlockItem(String name, Block block) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, name));
        BlockItem item = new BlockItem(block, new Item.Settings().registryKey(key));
        Registry.register(Registries.ITEM, key, item);
    }

    private static Block registerBlock(String name, AbstractBlock.Settings blockSettings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Unqualified_Chemistry.MOD_ID, name));
        Block block;

        if (name.equals("beaker_block")) {
            block = new BeakerBlock(blockSettings.registryKey(key));
        } else {
            block = new Block(blockSettings.registryKey(key));
        }

        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, key, block);
    }

    public static void registerModBlocks() {
        Unqualified_Chemistry.LOGGER.info("Registering Mod Blocks for " + Unqualified_Chemistry.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.GRAPHITE_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ModBlocks.BEAKER_BLOCK);
        });
    }
}