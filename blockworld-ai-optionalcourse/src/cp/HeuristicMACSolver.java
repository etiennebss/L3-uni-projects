package cp;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public class HeuristicMACSolver extends AbstractSolver {
    
    private final VariableHeuristic variableHeuristic;
    private final ValueHeuristic valueHeuristic;

    // Constructeur prenant les variables, contraintes, heuristique des variables et heuristique des valeurs
    public HeuristicMACSolver(Set<Variable> variables, Set<Constraint> constraints,
                              VariableHeuristic variableHeuristic, ValueHeuristic valueHeuristic) {
        super(variables, constraints);
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = initializeDomains();
        LinkedList<Variable> unassignedVariables = new LinkedList<>(variables);
        Map<Variable, Object> assignment = new HashMap<>();

        return MAC(assignment, unassignedVariables, domains);
    }

    private Map<Variable, Set<Object>> initializeDomains() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for (Variable variable : variables) {
            domains.put(variable, new HashSet<>(variable.getDomain()));
        }
        return domains;
    }

    // Implémentation de l'algorithme MAC avec heuristiques sur les variables et les valeurs
    private Map<Variable, Object> MAC(Map<Variable, Object> assignment, LinkedList<Variable> unassignedVariables,
                                      Map<Variable, Set<Object>> domains) {
        if (unassignedVariables.isEmpty()) {
            return assignment;  // Solution trouvée
        }

        // Réduction des domaines par la cohérence d'arc (AC-1)
        ArcConsistency ac = new ArcConsistency(constraints);
        if (!ac.ac1(domains)) {
            return null;  // Si AC1 échoue, retour à l'arrière
        }

        // Sélection de la meilleure variable non assignée selon l'heuristique
        Variable var = variableHeuristic.best(new HashSet<>(unassignedVariables), domains);
        unassignedVariables.remove(var);

        // Essayer chaque valeur du domaine de var, selon l'heuristique des valeurs
        List<Object> orderedValues = valueHeuristic.ordering(var, domains.get(var));
        for (Object value : orderedValues) {
            // Créer une nouvelle affectation partielle
            assignment.put(var, value);

            if (isConsistent(assignment)) {
                // Propagation et récursivité
                Map<Variable, Object> result = MAC(assignment, unassignedVariables, domains);
                if (result != null) {
                    return result;
                }
            }

            // Retirer l'affectation si elle ne mène pas à une solution
            assignment.remove(var);
        }

        // Remettre la variable dans les non-assignées
        unassignedVariables.addFirst(var);
        return null;  // Échec si aucune solution n'a été trouvée
    }
}
