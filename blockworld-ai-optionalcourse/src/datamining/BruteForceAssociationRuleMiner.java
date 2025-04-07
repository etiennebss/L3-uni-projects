package datamining;

import java.util.*;
import modelling.*;

public class BruteForceAssociationRuleMiner extends AbstractAssociationRuleMiner {
    public BruteForceAssociationRuleMiner(BooleanDatabase database) {
        super(database);
    }

    // On génère tous les sous-ensembles d'un ensemble, sauf l'ensemble complet et vide
    public static Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> itemset) {
        Set<Set<BooleanVariable>> candidates = new HashSet<>();
        for (int i = 1; i < (1 << itemset.size()) -1; i++) {
            Set<BooleanVariable> subset = new HashSet<>();
            int j = 0;
            for (BooleanVariable item : itemset) {
                if ((i & (1 << j)) != 0) {
                    subset.add(item);
                }
                j++;
            }
            candidates.add(subset);
        }
        return candidates;

    }

    @Override
    public Set<AssociationRule> extract(float minFrequency, float minConfidence) {
        Set<Itemset> frequentItemset = new Apriori(this.database).extract(minFrequency);
        Set<AssociationRule> rules = new HashSet<>();

        for (Itemset itemset : frequentItemset) {
            Set<BooleanVariable> items = itemset.getItems();
            Set<Set<BooleanVariable>> candidatePremise = allCandidatePremises(items);

            for (Set<BooleanVariable> premise : candidatePremise) {
                Set<BooleanVariable> conclusion = new HashSet<>(items);
                conclusion.removeAll(premise);

                // On calcule la confiance et la fréquence
                float confidence = confidence(premise, conclusion, frequentItemset);
                if (confidence >= minConfidence) {
                    rules.add(new AssociationRule(premise, conclusion, itemset.getFrequency(), confidence));
                }
            }
        }
        return rules;
    }


}
