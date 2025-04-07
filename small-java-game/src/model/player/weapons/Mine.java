package model.player.weapons;

public class Mine extends Objet {
    public Mine(int damage, int range, int posX, int posY, boolean canBeCatch) {
        super("Mine", damage, range, posX, posY, canBeCatch);
    }
}
