package edu.isu.cs2235;

import java.util.Scanner;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree.BinaryTreeNode;

/**
 * Driver for the classification program
 *
 * @author Isaac Griffith
 */
public class Driver {

    /**
     * Runs the program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ClassificationTree tree = new ClassificationTree(in);
        String more = "Y";
        boolean firstAnimal = true;
        while (more.equals("Y")) {
            if (tree.master == "animal") {
                if (firstAnimal) System.out.print("Do you have an animal to identify? (Y/N) > ");
                else System.out.print("Do you have another animal to identify? (Y/N) > ");
            }
            else {
                String prefix = "a ";
                Character c = tree.master.toUpperCase().charAt(0);
                if (c.equals('A') || c.equals('E') || c.equals('I') || c.equals('O') || c.equals('U')) prefix = "an ";
                if (firstAnimal) System.out.print("Do you have " + prefix + tree.master + " entry to identify? (Y/N) > ");
                else System.out.print("Do you have another" + tree.master + " entry to identify? (Y/N) > ");
            }
            more = in.next().trim().toUpperCase();
            while(more.hashCode() == 0) more = in.nextLine().trim().toUpperCase();

            if (more.equals("Y")) {
                firstAnimal = false;
                tree.identify(in, null);
            }
        }
        in.close();
        tree.save();
    }
}
