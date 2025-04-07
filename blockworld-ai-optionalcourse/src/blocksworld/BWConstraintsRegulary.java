package blocksworld;

import java.util.*;
import modelling.Constraint;
import modelling.Implication;
import modelling.Variable;


public class BWConstraintsRegulary {
    private BlocksWorld blocksWorld;
    private Set<Constraint> constraints;

    public BWConstraintsRegulary(BlocksWorld blocksWorld) {
        this.blocksWorld = blocksWorld;
        this.constraints = new HashSet<>(blocksWorld.getConstraints());
        createRegularyConstraints();
    }

    // Création des contraintes de régularité,  chaque bloc doit respecter un écart constant 
    // avec le bloc précédent dans la pile.

    private void createRegularyConstraints() {
        Set<Variable> onVariables = this.blocksWorld.getBwVariables().getOnbVariables();
        
        for (Variable on_i : onVariables) {
            int i = BWVariables.getIndex(on_i);
            
            for (Variable on_j : onVariables) {
                int j = BWVariables.getIndex(on_j);
                
                // Calcul de la position cible en fonction de l'écart
                int k = j - (i - j);

                if (i != j) {
                    Set<Object> domain_i = Set.of(j); // Valeur cible pour on_i
                    Set<Object> domain_j = new HashSet<>();

                    // Ajout des indices négatifs pour représenter les piles
                    for (int p = 1; p <= this.blocksWorld.getNbPiles(); p++) {
                        domain_j.add(-p);
                    }

                    // Ajout de la position k si elle est valide (autrement dit, si un bloc existe)
                    if (k >= 0 && k < this.blocksWorld.getNbBlocks()) {
                        domain_j.add(k);
                    }
                    
                    // Création de l'implication pour maintenir la régularité de la pile
                    constraints.add(new Implication(on_i, domain_i, on_j, domain_j));
                }
            }
        }
    }

    public Set<Constraint> getConstraints() { return constraints; }
    public void setConstraints(Set<Constraint> constraints) { this.constraints = constraints; }
}