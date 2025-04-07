package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.grid.Grid;
import model.proxy.InventoryProxy;
import model.player.Player;

public class ViewPlayer extends JFrame {
    private Player player;
    private Grid grid;
    private JPanel mainPanel;
    private JPanel gridPanel;
    private JLabel[][] cellLabels;
    private ImageIcon playerIcon;  // Store the player image
    private ImageIcon mineIcon;
    private ImageIcon playerBombIcon;
    private ImageIcon gunIcon;
    private ImageIcon bombIcon;
    private JPanel inventoryBar;
    private JPanel healthBarPanel;
    private ImageIcon unamorcedBombIcon;
    private ImageIcon unamorcedMineIcon;
    private ImageIcon healIcon;
    private ImageIcon wallIcon;
    // Constructeur qui prend un objet Player et la grille
    public ViewPlayer(Player player, Grid grid) {
        this.player = player;
        this.grid = grid;
        
        // Initialiser le panneau principal avant d'y ajouter quoi que ce soit
        this.mainPanel = new JPanel(new BorderLayout());

        setTitle("Fenetre du joueur " + this.player.getName());
        setSize(700, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialiser les images et la grille
        createImage();
        createGrid(grid);

        createInventory();
        updateAllInventoriesDisplay();
        createHealthBar();
        
        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
        
        setLocationRelativeTo(null);
        setVisible(true);
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

    public ImageIcon getImage(File file) throws IOException {
        int cellSize = 50;
        Image image = ImageIO.read(file);
        Image scaledImage = image.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void createGrid(Grid grid) {
        // Initialiser le panneau de grille
        this.gridPanel = new JPanel(new GridLayout(grid.getHeight(), grid.getWidth()));
        this.gridPanel.setPreferredSize(new Dimension(500, 500)); // Taille fixe pour la grille

        // Initialisation des labels pour chaque cellule
        this.cellLabels = new JLabel[grid.getHeight()][grid.getWidth()];
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                JLabel cellLabel = new JLabel();
                cellLabel.setOpaque(true);
                cellLabel.setBackground(Color.WHITE);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellLabel.setHorizontalAlignment(JLabel.CENTER); // Centrer l'image dans la cellule
                cellLabel.setVerticalAlignment(JLabel.CENTER);
                cellLabels[i][j] = cellLabel;
                gridPanel.add(cellLabel);
            }
        }

        // Ajouter le panneau de grille dans le panneau principal
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(gridPanel); // Ajouter la grille au centre
        mainPanel.add(centerWrapper, BorderLayout.CENTER); // Ajouter au centre
    }

    public void updateCellDisplay(int x, int y, boolean isPlayer, boolean hasMine, boolean hasBomb, boolean hasGun, boolean hasUnamorcedMine, boolean hasUnamorcedBomb,boolean hasHeal, boolean hasWall) {
        if (isPlayer) {
            if (playerIcon != null) {
                cellLabels[x][y].setIcon(playerIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.BLUE);
            }
        } else if (hasMine && grid.getCell(x, y).getObjet().getOwner() == this.player) {
            if (mineIcon != null) {
                cellLabels[x][y].setIcon(mineIcon);
            } else {
                // Carré bleu si l'image du joueur n'est pas chargée
                cellLabels[x][y].setIcon(null);
                cellLabels[x][y].setBackground(Color.GREEN);
            }
        }
        else if (hasBomb && grid.getCell(x, y).getObjet().getOwner() == this.player) {
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

    public void createInventory() {
        this.inventoryBar = new JPanel();
        this.inventoryBar.setLayout(new BoxLayout(inventoryBar, BoxLayout.Y_AXIS)); // Inventaire empilé verticalement
        this.inventoryBar.setBorder(BorderFactory.createTitledBorder("Inventaire"));
        this.inventoryBar.setPreferredSize(new Dimension(200, 300)); // Taille du panneau
    }

    public void updateAllInventoriesDisplay() {
        inventoryBar.removeAll(); // Effacer l'inventaire actuel
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

        inventoryBar.revalidate();
        inventoryBar.repaint();
    }

    public void createHealthBar() {
        // Panneau principal pour contenir la barre de vie et l'inventaire
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Organisation verticale
        leftPanel.setPreferredSize(new Dimension(200, 500)); // Largeur fixe

        // Panneau de la barre de vie
        this.healthBarPanel = new JPanel();
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.Y_AXIS)); // Une seule barre de vie
        healthBarPanel.setBorder(BorderFactory.createTitledBorder("Santé"));

        // Ajouter la barre de vie du joueur
        leftPanel.add(healthBarPanel);

        // Ajouter l'inventaire en dessous
        createInventory(); // Crée l'inventaire directement ici
        leftPanel.add(inventoryBar); // Ajouter l'inventaire au panneau principal

        // Ajouter le panneau principal à la gauche (WEST)
        mainPanel.add(leftPanel, BorderLayout.WEST);
    }

    public void updateHealthBars() {
        healthBarPanel.removeAll(); 

        // Créer une étiquette avec le nom du joueur
        JLabel playerNameLabel = new JLabel(player.getName());
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajouter l'étiquette avec le nom du joueur dans la barre de santé
        healthBarPanel.add(playerNameLabel);

        // Créer une barre de vie pour le joueur
        JProgressBar healthBar = new JProgressBar(0, player.getMaxEnergy());
        healthBar.setValue(player.getEnergy()); // Valeur actuelle de la vie du joueur
        healthBar.setString(player.getEnergy() + "/" + player.getMaxEnergy()); // Affiche la valeur actuelle/valeur maximale
        healthBar.setStringPainted(true); // Afficher les pourcentages sur la barre

        // Définir la couleur de la barre en fonction de l'énergie
        if ((float) player.getEnergy() / player.getMaxEnergy() > 0.5) {
            healthBar.setForeground(Color.GREEN); // Si plus de 50%, vert
        } else {
            healthBar.setForeground(Color.RED); // Si moins de 50%, rouge
        }
        
        healthBar.setPreferredSize(new Dimension(150, 30)); // Définir la taille de la barre de vie

        // Ajouter la barre de vie au panneau de la barre de vie
        healthBarPanel.add(healthBar);

        // Repeindre et réorganiser l'affichage de la barre de vie
        healthBarPanel.revalidate();
        healthBarPanel.repaint();
    }

}

