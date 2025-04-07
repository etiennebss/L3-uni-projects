package blocksworld;

import java.util.*;

import datamining.Apriori;
import datamining.AssociationRule;
import datamining.BooleanDatabase;
import datamining.BruteForceAssociationRuleMiner;
import datamining.Itemset;

public class BWDataExecutable {
    public static void main(String[] args) {

        // Initialisation de l'instance BWDatamining avec 5 blocs et 3 piles
        BWDatamining dataMiner = new BWDatamining(6, 3);
        System.out.println("****** Analyse de données pour 6 blocs et 3 piles avec fréquence min 3/5 et confiance 95% ****");

        // Génération de la base de données pour le monde des blocs
        BooleanDatabase db = dataMiner.generateDatabase(5000);

        System.out.println("**** Items fréquents ****");
        // Initialisation de l'algorithme Apriori pour extraire les itemsets fréquents
        Apriori apriori = new Apriori(db);
        Set<Itemset> frequentItemsets = apriori.extract(3f / 5);

        // Affichage des itemsets fréquents
        for (Itemset itemset : frequentItemsets) {
            System.out.println(itemset);
        }
        
        System.out.println("\n**** Règles d'association ****");
        // Initialisation du mineur de règles d'association brute force
        BruteForceAssociationRuleMiner bfRuleMiner = new BruteForceAssociationRuleMiner(db);
        Set<AssociationRule> associationRules = bfRuleMiner.extract(3f / 5, 0.95f);

        // Affichage des règles d'association extraites
        for (AssociationRule rule : associationRules) {
            System.out.println(rule);
        }
    }
}
