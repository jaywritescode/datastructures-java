package datastructures;

import java.util.HashMap;

public class Trie {

    Node root;

    public Trie() {
        root = new Node();
    }

    public boolean add(String string) {
        var currentNode = root;
        for (char c : string.toCharArray()) {
            currentNode = currentNode.getChild(c);
        }

        if (currentNode.isTerminus) {
            return false;
        }
        currentNode.isTerminus = true;
        return true;
    }

    public boolean contains(String string) {
        var currentNode = root;
        for (char c : string.toCharArray()) {
            if (!currentNode.hasChild(c)) {
                return false;
            }
            currentNode = currentNode.getChild(c);
        }
        return currentNode.isTerminus;
    }

    class Node {
        HashMap<Character, Node> children;
        boolean isTerminus = false;

        Node() {
            children = new HashMap<>();
        }

        Node getChild(char c) {
            children.putIfAbsent(c, new Node());
            return children.get(c);
        }

        boolean hasChild(char c) {
            return children.containsKey(c);
        }
    }
}
