package edu.isu.cs2235.traversals;

import edu.isu.cs2235.structures.Tree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree.BinaryTreeNode;

import java.util.ArrayList;

/**
 * @author Brandon Watkins
 */
public class BreadthFirstTraversal<E> extends AbstractTraversal<E> {

    public BreadthFirstTraversal(Tree t){ super(t); }

    /**
     * Recursively traverse from the given node.
     * @param node The node whose subtree you want to traverse.
     */
    @Override
    public void traverseFromRecursive(BinaryTreeNode node){
        this.waiting = new ArrayList<>();
        waiting.add(node);
        traverseWaitingRecursive();
    }

    /**
     * Recursively traverse the waiting list, the children of visited nodes.
     */
    public void traverseWaitingRecursive(){
        int waitSize = this.waiting.size();
        if (waitSize == 0) return;
        for (int i = 0; i < waitSize; i++) {
            BinaryTreeNode node = (BinaryTreeNode) waiting.remove(0);

            if (this.visitAction != null) this.visitAction.execute(this.tree, node);
            visited.add(node);

            if (node.getLeft() != null) waiting.add(node.getLeft());
            if (node.getRight() != null) waiting.add(node.getRight());
        }
        traverseWaitingRecursive();
    }
}
