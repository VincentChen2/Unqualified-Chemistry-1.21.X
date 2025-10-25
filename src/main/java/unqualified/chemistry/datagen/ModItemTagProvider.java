package unqualified.chemistry.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import unqualified.chemistry.item.ModItems;
import unqualified.chemistry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {


    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ModTags.Items.CARBON_ALLOTROPES)
                .add(ModItems.GRAPHITE_DUST)
                .add(Items.COAL)
                .add(Items.COAL_BLOCK)
                .add(Items.CHARCOAL)
                .add(Items.DIAMOND);
    }
}
