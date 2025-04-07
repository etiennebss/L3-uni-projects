package blocksworld;

import java.util.*;
import modelling.*;
import planning.*;


public class BWPlannerExecutable {
    public static void main(String[] args) {

        BWActions actionsGenerator = new BWActions(5,3);
        BWVariables BwVariable = actionsGenerator.getBlocksWorld().getBwVariables();
        Set<Action> actions = actionsGenerator.getActions();
        
        int[][] initialConfig = {
            {2,4,0},
            {},
            {3,1}
        };

        Map<Variable, Object> initialState = BwVariable.getState(initialConfig);



        int[][] goalConfig = {
            {3,1, 0},
            {4,2},
            {}
        };

        Map<Variable, Object> goalState = BwVariable.getState(goalConfig);


        Goal goal = new BasicGoal(goalState);


        // Instanciation des solvers
        BFSPlanner bfsPlanner = new BFSPlanner(initialState, actions, goal);
        System.out.println("ON REGARDE SI TOUS LES ATTRIBUTS NE SONT PAS VIDES");
        System.out.println();
        System.out.println("initialState : ");
        System.out.println(initialState);
        System.out.println();
        System.out.println("actions : ");
        System.out.println(actions);
        System.out.println();
        System.out.println("goal : ");
        System.out.println(goal);

        bfsPlanner.activateNodeCount(true);
        System.out.println();
        System.out.println("bfsPlanner : ");
        System.out.println(bfsPlanner);

        new BasicGoal(goalState);

        DFSPlanner dfsPlanner = new DFSPlanner(initialState, actions, goal);
        dfsPlanner.activateNodeCount(true);

        DijkstraPlanner dijkstraPlanner = new DijkstraPlanner(initialState, actions, goal);
        dijkstraPlanner.activateNodeCount(true);

        BWHeuristicBlocksAtWrongPlace heuristic1 = new BWHeuristicBlocksAtWrongPlace(goalState);
        AStarPlanner aStarPlanner1 = new AStarPlanner(initialState, actions, goal, heuristic1);
        aStarPlanner1.activateNodeCount(true);

        BWHeuristicNbOfFixedBlocksAtBadPosition heuristic2 = new BWHeuristicNbOfFixedBlocksAtBadPosition(goalState);
        AStarPlanner aStarPlanner2 = new AStarPlanner(initialState, actions, goal, heuristic2);
        aStarPlanner2.activateNodeCount(true);

        long startbfs = System.nanoTime();
        List<Action> bfsPlan = bfsPlanner.plan();
        long endbfs = System.nanoTime();

        long startdfs = System.nanoTime();
        List<Action> dfsPlan = dfsPlanner.plan();
        long enddfs = System.nanoTime();

        long startDijkstra = System.nanoTime();
        List<Action> dijkstraPlan = dijkstraPlanner.plan();
        long endDijkstra = System.nanoTime();

        long startaStar1 = System.nanoTime();
        List<Action> aStarPlan1 = aStarPlanner1.plan();
        long endaStar1 = System.nanoTime();

        long startaStar2 = System.nanoTime();
        List<Action> aStarPlan2 = aStarPlanner2.plan();
        long endaStar2 = System.nanoTime();

        // Affichage des résultats

        System.out.println("******************************** BFS ************************");
        System.out.printf("Nombre de noeuds visités %d, Longueur du plan %d, Temps de calcul %.3f", bfsPlanner.getNodeCount(), bfsPlan.size(), (float) (endbfs - startbfs));
        System.out.println("\n");

        System.out.println("******************************** DFS ************************");
        System.out.printf("Nombre de noeuds visités %d, Longueur du plan %d, Temps de calcul %.3f", dfsPlanner.getNodeCount(), dfsPlan.size(), (float) (enddfs - startdfs));
        System.out.println("\n");

        System.out.println("******************************** Dijkstra ************************");
        System.out.printf("Nombre de noeuds visités %d, Longueur du plan %d, Temps de calcul %.3f", dijkstraPlanner.getNodeCount(), dijkstraPlan.size(), (float) (endDijkstra - startDijkstra));
        System.out.println("\n");

        System.out.println("******************************** A* avec heuristique wrongblocks************************");
        System.out.printf("Nombre de noeuds visités %d, Longueur du plan %d, Temps de calcul %.3f", aStarPlanner1.getNodeCount(), aStarPlan1.size(), (float) (endaStar1 - startaStar1));
        System.out.println("\n");

        System.out.println("******************************** A* avec heuristique FixedblocksAtBadPosition************************");
        System.out.printf("Nombre de noeuds visités %d, Longueur du plan %d, Temps de calcul %.3f", aStarPlanner2.getNodeCount(), aStarPlan2.size(), (float) (endaStar2 - startaStar2));
        System.out.println("\n");

    
    
    
    
        BWView viewBfs = new BWView(BwVariable, initialState, "Bfs Plan");

    // DFS
    BWView viewDfs = new BWView(BwVariable, initialState, "Dfs Plan");

    // Dijkstra
    BWView viewDijkstra = new BWView(BwVariable, initialState, "Dijkstra Plan");

    // Astar with heuristic computing the number of blocks on wrong positions
    BWView viewAstar = new BWView(BwVariable, initialState, "Astar wrong block number heuristic plan");

    // Astar with heuristic computing the number of bottom blocks on wrong positions
    BWView viewAstar2 = new BWView(BwVariable, initialState, "Astar wrong fixed block number heuristic plan");

    
    new PlanExecution(viewBfs, bfsPlan, 0, 0).start();
    new PlanExecution(viewDfs, dfsPlan, 600, 0).start();
    new PlanExecution(viewDijkstra, dijkstraPlan, 1200, 0).start();
    new PlanExecution(viewAstar, aStarPlan1, 0, 600).start();
    new PlanExecution(viewAstar2, aStarPlan2, 600, 1200).start();
    }
}
