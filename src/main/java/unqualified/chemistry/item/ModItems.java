package unqualified.chemistry.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;

public class ModItems {
    public static final Item CARBON_DUST = registerItem("carbon_dust", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID,"carbon_dust")))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Unqualified_Chemistry.LOGGER.info("Registering Mod Items" + Unqualified_Chemistry.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(CARBON_DUST);
        });
    }
}