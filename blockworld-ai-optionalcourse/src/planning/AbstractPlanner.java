package planning;


public abstract class AbstractPlanner implements Planner {
    protected boolean nodeCountActive = false; // Indicateur pour activer/désactiver le comptage
    protected int nodeCount = 0; // Compteur de noeuds explorés

    @Override
    public void activateNodeCount(boolean activate) {
        this.nodeCountActive = activate;
        this.nodeCount = 0; // Réinitialiser le compteur
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    protected void incrementNodeCount() {
        if (nodeCountActive) {
            nodeCount++;
        }
    }
}
