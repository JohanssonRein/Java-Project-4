package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class PathFindingService {
    Tile source;
    Graph g;
    ArrayList<Tile> s;

    public PathFindingService(Tile start) {
        this.source = start;
    }

    public abstract void generateGraph();

    public ArrayList<Tile> findPath(Tile startNode) {
        s = new ArrayList<>();
        for (Tile vertex : g.getVertices()) {
            vertex.costEstimate = Double.POSITIVE_INFINITY;
            vertex.predecessor = null;
        }
        startNode.costEstimate = 0;
        TilePriorityQ q = new TilePriorityQ(g.getVertices());
        while (!q.isEmpty()) {
            Tile u = q.removeMin();
            s.add(u);
            for (Tile v : g.getNeighbors(u)) {
                double newValues = u.costEstimate + g.getEdgeWeight(u, v);
                if (newValues < v.costEstimate) {
                    v.costEstimate = newValues;
                    v.predecessor = u;
                    q.updateKeys(v, u, v.costEstimate);
                }
            }
        }
        ArrayList<Tile> pathFromEndToStart = new ArrayList<>();
        ArrayList<Tile> pathFromStartToEnd = new ArrayList<>();
        Tile end = null;
        
        for (int i = 0; i < g.getVertices().size(); i++) {
            if (g.getVertices().get(i).isDestination) {
                end = g.getVertices().get(i);
            }
        }
        
        int endIndex = s.indexOf(end);
        
        if (endIndex != -1) {
            Tile current = s.get(endIndex);
            while (current != null) {
                pathFromEndToStart.add(current);
                current = current.predecessor;
            }
        }
        for (int i = pathFromEndToStart.size() - 1; i >= 0; i--) {
            pathFromStartToEnd.add(pathFromEndToStart.get(i));
        }
        return pathFromStartToEnd;
    }

    public ArrayList<Tile> findPath(Tile start, Tile end) {
        findPath(start);
        ArrayList<Tile> pathFromEndToStart = new ArrayList<>();
        ArrayList<Tile> pathFromStartToEnd = new ArrayList<>();
        int endIndex = s.indexOf(end);
        if (endIndex != -1) {
            Tile current = s.get(endIndex);
            while (current != null) {
                pathFromEndToStart.add(current);
                current = current.predecessor;
            }
        }
        for (int i = pathFromEndToStart.size() - 1; i >= 0; i--) {
            pathFromStartToEnd.add(pathFromEndToStart.get(i));
        }
        return pathFromStartToEnd;
    }

    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
        
        Tile end = null;
        
        for (int i = 0; i < g.getVertices().size(); i++) {
            
            if (g.getVertices().get(i).isDestination) {
                end = g.getVertices().get(i);
            }
        }
        
        ArrayList<Tile> fullPath = new ArrayList<>();
        Tile currentWaypoint = start;
        Tile nextWaypoint;
        
        for (Tile waypoint : waypoints) {
            
            nextWaypoint = waypoint;
            fullPath.addAll(findPath(currentWaypoint, nextWaypoint));
            fullPath.remove(fullPath.size() - 1);
            currentWaypoint = nextWaypoint;
        }
        
        fullPath.addAll(findPath(currentWaypoint, end));
        return fullPath;
    }
}



