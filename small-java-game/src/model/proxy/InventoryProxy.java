package model.proxy;

import model.player.Inventory;

import java.util.Map;
import java.util.stream.Collectors;

/*Création du proxy de l'inventaire */
public class InventoryProxy {
    private Inventory inventory;

    public InventoryProxy(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getInventoryDisplay() {
    Map<Class<?>, Integer> items = inventory.getInventory(); // Utilisation explicite de Class<?> comme clé
    if (items == null || items.isEmpty()){
        return "Inventaire vide";
    }
    return items.entrySet().stream()
            .map(entry -> {
                String itemName = entry.getKey().getSimpleName(); // Utilise getSimpleName() pour récupérer le nom de la classe
                int quantity = entry.getValue();
                return itemName + ": " + quantity;
            })
            .collect(Collectors.joining("\n"));
}




}
