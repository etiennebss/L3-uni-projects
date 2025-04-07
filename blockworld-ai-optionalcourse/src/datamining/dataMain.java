package datamining;

import java.util.*;
import modelling.BooleanVariable;

public class dataMain {
    public static void main(String[] args) {
        // Création de variables booléennes pour représenter les items
        BooleanVariable a = new BooleanVariable("A");
        BooleanVariable b = new BooleanVariable("B");
        BooleanVariable c = new BooleanVariable("C");
        BooleanVariable d = new BooleanVariable("D");

        // Initialisation d'un ensemble d'items
        Set<BooleanVariable> items = new HashSet<>();
        items.add(a);
        items.add(b);
        items.add(c);
        items.add(d);

        // Création de la base de données transactionnelle
        BooleanDatabase db = new BooleanDatabase(items);

        // Ajout de transactions (ensembles d'items)
        db.add(new HashSet<>(Arrays.asList(a, b))); // Transaction 1 : {A, B}
        db.add(new HashSet<>(Arrays.asList(b, c))); // Transaction 2 : {B, C}
        db.add(new HashSet<>(Arrays.asList(a, c))); // Transaction 3 : {A, C}
        db.add(new HashSet<>(Arrays.asList(a, b, c))); // Transaction 4 : {A, B, C}
        db.add(new HashSet<>(Arrays.asList(a, b))); // Transaction 5 : {A, B}

        // Fréquence minimale pour les itemsets fréquents
        float minFrequency = 0.4f;

        // Création de l'algorithme Apriori avec la base de données
        Apriori apriori = new Apriori(db);

        // Extraction des itemsets fréquents
        Set<Itemset> frequentItemsets = apriori.extract(minFrequency);

        // Affichage des itemsets fréquents et de leurs fréquences
        System.out.println("Itemsets fréquents : ");
        for (Itemset itemset : frequentItemsets) {
            System.out.println(itemset.getItems() + " - Fréquence : " + itemset.getFrequency());
        }

        float minConfidence = 0.7f;
        System.out.println("Règles d'association fréquentes et précises : ");
        BruteForceAssociationRuleMiner miner = new BruteForceAssociationRuleMiner(db);
        Set<AssociationRule> rules = miner.extract(minFrequency, minConfidence);
        for (AssociationRule rule : rules) {
            System.out.println(rule);
        }
    }
}