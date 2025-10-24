package unqualified.chemistry;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unqualified.chemistry.block.ModBlocks;
import unqualified.chemistry.item.ModItemGroups;
import unqualified.chemistry.item.ModItems;

public class Unqualified_Chemistry implements ModInitializer {
	public static final String MOD_ID = "unqualified_chemistry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}