package datamining;

import java.util.*;
import modelling.BooleanVariable;

public class BooleanDatabase {
    private Set<BooleanVariable> items;
    private List<Set<BooleanVariable>> transactions;

    // Constructeur prenant l'ensemble des items
    public BooleanDatabase(Set<BooleanVariable> items) {
        this.items = items;
        this.transactions = new ArrayList<>();
    }
    // Méthode pour ajouter une transaction
    public void add(Set<BooleanVariable> transaction) {
        this.transactions.add(transaction);
    }

    // Accesseur pour obtenir les items
    public Set<BooleanVariable> getItems() {
        return this.items;
    }

    // Accesseur pour obtenir les transactions
    public List<Set<BooleanVariable>> getTransactions() {
        return this.transactions;
    }
}

