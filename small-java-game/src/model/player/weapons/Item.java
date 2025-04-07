package model.player.weapons;

public interface Item{
    public int getDamage();
    public int getRange();
    public int getPosX();
    public int getPosY();
    public boolean getCanBeCatch();
    public String getName();
    public String toString();
}