package datamining;

import java.util.*;

public interface ItemsetMiner {
    BooleanDatabase getDatabase(); // Renvoie la base de données
    Set<Itemset> extract(float minFrequency); // Extrait les itemsets fréquents
}