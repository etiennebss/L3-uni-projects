package datamining;

import java.util.*;
import modelling.BooleanVariable;

public abstract class AbstractItemsetMiner implements ItemsetMiner {
    protected BooleanDatabase database;

    // Constructeur prenant une base de données en argument
    public AbstractItemsetMiner(BooleanDatabase database) {
        this.database = database;
    }

    // Accesseur pour obtenir la base de données
    @Override
    public BooleanDatabase getDatabase() {
        return this.database;
    }

    // Méthode pour calculer la fréquence d'un ensemble d'items
    public float frequency(Set<BooleanVariable> itemset) {
        int count = 0;
        for (Set<BooleanVariable> transaction : database.getTransactions()) {
            if (transaction.containsAll(itemset)) {
                count++;
            }
        }
        return (float) count / database.getTransactions().size();
    }

    // Attribut statique pour comparer des items par leur nom
    public static final Comparator<BooleanVariable> COMPARATOR =
        (var1, var2) -> var1.getName().compareTo(var2.getName());
}