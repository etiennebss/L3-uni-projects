package cp;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public class MACSolver extends AbstractSolver {

    private ArcConsistency arcConsistency;  // Instance pour gérer la cohérence d'arc

    // Constructeur de MACSolver prenant un ensemble de variables et un ensemble de contraintes
    public MACSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints); // Appel au constructeur de la classe parente
        this.arcConsistency = new ArcConsistency(constraints); // Initialisation de l'instance d'ArcConsistency
    }

    // Méthode solve pour résoudre le CSP en utilisant la cohérence d'arc
    public Map<Variable, Object> solve() {
        // Création des domaines initiaux pour chaque variable
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for (Variable var : variables) {
            domains.put(var, new HashSet<>(var.getDomain())); // On suppose que Variable a une méthode getDomain()
        }

        // Appel de la méthode MAC avec une affectation partielle vide et les variables non instanciées
        return MAC(new HashMap<>(), new LinkedList<>(variables), domains);
    }

    private Map<Variable, Object> MAC(Map<Variable, Object> partialAssignment, List<Variable> unassigned, Map<Variable, Set<Object>> domains) {
        // Conditions d'arrêt de la récursivité
        if (unassigned.isEmpty()) {
            return partialAssignment; // Retourner l'affectation complète
        }
    
        // Réduction des domaines des variables par la cohérence d'arc
        if (!arcConsistency.ac1(domains)) {
            return null; // Si la cohérence d'arc échoue, retourner null
        }
    
        // Choisir une variable non encore instanciée
        Variable currentVariable = unassigned.remove(0); // Retirer la première variable de la liste
    
        // Choisir une valeur dans le domaine de currentVariable
        for (Object value : domains.get(currentVariable)) {
            Map<Variable, Object> newAssignment = new HashMap<>(partialAssignment);
            newAssignment.put(currentVariable, value); // Ajouter la valeur à l'affectation partielle
    
            // Vérifier la cohérence de l'affectation
            if (isConsistent(newAssignment)) {
                // Appel récursif de MAC avec la nouvelle affectation
                Map<Variable, Object> result = MAC(newAssignment, unassigned, domains);
                if (result != null) {
                    return result; // Retourner la solution trouvée
                }
            }
        }
    
        unassigned.add(0, currentVariable); // Réajouter la variable non assignée si aucune solution n'est trouvée
        return null; // Retourner null si aucune solution n'est trouvée
    }
}
