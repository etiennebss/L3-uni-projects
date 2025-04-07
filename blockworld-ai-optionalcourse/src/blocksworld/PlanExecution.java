package blocksworld;

import java.util.*;
import planning.*;

public class PlanExecution extends Thread {
    private BWView viewer;
    private List<Action> actionPlan;
    private int posX;
    private int posY;

    // Constructeur pour créer un fil d'exécution qui affiche l'exécution du plan
    public PlanExecution(BWView viewer, List<Action> actionPlan, int posX, int posY) {
        this.viewer = viewer;
        this.actionPlan = actionPlan;
        this.posX = posX;
        this.posY = posY;
    }

    // affiche le plan d'actions en séquence
    @Override
    public void run() {
        viewer.showActionPlan(actionPlan, posX, posY);
    }
}