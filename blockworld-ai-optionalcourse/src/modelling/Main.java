package modelling;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Création d'une variable avec son domaine
        Variable var1 = new Variable("x", Set.of(1, 2, 3));
        Variable var2 = new Variable("y", Set.of(1, 2, 3));

        // Définition des sous-ensembles S
        Set<Object> S1 = new HashSet<>();
        S1.add(2);
        S1.add(3);
        Set<Object> S2 = new HashSet<>();
        S2.add(2);

/*                       CONTRAINTE DIFF                   */
        System.out.println("TEST POUR DIFF CONTRAINTE \n");
        DifferenceConstraint diffConstraint = new DifferenceConstraint(var1, var2);

        // Création d'une instanciation
        Map<Variable, Object> assignment = new HashMap<>();
        assignment.put(var1, 1);
        assignment.put(var2, 2);

        // Vérification si la contrainte est satisfaite
        boolean isSatisfied = diffConstraint.isSatisfiedBy(assignment);
        System.out.println("La contrainte est satisfaite: " + isSatisfied); // Affiche true car 1 != 2

        // Modification de l'affectation
        assignment.put(var2, 1);
        isSatisfied = diffConstraint.isSatisfiedBy(assignment);
        System.out.println("La contrainte est satisfaite: " + isSatisfied); // Affiche false car 1 == 1


/*                       CONTRAINTE POUR IMPLICATION       */
        System.out.println("TEST POUR IMPLICATION \n");
        Implication implicationConstraint = new Implication(var1, S1, var2, S2);

        // Création d'une instanciation (affectation des valeurs)
        assignment = new HashMap<>();
        assignment.put(var1, 1); // x appartient S1
        assignment.put(var2, 2); // y apartient S2

        // Vérification si la contrainte est satisfaite
        isSatisfied = implicationConstraint.isSatisfiedBy(assignment);
        System.out.println("La contrainte est satisfaite: " + isSatisfied); // Affiche true

/*                       CONTRAINTE UNAIRE                 */
        System.out.println("TEST POUR CONTRAINTE UNAIRE \n");
        // Création d'une contrainte unaire
        UnaryConstraint unaryConstraint = new UnaryConstraint(var1, S1);

        // Création d'une instanciation (affectation des valeurs)
        assignment = new HashMap<>();
        assignment.put(var1, 2);

        // Vérification si la contrainte est satisfaite
        isSatisfied = unaryConstraint.isSatisfiedBy(assignment);
        System.out.println("La contrainte est satisfaite: " + isSatisfied); // Affiche true car 2 appartient à l'ensemble

        // Modification de l'affectation pour avoir false
        assignment.put(var1, 1); 
        isSatisfied = unaryConstraint.isSatisfiedBy(assignment);
        System.out.println("La contrainte est satisfaite: " + isSatisfied); // Affiche false car 1 n'appartient pas à l'ensemble
    }
}