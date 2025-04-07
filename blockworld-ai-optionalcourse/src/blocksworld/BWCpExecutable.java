package blocksworld;

import cp.*;
import java.util.*;
import modelling.*;


public class BWCpExecutable {
    public static void main(String[] args) {
        
        BlocksWorld blocksworld = new BlocksWorld(5, 3);
        BWVariables BwVariable = blocksworld.getBwVariables();

        System.out.println("************************** Demo pour regularyConstraint *******************");
        BWConstraintsRegulary bwRC = new BWConstraintsRegulary(blocksworld);
        Set<Constraint> constraintsRC = bwRC.getConstraints();
        Solver backTrackSolver = new BacktrackSolver(BwVariable.getVariables(), constraintsRC);
        Solver macSolver = new MACSolver(BwVariable.getVariables(), constraintsRC);
        
        VariableHeuristic variableDomainHeuristic = new DomainSizeVariableHeuristic(true);
        ValueHeuristic vhHeuristic = new RandomValueHeuristic( new Random());
        Solver heuristicMAC = new HeuristicMACSolver(BwVariable.getVariables(), constraintsRC, variableDomainHeuristic, vhHeuristic);

        long t1 = System.nanoTime();
        backTrackSolver.solve();
        long t2 = System.nanoTime();
        macSolver.solve();
        long t3 = System.nanoTime();
        heuristicMAC.solve();
        long t4 = System.nanoTime();

        System.out.println("Temps backtrack avec des contraintes régulières : " + (float) (t2 - t1));
        System.out.println("Temps MACSolver avec des contraintes régulières: " + (float)(t3 - t2));
        System.out.println("Temps HeuristicMACSolver avec des contraintes régulières: " + (float)(t4-t3));
        System.out.println("\n");





        System.out.println("************************** Demo pour AscendingConstraint *******************");

        BWConstraintsAscending bwAC = new BWConstraintsAscending(blocksworld);
        Set<Constraint> constraintsAC = bwAC.getConstraints();
        Solver backtrackSolver2 = new BacktrackSolver(BwVariable.getVariables(), constraintsAC);
        Solver macSolver2 = new MACSolver(BwVariable.getVariables(), constraintsAC);

        VariableHeuristic var_DomaineAC = new DomainSizeVariableHeuristic(true);
        ValueHeuristic     vh_RandomAC = new RandomValueHeuristic( new Random());
        Solver heurisMAC_AC = new HeuristicMACSolver(BwVariable.getVariables(),constraintsAC,var_DomaineAC,vh_RandomAC);

        long ta = System.nanoTime();
        backtrackSolver2.solve();
        long tb = System.nanoTime();
        macSolver2.solve();
        long tc = System.nanoTime();
        heurisMAC_AC.solve();
        long td = System.nanoTime();


        System.out.println("Temps backtrack avec des contraintes ascendantes : " + (float) (tb - ta));
        System.out.println("Temps MACSolver avec des contraintes ascendantes : " + (float)(tc - tb));
        System.out.println("Temps HeuristicMACSolver avec des contraintes ascendantes : " + (float)(td-tc));



    }
}
