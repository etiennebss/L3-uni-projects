package model.grid;

import model.player.weapons.*;
import model.proxy.*;

public class Cell {
    private int posX;
    private int posY;
    private boolean isOccupied;
    private Object object;
    private Objet objet;

    public Cell(int posX, int posY, Object object) {
        this.posX = posX;
        this.posY = posY;
        setObject(object); // This will handle both object and isOccupied
        this.objet = null;
    }

    public Cell(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        setObject(null); // This will handle both object and isOccupied
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
        this.isOccupied = (object != null); // Update isOccupied based on object
    }


    public void setObjet(Objet objet){
        this.objet = objet;
    }

    public Objet getObjet(){
        return this.objet;
    }

    public boolean hasMine(){
        if (this.objet != null && this.objet instanceof Mine){
            return true;
        }
        return false;
    }

    public boolean hasWall(){
        if (this.objet != null && this.objet instanceof Wall){
            return true;
        }
        return false;
    }

    public boolean hasHeal(){
        if (this.objet != null && this.objet instanceof Heal){
            return true;
        }
        return false;
    }


    public boolean hasAmorcedMine(){
        if (this.objet != null && this.objet instanceof MineProxy){
            return true;
        }
        return false;
    }

    public boolean hasBomb(){
        if (this.objet != null && this.objet instanceof Bomb){
            return true;
        }
        return false;
    }

    public boolean hasAmorcedBomb(){
        if (this.objet != null && this.objet instanceof BombProxy){
            return true;
        }
        return false;
    }

    public boolean hasGun(){
        if (this.objet != null && this.objet instanceof Gun){
            return true;
        }
        return false;
    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
        if (!occupied) {
            this.object = null; // Clear object when cell is unoccupied
        }
    }
}