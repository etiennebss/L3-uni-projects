package blocksworld;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public class BWExecutable {
    
    public static void main(String[] args) {
        BlocksWorld bw = new BlocksWorld(6, 4);
        BWConstraintsRegulary bwCR = new BWConstraintsRegulary(bw);
        BWConstraintsAscending bwCAsc = new BWConstraintsAscending(bw);
        BWVariables bwVariables = bw.getBwVariables();

        System.out.println("***************************  Demo Constraints ************************");

        // États de test définis comme des listes de piles
        int[][] listePile1 = { {0}, {1,2, 3}, {4, 5, 6}, {7} };
        int[][] listePile2 = { {0, 1, 2}, {3, 5, 7}, {4, 6},{} };
        int[][] listePile3 = { {0, 3, 1}, {2, 4, 5, 6}, {7}, {} };
        int[][] listePile4 = { {0, 1, 2}, {3,4}, {5}, {} };

        // Conversion des états en mappage de variables
        Map<Variable, Object> state1 = bwVariables.getState(listePile1);
        Map<Variable, Object> state2 = bwVariables.getState(listePile2);
        Map<Variable, Object> state3 = bwVariables.getState(listePile3);
        Map<Variable, Object> state4 = bwVariables.getState(listePile4);

        boolean allSatisfied;

        // Vérification des contraintes de base pour l'état state1
        allSatisfied = verifyConstraints(bw.getConstraints(), state1);
        System.out.println(allSatisfied ? 
            "Contraintes de base satisfaite pour l'état " + Arrays.deepToString(listePile1) : //On utilise deepToString pour acceder de manière lisible à un tableau multidimensionnel
            "Contraintes de base non satisfaite pour l'état " + Arrays.deepToString(listePile1));

        // Vérification des contraintes de régularité pour l'état state2
        allSatisfied = verifyConstraints(bwCR.getConstraints(), state2);
        System.out.println(allSatisfied ? 
            "Contraintes de régularité satisfaite pour l'état " + Arrays.deepToString(listePile2) : 
            "Contraintes de régularité non satisfaite pour l'état " + Arrays.deepToString(listePile2));

        // Vérification des contraintes de croissance pour l'état state3
        allSatisfied = verifyConstraints(bwCAsc.getConstraints(), state3);
        System.out.println(allSatisfied ? 
            "Contraintes de croissance satisfaite pour l'état " + Arrays.deepToString(listePile3) : 
            "Contraintes de croissance non satisfaite pour l'état " + Arrays.deepToString(listePile3));

        // Vérification des contraintes de régularité et de croissance pour l'état state4
        Set<Constraint> allConstraints = new HashSet<>(bwCR.getConstraints());
        allConstraints.addAll(bwCAsc.getConstraints());
        allSatisfied = verifyConstraints(allConstraints, state4);
        System.out.println(allSatisfied ? 
            "Contraintes de régularité et de croissance satisfaite pour l'état " + Arrays.deepToString(listePile4) : 
            "Contraintes de régularité et de croissance non satisfaite pour l'état " + Arrays.deepToString(listePile4));
    }

    private static boolean verifyConstraints(Set<Constraint> constraints, Map<Variable, Object> state) {
        for (Constraint constraint : constraints) {
            if (!constraint.isSatisfiedBy(state)) {  // On utilise la méthode isSatisfiedBy faite pendant le semestre. 
                return false;
            }
        }
        return true;
    }
}
