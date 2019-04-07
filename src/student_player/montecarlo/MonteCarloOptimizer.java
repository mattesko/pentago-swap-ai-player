package student_player.montecarlo;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;

import java.util.ArrayList;

public class MonteCarloOptimizer {
    private static final int WIN_SCORE = 100;
    private static final int MAX_SEARCH_TIME_MS = 1950;
    private int opponent;

    public MonteCarloOptimizer() {super();}

    public Move findNextMove(PentagoBoardState pentagoBoardState) {
        long endTime = System.currentTimeMillis() + MAX_SEARCH_TIME_MS;

        MonteCarloNode rootNode = new MonteCarloNode(pentagoBoardState, null);
        rootNode.getState().setPlayerNo(pentagoBoardState.getOpponent());
        this.opponent = pentagoBoardState.getOpponent();

        while (System.currentTimeMillis() < endTime) {

            // Phase 1 - Selection
            MonteCarloNode promisingNode = selectPromisingNode(rootNode);

            // Phase 2 - Expansion
            if (promisingNode.getState().getBoardState().getWinner() == Board.NOBODY) {
                expandNode(promisingNode);
            }

            // Phase 3 - Simulation
            MonteCarloNode nodeToExplore = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Phase 4 - Update
            backPropagation(nodeToExplore, playoutResult);
        }

        MonteCarloNode winnerNode = rootNode.getChildWithMaxScore();
        return winnerNode.getState().getPentagoMove();
    }

    private MonteCarloNode selectPromisingNode(MonteCarloNode rootNode) {
        MonteCarloNode node = rootNode;
        while (node.getChildren().size() != 0) {
            node = MonteCarloUCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(MonteCarloNode node) {
        ArrayList<MonteCarloState> possibleStates = node.getState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            MonteCarloNode newNode = new MonteCarloNode(state);
            newNode.setParent(node);
            newNode.getState().setPlayerNo(node.getState().getOpponent());

            ArrayList<MonteCarloNode> children = node.getChildren();
            children.add(newNode);

            node.setChildren(children);
        });
    }

    private int simulateRandomPlayout(MonteCarloNode node) {
        MonteCarloNode tempNode = new MonteCarloNode(node);
        MonteCarloState tempState = tempNode.getState();
        int winner = tempState.getBoardState().getWinner();

        while (winner == Board.NOBODY) {
            tempState.togglePlayer();
            tempState.performRandomMove();
            winner = tempState.getBoardState().getWinner();
        }

        if (winner == this.opponent) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
        }
        else {
            tempNode.getParent().getState().setWinScore(Integer.MAX_VALUE);
        }

        return winner;
    }

    private void backPropagation(MonteCarloNode nodeToExplore, int playerNo) {
        MonteCarloNode tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getPlayerNo() == playerNo)
                tempNode.getState().addScore(WIN_SCORE);
            tempNode = tempNode.getParent();
        }
    }
}
