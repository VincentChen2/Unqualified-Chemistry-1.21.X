package unqualified.chemistry.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WritableBookContentComponent;
import net.minecraft.item.Item;
import net.minecraft.item.WritableBookItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;

public class ModItems {
    public static final Item GRAPHITE_DUST = registerItem("graphite_dust",
            new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID,"graphite_dust")))));
    public static final Item PENCIL = registerItem("pencil",
            new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, "pencil")))));
    public static final Item BOOK_AND_PENCIL = registerItem("book_and_pencil",
            new WritableBookItem(new Item.Settings()
                    .maxCount(1)
                    .component(DataComponentTypes.WRITABLE_BOOK_CONTENT, WritableBookContentComponent.DEFAULT)
                    .registryKey((RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, "book_and_pencil"))))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Unqualified_Chemistry.LOGGER.info("Registering Mod Items for " + Unqualified_Chemistry.MOD_ID);
    }
}