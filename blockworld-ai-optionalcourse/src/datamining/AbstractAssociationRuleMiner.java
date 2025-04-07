package datamining;

import java.util.*;
import modelling.*;

public abstract class AbstractAssociationRuleMiner implements AssociationRuleMiner {
    protected BooleanDatabase database;

    public AbstractAssociationRuleMiner(BooleanDatabase database) {
        this.database = database;
    }

    @Override
    public BooleanDatabase getDatabase() {
        return database;
    }
    // Calcul de la fréquence d'un sous-ensemble provenant d'un ensemble itemsets
    public static float frequency(Set<BooleanVariable> itemset, Set<Itemset> itemsets) {
        for(Itemset item : itemsets) {
            if (item.getItems().equals(itemset)) {
                return item.getFrequency();
            }
        }
        throw new IllegalArgumentException("Non trouvé");
    }

    // Calcul de la confiance d'une règle
    public static float confidence(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, Set<Itemset> itemsets) {
        Set<BooleanVariable> union = new HashSet<>(premise);
        union.addAll(conclusion);
        float freqUnion = frequency(union, itemsets);
        float freqPremise = frequency(premise, itemsets);

        return freqUnion / freqPremise;
    }


}
