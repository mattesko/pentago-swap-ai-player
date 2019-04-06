package student_player.montecarlo;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Node {
    State state;
    Node parent;
    ArrayList<Node> children;

    public Node(PentagoBoardState pentagoBoardState, PentagoMove move) {
        this.state = new State(pentagoBoardState, move);
        this.children = new ArrayList<>();
    }

    public Node(State state) {
        this.state = state;
        this.children = new ArrayList<>();
    }

    public Node(State state, Node parent, ArrayList<Node> children) {
        this.state = state;
        this.parent = parent;
        this.children = children;
    }

    public Node(Node node) {
        this.children = new ArrayList<>();
        this.state = new State(node.getState());
        if (node.getParent() != null)
            this.parent = node.getParent();
        ArrayList<Node> childArray = node.getChildren();
        for (Node child : childArray) {
            this.children.add(new Node(child));
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public Node getRandomChildNode() {
        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.children.get(selectRandom);
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.children, Comparator.comparing(c -> c.getState().getVisitCount()));
    }
}
