package blocksworld;

import java.util.*;
import modelling.Constraint;
import modelling.UnaryConstraint;
import modelling.Variable;

public class BWConstraintsAscending {
    private BlocksWorld blocksWorld;
    private Set<Constraint> constraints;

    public BWConstraintsAscending(BlocksWorld blocksWorld) {
        this.blocksWorld = blocksWorld;
        this.constraints = new HashSet<>(blocksWorld.getConstraints());
        createConstraintsAscending();
    }

    // Création des contraintes de croissance : chaque bloc doit être placé sur un bloc
    // de numéro plus petit ou directement sur la table.
    private void createConstraintsAscending() {
        Set<Variable> onVariables = this.blocksWorld.getBwVariables().getOnbVariables();
        
        for (Variable on_i : onVariables) {
            int i = BWVariables.getIndex(on_i);
            Set<Object> domain_i = new HashSet<>();

            // Ajoute tous les blocs de numéro inférieur et les indices de piles
            for (int j = -this.blocksWorld.getNbPiles(); j < i; j++) {
                domain_i.add(j);
            }

            // Contrainte pour que 'on_i' prenne seulement des valeurs valides de croissance
            constraints.add(new UnaryConstraint(on_i, domain_i));
        }
    }

    public Set<Constraint> getConstraints() { return constraints; }
    public void setConstraints(Set<Constraint> constraints) { this.constraints = constraints; }
}