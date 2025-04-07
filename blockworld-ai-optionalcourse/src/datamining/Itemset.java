package datamining;

import java.util.*;
import modelling.BooleanVariable;

public class Itemset {
    private Set<BooleanVariable> items;
    private float frequency;

    public Itemset(Set<BooleanVariable> items, float frequency) {
        this.items = items;
        this.frequency = frequency;
    }

    // Accesseur pour obtenir l'ensemble des items
    public Set<BooleanVariable> getItems() {
        return this.items;
    }

    // Accesseur pour obtenir la fréquence
    public float getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "Itemset : " + items + " (Fréquence : " + frequency + ")";
    }
}
