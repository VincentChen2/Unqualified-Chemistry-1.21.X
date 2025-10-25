package unqualified.chemistry.customTypes;

import net.minecraft.item.Item;

import java.util.Map;
import java.util.Collections;

public class ChemicalItem extends Item {
    private final Map<String, Integer> chemicalFormula;

    public ChemicalItem(Settings settings, Map<String, Integer> chemicalFormula) {
        super(settings);
        this.chemicalFormula = Collections.unmodifiableMap(chemicalFormula);
    }

    public Map<String, Integer> getChemicalFormula() {
        return chemicalFormula;
    }

    public boolean containsElement(String element) {
        return chemicalFormula.containsKey(element);
    }

    public int getElementCount(String element) {
        return chemicalFormula.getOrDefault(element, 0);
    }

    public String getFormulaAsString() {
        StringBuilder sb = new StringBuilder();
        chemicalFormula.forEach((element, count) -> {
            sb.append(element);
            if (count > 1) {
                sb.append(count);
            }
        });
        return sb.toString();
    }
}