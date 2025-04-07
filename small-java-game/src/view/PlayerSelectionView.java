package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelectionView extends JFrame {
    private List<String[]> selectedPlayers = new ArrayList<>();
    private JPanel playerConfigPanel;
    private Runnable onGameStart; // Callback pour le démarrage du jeu

    public PlayerSelectionView() {
        setTitle("Sélection des Joueurs");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Ajoutez les joueurs et choisissez leurs types :", SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.NORTH);

        playerConfigPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(playerConfigPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton addPlayerButton = new JButton("Ajouter Joueur");
        JButton startGameButton = new JButton("Lancer le Jeu");

        addPlayerButton.addActionListener(e -> addPlayerConfig());
        startGameButton.addActionListener(e -> {
            if (selectedPlayers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ajoutez au moins un joueur pour commencer !");
            } else {
                if (onGameStart != null) {
                    onGameStart.run(); // Notifie que le jeu doit commencer
                }
                setVisible(false); // Cacher la fenêtre une fois terminée
                dispose();
            }
        });

        controlPanel.add(addPlayerButton);
        controlPanel.add(startGameButton);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addPlayerConfig() {
        JPanel configPanel = new JPanel(new FlowLayout());
        
        // Créer le champ de texte avec le nom par défaut
        String defaultName = "Joueur " + (selectedPlayers.size() + 1); // "Joueur 1", "Joueur 2", etc.
        JTextField playerNameField = new JTextField(defaultName, 10); // Champ avec nom par défaut
        
        // Créer la JComboBox pour le type de joueur
        JComboBox<String> playerTypeCombo = new JComboBox<>(new String[]{"Default", "Tank", "Scout"});
        
        // Créer le bouton de suppression
        JButton removeButton = new JButton("Supprimer");

        removeButton.addActionListener(e -> {
            int index = playerConfigPanel.getComponentZOrder(configPanel);
            playerConfigPanel.remove(configPanel);
            selectedPlayers.remove(index); // Retirer les données correspondantes
            playerConfigPanel.revalidate();
            playerConfigPanel.repaint();
        });

        configPanel.add(new JLabel("Nom :"));
        configPanel.add(playerNameField);
        configPanel.add(new JLabel("Type :"));
        configPanel.add(playerTypeCombo);
        configPanel.add(removeButton);

        playerConfigPanel.add(configPanel);
        playerConfigPanel.revalidate();
        playerConfigPanel.repaint();

        // Ajouter les données à selectedPlayers avec le nom par défaut
        selectedPlayers.add(new String[]{defaultName, "Default"}); // Le nom est par défaut

        // Mettre à jour la liste selectedPlayers lorsque le texte ou le type change
        playerNameField.getDocument().addDocumentListener(new DocumentListener() {
            private void updatePlayerData() {
                int index = playerConfigPanel.getComponentZOrder(configPanel);
                if (index >= 0 && index < selectedPlayers.size()) {
                    selectedPlayers.set(index, new String[]{playerNameField.getText(), (String) playerTypeCombo.getSelectedItem()});
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePlayerData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePlayerData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePlayerData();
            }
        });

        playerTypeCombo.addActionListener(e -> {
            int index = playerConfigPanel.getComponentZOrder(configPanel);
            if (index >= 0 && index < selectedPlayers.size()) {
                selectedPlayers.set(index, new String[]{playerNameField.getText(), (String) playerTypeCombo.getSelectedItem()});
            }
        });
    }

    public void setOnGameStart(Runnable onGameStart) {
        this.onGameStart = onGameStart;
    }

    public List<String[]> getSelectedPlayers() {
        return selectedPlayers;
    }
}
