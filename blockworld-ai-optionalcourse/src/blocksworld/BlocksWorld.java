package blocksworld;

import java.util.*;
import modelling.Constraint;
import modelling.DifferenceConstraint;
import modelling.Implication;
import modelling.Variable;


public class BlocksWorld {
    private int nbBlocks;
    private int nbPiles;
    private BWVariables bwVariables;
    private Set<Constraint> constraints;

    public BlocksWorld(int nbBlocks, int nbPiles) {
        this.nbBlocks = nbBlocks;
        this.nbPiles = nbPiles;
        this.bwVariables = new BWVariables(nbBlocks, nbPiles);
        this.constraints = new HashSet<>();
        initConstraints();
    }


    // Initialise toutes les contraintes
    private void initConstraints() {
        createDifferenceConstraints();
        createImplicationConstraints();
    }

    // Crée les contraintes de différence, chaque bloc doit être sur une position unique.

    private void createDifferenceConstraints() {
        Set<Variable> onbVariables = this.bwVariables.getOnbVariables();
        for (Variable onb1 : onbVariables) {
            for (Variable onb2 : onbVariables) {
                if (!onb1.equals(onb2)) {
                    this.constraints.add(new DifferenceConstraint(onb1, onb2));
                }
            }
        }
    }

    // Création des contraintes d'implication, gère les conditions sur les positions et états de blocs.
    private void createImplicationConstraints() {
        for (Variable onb : this.bwVariables.getOnbVariables()) {
            int bIndex = BWVariables.getIndex(onb);

            for (Variable fixedb : this.bwVariables.getFixedbVariables()) {
                if (bIndex != BWVariables.getIndex(fixedb)) {
                    addImplicationConstraint(onb, fixedb, bIndex);
                }
            }

            for (Variable freep : this.bwVariables.getFreepVariables()) {
                addFreePileConstraint(onb, freep);
            }
        }
    }

    private void addImplicationConstraint(Variable onb, Variable fixedb, int bIndex) {
        Set<Object> onbDomain = Set.of(bIndex);
        Set<Object> fixedDomain = Set.of(true);
        this.constraints.add(new Implication(onb, onbDomain, fixedb, fixedDomain));
    }

    private void addFreePileConstraint(Variable onb, Variable freep) {
        Set<Object> onbDomain = Set.of(-(BWVariables.getIndex(freep) + 1));
        Set<Object> freepDomain = Set.of(false);
        this.constraints.add(new Implication(onb, onbDomain, freep, freepDomain));
    }

    public int getNbBlocks() { return nbBlocks; }
    public int getNbPiles() { return nbPiles; }
    public BWVariables getBwVariables() { return bwVariables; }
    public Set<Constraint> getConstraints() { return constraints; }
}
