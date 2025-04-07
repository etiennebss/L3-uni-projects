package controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import model.grid.*;
import model.player.Player;
import model.player.weapons.*;
import model.proxy.*;
import view.*;

public class Controller {
    private List<Player> players;
    private View view;
    private Grid grid;
    private int currentPlayerIndex;
    private List<ViewPlayer> viewPlayers;

    public Controller(List<Player> players, List<Objet> objets, View view, Grid grid, List<ViewPlayer> viewPlayers) {
        this.players = players;
        this.view = view;
        this.grid = grid;
        this.currentPlayerIndex = 0;
        this.viewPlayers = viewPlayers;
        
        // Initialise la grille avec les joueurs
        for (Player player : players) {
            grid.getCell(player.getPosX(), player.getPosY()).setObject(player);
            this.updateCellDisplay(player.getPosX(), player.getPosY(), true, false, false, false, false, false, false, false);
        }

         for (Objet objet : objets) {
            Cell cell = grid.getCell(objet.getPosX(), objet.getPosY());
            cell.setObjet(objet);
            if(objet instanceof Gun){
                this.updateCellDisplay(objet.getPosX(), objet.getPosY(), false, false, false, true, false, false, false, false);
            }
            else if (objet instanceof Bomb){
                this.updateCellDisplay(objet.getPosX(), objet.getPosY(), false, false, false, false, false, true, false, false);
            }
            else if (objet instanceof Mine){
                this.updateCellDisplay(objet.getPosX(), objet.getPosY(), false, false, false, false, true, false, false, false);
            }
            else if (objet instanceof Wall){
                this.updateCellDisplay(objet.getPosX(), objet.getPosY(), false, false, false, false, false, false, false, true);
            }
            else if (objet instanceof Heal){
                this.updateCellDisplay(objet.getPosX(), objet.getPosY(), false, false, false, false, false, false, true, false);
            }
        }

        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        this.updateAll();
        view.setFocusable(true);

    }

    private void handleKeyPress(KeyEvent e) {
        Player currentPlayer = players.get(currentPlayerIndex);
        int dx = 0, dy = 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: case KeyEvent.VK_Z: dx = -1; break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S: dx = 1; break;
            case KeyEvent.VK_LEFT: case KeyEvent.VK_Q: dy = -1; break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: dy = 1; break;
            case KeyEvent.VK_M: handleMinePlacement(currentPlayer); break;
            case KeyEvent.VK_B: handleBombPlacement(currentPlayer); break;
            case KeyEvent.VK_G: handleGunUsing(currentPlayer); break;
        }

        if (dx != 0 || dy != 0) {
            movePlayer(currentPlayer, dx, dy);
            nextPlayer();
        }
        
