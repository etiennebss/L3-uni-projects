package model.player;

import model.grid.Cell;
import model.grid.Grid;
import model.player.weapons.*;
import model.proxy.*;
import model.proxy.InventoryProxy;

public class Player {
    private String name;
    private int energy;
    private boolean shield;
    private int posX;
    private int posY;
    private Inventory inventory;
    private int maxEnergy;

    public Player(String name, int posX, int posY) {
        this.name = name;
        this.energy = 100;
        this.maxEnergy = 100;
        this.shield = false;
        this.posX = posX;
        this.posY = posY;
        this.inventory = new Inventory();
    }

    public Player(String name) {
        this(name, 0, 0);
    }

    @Override
    public String toString() {
        return "Player: " + name + " Energy: " + energy + " Shield: " + shield + " Position: (" + posX + ", " + posY + ")";
    }

    public void move(int dx, int dy, Grid grid) {
        int newX = posX + dx;
        int newY = posY + dy;

        if (newX >= 0 && newX < grid.getHeight() && newY >= 0 && newY < grid.getWidth()) {
            Cell currentCell = grid.getCell(posX, posY);
            Cell targetCell = grid.getCell(newX, newY);

            if (!targetCell.isOccupied()) {
                // Mettre à jour les cellules
                currentCell.setOccupied(false);
                targetCell.setOccupied(true);

                // Mettre à jour la position du joueur
                setPosX(newX);
                setPosY(newY);
            }
        }
    }

    public void placeMine(Grid grid){
            Cell currentCell = grid.getCell(posX, posY);
            currentCell.setObjet(new MineProxy(new Mine(50, 1, this.posX, this.posY, false), this));
    }

    public void placeBomb(Grid grid){
            Cell currentCell = grid.getCell(posX, posY);
            currentCell.setObjet(new BombProxy(new Bomb(50, 1, this.posX, this.posY, false), this));
    }


    public void isAttacked(int damage) {
        if (shield) {
            shield = false;
        } else {
            energy -= damage;
        }
    }


    public String getName() {
        return this.name;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean hasShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Inventory getInventory() {
        return inventory;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public InventoryProxy getInventoryProxy() {
        return new InventoryProxy(this.inventory);
    }

    public int getMaxEnergy(){
        return this.maxEnergy;
    }

}




