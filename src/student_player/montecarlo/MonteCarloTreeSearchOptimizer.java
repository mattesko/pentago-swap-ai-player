package student_player.montecarlo;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;

import java.util.ArrayList;

public class MonteCarloTreeSearchOptimizer {
    private static final int WIN_SCORE = 10;
    private static final int MCTS_MAX_TIME = 100;
    private int level;

    public MonteCarloTreeSearchOptimizer() {
        this.level = 4;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    public Move findNextMove(PentagoBoardState pentagoBoardState) {
        long endTime = System.currentTimeMillis() + MCTS_MAX_TIME * getMillisForCurrentLevel();

        int opponent = pentagoBoardState.getOpponent();
        Node rootNode = new Node(pentagoBoardState, null);
        rootNode.getState().setPlayerNo(opponent);

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
        int boardStatus = tempState.getBoardState().getWinner();

        if (boardStatus == node.getState().getBoardState().getOpponent()) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return boardStatus;
        }
        while (boardStatus == Board.NOBODY) {
            tempState.togglePlayer();
            tempState.performRandomMove();
            boardStatus = tempState.getBoardState().getWinner();
        }

        return boardStatus;
    }
}
