package model.grid;

import java.util.List;
import model.player.Player;

public class Grid {
    
    private int h;
    private int w;
    private Cell[][] grid;

    public Grid(int h, int w) {
        this.h = h;
        this.w = w;

        this.grid = new Cell[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Cell cell = new Cell(i, j);
                grid[i][j] = cell;
            }
        }
        
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public int getHeight() {
        return h;
    }

    public int getWidth() {
        return w;
    }

    public void addPlayer(Player player, int x, int y) {
        if (!grid[x][y].isOccupied()) {
            grid[x][y].setOccupied(true);
            grid[x][y].setObject(player);
        }
    }

    public boolean isPositionOccupied(List<int[]> occupiedPositions, int posX, int posY) {
            for (int[] position : occupiedPositions) {
                if (position[0] == posX && position[1] == posY) {
                    return true; // La position est occupée
                }
            }
            return false; // La position est libre
    }
}
