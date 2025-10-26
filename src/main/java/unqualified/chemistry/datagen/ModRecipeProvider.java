package unqualified.chemistry.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import unqualified.chemistry.block.ModBlocks;
import unqualified.chemistry.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                offerBlasting(List.of(Items.COAL_BLOCK), RecipeCategory.MISC, ModBlocks.GRAPHITE_BLOCK,
                        1.0f, 600, "graphite_blasting");

                createShaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAPHITE_BLOCK)
                        .pattern("###")
                        .pattern("###")
                        .pattern("###")
                        .input('#', ModItems.GRAPHITE_DUST)
                        .criterion(hasItem(ModItems.GRAPHITE_DUST), conditionsFromItem(ModItems.GRAPHITE_DUST))
                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.GRAPHITE_DUST, 9)
                        .input(ModBlocks.GRAPHITE_BLOCK)
                        .criterion(hasItem(ModBlocks.GRAPHITE_BLOCK), conditionsFromItem(ModBlocks.GRAPHITE_BLOCK))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.MISC, ModItems.PENCIL, 4)
                        .pattern(" g ")
                        .pattern(" c ")
                        .pattern(" s ")
                        .input('g', ModItems.GRAPHITE_DUST)
                        .input('c', Items.CLAY)
                        .input('s', Items.STICK)
                        .criterion(hasItem(ModItems.GRAPHITE_DUST), conditionsFromItem(ModItems.GRAPHITE_DUST))
                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.TOOLS, ModItems.BOOK_AND_PENCIL, 1)
                        .input(ModItems.PENCIL)
                        .input(Items.BOOK)
                        .criterion(hasItem(ModItems.PENCIL), conditionsFromItem(ModItems.PENCIL))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.DECORATIONS, ModBlocks.BEAKER_BLOCK)
                        .pattern("G G")
                        .pattern("G G")
                        .pattern("GGG")
                        .input('G', Items.GLASS)
                        .criterion(hasItem(Items.GLASS), conditionsFromItem(Items.GLASS))
                        .offerTo(recipeExporter);
            }
        };
    }

    @Override
    public String getName() {
        return "Unqualified Chemistry Recipes";
    }
}