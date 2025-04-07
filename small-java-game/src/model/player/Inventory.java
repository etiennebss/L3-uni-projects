package model.player;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Class<?>, Integer> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
    }

    public void add(Object item, int quantity) {
        if (inventory.containsKey(item.getClass())) {
            inventory.put(item.getClass(), inventory.get(item.getClass()) + quantity);
        } else {
            inventory.put(item.getClass(), quantity);
        }
    }

    public void remove(Object item, int quantity) {
        if (inventory.containsKey(item.getClass())) {
            inventory.put(item.getClass(), inventory.get(item.getClass()) - quantity);
            if (inventory.get(item.getClass()) <= 0) {
                inventory.remove(item.getClass());
            }
        }
    }

    public int getQuantity(Object item) {
        return inventory.getOrDefault(item.getClass(), 0);
    }

    public void set(Map<Class<?>, Integer> inventory) {
        this.inventory = inventory;
    }

    public String toString() {
        return inventory.toString();
    }

    public Map<Class<?>, Integer> getInventory(){
        return this.inventory;
    }
}
