package edu.isu.cs2235.traversals;

import edu.isu.cs2235.structures.Tree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree.BinaryTreeNode;

/**
 * @author Brandon Watkins
 */
public class InOrderTraversal<E> extends DepthFirstTraversal<E> {

    public InOrderTraversal(Tree t){ super(t); }

    /**
     * Recursively traverse from the given node.
     * @param node The node whose subtree you want to traverse.
     */
    @Override
    public void traverseFromRecursive(BinaryTreeNode node) {
        if (node.getLeft() != null) traverseFromRecursive(node.getLeft());

        if (this.visitAction != null) this.visitAction.execute(this.tree, node);
        visited.add(node);

        if (node.getRight() != null) traverseFromRecursive(node.getRight());
    }

}
