package finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;

public class SafestShortestPath extends ShortestPath {
    public int health;
    public Graph costGraph;
    public Graph damageGraph;
    public Graph aggregatedGraph;

    public SafestShortestPath(Tile start, int health) {
        super(start);
        this.health = health;
        generateGraph();
    }


    public void generateGraph() {
        ArrayList<Tile> tiles = GraphTraversal.DFS(source);
        costGraph = new Graph(tiles);
        damageGraph = new Graph(tiles);
        aggregatedGraph = new Graph(tiles);
        g = costGraph;
        for (Tile tile : tiles) {
            for (Tile neighbor : aggregatedGraph.getNeighbors(tile)) {
                if (neighbor.isWalkable()) {
                    costGraph.addEdge(tile, neighbor, neighbor.distanceCost);
                    damageGraph.addEdge(tile, neighbor, neighbor.damageCost);
                    aggregatedGraph.addEdge(tile, neighbor, neighbor.damageCost);
                 }
            }
        }
}

    @Override
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
        g = costGraph;
        ArrayList<Tile> pc = super.findPath(start, waypoints);
        double totalDamageCostPC = damageGraph.computePathCost(pc);
        if (totalDamageCostPC < health) {
            return pc;
        }
        g = damageGraph;
        ArrayList<Tile> pd = super.findPath(start, waypoints);
        double totalDamageCostPD = damageGraph.computePathCost(pd);
        if (totalDamageCostPD > health) {
            return null;
        }
        while (true) {
            double lambda = calculateLambda(pc, pd);
            for (int i = 0; i < aggregatedGraph.getAllEdges().size(); i++) {
                Graph.Edge edge = aggregatedGraph.getAllEdges().get(i);
                double distanceCost = costGraph.getEdgeWeight(edge.getStart(), edge.getEnd());
                double damageCost = damageGraph.getEdgeWeight(edge.getStart(), edge.getEnd());
                edge.weight = distanceCost + lambda * damageCost;
            }
            g = aggregatedGraph;
            ArrayList<Tile> pr = super.findPath(start, waypoints);
            if (aggregatedGraph.computePathCost(pr) == aggregatedGraph.computePathCost(pc)) {
                return pd;
            } else if (damageGraph.computePathCost(pr) <= health) {
                pd = pr;
            } else {
                pc = pr;
            }
        }
    }

    private double calculateLambda(ArrayList<Tile> pc, ArrayList<Tile> pd) {
        double costPC = costGraph.computePathCost(pc);
        double costPD = costGraph.computePathCost(pd);
        double damagePC = damageGraph.computePathCost(pc);
        double damagePD = damageGraph.computePathCost(pd);
        return (costPC - costPD) / (damagePD - damagePC);
    }
}

