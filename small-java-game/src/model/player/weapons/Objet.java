package model.player.weapons;

import model.player.Player;

public class Objet implements Item{
    private int damage;
    private int range;
    private int posX;
    private int posY;
    private boolean canBeCatch;
    private String name;

    public Objet(String name, int damage, int range, int posX, int posY, boolean canBeCatch) {
        this.damage = damage;
        this.range = range;
        this.posX = posX;
        this.posY = posY;
        this.canBeCatch = canBeCatch;
        this.name = name;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public int getRange() {
        return this.range;
    }

    @Override
    public int getPosX() {
        return this.posX;
    }

    @Override
    public int getPosY() {
        return this.posY;
    }

    @Override
    public boolean getCanBeCatch(){
        return this.canBeCatch;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.getName();
    }

    public Player getOwner(){
        return new Player("joueur test", 0, 0);
    }
}
