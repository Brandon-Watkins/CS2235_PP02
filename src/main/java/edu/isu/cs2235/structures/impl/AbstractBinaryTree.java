package edu.isu.cs2235.structures.impl;

import edu.isu.cs2235.structures.BinaryTree;
import edu.isu.cs2235.structures.Node;

/**
 * @author Brandon Watkins
 */
public abstract class AbstractBinaryTree<E> implements BinaryTree {

    Node<E> root;
    int size = 0;

    @Override
    public Node<E> root() {
        if (this.root == null) return null;
        return this.root;
    }

    @Override
    public Node<E> parent(Node p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getParent();
    }

    @Override
    public boolean isRoot(Node p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        if (this.root() == node && node.getParent() == null) return true;
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
