package unqualified.chemistry.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup UNQUALIFIED_CHEMISTRY_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Unqualified_Chemistry.MOD_ID, "unqualified_chemistry_items"),
            FabricItemGroup.builder().
                    icon(() -> new ItemStack(ModItems.GRAPHITE_DUST)).
                    displayName(Text.translatable("itemgroup.unqualified_chemistry.unqualified_chemistry_items")).
                    entries((displayContext, entries) -> {
                        entries.add(ModItems.GRAPHITE_DUST);}).
                    build());

    public static final ItemGroup UNQUALIFIED_CHEMISTRY_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Unqualified_Chemistry.MOD_ID, "unqualified_chemistry_blocks"),
            FabricItemGroup.builder().
                    icon(() -> new ItemStack(ModBlocks.GRAPHITE_BLOCK)).
                    displayName(Text.translatable("itemgroup.unqualified_chemistry.unqualified_chemistry_blocks")).
                    entries((displayContext, entries) -> {
                        entries.add(ModBlocks.GRAPHITE_BLOCK);}).
                    build());

    public static void registerItemGroups() {
        Unqualified_Chemistry.LOGGER.info("Registering Mod Item Groups for" + Unqualified_Chemistry.MOD_ID);
    }
}
