package view;

import java.util.List;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.grid.Grid;
import model.proxy.InventoryProxy;
import model.player.Player;

public class View extends JFrame {
    private JPanel mainPanel;
    private JPanel gridPanel;
    private JPanel inventoryBar;
    private JLabel[][] cellLabels;
    private ImageIcon playerIcon;  // Store the player image
    private ImageIcon mineIcon;
    private ImageIcon playerBombIcon;
    private ImageIcon gunIcon;
    private ImageIcon bombIcon;
    private ImageIcon unamorcedBombIcon;
    private ImageIcon unamorcedMineIcon;
    private ImageIcon healIcon;
    private ImageIcon wallIcon;
    private JPanel healthBarPanel;

    public View(Grid grid) {
        setTitle("Jeu de Stratégie - Fenetre principale");
        setSize(700, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createImage();
        this.mainPanel = new JPanel(new BorderLayout());
        createInventory();
        createGrid(grid);
        createHealtBar();
        createCommandsPanel();
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Création du panneau de la grille
    public void createGrid(Grid grid){
        this.gridPanel = new JPanel(new GridLayout(grid.getHeight(), grid.getWidth()));
        this.gridPanel.setPreferredSize(new Dimension(500, 500)); // Fixed size for grid

        // Initialisation des labels pour chaque cellule
        this.cellLabels = new JLabel[grid.getHeight()][grid.getWidth()];
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                JLabel cellLabel = new JLabel();
                cellLabel.setOpaque(true);
                cellLabel.setBackground(Color.WHITE);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellLabel.setHorizontalAlignment(JLabel.CENTER); // Centre l'image dans la cellule
                cellLabel.setVerticalAlignment(JLabel.CENTER);
                cellLabels[i][j] = cellLabel;
                gridPanel.add(cellLabel);
            }
        }

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(gridPanel); // Ajout de la grille au centre
        mainPanel.add(centerWrapper, BorderLayout.CENTER); // Ajout du centre (grille)
    }

