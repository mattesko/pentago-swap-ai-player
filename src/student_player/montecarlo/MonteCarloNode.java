package student_player.montecarlo;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MonteCarloNode {
    private MonteCarloState state;
    private MonteCarloNode parent;
    private ArrayList<MonteCarloNode> children;

    MonteCarloNode(PentagoBoardState pentagoBoardState, PentagoMove move) {
        this.state = new MonteCarloState(pentagoBoardState, move);
        this.children = new ArrayList<>();
    }

    MonteCarloNode(MonteCarloState state) {
        this.state = state;
        this.children = new ArrayList<>();
    }

    MonteCarloNode(MonteCarloState state, MonteCarloNode parent, ArrayList<MonteCarloNode> children) {
        this.state = state;
        this.parent = parent;
        this.children = children;
    }

    MonteCarloNode(MonteCarloNode node) {
        this.children = new ArrayList<>();
        this.state = new MonteCarloState(node.getState());
        if (node.getParent() != null)
            this.parent = node.getParent();
        ArrayList<MonteCarloNode> childArray = node.getChildren();
        for (MonteCarloNode child : childArray) {
            this.children.add(new MonteCarloNode(child));
        }
    }

    MonteCarloNode getRandomChildNode() {
        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.children.get(selectRandom);
    }

    MonteCarloNode getChildWithMaxScore() {
        return Collections.max(this.children, Comparator.comparing(c -> c.getState().getVisitCount()));
    }

    MonteCarloState getState() {
        return state;
    }

    void setState(MonteCarloState state) {
        this.state = state;
    }

    MonteCarloNode getParent() {
        return parent;
    }

    void setParent(MonteCarloNode parent) {
        this.parent = parent;
    }

    ArrayList<MonteCarloNode> getChildren() {
        return children;
    }

    void setChildren(ArrayList<MonteCarloNode> children) {
        this.children = children;
    }

}
