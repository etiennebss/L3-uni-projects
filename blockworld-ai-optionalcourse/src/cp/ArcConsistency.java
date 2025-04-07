package cp;

import java.util.*;
import modelling.*;

public class ArcConsistency {
    
    Set<Constraint> constraints;
    
    public ArcConsistency(Set<Constraint> constraints){
        this.constraints = constraints;

        for (Constraint c : constraints) {
            int scope = c.getScope().size();
            if (scope > 2) {
                throw new IllegalArgumentException("Arc consistency ne fonctionne que pour les contraintes binaires ou unaires.");
            }
        }
    }


    public boolean enforceNodeConsistency(Map<Variable, Set<Object>> domains) {
        boolean consistent = true; // Indique si tous les domaines restent consistants

        // Parcourir toutes les contraintes unaires
        for (Constraint constraint : constraints) {
            if (constraint.getScope().size() == 1) { // On regarde les contraintes unaires.
                Variable var = constraint.getScope().iterator().next(); 
                Set<Object> domain = domains.get(var); // domaine de la variable

                if (domain != null) {
                    Iterator<Object> it = domain.iterator();
                    while (it.hasNext()) {
                        Object value = it.next();

                        // Affectation temporaire pour tester la contrainte unaire
                        Map<Variable, Object> assignment = new HashMap<>();
                        assignment.put(var, value);

                        // On la retire du domaine
                        if (!constraint.isSatisfiedBy(assignment)) {
                            it.remove();
                        }
                    }
                    if (domain.isEmpty()) {
                        consistent = false;
                    }
                }
            }
        }

        return consistent;
    }

    public boolean revise(Variable v1, Set<Object> D1, Variable v2, Set<Object> D2) {
        boolean del = false; // Indique si une suppression a été effectuée

        // Parcourt toutes les valeurs vi de D1 (domaine de v1)
        Iterator<Object> it1 = D1.iterator();
        while (it1.hasNext()) {
            Object vi = it1.next();
            boolean viable = false;

            // Parcourt toutes les valeurs vj de D2 (domaine de v2)
            for (Object vj : D2) {
                boolean toutSatisfait = true;

                // Vérifie toutes les contraintes binaires entre v1 et v2
                for (Constraint c : constraints) {
                    if (c.getScope().contains(v1) && c.getScope().contains(v2)) {
                        // Création d'une affectation temporaire pour tester la contrainte
                        Map<Variable, Object> assignment = new HashMap<>();
                        assignment.put(v1, vi);
                        assignment.put(v2, vj);

                        // Si la contrainte n'est pas satisfaite, on marque l'affectation comme invalide
                        if (!c.isSatisfiedBy(assignment)) {
                            toutSatisfait = false;
                            break;
                        }
                    }
                }

                // Si une valeur vj satisfait toutes les contraintes binaires, on arrête la recherche
                if (toutSatisfait) {
                    viable = true;
                    break;
                }
            }

            // Si aucune valeur de v2 ne supporte vi, on supprime vi de D1
            if (!viable) {
                it1.remove();  // Supprimer vi de D1
                del = true;  // Au moins une valeur a été supprimée
            }
        }

        return del;  // Retourne true si une valeur a été supprimée de D1, sinon false
    }


    public boolean ac1(Map<Variable, Set<Object>> domains) {
        // Appliquer la consistance de noeud (Node Consistency)
        if (!enforceNodeConsistency(domains)) {
            return false; // Si un domaine est vide après la consistance de noeud
        }
    
        boolean change;
    
        // Répéter tant qu'il y a des changements
        do {
            change = false;
    
            // Parcourir toutes les variables v1 et v2 du CSP
            for (Variable v1 : domains.keySet()) {
                for (Variable v2 : domains.keySet()) {
                    if (!v1.equals(v2)) {
                        // Appeler revise pour les couples de variables
                        boolean revised = revise(v1, domains.get(v1), v2, domains.get(v2));
                        if (revised) {
                            change = true; // Si revise a supprimé des valeurs, indiquer un changement
                        }
                    }
                }
            }
    
        } while (change); // Tant que des changements sont effectués
    
        // Après la boucle, vérifier si un domaine est vide
        for (Variable v : domains.keySet()) {
            if (domains.get(v).isEmpty()) {
                return false; // Retourner false si un domaine est vide
            }
        }
    
        return true; // Si aucun domaine n'est vide, retourner true
    }
}
