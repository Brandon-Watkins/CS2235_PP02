package edu.isu.cs2235.structures.impl;

import edu.isu.cs2235.structures.Node;

import java.util.ArrayList;

/**
 * @author Brandon Watkins
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E>{

    public class BinaryTreeNode<E> implements Node<E> {// only public because one of the tests tries calling this class

        BinaryTreeNode<E> parent;
        BinaryTreeNode<E> leftChild;
        BinaryTreeNode<E> rightChild;
        E element;

        public BinaryTreeNode(E element, BinaryTreeNode<E> parent) throws IllegalArgumentException {
            this(element, parent, (E) null, (E) null);
        }

        public BinaryTreeNode(E element, BinaryTreeNode<E> parent, BinaryTreeNode<E> leftChild, BinaryTreeNode<E> rightChild) throws IllegalArgumentException {
            if (element == null) throw new IllegalArgumentException("Can't create a node without a value.");
            this.parent = parent;
            this.element = element;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public BinaryTreeNode(E element, BinaryTreeNode<E> parent, E leftValue, E rightValue) throws IllegalArgumentException {
            if (element == null) throw new IllegalArgumentException("Can't create a node without a value.");
            this.parent = parent;
            this.element = element;
            if (leftValue != null) this.leftChild = new BinaryTreeNode<E>(leftValue, this);
            else this.leftChild = null;
            if (rightValue != null) this.rightChild = new BinaryTreeNode<E>(rightValue, this);
            else this.rightChild = null;
        }

        /**
         * Node Methods
         */

        @Override
        public E getElement() {
            return this.element;
        }

        @Override
        public void setElement(Object element) throws IllegalArgumentException {
            if (element == null)
                throw new IllegalArgumentException("Attempted to store a null element in a BinaryTreeNode.");
            this.element = (E) element;
        }

        @Override
        public BinaryTreeNode<E> getParent() {
            return this.parent;
        }

        /** BinaryTreeNode Methods */

        public BinaryTreeNode<E> getLeft(){
            return this.leftChild;
        }

        public BinaryTreeNode<E> getRight(){
            return this.rightChild;
        }

        /**
         * Set all BinaryTreeNode values to null, enabling deletion by garbage collector.
         * Use only on nodes with no (or already deleted) children.
         * @return True as long as the node's element isn't already null, which shouldn't be possible.
         */
        public boolean delete() {
            if (this.element == null) return false;
            if (this.getParent().leftChild == this) this.getParent().leftChild = null;
            else if (this.getParent().rightChild == this) this.getParent().rightChild = null;
            this.parent = null;
            this.element = null;
            try {
                this.finalize();
            } catch (Throwable e) {
                return true;
            }
            return true;
        }
    }

    public BinaryTreeNode<E> createNode(E element, BinaryTreeNode<E> parent, E leftChild, E rightChild){
        BinaryTreeNode<E> node = new BinaryTreeNode<E>(element, parent, leftChild, rightChild);
        return node;
    }

    /** Binary Tree Methods */

    @Override
    public BinaryTreeNode<E> left(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> node = this.validate(p);
        return node.getLeft();
    }

    @Override
    public BinaryTreeNode<E> right(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> node = this.validate(p);
        return node.getRight();
    }

    @Override
    public BinaryTreeNode<E> sibling(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> parent = this.validate(p).getParent();
        if (parent.getLeft() == p) return parent.getRight();
        else return parent.getLeft();
    }

    @Override
    public BinaryTreeNode<E> addLeft(Node p, Object element) throws IllegalArgumentException {
        BinaryTreeNode<E> parent = this.validate(p);
        if (parent.getLeft() != null) throw new IllegalArgumentException("Parent already has a left child.");
        BinaryTreeNode<E> node = validBinaryTreeNode(element);
        if (node == null){
            E n = validValue((E) element);
            node = new BinaryTreeNode<E>(n, parent);
        }
        parent.leftChild = node;
        this.size += this.subTreeSize(node);
        return node;
    }

    @Override
    public BinaryTreeNode<E> addRight(Node p, Object element) throws IllegalArgumentException {
        BinaryTreeNode<E> parent = this.validate(p);
        if (parent.getRight() != null) throw new IllegalArgumentException("Parent already has a right child.");
        BinaryTreeNode<E> node = validBinaryTreeNode(element);
        if (node == null){
            E n = validValue((E) element);
            node = new BinaryTreeNode<E>(n, parent);
        }
        parent.rightChild = node;
        this.size += this.subTreeSize(node);
        return node;
    }

    /** Tree Methods */

    /** unsure if i'm supposed to add the old root's children to the new root. Seems it'd be pointless, so I'm just
     * going to make it the root of the tree, without any children, leaving my old tree floating around in memory 
     * @param element The value to set the root to.
     */
    @Override
    public BinaryTreeNode<E> setRoot(Object element) {
        if(element == null){
            this.size = 0;
            this.root = null;
            return null;
        }
        BinaryTreeNode<E> node = validBinaryTreeNode(element);
        if (node == null && (this.root() == null || element != this.root().getElement())) {
            node = new BinaryTreeNode<E>((E) element, null);
            this.root = node;
            this.size = 1;
        }
        else {
            if (this.root() == node) return node;
            this.root = node;
            this.size = this.subTreeSize(this.root());
        }
        return node;
    }

    @Override
    public Iterable<BinaryTreeNode<E>> children(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> parent = this.validate(p);
        ArrayList<BinaryTreeNode<E>> children = new ArrayList<>();
        if (parent.getLeft() != null) children.add(parent.getLeft());
        if (parent.getRight() != null) children.add(parent.getRight());
        return children;
    };

    @Override
    public int numChildren(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> parent = this.validate(p);
        int children = 0;
        if (parent.getLeft() != null) children++;
        if (parent.getRight() != null) children++;
        return children;
    }

    @Override
    public boolean isInternal(Node p) throws IllegalArgumentException {
        return this.numChildren(p) > 0;
    }

    @Override
    public boolean isExternal(Node p) throws IllegalArgumentException {
        return this.numChildren(p) == 0;
    }

    @Override
    public BinaryTreeNode<E> insert(Object element, Node p) {
        if (element == null) throw new IllegalArgumentException("Can't insert a null value.");
        if (p == null){
            if (this.root() != null && this.root().getElement() != null) throw new IllegalArgumentException("Root node already has a value.");
            BinaryTreeNode<E> node = new BinaryTreeNode<E>((E)element, null);
            this.setRoot(node);
            return node;
        }
        else {
            BinaryTreeNode<E> k = this.validate(p);
            if (k.getLeft() != null){
                if (k.getRight() != null){
                    throw new IllegalArgumentException("Parent node already has two child nodes.");
                }
                else{
                    this.addRight(k, element);
                    return k.getRight();
                }
            }
            else{
                this.addLeft(k, element);
                return k.getLeft();
            }
        }
    }

    @Override
    public boolean remove(Object item, Node p) throws IllegalArgumentException {
        if (p == null) return false;
        BinaryTreeNode<E> parent = this.validate(p);
        if (item == null) return false;
        if (parent == null || (parent.getParent() == null & this.root() != parent)) return false;
        BinaryTreeNode<E> c = parent.getLeft();
        if (c != null && c.getElement() == item){
            return removeNodeAndAncestors(c);
        }
        else{
            BinaryTreeNode<E> d = parent.getRight();
            if (d != null && d.getElement() == item)
                return removeNodeAndAncestors(d);
        }
        return false; //no children of p had an element equal to item (or p had no children).
    }

    public boolean removeNodeAndAncestors(BinaryTreeNode<E> parent) {

        if (parent.getLeft() != null) removeNodeAndAncestors(parent.getLeft());
        if (parent.getRight() != null) removeNodeAndAncestors(parent.getRight());
        if (this.numChildren(parent) == 0) {
            this.size--;
            return parent.delete();
        }
        return false;
    }

    @Override
    public E set(Node node, Object element) throws IllegalArgumentException {
        BinaryTreeNode<E> n = this.validate(node);
        E k = validValue((E)element);
        E temp = n.getElement();
        n.setElement(k);
        return temp;
    }

    @Override
    public BinaryTreeNode<E> validate(Node p) throws IllegalArgumentException {
        if (p == null) throw new IllegalArgumentException("Given node was null.");
        if (p.getElement() == null) throw new IllegalArgumentException("Given node had a null element.");
        Class<? extends Object> ele = p.getClass();
        if (ele.getName().compareTo("edu.isu.cs2235.structures.impl.LinkedBinaryTree$BinaryTreeNode") != 0) throw new
                IllegalArgumentException("Given node was of type " + ele +
                ", not the required edu.isu.cs2235.structures.impl.LinkedBinaryTree$BinaryTreeNode.");
        BinaryTreeNode<E> node = validBinaryTreeNode(p);
        if (this.root() == null || (this.root() != node && node.getParent() == null)) throw new
                IllegalArgumentException("Given node was not in the tree.");
        Class<? extends Object> rt = this.root().getClass();
        if (ele != rt) throw new IllegalArgumentException(("Given node was of type " + ele + ", not the required " + rt + "."));
        ele = p.getElement().getClass();
        rt = this.root().getElement().getClass();
        if (ele != rt) throw new IllegalArgumentException("Given node's element was of type " + ele + ", not the required " + rt + ".");
        while (node.getParent() != null) node = node.getParent();
        if (node == this.root()) return (BinaryTreeNode<E>) p;
        throw new IllegalArgumentException("Given node was not a part of the tree.");
    }

    public BinaryTreeNode<E> validBinaryTreeNode(Object p){
        if (p == null) throw new IllegalArgumentException("Given element is null.");
        String n = p.getClass().getName();
        if (n.compareTo("edu.isu.cs2235.structures.impl.LinkedBinaryTree$BinaryTreeNode") == 0) {
            BinaryTreeNode<E> node = (BinaryTreeNode) p;
            if (this.root() == null) return node;
            Class<? extends Object> nodeElementType = node.getElement().getClass();
            Class<? extends Object> rootElementType = this.root().getElement().getClass();
            if (nodeElementType != rootElementType)
                throw new IllegalArgumentException(("Given node's element was of type " +
                        nodeElementType + ", not the required " + rootElementType + "."));
            return node;
        }
        return null;
    }

    public E validValue(E element){
        if (element == null) throw new IllegalArgumentException("Given element is null.");
        if (this.root() == null) return element;
        Class<? extends Object> ele = element.getClass();
        Class<? extends Object> rt = this.root().getElement().getClass();
        if (ele != rt) throw new IllegalArgumentException(("Given node's element was of type " + ele + ", not the required " + rt + "."));
        return element;
    }

    @Override
    public int depth(Node p) throws IllegalArgumentException {
        BinaryTreeNode<E> node = this.validate(p);
        int i;
        for (i = 0; node.getParent() != null; i++) node = node.getParent();
        return i;
    }

    @Override
    public int subTreeSize(Node node) throws IllegalArgumentException {
        BinaryTreeNode<E> n = this.validate(node);
        if (this.isExternal((n))) return 1;
        else {
            if (n.getLeft() != null && n.getRight() == null) return subTreeSize(n.getLeft()) + 1;
            else if (n.getLeft() == null && n.getRight() != null) return subTreeSize(n.getRight()) + 1;
            else return subTreeSize(n.getLeft()) + subTreeSize(n.getRight()) + 1;
        }
    }

    @Override
    public boolean isLastChild(Node node) throws IllegalArgumentException {
        BinaryTreeNode<E> n = this.validate(node);
        if (n.getParent() == null) return true;
        return this.sibling(n) == n.getParent().getLeft() || this.sibling(n) == null;
    }


}
