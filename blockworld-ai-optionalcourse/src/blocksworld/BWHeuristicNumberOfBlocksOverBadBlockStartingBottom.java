package blocksworld;

import java.util.*;
import modelling.*;
import planning.Heuristic;

public class BWHeuristicNumberOfBlocksOverBadBlockStartingBottom implements Heuristic {
    
    private Map<Variable, Object> goal;

    public BWHeuristicNumberOfBlocksOverBadBlockStartingBottom(Map<Variable, Object> goal) {
        this.goal = goal;
    }


    
    @Override
    public float estimate(Map<Variable, Object> state){
        // Compte le nombre de blocs qui sont au-dessus du premier mauvais bloc dans chaque pile de l'état actuel a partir du bas
        
        float estimate = 0;
        
        for (Variable block : goal.keySet()) {

            if (block != null && block.getName().contains("on")) {

                System.out.println("Analyzing block: " + block.getName());

                int[] currentStateStack = getStack((int) state.get(block), state);
                int[] goalStack = getStack((int) goal.get(block), goal);

                System.out.println("Current stack: " + Arrays.toString(currentStateStack));
                System.out.println("Goal stack: " + Arrays.toString(goalStack));

                // Vérifie les blocs dans les deux piles, jusqu'à la longueur de la pile la plus courte
                for (int i = 0; i < Math.min(currentStateStack.length, goalStack.length); i++) {
                    if (currentStateStack[i] != goalStack[i]) {
                        System.out.println("Difference found at index " + i + ": " + currentStateStack[i] + " != " + goalStack[i]);
                        //System.out.println("Increasing estimation by " + ((currentStateStack.length)-i));

                        estimate += currentStateStack.length-i;
                        break;
                    }
                }
                System.out.println("\n");
            }
        } 


        System.out.println("Estimation : " + estimate);
        return estimate;
    }


    
    private int[] getStack(int index, Map<Variable, Object> state) {
        // Récupère la pile des blocs à partir de l'index donné
        List<Integer> pile = new ArrayList<>();
        pile.add(index);

        // On cherche chaque bloc au-dessus de l'index actuel pour reconstruire la pile
        while (true) {
            boolean foundNextBlock = false;
            for (Variable block : state.keySet()) {
                if (block != null && block.getName().contains("on") && (int) state.get(block) == index) {
                    index = Integer.parseInt(block.getName().substring(3)); // Récupère l'index du bloc au-dessus
                    pile.add(index);
                    foundNextBlock = true;
                    break;
                }
            }
            if (!foundNextBlock) {
                break; // Sortir si aucun bloc au-dessus n'est trouvé
            }
        }
        return pile.stream().mapToInt(i -> i).toArray(); 
    }

}