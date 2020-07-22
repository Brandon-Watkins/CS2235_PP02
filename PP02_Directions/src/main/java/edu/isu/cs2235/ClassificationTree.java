package edu.isu.cs2235;

import edu.isu.cs2235.structures.BinaryTree;
import edu.isu.cs2235.structures.Node;
import edu.isu.cs2235.structures.impl.BinarySearchTree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree;
import edu.isu.cs2235.traversals.BreadthFirstTraversal;
import edu.isu.cs2235.traversals.InOrderTraversal;
import edu.isu.cs2235.traversals.PreOrderTraversal;
import edu.isu.cs2235.traversals.TreeTraversal;
import edu.isu.cs2235.traversals.commands.EnumeratedSaveCommand;
import edu.isu.cs2235.traversals.commands.EnumerationCommand;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * A very simple classification tree example using a BinaryTree and console
 * input.
 *
 * @author Isaac Griffith
 */
public class ClassificationTree {

    private BinaryTree<Datum> tree;

    /**
     * Constructs a new Animal tree class which manages an underlying animal
     * tree
     */
    public ClassificationTree() {
        tree = new LinkedBinaryTree<>();
        load();
    }

    /**
     * Main method which controls the identification and tree management loop.
     */
    public void identify() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Saves a tree to a file.
     */
    public void save() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Loads a tree from the given file, if an exception occurs during file
     * operations, a hardcoded basic tree will be loaded instead.
     */
    public void load() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
