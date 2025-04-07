
package model.proxy;

import model.player.Player;

import model.player.weapons.Bomb;

public class BombProxy extends Bomb{
    private Bomb bomb;
    private Player owner;
    private int timer;

    public BombProxy(Bomb bomb, Player owner) {
        super(bomb.getDamage(), bomb.getRange(), bomb.getPosX(), bomb.getPosY(), bomb.getCanBeCatch());
        this.bomb = bomb;
        this.owner = owner;
        this.timer = 7;
    }

    public boolean isVisibleTo(Player player){
        if (this.owner.equals(player)){
            return true;
        }
        return false;
    }

    public boolean canExploseTo(Player player){
        if (this.owner.equals(player)){
            return false;
        }
        return true;
    }

    public int getTimer() {
        return timer;
    }

    public void tick() {
        if (timer > 0) {
            timer--;
        }
    }

    public boolean isExploded() {
        return timer <= 0;
    }

    public Bomb getBomb(){
        return this.bomb;
    }

    public Player getOwner(){
        return this.owner;
    }
}