import controller.Controller;
import java.util.*;
import model.player.Player;
import model.player.PlayerFactory;
import view.PlayerSelectionView;
import view.View;
import view.ViewPlayer;
import model.grid.Grid;
import model.proxy.*;
import model.player.weapons.*;

public class Game {
    public static void main(String[] args) {
        int gridHeight = 10;
        int gridWidth = 10;
        Random rand = new Random();
        PlayerSelectionView selectionView = new PlayerSelectionView();


        selectionView.setOnGameStart(() -> {
            List<String[]> selectedPlayers = selectionView.getSelectedPlayers();
            Grid grid = new Grid(gridHeight, gridWidth);
            List<Player> players = new ArrayList<>();
            List<Objet> objets = new ArrayList<>();  // Liste pour les bombes et les armes
            List<int[]> occupiedPositions = new ArrayList<>();  // Liste pour les positions occupées par les joueurs

            int i = 0;
            // Création des joueurs
            for (String[] playerData : selectedPlayers) {
                String name = playerData[0];
                String type = playerData[1];
                int posX = i % gridHeight;
                int posY = i / gridHeight;
                Player player = PlayerFactory.createPlayer(type, name, posX, posY);
                players.add(player);
                occupiedPositions.add(new int[]{posX, posY}); // Ajouter la position du joueur aux positions occupées
                i++;
            }

            // Nombre de bombes et d'armes à générer
            int nbOfBombs = rand.nextInt(3);  // Génère entre 0 et 2 bombes
            int nbOfGuns = rand.nextInt(3);   // Génère entre 0 et 2 armes
            int nbOfMines = rand.nextInt(3); 

            // Générer des bombes
            for (int j = 0; j < nbOfBombs; j++) {
                int posX, posY;
                do {
                    posX = rand.nextInt(gridWidth);  // Position aléatoire en X
                    posY = rand.nextInt(gridHeight); // Position aléatoire en Y
                } while (grid.isPositionOccupied(occupiedPositions, posX, posY)); // Vérifie si la position est déjà occupée par un joueur

                Bomb bomb = new Bomb(50, 1, posX,posY, false);
                objets.add(bomb);
                occupiedPositions.add(new int[]{posX, posY}); // Ajouter la position de la bombe aux positions occupées
            }

            for (int j = 0; j < nbOfMines; j++) {
                int posX, posY;
                do {
                    posX = rand.nextInt(gridWidth);  // Position aléatoire en X
                    posY = rand.nextInt(gridHeight); // Position aléatoire en Y
                } while (grid.isPositionOccupied(occupiedPositions, posX, posY)); // Vérifie si la position est déjà occupée par un joueur

                Mine mine = new Mine(50, 1, posX,posY, true);
                objets.add(mine);
                occupiedPositions.add(new int[]{posX, posY}); // Ajouter la position de la bombe aux positions occupées
            }

            // Générer des armes
            for (int j = 0; j < nbOfGuns; j++) {
                int posX, posY;
                do {
                    posX = rand.nextInt(gridWidth);  // Position aléatoire en X
                    posY = rand.nextInt(gridHeight); // Position aléatoire en Y
                } while (grid.isPositionOccupied(occupiedPositions, posX, posY)); // Vérifie si la position est déjà occupée par un joueur

                Gun gun = new Gun(25, 1, posX,posY, true);
                objets.add(gun);
                occupiedPositions.add(new int[]{posX, posY}); // Ajouter la position de l'arme aux positions occupées
            }


            int nbOfWalls = rand.nextInt(5) + 5;  // Génère entre 5 et 9 murs
            int nbOfHeals = rand.nextInt(5) + 3;

            // Générer des murs
            for (int j = 0; j < nbOfWalls; j++) {
                    int posX, posY;
                   do {
                        posX = rand.nextInt(gridWidth);
                        posY = rand.nextInt(gridHeight);
                    } while (grid.isPositionOccupied(occupiedPositions, posX, posY));
    
                    Wall wall = new Wall(posX, posY);
                   objets.add(wall);
                    occupiedPositions.add(new int[]{posX, posY});
                }
                // Générer des objets de soin
                for (int j = 0; j < nbOfHeals; j++) {
                    int posX, posY;
                do {
                    posX = rand.nextInt(gridWidth);
                    posY = rand.nextInt(gridHeight);
                } while (grid.isPositionOccupied(occupiedPositions, posX, posY));

               Heal heal = new Heal( 30,posX, posY);
               objets.add(heal);
               occupiedPositions.add(new int[]{posX, posY});
           }
    
        
            for (Player player : players){
                player.getInventory().add(new MineProxy(new Mine(50, 2, 0, 0, false), players.get(0)), 1);
                player.getInventory().add(new Gun(50, 1, 0, 0, false), 1);
                player.getInventory().add(new BombProxy(new Bomb(50, 1, 0, 0, false), players.get(0)), 1);
            }
            View view = new View(grid);
            List<ViewPlayer> viewPlayers = new ArrayList<>();
            for (Player player : players){
                viewPlayers.add(new ViewPlayer(player, grid));
            }
            Controller controller = new Controller(players, objets, view, grid, viewPlayers);

            System.out.println("Jeu de stratégie lancé. Utilisez les touches pour déplacer les joueurs.");
        });
    }

}