        this.updateAll();
    }

    private void handleMinePlacement(Player player){
        MineProxy test = new MineProxy(new Mine(50, 1, 0, 0, false), player);
        if(player.getInventory().getQuantity(test) > 0){
            int posX = player.getPosX();
            int posY = player.getPosY();
            player.placeMine(grid);
            player.getInventory().remove(test,1);
            this.updateCellDisplay(posX, posY, true, true, false, false, false, false, false, false);
        }
        this.updateAll();
    }

    private void handleBombPlacement(Player player){
        BombProxy test = new BombProxy(new Bomb(50, 1, 0, 0, false), null);
        if(player.getInventory().getQuantity(test) > 0){
            int posX = player.getPosX();
            int posY = player.getPosY();
            player.placeBomb(grid);
            player.getInventory().remove(test,1);
            this.updateCellDisplay(posX, posY, true, false, true, false, false, false, false, false);
        }
        this.updateAll();
    }

    private void handleGunUsing(Player player){
        Gun test = new Gun(50, 1, 0, 0, false);
        if(player.getInventory().getQuantity(test) > 0){
            Player otherPlayer;
            if (currentPlayerIndex < players.size() - 1) {
                otherPlayer = players.get(currentPlayerIndex + 1);

            } else {
                otherPlayer = players.get(0);
            }
            player.getInventory().remove(test, 1);
            otherPlayer.isAttacked(25);
            if (otherPlayer.getEnergy() <= 0) {
                    players.remove(otherPlayer);
                    grid.getCell(otherPlayer.getPosX(), otherPlayer.getPosY()).setObject(null);
                    this.updateCellDisplay(otherPlayer.getPosX(), otherPlayer.getPosY(), false, false, false, false, false, false, false, false);
                    checkVictory();
                    if (currentPlayerIndex >= players.size()) {
                        currentPlayerIndex = 0;
                    }
            }
            this.updateAll();
        }
    }

    private void movePlayer(Player player, int dx, int dy) {
        int oldX = player.getPosX();
        int oldY = player.getPosY();
        int newX = oldX + dx;
        int newY = oldY + dy;

        // Check bounds
        if (newX >= 0 && newX < grid.getHeight() && newY >= 0 && newY < grid.getWidth()) {
            if(grid.getCell(newX, newY).hasWall()) {
                // On ne peut pas bouger
                return;
            }
            if (grid.getCell(newX, newY).hasHeal()) {
                Heal heal = (Heal) grid.getCell(newX, newY).getObjet();
                heal.healPlayer(player); // Soigne le joueur
                player.setPosX(newX);
                player.setPosY(newY);
                grid.getCell(oldX, oldY).setObject(null); 
                grid.getCell(newX, newY).setObject(player); // Met à jour la position du joueur
                grid.getCell(newX, newY).setObjet(null); // Enlève l'objet heal de la case
                this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                System.out.println("Zone de soin : +" + heal.getHealAmount() + " PV !");
            }
    

            // Attack case
            if (grid.getCell(newX, newY).getObject() instanceof Player && grid.getCell(newX, newY).getObject() != player) {
                Object targetObject = grid.getCell(newX, newY).getObject();
                Player otherPlayer = (Player) targetObject;
                otherPlayer.isAttacked(20); // Attaque au corps à corps
                System.out.println(player.getName() + " attaque càc " + otherPlayer.getName());

                if (otherPlayer.getEnergy() <= 0) {
                    // Enlève le joueur mort de la liste des joueurs
                    players.remove(otherPlayer);
                    // Clear la cellule du joueur mort
                    grid.getCell(newX, newY).setObject(null);
                    this.updateCellDisplay(newX, newY, false, false, false, false, false, false, false, false);
                    checkVictory();
                    // Ajuste l'index du joueur à jouer
                    if (currentPlayerIndex >= players.size()) {
                        currentPlayerIndex = 0;
                    }
                }
            }
            else if(grid.getCell(newX, newY).hasAmorcedMine()){
                MineProxy mine = (MineProxy)grid.getCell(newX, newY).getObjet();
                if(mine.canExploseTo(player)){
                   player.isAttacked(mine.getDamage());
                   if (player.getEnergy() <= 0) {
                        // Enlève le joueur mort de la liste des joueurs
                        players.remove(player);
                        // Clear la cellule du joueur mort
                        grid.getCell(oldX, oldY).setObject(null);
                        grid.getCell(newX, newY).setObjet(null);
                        this.updateCellDisplay(newX, newY, false, false, false, false, false, false, false, false);
                        this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                        checkVictory();
                        // Ajuste l'index du joueur à jouer
                        if (currentPlayerIndex >= players.size()) {
                            currentPlayerIndex = 0;
                        }
                    }
                    else{
                        player.setPosX(newX);
                        player.setPosY(newY);
                        // Set new cell
                        grid.getCell(newX, newY).setObject(player);
                        grid.getCell(newX, newY).setObjet(null);
                        this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                        this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                    }
                }
            }
            else if(grid.getCell(newX, newY).hasAmorcedBomb()){
                BombProxy bomb = (BombProxy) grid.getCell(newX, newY).getObjet();
                if(bomb.canExploseTo(player)){
                   player.isAttacked(bomb.getDamage());
                   if (player.getEnergy() <= 0) {
                        // Enlève le joueur mort de la liste des joueurs
                        players.remove(player);
                        // Clear la cellule du joueur mort
                        grid.getCell(oldX, oldY).setObject(null);
                        grid.getCell(newX, newY).setObjet(null);
                        this.updateCellDisplay(newX, newY, false, false, false, false, false, false, false, false);
                        this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                        checkVictory();
                        // Ajuste l'index du joueur à jouer
                        if (currentPlayerIndex >= players.size()) {
                            currentPlayerIndex = 0;
                        }
                    }
                    else{
                        player.setPosX(newX);
                        player.setPosY(newY);
                        // Set new cell
                        grid.getCell(newX, newY).setObject(player);
                        grid.getCell(newX, newY).setObjet(null);
                        this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                        this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                    }
                }
            }
            else if(grid.getCell(newX, newY).hasGun()){
                Gun test = new Gun(25, 1, 0, 0, false);
                player.getInventory().add(test, 1);
                player.setPosX(newX);
                player.setPosY(newY);
                grid.getCell(newX, newY).setObjet(null);
                this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                grid.getCell(oldX, oldY).setObject(null);
                grid.getCell(newX, newY).setObject(player);
                
            }
            else if(grid.getCell(newX, newY).hasBomb()){
                Bomb test = new Bomb(25, 1, 0, 0, false);
                player.getInventory().add(new BombProxy(test, player), 1);
                player.setPosX(newX);
                player.setPosY(newY);
                grid.getCell(newX, newY).setObjet(null);
                this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                grid.getCell(oldX, oldY).setObject(null);
                grid.getCell(newX, newY).setObject(player);
                
            }
            else if(grid.getCell(newX, newY).hasMine()){
                Mine test = new Mine(25, 1, 0, 0, false);
                player.getInventory().add(new MineProxy(test, player), 1);
                player.setPosX(newX);
                player.setPosY(newY);
                grid.getCell(newX, newY).setObjet(null);
                this.updateCellDisplay(oldX, oldY, false, false, false, false, false, false, false, false);
                this.updateCellDisplay(newX, newY, true, false, false, false, false, false, false, false);
                grid.getCell(oldX, oldY).setObject(null);
                grid.getCell(newX, newY).setObject(player);
                
            }
            // Movement case
            else if (!grid.getCell(newX, newY).isOccupied()) {
                // Clear old cell
                grid.getCell(oldX, oldY).setObject(null);
                this.updateCellDisplay(oldX, oldY, false, grid.getCell(oldX, oldY).hasMine(), grid.getCell(oldX, oldY).hasAmorcedBomb(), false, false, false, false, false);

                // Update player position
                player.setPosX(newX);
                player.setPosY(newY);

                // Set new cell
                grid.getCell(newX, newY).setObject(player);
                this.updateCellDisplay(newX, newY, true, grid.getCell(newX, newY).hasMine(), grid.getCell(oldX, oldY).hasAmorcedBomb(), false, false, false, false, false);
            }
            checkBombs();
            this.updateAll();
        }
    }

    private void nextPlayer() {
        if (!players.isEmpty()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            this.updateAll();
        }
    }

    private void checkVictory() {
        if (players.size() == 1) { // Condition : un seul joueur restant
            view.updateHealthBars(this.players);
            Player winner = players.get(0);
            view.showVictoryMessage(winner.getName());
        }
    }

    private void checkBombs() {
        for (int x = 0; x < grid.getHeight(); x++) {
            for (int y = 0; y < grid.getWidth(); y++) {
                Cell cell = grid.getCell(x, y);
                if (cell.hasAmorcedBomb()) {
                    BombProxy bombProxy = (BombProxy) cell.getObjet();
                    bombProxy.tick();
                    if (bombProxy.isExploded()) {
                        explodeBomb(x, y, bombProxy.getBomb());
                        cell.setObjet(null);
                        this.updateCellDisplay(x, y, false, false, false, false, false, false, false, false);
                    }
                }
            }
        }
    }

    private void explodeBomb(int x, int y, Bomb bomb) {
        int damage = bomb.getDamage();

        // Vérifier les cellules dans un rayon de 2 cases autour de (x, y)
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int targetX = x + i;
                int targetY = y + j;

                if (targetX >= 0 && targetX < grid.getHeight() && targetY >= 0 && targetY < grid.getWidth()) {
                    Cell targetCell = grid.getCell(targetX, targetY);
                    if (targetCell.getObject() instanceof Player) {
                        Player targetPlayer = (Player) targetCell.getObject();
                        targetPlayer.isAttacked(damage);
                        if (targetPlayer.getEnergy() <= 0) {
                            players.remove(targetPlayer);
                            grid.getCell(targetX, targetY).setObject(null);
                            this.updateCellDisplay(targetX, targetY, false, false, false, false, false, false, false, false);
                        }
                    }
                }
            }
        }
        this.updateAll();
    }

   public void updateAll(){
        view.updateAllInventoriesDisplay(this.players);
        view.updateHealthBars(this.players);
        for (ViewPlayer viewPlayer : viewPlayers){
            viewPlayer.updateAllInventoriesDisplay();
            viewPlayer.updateHealthBars();
        }
        this.checkVictory();
    }

    public void updateCellDisplay(int x, int y, boolean isPlayer, boolean hasMine, boolean hasBomb, boolean hasGun, boolean hasUnamorcedMine, boolean hasUnamorcedBomb, boolean hasHeal, boolean  hasWall) {
        view.updateCellDisplay(x, y, isPlayer, hasMine, hasBomb, hasGun, hasUnamorcedMine, hasUnamorcedBomb, hasHeal, hasWall);
        for (ViewPlayer viewPlayer : viewPlayers){
            viewPlayer.updateCellDisplay(x, y, isPlayer, hasMine, hasBomb, hasGun, hasUnamorcedMine, hasUnamorcedBomb,hasHeal, hasWall);
        }
    }

}