    //méthode pour creéer les bars d'énergie.
    public void createHealtBar() {
        // Panneau principal pour contenir barres de vie et inventaire
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Organisation verticale
        leftPanel.setPreferredSize(new Dimension(200, 500)); // Largeur fixe

        // Panneau des barres de vie
        this.healthBarPanel = new JPanel();
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.Y_AXIS)); // Barres empilées verticalement
        healthBarPanel.setBorder(BorderFactory.createTitledBorder("Health Bars"));

        // Ajout des barres de vie au panneau principal
        leftPanel.add(healthBarPanel);

        // Ajout de l'inventaire en dessous
        createInventory(); // Crée l'inventaire directement ici
        leftPanel.add(inventoryBar); // Ajout de l'inventaire au panneau principal

        // Ajout du panneau principal à la gauche (WEST)
        mainPanel.add(leftPanel, BorderLayout.WEST);
    }

    public ImageIcon getImage(File file) throws IOException {
        int cellSize = 50;
        Image image = ImageIO.read(file);
        Image scaledImage = image.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void createImage(){
        // Chargement image du joueur
        try {
            playerIcon = getImage(new File("src/view/assets/player.png"));

            mineIcon = getImage(new File("src/view/assets/mine.png"));

            gunIcon = getImage(new File("src/view/assets/Gun.png"));

            unamorcedBombIcon = getImage(new File("src/view/assets/unamorcedBomb.png"));

            unamorcedMineIcon = getImage(new File("src/view/assets/unamorcedMine.png"));

            bombIcon = getImage(new File("src/view/assets/bomb.png"));
            wallIcon = getImage(new File("src/view/assets/Wall.png"));
            healIcon = getImage(new File("src/view/assets/Heal.png"));

        } catch (IOException e) {
            System.err.println("Error loading player image: " + e.getMessage());
            playerIcon = null;
        }
    }

    //Méthode pour créer l'inventaire
    public void createInventory() {
        this.inventoryBar = new JPanel();
        this.inventoryBar.setLayout(new BoxLayout(inventoryBar, BoxLayout.Y_AXIS)); // Inventaires empilés verticalement
        this.inventoryBar.setBorder(BorderFactory.createTitledBorder("Inventaires"));
        this.inventoryBar.setPreferredSize(new Dimension(200, 300)); // Taille du panneau
    }


    // Méthode pour mettre à jour l'affichage d'une cellule
    public void updateCellDisplay(int x, int y, boolean isPlayer, boolean hasMine, boolean hasBomb, boolean hasGun, boolean hasUnamorcedMine, boolean hasUnamorcedBomb, boolean hasHeal, boolean hasWall) {
        if (isPlayer) {
            if (playerIcon != null) {
                cellLabels[x][y].setIcon(playerIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.BLUE);
            }
        } else if (hasMine) {
            if (mineIcon != null) {
                cellLabels[x][y].setIcon(mineIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.GREEN);
            }
        }
        else if (hasBomb) {
            if (bombIcon != null) {
                cellLabels[x][y].setIcon(bombIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.MAGENTA);
            }
        }else if (hasUnamorcedBomb) {
            if (unamorcedBombIcon != null) {
                cellLabels[x][y].setIcon(unamorcedBombIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.ORANGE);
            } 
        }
        else if (hasUnamorcedMine) {
            if (unamorcedMineIcon != null) {
                cellLabels[x][y].setIcon(unamorcedMineIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.LIGHT_GRAY);
            } 
        }else if (hasGun) {
            if (gunIcon != null) {
                cellLabels[x][y].setIcon(gunIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.YELLOW);
            } 
        }else if (hasHeal) {
            if (healIcon != null) {
                cellLabels[x][y].setIcon(healIcon);
            } else {
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.PINK);
            } 
        }else if (hasWall) {
            if (wallIcon != null) {
                cellLabels[x][y].setIcon(wallIcon);
            } else {
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.BLACK);
            } 
        }
        else {
            cellLabels[x][y].setIcon(null);
            cellLabels[x][y].setBackground(Color.WHITE);
        }
        cellLabels[x][y].repaint();
    }

    // Afficher un message de victoire
    public void showVictoryMessage(String winnerName) {
        JOptionPane.showMessageDialog(this, winnerName + " a gagné ! Félicitations !", "Victoire", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Ferme l'application après l'annonce
    }

    // Met à jour l'affichage de l'inventaire du joueur
    public void updateAllInventoriesDisplay(List<Player> players) {
        inventoryBar.removeAll(); // Effacer l'inventaire actuel

        for (Player player : players) {
            // Créer un sous-panneau pour chaque joueur
            JPanel playerInventoryPanel = new JPanel();
            playerInventoryPanel.setLayout(new BorderLayout());
            playerInventoryPanel.setBorder(BorderFactory.createTitledBorder(player.getName())); // Nom du joueur

            // Ajouter l'affichage de l'inventaire
            InventoryProxy inventoryProxy = player.getInventoryProxy();
            String inventoryDisplay = inventoryProxy.getInventoryDisplay();
            JTextArea inventoryArea = new JTextArea(inventoryDisplay);
            inventoryArea.setEditable(false);
            inventoryArea.setLineWrap(true);
            inventoryArea.setWrapStyleWord(true);

            // Ajout du texte dans une zone déroulante
            playerInventoryPanel.add(new JScrollPane(inventoryArea), BorderLayout.CENTER);

            // Ajouter le sous-panneau au panneau principal d'inventaire
            inventoryBar.add(playerInventoryPanel);
        }

        inventoryBar.revalidate();
        inventoryBar.repaint();
    }


    // Mise à jour des barres de vie pour chaque joueur
    public void updateHealthBars(List<Player> players) {
        healthBarPanel.removeAll(); 

        // Crée une barre de vie pour chaque joueur
        for (Player player : players) {
            // Créer un label pour le nom du joueur
            JLabel playerNameLabel = new JLabel(player.getName());
            playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Créer une barre de vie pour le joueur
            JProgressBar healthBar = new JProgressBar(0, player.getMaxEnergy());
            healthBar.setValue(player.getEnergy());
            healthBar.setString(player.getEnergy() + "/" + player.getMaxEnergy());
            healthBar.setStringPainted(true);
            if ( (float) (player.getMaxEnergy() / player.getEnergy()) > 1){
                healthBar.setForeground(Color.RED);
            } else {
                healthBar.setForeground(Color.GREEN);
            }
            healthBar.setPreferredSize(new Dimension(150, 30)); // Définir la taille de la barre de vie

            // Ajouter le nom du joueur et la barre de vie au panneau
            healthBarPanel.add(playerNameLabel);
            healthBarPanel.add(healthBar);

            // Ajouter un espacement entre les éléments
            healthBarPanel.add(Box.createVerticalStrut(15));
        }

        healthBarPanel.revalidate();
        healthBarPanel.repaint();
    }

    public void createCommandsPanel() {
        JPanel commandsPanel = new JPanel();
        commandsPanel.setLayout(new BorderLayout());
        String commandsText = "Commandes :\n"
                + "1. Déplacer le joueur : Utilisez les touches fléchées.\n"
                + "2. Déposer une bombe : Appuyez sur 'B'.\n"
                + "3. Activer une mine : Appuyez sur 'M'.\n"
                + "4. Utiliser une arme : Appuyez sur 'G'.\n";
        JTextArea commandsArea = new JTextArea(commandsText);
        commandsArea.setEditable(false);
        commandsArea.setBackground(UIManager.getColor("Panel.background"));
        commandsArea.setLineWrap(true);
        commandsArea.setWrapStyleWord(true);
        commandsPanel.add(new JScrollPane(commandsArea), BorderLayout.CENTER);
        mainPanel.add(commandsPanel, BorderLayout.SOUTH);
    }



}
