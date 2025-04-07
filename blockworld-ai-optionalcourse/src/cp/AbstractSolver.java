package cp;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public abstract class AbstractSolver implements Solver {
    protected final Set<Variable> variables;
    protected final Set<Constraint> constraints;

    // Constructeur prenant les variables et les contraintes
    public AbstractSolver(Set<Variable> variables, Set<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    // Méthode pour vérifier si une affectation partielle est cohérente avec les contraintes
    public boolean isConsistent(Map<Variable, Object> partialAssignment) {
        for (Constraint constraint : constraints) {
            // Créer un ensemble d'assignations pour les variables de la contrainte
            Map<Variable, Object> currentAssignment = new HashMap<>();
            boolean allAssigned = true; // Indicateur pour savoir si toutes les variables sont assignées
    
            for (Variable var : constraint.getScope()) {
                if (partialAssignment.containsKey(var)) {
                    currentAssignment.put(var, partialAssignment.get(var));
                } else {
                    allAssigned = false; // Une variable n'est pas assignée
                }
            }
    
            // Si toutes les variables de la contrainte ne sont pas assignées, passer à la suivante
            if (!allAssigned) {
                continue;
            }
    
            // Vérifier si la contrainte est satisfaite
            if (!constraint.isSatisfiedBy(currentAssignment)) {
                return false;  // Si une contrainte est violée, retourner faux
            }
        }
        return true;  // Toutes les contraintes sont satisfaites
    }
}
