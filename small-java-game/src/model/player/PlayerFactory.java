package model.player;

public class PlayerFactory {
    public static Player createPlayer(String type, String name, int posX, int posY) {
        switch (type) {
            case "Tank":
                return new Player(name, posX, posY) {{
                    setEnergy(200); // Plus d'énergie
                }};
            case "Scout":
                return new Player(name, posX, posY) {{
                    setEnergy(50); // Moins d'énergie mais peut être ajouté une capacité spéciale plus tard
                }};
            case "Default":
            default:
                return new Player(name, posX, posY);
        }
    }
}