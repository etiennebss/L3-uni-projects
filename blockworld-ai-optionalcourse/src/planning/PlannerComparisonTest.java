package planning;

import modelling.Variable;

import java.util.*;

public class PlannerComparisonTest {
    public static void main(String[] args) {
        // Création des variables
        Variable x = new Variable("x", new HashSet<>(Arrays.asList("a", "b", "c")));
        Variable y = new Variable("y", new HashSet<>(Arrays.asList("b", "c", "d")));
        Variable z = new Variable("z", new HashSet<>(Arrays.asList("a", "d")));
        Variable w = new Variable("w", new HashSet<>(Arrays.asList("p", "q")));

        // Etats initiaux
        Map<Variable, Object> initialState = new HashMap<>();
        initialState.put(x, "a");
        initialState.put(y, "b");
        initialState.put(z, "a");
        initialState.put(w, "p"); 

        // Goal 
        Map<Variable, Object> goalState = new HashMap<>();
        goalState.put(x, "a");
        goalState.put(y, "c");
        goalState.put(z, "d");
        goalState.put(w, "q"); 

        // Creation du set action
        Set<Action> actions = new HashSet<>();
        
        // Action 1: Change x de "a" à "b"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(x, "a"); }},  // Preconditions
            new HashMap<>() {{ put(x, "b"); }},  // Effets
            1));

        // Action 2: Change x de "b" à "c"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(x, "b"); }},  // Preconditions
            new HashMap<>() {{ put(x, "c"); }},  // Effets
            1));

        // Action 3: Change y de "b" à "c"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(y, "b"); }}, // Preconditions
            new HashMap<>() {{ put(y, "c"); }},  // Effets
            2));

        // Action 4: Change y de "c" à "d"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(y, "c"); }}, // Preconditions
            new HashMap<>() {{ put(y, "d"); }},  // Effets
            2));
        
       // Action 5: Change z de "a" à "d"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(z, "a"); }}, // Preconditions
            new HashMap<>() {{ put(z, "d"); }},  // Effets
            3));

        // Action 6: Change w de "p" à "q", si z est "d"
        actions.add(new BasicAction(
            new HashMap<>() {{ put(w, "p"); put(z, "d"); }}, // Preconditions
            new HashMap<>() {{ put(w, "q"); }},  // Effets
            4));

        // Creations des planners avec les états, les actions et le but
        BFSPlanner bfsPlanner = new BFSPlanner(initialState, actions, new BasicGoal(goalState));
        DFSPlanner dfsPlanner = new DFSPlanner(initialState, actions, new BasicGoal(goalState));
        DijkstraPlanner dijkstraPlanner = new DijkstraPlanner(initialState, actions, new BasicGoal(goalState));


        Heuristic heuristic = (state) -> {
            int count = 0;
            for (Variable var : goalState.keySet()) {
                if (!state.get(var).equals(goalState.get(var))) {
                    count++;
                }
            }
            return count;
        };

        AStarPlanner aStarPlanner = new AStarPlanner(initialState, actions, new BasicGoal(goalState), heuristic);

        // Activation de la sonde
        bfsPlanner.activateNodeCount(true);
        dfsPlanner.activateNodeCount(true);
        dijkstraPlanner.activateNodeCount(true);
        aStarPlanner.activateNodeCount(true);

        // On trouve un plan
        List<Action> bfsPlan = bfsPlanner.plan();
        List<Action> dfsPlan = dfsPlanner.plan();
        List<Action> dijkstraPlan = dijkstraPlanner.plan();
        List<Action> aStarPlan = aStarPlanner.plan();

        System.out.println("BFS Plan: " + bfsPlan);
        System.out.println("DFS Plan: " + dfsPlan);
        System.out.println("Dijkstra Plan: " + dijkstraPlan);
        System.out.println("A* Plan: " + aStarPlan);

        System.out.println("Nombre de noeuds explorés (BFS): " + bfsPlanner.getNodeCount());
        System.out.println("Nombre de noeuds explorés (DFS): " + dfsPlanner.getNodeCount());
        System.out.println("Nombre de noeuds explorés (Dijkstra): " + dijkstraPlanner.getNodeCount());
        System.out.println("Nombre de noeuds explorés (A*): " + aStarPlanner.getNodeCount());
    }
}