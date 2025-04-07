package model.player.weapons;

public class Gun extends Objet{
    public Gun(int damage, int range, int posX, int posY, boolean canBeCatch) {
        super("Gun", damage, range, posX, posY, canBeCatch);
    }
}
