package datamining;

import java.util.*;
import modelling.BooleanVariable;

public class Apriori extends AbstractItemsetMiner {

    // Constructeur prenant une base de données en argument
    public Apriori(BooleanDatabase database) {
        super(database);
    }

    // Méthode pour extraire les singletons fréquents
    public Set<Itemset> frequentSingletons(float minFrequency) {
        Set<Itemset> singletons = new HashSet<>();
        for (BooleanVariable item : database.getItems()) {
            Set<BooleanVariable> singleton = new HashSet<>();
            singleton.add(item);
            float freq = frequency(singleton);
            if (freq >= minFrequency) {
                singletons.add(new Itemset(singleton, freq));
            }
        }
        return singletons;
    }

    // Méthode statique pour combiner deux ensembles triés
    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2) {
        // Vérifier si les deux ensembles ne sont pas vides et ont la même taille
        if (set1 == null || set2 == null || set1.isEmpty() || set2.isEmpty() || set1.size() != set2.size()) {
            return null;
        }
    
        // Création des itérateurs
        Iterator<BooleanVariable> it1 = set1.iterator();
        Iterator<BooleanVariable> it2 = set2.iterator();
    
        // Vérifier les k-1 premiers éléments
        for (int i = 0; i < set1.size() - 1; i++) {
            BooleanVariable var1 = it1.next();
            BooleanVariable var2 = it2.next();
            if (!var1.equals(var2)) {
                return null;  // Si les k-1 premiers éléments ne sont pas les mêmes, on retourne null
            }
        }
    
        // Comparer le dernier élément (kes)
        BooleanVariable last1 = it1.next();
        BooleanVariable last2 = it2.next();
        if (!last1.equals(last2)) {
            SortedSet<BooleanVariable> combined = new TreeSet<>(COMPARATOR);
            combined.addAll(set1);
            combined.add(last2);
            return combined;  // Si les derniers éléments sont différents, on combine les deux ensembles
        }
    
        return null;  // Si les ensembles sont identiques, on retourne null
    }

    // Méthode pour vérifier si tous les sous-ensembles d'un ensemble sont fréquents
    public static boolean allSubsetsFrequent(Set<BooleanVariable> itemset, Collection<SortedSet<BooleanVariable>> frequentSets) {
        for (BooleanVariable item : itemset) {
            Set<BooleanVariable> subset = new HashSet<>(itemset);
            subset.remove(item);
            if (!frequentSets.contains(subset)) return false;
        }
        return true;
    }

    // Méthode pour extraire les itemsets fréquents

    @Override
    public Set<Itemset> extract(float minFrequency) {
        // Ensemble pour stocker les itemsets fréquents
        Set<Itemset> frequentItemsets = new HashSet<>();
    
        // Extraire les singletons fréquents (itemsets de taille 1)
        frequentItemsets.addAll(this.frequentSingletons(minFrequency));
    
        // Liste des ensembles d'items fréquents de taille 1
        ArrayList<SortedSet<BooleanVariable>> frequentSets = new ArrayList<>();
        for (Itemset itemset : frequentItemsets) {
            // On transforme chaque itemset en ensemble trié
            frequentSets.add(new TreeSet<>(COMPARATOR) {{ addAll(itemset.getItems()); }});
        }
    
        // Tant qu'il y a des ensembles fréquents de taille k, on tente de générer des ensembles de taille k+1
        while (!frequentSets.isEmpty()) {
            ArrayList<SortedSet<BooleanVariable>> nextGenFrequentSets = new ArrayList<>();
    
            // Combiner chaque paire d'ensembles fréquents de taille k pour créer des candidats de taille k+1
            for (int i = 0; i < frequentSets.size(); i++) {
                for (int j = i + 1; j < frequentSets.size(); j++) {
                    // Générer un candidat en combinant deux ensembles
                    SortedSet<BooleanVariable> sortedSet1 = frequentSets.get(i);
                    SortedSet<BooleanVariable> sortedSet2 = frequentSets.get(j);
                    SortedSet<BooleanVariable> candidate = combine(sortedSet1, sortedSet2);
    
                    // Si la combinaison est valide (c'est-à-dire que les ensembles ont les k-1 mêmes éléments)
                    if (candidate != null) {
                        // Vérifier que tous les sous-ensembles propres de cet ensemble candidat sont eux-mêmes fréquents
                        if (allSubsetsFrequent(candidate, frequentSets)) {
                            // Calculer la fréquence du candidat
                            float freq = this.frequency(candidate);
    
                            // Si la fréquence du candidat est supérieure ou égale à la fréquence minimale, c'est un ensemble fréquent
                            if (freq >= minFrequency) {
                                frequentItemsets.add(new Itemset(candidate, freq)); // Ajouter aux itemsets fréquents
                                nextGenFrequentSets.add(candidate); // Ajouter à la génération suivante pour d'éventuelles combinaisons
                            }
                        }
                    }
                }
            }
    
            // On passe aux ensembles d'items fréquents de taille k+1
            frequentSets = nextGenFrequentSets;
        }
    
        // Retourner l'ensemble des itemsets fréquents découverts
        return frequentItemsets;
    }
}