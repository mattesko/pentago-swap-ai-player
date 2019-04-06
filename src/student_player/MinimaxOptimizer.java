package student_player;

import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.ArrayList;
import java.util.Arrays;

public class MinimaxOptimizer {

    PentagoBoardStateNode rootNode;

    public MinimaxOptimizer(PentagoBoardState pentagoBoardState) {
        this.rootNode = new PentagoBoardStateTreeFactory().generateTree(pentagoBoardState, 1);
    }

    public Move getNextBestMove(int depth) {

        for (int i = 0; i < depth; i++) {

        }

        // TODO return best move
        return null;
    }

    private int getScore(PentagoBoardState pentagoBoardState) {

        if (pentagoBoardState.gameOver()) {
            int winner = pentagoBoardState.getWinner();
            if (winner == Board.DRAW) {
                return 0;
            }
            else if (winner == pentagoBoardState.getOpponent()) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else {
            return 0;
        }
    }

    private boolean winExists(PentagoBoardState pentagoBoardState) {



        return false;
    }


    private class PentagoBoardStateTreeFactory {

        private PentagoBoardStateTreeFactory() {super();}

        private PentagoBoardStateNode generateTree(PentagoBoardState pentagoBoardState, int depth) {

            PentagoBoardStateNode rootNode = new PentagoBoardStateNode(pentagoBoardState);
            ArrayList<PentagoBoardStateNode> currentNodes = new ArrayList<>(Arrays.asList(rootNode));

            for(int i = 0; i < depth; i++) {
                ArrayList<PentagoBoardStateNode> children = new ArrayList<>();

                for (PentagoBoardStateNode node : currentNodes) {

                    for (PentagoMove move : pentagoBoardState.getAllLegalMoves()) {
                        PentagoBoardState child = (PentagoBoardState) pentagoBoardState.clone();
                        child.processMove(move);
                        children.add(new PentagoBoardStateNode(child));
                    }

                    node.setChildrenNodes(children);
                }

                currentNodes = children;
            }

            return rootNode;
        }
    }

    private class PentagoBoardStateNode {

        private PentagoBoardState pentagoBoardState;
        private ArrayList<PentagoBoardStateNode> childrenNodes;

        private PentagoBoardStateNode(PentagoBoardState pentagoBoardState) {

            this.pentagoBoardState = pentagoBoardState;
            this.childrenNodes = null;
        }

        private PentagoBoardStateNode(PentagoBoardState pentagoBoardState, ArrayList<PentagoBoardStateNode> childrenNodes) {
            this.pentagoBoardState = pentagoBoardState;
            this.childrenNodes = childrenNodes;
        }

        private void setChildrenNodes(ArrayList<PentagoBoardStateNode> childrenNodes) {
            this.childrenNodes = childrenNodes;
        }

        private void setPentagoBoardState(PentagoBoardState pentagoBoardState) {
            this.pentagoBoardState = pentagoBoardState;
        }

        private PentagoBoardState getPentagoBoardState() {
            return this.pentagoBoardState;
        }

        private ArrayList<PentagoBoardStateNode> getChildrenNodes() {
            return this.childrenNodes;
        }

    }
}

