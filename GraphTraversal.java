package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal {
    public static ArrayList<Tile> BFS(Tile s) {
        ArrayList<Tile> visited = new ArrayList<>();
        LinkedList<Tile> queue = new LinkedList<>();
        HashSet<Tile> set = new HashSet<>();

        queue.add(s);
        set.add(s);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            visited.add(current);

            for (Tile neighbor : current.neighbors) {
                if (!set.contains(neighbor) && neighbor.isWalkable()) {
                    queue.add(neighbor);
                    set.add(neighbor);
                }
            }
        }
        return visited;
    }

    public static ArrayList<Tile> DFS(Tile s) {
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> set = new HashSet<>();
        DFS(s, visited, set);
        return visited;
    }

    private static void DFS(Tile current, ArrayList<Tile> visited, HashSet<Tile> set) {
        visited.add(current);
        set.add(current);

        for (Tile neighbor : current.neighbors) {
            if (!set.contains(neighbor) && neighbor.isWalkable()) {
                DFS(neighbor, visited, set);
            }
        }
    }
}

