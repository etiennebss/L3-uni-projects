package model.player.weapons;

public class Bomb extends Objet {
    public Bomb(int damage, int range, int posX, int posY, boolean canBeCatch) {
        super("Bomb", damage, range, posX, posY, canBeCatch);
    }
}
