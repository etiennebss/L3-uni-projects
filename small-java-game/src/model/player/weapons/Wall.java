package model.player.weapons;

import model.player.Player;
import model.player.weapons.Objet;

public class Wall extends Objet {
    public Wall(int posX, int posY) {
        super("Wall",0,1,posX, posY, false);
    }
}
