package blocksworld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Variable;
import planning.Action;
import planning.BasicAction;

public class BWActions {
    private BlocksWorld blockEnvironment;
    private Set<Action> availableActions;

    public BWActions(int totalBlocks, int totalPiles) {
        this.blockEnvironment = new BlocksWorld(totalBlocks, totalPiles);
        this.availableActions = new HashSet<>();
        generateAllActions();
    }

    // Génère une carte de variables et de valeurs pour les préconditions ou effets.
    private Map<Variable, Object> createVariableMap(Variable var1, Variable var2, Variable var3,
                                                    Object val1, Object val2, Object val3) {
        Map<Variable, Object> map = new HashMap<>();
        map.put(var1, val1);
        map.put(var2, val2);
        map.put(var3, val3);
        return map;
    }

    // Génère toutes les actions possibles dans le monde des blocs.
    private void generateAllActions() {
        moveBlockFromBlockToBlock();
        moveBlockFromBlockToPile();
        moveBlockFromPileToBlock();
        transferBlockBetweenPiles();
    }

    // Actions pour déplacer un bloc d'un autre bloc vers un troisième bloc.
    private void moveBlockFromBlockToBlock() {
        for (int b1 = 0; b1 < blockEnvironment.getNbBlocks(); b1++) {
            for (int b2 = 0; b2 < blockEnvironment.getNbBlocks(); b2++) {
                if (b1 == b2) continue;

                for (int b3 = 0; b3 < blockEnvironment.getNbBlocks(); b3++) {
                    if (b3 == b1 || b3 == b2) continue;

                    Variable onBlock1 = blockEnvironment.getBwVariables().getOnbVarByIndex(b1);
                    Variable fixedBlock1 = blockEnvironment.getBwVariables().getFixedVarByIndex(b1);
                    Variable fixedBlock2 = blockEnvironment.getBwVariables().getFixedVarByIndex(b2);
                    Variable fixedBlock3 = blockEnvironment.getBwVariables().getFixedVarByIndex(b3);

                    Map<Variable, Object> precondition = createVariableMap(onBlock1, fixedBlock1, fixedBlock3, b2, false, false);
                    Map<Variable, Object> effect = createVariableMap(onBlock1, fixedBlock2, fixedBlock3, b3, false, true);

                    availableActions.add(new BasicAction(precondition, effect, 1));
                }
            }
        }
    }

    // Actions pour déplacer un bloc d'un autre bloc vers une pile.
    private void moveBlockFromBlockToPile() {
        for (int b1 = 0; b1 < blockEnvironment.getNbBlocks(); b1++) {
            for (int b2 = 0; b2 < blockEnvironment.getNbBlocks(); b2++) {
                if (b1 == b2) continue;

                for (int p = 0; p < blockEnvironment.getNbPiles(); p++) {
                    Variable onBlock1 = blockEnvironment.getBwVariables().getOnbVarByIndex(b1);
                    Variable fixedBlock1 = blockEnvironment.getBwVariables().getFixedVarByIndex(b1);
                    Variable freePile = blockEnvironment.getBwVariables().getFreepVarByIndex(p);
                    Variable fixedBlock2 = blockEnvironment.getBwVariables().getFixedVarByIndex(b2);

                    Map<Variable, Object> precondition = createVariableMap(onBlock1, fixedBlock1, freePile, b2, false, true);
                    Map<Variable, Object> effect = createVariableMap(onBlock1, fixedBlock2, freePile, -p - 1, false, false);

                    availableActions.add(new BasicAction(precondition, effect, 1));
                }
            }
        }
    }

    // Actions pour déplacer un bloc d'une pile vers un autre bloc.
    private void moveBlockFromPileToBlock() {
        for (int b1 = 0; b1 < blockEnvironment.getNbBlocks(); b1++) {
            for (int b2 = 0; b2 < blockEnvironment.getNbBlocks(); b2++) {
                if (b1 == b2) continue;

                for (int p = 0; p < blockEnvironment.getNbPiles(); p++) {
                    Variable onBlock1 = blockEnvironment.getBwVariables().getOnbVarByIndex(b1);
                    Variable fixedBlock1 = blockEnvironment.getBwVariables().getFixedVarByIndex(b1);
                    Variable freePile = blockEnvironment.getBwVariables().getFreepVarByIndex(p);
                    Variable fixedBlock2 = blockEnvironment.getBwVariables().getFixedVarByIndex(b2);

                    Map<Variable, Object> precondition = createVariableMap(onBlock1, fixedBlock1, fixedBlock2, -p - 1, false, false);
                    Map<Variable, Object> effect = createVariableMap(onBlock1, freePile, fixedBlock2, b2, true, true);

                    availableActions.add(new BasicAction(precondition, effect, 1));
                }
            }
        }
    }

    // Actions pour déplacer un bloc d'une pile vers une autre pile.
    private void transferBlockBetweenPiles() {
        for (int b = 0; b < blockEnvironment.getNbBlocks(); b++) {
            for (int p1 = 0; p1 < blockEnvironment.getNbPiles(); p1++) {
                for (int p2 = 0; p2 < blockEnvironment.getNbPiles(); p2++) {
                    if (p1 == p2) continue;

                    Variable onBlock = blockEnvironment.getBwVariables().getOnbVarByIndex(b);
                    Variable fixedBlock = blockEnvironment.getBwVariables().getFixedVarByIndex(b);
                    Variable freePile2 = blockEnvironment.getBwVariables().getFreepVarByIndex(p2);
                    Variable freePile1 = blockEnvironment.getBwVariables().getFreepVarByIndex(p1);

                    Map<Variable, Object> precondition = createVariableMap(onBlock, fixedBlock, freePile2, -p1 - 1, false, true);
                    Map<Variable, Object> effect = createVariableMap(onBlock, freePile1, freePile2, -p2 - 1, true, false);

                    availableActions.add(new BasicAction(precondition, effect, 1));
                }
            }
        }
    }

    // Getters
    public BlocksWorld getBlocksWorld() {
        return blockEnvironment;
    }

    public Set<Action> getActions() {
        return availableActions;
    }
}
