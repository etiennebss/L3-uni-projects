package model.player.weapons;

import model.player.Player;
import model.player.weapons.Objet;

public class Heal extends Objet {
    private int healAmount;

    public Heal(int healAmount, int posX, int posY) {
        super("Heal", 0, 1, posX, posY, true);  // Heal does not deal damage
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    // Apply healing to a player
    public void healPlayer(Player player) {
        player.setEnergy(player.getEnergy() + healAmount);
    }
}
