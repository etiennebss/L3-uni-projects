package blocksworld;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import bwmodel.BWState;
import bwmodel.BWStateBuilder;
import bwui.BWComponent;
import bwui.BWIntegerGUI;

import modelling.Variable;
import planning.Action;

public class BWView {
    private BWVariables blockWorldVars;
    private Map<Variable, Object> currentState;
    private String windowTitle;

    public BWView(BWVariables blockWorldVars, Map<Variable, Object> currentState, String windowTitle) {
        this.blockWorldVars = blockWorldVars;
        this.currentState = currentState;
        this.windowTitle = windowTitle;

        System.out.println("Variables in BWVariables: " + blockWorldVars.getOnbVariables());
        System.out.println("Variables in currentState: " + currentState.keySet());
    }

    public BWView(BWVariables blockWorldVars, Map<Variable, Object> currentState) {
        this(blockWorldVars, currentState, "Visualisation de l'état");
    }

    // Crée une instance de l'état du monde de blocs à partir de l'état actuel
    public BWState<Integer> buildBlockWorldState() {
        int numberOfBlocks = blockWorldVars.getOnbVariables().size();
        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(numberOfBlocks);
        for (Variable var : blockWorldVars.getOnbVariables()) {
            if (!currentState.containsKey(var)) {
                System.out.println("Variable missing in currentState: " + var);
            }
            int index = BWVariables.getIndex(var);
            int belowBlock = (int) currentState.get(var);
            if (belowBlock >= 0){
                builder.setOn(index, belowBlock);
            }
        }
        return builder.getState();
    }

    // Afiche l'état dans une fenêtre
    public void showCurrentState(int posX, int posY) {
        int blockCount = blockWorldVars.getOnbVariables().size();
        BWState<Integer> bwState = buildBlockWorldState();
        BWIntegerGUI gui = new BWIntegerGUI(blockCount);
        JFrame frame = new JFrame(windowTitle);
        frame.setLocation(posX, posY);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.add(gui.getComponent(bwState));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Affiche l'exécution des plans
    public void showActionPlan(List<Action> actionSequence, int posX, int posY) {
        int blockCount = blockWorldVars.getOnbVariables().size();
        BWIntegerGUI gui = new BWIntegerGUI(blockCount);
        JFrame frame = new JFrame(windowTitle);
        frame.setLocation(posX, posY);
        frame.setPreferredSize(new Dimension(500, 500));
        BWState<Integer> bwState = buildBlockWorldState();
        BWComponent<Integer> component = gui.getComponent(bwState);
        frame.add(component);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        for (Action action : actionSequence) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            currentState = action.successor(currentState);
            component.setState(buildBlockWorldState());
        }
    }
}