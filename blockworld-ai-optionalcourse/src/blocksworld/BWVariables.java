package blocksworld;

import java.util.*;
import modelling.Variable;


public class BWVariables {
    private int nbBlocks;
    private int nbPiles;
    private Set<Variable> onbVariables = new HashSet<>();
    private Set<Variable> fixedVariables = new HashSet<>();
    private Set<Variable> freeVariables = new HashSet<>();
    private Map<String, Variable> mapVariable = new HashMap<>();
    private Set<Variable> variables = new HashSet<>();

    public BWVariables(int nbBlocks, int nbPiles) {
        this.nbBlocks = nbBlocks;
        this.nbPiles = nbPiles;
        variables.addAll(onbVariables);
        variables.addAll(fixedVariables);
        variables.addAll(freeVariables);
        initVariables();
    }

    private void initVariables() {
        createOnbVariables();
        createFixedVariables();
        createFreeVariables();
    }


    // Création des variables onB pour les positions des blocs
    private void createOnbVariables() {
        Set<Object> domain = getDomain(nbBlocks, nbPiles);
        for (int i = 0; i < nbBlocks; i++) {
            Set<Object> domainI = new HashSet<>(domain);
            domainI.remove(i);
            addVariable("on_" + i, domainI, onbVariables);
        }
    }

    private Set<Object> getDomain(int nbBlocks, int nbPiles) {
        Set<Object> domain = new HashSet<>();
        for (int i = -nbPiles; i < nbBlocks; i++) {
            domain.add(i);
        }
        return domain;
    }

    private void addVariable(String name, Set<Object> domain, Set<Variable> variablesSet) {
        Variable var = new Variable(name, domain);
        variablesSet.add(var);
        mapVariable.put(name, var);
    }

    // Création des variables fixedB pour indiquer si un bloc est fixé
    private void createFixedVariables() {
        for (int i = 0; i < nbBlocks; i++) {
            addVariable("fixed_" + i, Set.of(true, false), fixedVariables);
        }
    }

    // Création des variables freeP pour indiquer si une pile est libre
    private void createFreeVariables() {
        for (int i = 0; i < nbPiles; i++) {
            addVariable("free_" + i, Set.of(true, false), freeVariables);
        }
    }

    public Map<Variable, Object> getState(int[][] state) {
        Map<Variable, Object> res = new HashMap<>();
        int nbPiles = state.length;
        for (int i = 0; i < nbPiles; i++) {
          int nbBlocks = state[i].length;
          String str = "free_" + Integer.toString(i);
          Variable free = mapVariable.get(str);
          if (nbBlocks == 0) {
            res.put(free, true);
          } else {
            res.put(free, false);
          }
          for (int j = 0; j < nbBlocks; j++) {
            String strOn = "on_" + Integer.toString(state[i][j]);
            String strfix = "fixed_" + Integer.toString(state[i][j]);
            Variable on = mapVariable.get(strOn);
            Variable fixed = mapVariable.get(strfix);
            if (j == 0) {
              res.put(on, -i - 1);
              res.put(fixed, true);
            }
            if (nbBlocks == 1) {
              res.put(on, -i - 1);
              res.put(fixed, false);
            }
            if (j > 0 && j == nbBlocks - 1) {
              res.put(on, state[i][j - 1]);
              res.put(fixed, false);
            }
            if (j > 0 && j < nbBlocks - 1) {
              res.put(on, state[i][j - 1]);
              res.put(fixed, true);
            }
    
          }
        }
        return res;
    }

    // Méthodes pour obtenir des variables par nom et index
    public static int getIndex(Variable var) {
        return Integer.parseInt(var.getName().replaceAll("[^0-9]", ""));
    }

    public Variable getOnbVarByIndex(int index) {
        return mapVariable.get("on_" + index);
    }

    public Variable getFixedVarByIndex(int index) {
        return mapVariable.get("fixed_" + index);
    }

    public Variable getFreepVarByIndex(int index) {
        return mapVariable.get("free_" + index);
    }

    public Set<Variable> getOnbVariables() { return onbVariables; }
    public Set<Variable> getFixedbVariables() { return fixedVariables; }
    public Set<Variable> getFreepVariables() { return freeVariables; }
    public Variable getVarByName(String str) { return mapVariable.get(str); }
    public Set<Variable> getVariables() { return variables;}
}