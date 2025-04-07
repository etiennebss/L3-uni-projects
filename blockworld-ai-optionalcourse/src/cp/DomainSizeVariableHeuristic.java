package cp;

import java.util.*;
import modelling.Variable;

public class DomainSizeVariableHeuristic implements VariableHeuristic {

    private final boolean preferLargestDomain;

    // Constructeur prenant le booléen en argument
    public DomainSizeVariableHeuristic(boolean preferLargestDomain) {
        this.preferLargestDomain = preferLargestDomain;
    }

    // Méthode pour trouver la meilleure variable en fonction de la taille de son domaine
    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        // Trouver la meilleure variable selon la taille de son domaine
        return variables.stream().max((v1, v2) -> {
            int size1 = domains.get(v1).size();
            int size2 = domains.get(v2).size();

            // Si on préfère les variables avec le plus grand domaine
            if (preferLargestDomain) {
                return Integer.compare(size1, size2);  // Ordre croissant
            } else {
                return Integer.compare(size2, size1);  // Ordre décroissant
            }
        }).orElse(null); // Retourne null si aucun résultat trouvé
    }
}
