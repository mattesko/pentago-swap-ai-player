package student_player.montecarlo;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;

import java.util.ArrayList;

public class MonteCarloTreeSearchOptimizer {
    private static final int WIN_SCORE = 10;
    private static final int MAX_SEARCH_TIME_MS = 1950;

    public MonteCarloTreeSearchOptimizer() {super();}

    public Move findNextMove(PentagoBoardState pentagoBoardState) {
        long endTime = System.currentTimeMillis() + MAX_SEARCH_TIME_MS;

        Node rootNode = new Node(pentagoBoardState, null);
        rootNode.getState().setPlayerNo(pentagoBoardState.getOpponent());

        while (System.currentTimeMillis() < endTime) {

            // Phase 1 - Selection
            Node promisingNode = selectPromisingNode(rootNode);

            // Phase 2 - Expansion
            if (!promisingNode.getState().getBoardState().gameOver())
                expandNode(promisingNode);

            // Phase 3 - Simulation
            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Phase 4 - Update
            backPropogation(nodeToExplore, playoutResult);
        }

        Node winnerNode = rootNode.getChildWithMaxScore();
        return winnerNode.getState().getPentagoMove();
    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(Node node) {
        ArrayList<State> possibleStates = node.getState().getAllPossibleStates();
        possibleStates.forEach(state -> {

            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayerNo(node.getState().getOpponent());

            ArrayList<Node> children = node.getChildren();
            children.add(newNode);

            node.setChildren(children);
        });
    }

    private void backPropogation(Node nodeToExplore, int playerNo) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getPlayerNo() == playerNo)
                tempNode.getState().addScore(WIN_SCORE);
            tempNode = tempNode.getParent();
        }
    }

    private int simulateRandomPlayout(Node node) {
        Node tempNode = new Node(node);
        State tempState = tempNode.getState();
        int winner = tempState.getBoardState().getWinner();

        if (winner == node.getState().getBoardState().getOpponent()) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return winner;
        }
        while (winner == Board.NOBODY) {
            tempState.togglePlayer();
            tempState.performRandomMove();
            winner = tempState.getBoardState().getWinner();
        }

        return winner;
    }
}
