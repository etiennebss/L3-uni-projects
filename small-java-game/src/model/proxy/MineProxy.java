
package model.proxy;

import model.player.Player;

import model.player.weapons.Mine;

public class MineProxy extends Mine{
    private Mine mine;
    private Player owner;

    public MineProxy(Mine mine, Player owner) {
        super(mine.getDamage(), mine.getRange(), mine.getPosX(), mine.getPosY(), mine.getCanBeCatch());
        this.mine = mine;
        this.owner = owner;
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

    public Player getOwner(){
        return this.owner;
    }
}