package cp;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public class NbConstraintsVariableHeuristic implements VariableHeuristic {

    private final Set<Constraint> constraints;
    private final boolean preferMostConstraints;

    // Constructeur prenant les contraintes et le booléen en argument
    public NbConstraintsVariableHeuristic(Set<Constraint> constraints, boolean preferMostConstraints) {
        this.constraints = constraints;
        this.preferMostConstraints = preferMostConstraints;
    }

    // Méthode pour trouver la meilleure variable en fonction du nombre de contraintes
    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        // Créer une map pour compter le nombre de contraintes par variable
        Map<Variable, Integer> constraintCount = new HashMap<>();
        
        for (Variable var : variables) {
            constraintCount.put(var, 0);  // Initialiser à 0
            // Compter les contraintes qui incluent cette variable
            for (Constraint constraint : constraints) {
                if (constraint.getScope().contains(var)) {
                    constraintCount.put(var, constraintCount.get(var) + 1);
                }
            }
        }

        // Trouver la meilleure variable selon le nombre de contraintes
        return variables.stream().min((v1, v2) -> {
            int count1 = constraintCount.get(v1);
            int count2 = constraintCount.get(v2);

            // Si on préfère les variables avec le plus de contraintes
            if (preferMostConstraints) {
                return Integer.compare(count2, count1);  // Ordre décroissant
            } else {
                return Integer.compare(count1, count2);  // Ordre croissant
            }
        }).orElse(null); // Retourne null si aucun résultat trouvé
    }
}
