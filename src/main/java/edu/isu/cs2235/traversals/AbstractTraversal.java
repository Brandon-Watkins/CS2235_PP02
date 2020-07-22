package edu.isu.cs2235.traversals;

import edu.isu.cs2235.structures.Node;
import edu.isu.cs2235.structures.Tree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree.BinaryTreeNode;
import edu.isu.cs2235.traversals.commands.TraversalCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTraversal<E> implements TreeTraversal<E>{

    Tree tree;
    ArrayList<Node> waiting;
    ArrayList<Node> visited;
    TraversalCommand visitAction;

    public AbstractTraversal(Tree tree){
        if (tree == null) throw new IllegalArgumentException(("No tree was given for traversal."));
        this.tree = tree;
        this.visitAction = null;
    }

    /**
     * Method which initiates the traversal of a tree from the root node. This
     * method returns the an iterable container of nodes representing a
     * resulting traversal of the tree.
     *
     * @return An iterable container of nodes representing the traversal of a
     * tree.
     */
    public Iterable<Node<E>> traverse() { return traverseFrom(this.tree.root()); }

    /**
     * Method which initiates the traversal of a tree from the root node. This
     * method returns the an iterable container of nodes representing a
     * resulting traversal of the tree.
     *
     * @param node Root of the subtree to start the traversal at.
     * @return An iterable container of nodes representing the traversal of a
     * tree.
     */
    public Iterable<Node<E>> traverseFrom(Node<E> node) {
        if (this.tree.root() == null) return (Iterable)(new ArrayList<Node>());
        if (this.tree == null || this.tree.validate(node) == null) throw new IllegalArgumentException("Given node is not in the tree.");
        this.waiting = new ArrayList<>();
        this.visited = new ArrayList<>();
        traverseFromRecursive((BinaryTreeNode)node);
        return (Iterable)this.visited;
    }

    /**
     * Recursively traverse from the given node.
     * @param node The node whose subtree you want to traverse.
     */
    public void traverseFromRecursive(BinaryTreeNode node) { }

    /**
     * Traverses from the given node, adding the visited nodes to a given list.
     * @param node The node whose subtree you want to traverse.
     * @param list The list you want the visited nodes added to.
     */
    public void subtree(Node node, List list){
        if (list == null) throw new IllegalArgumentException("No list given for subtree.");
        if (node == null || this.tree.validate(node) == null) throw new IllegalArgumentException("Invalid node given for subtree.");
        Iterable<Node> newList = traverseFrom(node);
        for(Node n : newList){
            list.add(n);
        }
    }

    /**
     * Traverses from the given node, returning a list of the visited nodes.
     * @param node The node whose subtree you want to traverse.
     * @return An iterable list of nodes contained in the subtree.
     */
    public Iterable<Node<E>> subTreeTraverse(Node node){
        if (node == null || this.tree.validate(node) == null) throw new IllegalArgumentException("Invalid node given for subtree.");
        ArrayList<Node> list = new ArrayList<>();
        subtree(node, list);
        return (Iterable)list;
    }

    /**
     * Sets the executable command to the provided value.
     *
     * @param cmd The new executable command
     */
    public void setCommand(TraversalCommand cmd){
        this.visitAction = cmd;
    }

}
