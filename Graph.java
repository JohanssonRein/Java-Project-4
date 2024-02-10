package finalproject;

import java.util.ArrayList;
import java.util.HashSet;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
    private ArrayList<Tile> vertices;
    private ArrayList<Edge> edges;

    public Graph(ArrayList<Tile> vertices) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>();
    }

    public void fillTimeCost() {
        for (Tile tile : vertices) {
            for (Tile neighbor : getNeighbors(tile)) {
                if (tile.type.equals(TileType.Metro) && neighbor.type.equals(TileType.Metro)) {
                    ((MetroTile) neighbor).fixMetro(tile);
                    addEdge(tile, neighbor, ((MetroTile) neighbor).metroTimeCost);
                } else {
                    addEdge(tile, neighbor, neighbor.timeCost);
                }
            }
        }
    }

    public void fillDistanceCost() {
        for (Tile tile : vertices) {
            for (Tile neighbor : getNeighbors(tile)) {
                if (tile.type.equals(TileType.Metro) && neighbor.type.equals(TileType.Metro)) {
                    ((MetroTile) neighbor).fixMetro(tile);
                    addEdge(tile, neighbor, ((MetroTile) neighbor).metroDistanceCost);
                } else {
                    addEdge(tile, neighbor, neighbor.distanceCost);
                }
            }
        }
    }

    public void addEdge(Tile origin, Tile destination, double weight) {
        edges.add(new Edge(origin, destination, weight));
    }

    public ArrayList<Edge> getAllEdges() {
        return edges;
    }

    public ArrayList<Tile> getVertices() {
        return new ArrayList<>(vertices);
    }

    public ArrayList<Tile> getNeighbors(Tile t) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (int i = 0; i < t.neighbors.size(); i++) {
            if (t.neighbors.get(i).isWalkable()) {
                neighbors.add(t.neighbors.get(i));
             }
        }
        for (Edge edge : edges) { //new
            if (edge.getStart().equals(t)) {
                if (!neighbors.contains(edge.getEnd())) {
                     neighbors.add(edge.getEnd());
                }
            }
        }
        return neighbors;
}

    public double computePathCost(ArrayList<Tile> path) {
        double totalWeight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Tile current = path.get(i);
            Tile next = path.get(i + 1);
            totalWeight += getEdgeWeight(current, next);
        }
        return totalWeight;
    }

    public double getEdgeWeight(Tile origin, Tile destination) {
        for (Edge edge : edges) {
            if (edge.origin.equals(origin) && edge.destination.equals(destination)) {
                return edge.weight;
            }
        }
        return 0;
    }


    public static class Edge {
        Tile origin;
        Tile destination;
        double weight;

        public Edge(Tile s, Tile d, double cost) {
            this.origin = s;
            this.destination = d;
            this.weight = cost;
        }

        public Tile getStart() {
            return origin;
        }

        public Tile getEnd() {
            return destination;
        }
    }
}
