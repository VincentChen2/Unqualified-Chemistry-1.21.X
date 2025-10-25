package unqualified.chemistry.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import unqualified.chemistry.Unqualified_Chemistry;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Unqualified_Chemistry.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CARBON_ALLOTROPES = createTag("carbon_allotropes");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Unqualified_Chemistry.MOD_ID, name));
        }
    }
}
