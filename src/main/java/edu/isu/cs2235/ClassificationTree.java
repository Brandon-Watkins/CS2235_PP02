package edu.isu.cs2235;

import edu.isu.cs2235.structures.BinaryTree;
import edu.isu.cs2235.structures.Node;
import edu.isu.cs2235.structures.impl.BinarySearchTree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree;
import edu.isu.cs2235.structures.impl.LinkedBinaryTree.BinaryTreeNode;
import edu.isu.cs2235.traversals.BreadthFirstTraversal;
import edu.isu.cs2235.traversals.InOrderTraversal;
import edu.isu.cs2235.traversals.PreOrderTraversal;
import edu.isu.cs2235.traversals.TreeTraversal;
import edu.isu.cs2235.traversals.commands.EnumeratedSaveCommand;
import edu.isu.cs2235.traversals.commands.EnumerationCommand;

import java.io.*;
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
 * @author Isaac Griffith, Brandon Watkins
 */
public class ClassificationTree {

    private File file;
    private BinaryTree<Datum> tree;
    String master;

    /**
     * Constructs a new Animal tree class which manages an underlying animal
     * tree
     */
    public ClassificationTree(Scanner inputScanner) {
        tree = new LinkedBinaryTree<>();
        load(inputScanner);
    }

    /**
     * Main method which controls the identification and tree management loop.
     */
    public void identify(Scanner in, BinaryTreeNode pointer) {
        if (pointer == null) pointer = (BinaryTreeNode)this.tree.root();
        // until we come across a leaf(answer), continue asking user prompts.
        while (pointer != null && this.tree.isInternal(pointer)){
            pointer = askUserInternalPrompt(in, pointer);
        }
        // determine proper wording for our guess.
        BinaryTreeNode finalPointer = pointer;
        String guessedObjectString = determineObjectWording(((Datum)pointer.getElement()).getPrompt());
        // make our classification guess
        boolean guessedCorrectly = guessObject(in, pointer, guessedObjectString);
        // if we guessed wrong, add the correct object to our classification tree.
        if (!guessedCorrectly) addNewObjectToTree(in, guessedObjectString, finalPointer);
    }

    private void addNewObjectToTree(Scanner in, String guessedObjectString, BinaryTreeNode finalPointer){
        // how does this object differ from my guessed object?
        System.out.print("What is the new " + this.master + "? > ");
        String object = in.nextLine().trim();
        if (object.hashCode() == 0) {
            while (((object = in.nextLine().trim()).hashCode() == 0)) {
                System.out.print(System.lineSeparator() + "Invalid response. What is the new " + this.master + "? > ");
            }
        }

        String newObjectString = determineObjectWording(object);

        // what characteristic sets it apart?
        System.out.print("What characteristic does " + newObjectString + " have that " + guessedObjectString + " does not? > ");
        String characteristic = in.nextLine().trim();
        if (characteristic.hashCode() == 0) {
            while (((characteristic = in.nextLine().trim()).hashCode() == 0)) {
                System.out.print(System.lineSeparator() + "Invalid response. What is the new " + this.master + "? > ");
            }
        }

        // add new object to tree
        String temp = ((Datum)(finalPointer.getElement())).getPrompt();
        this.tree.set(finalPointer, new Datum(characteristic));
        this.tree.addLeft(finalPointer, new Datum(object));
        this.tree.addRight(finalPointer, new Datum(temp));

        // update the node numbering
        TreeTraversal<Datum> traversal = new InOrderTraversal<>(this.tree);
        traversal.setCommand(new EnumerationCommand());
        traversal.traverse();

        // output the new node positions, as required by assignment.
        System.out.println(System.lineSeparator() + "Node #" + ((Datum)finalPointer.getElement()).getNumber() + " now contains " + characteristic + ".");
        System.out.println("Added node #" + ((Datum)finalPointer.getLeft().getElement()).getNumber() + " containing " + ((Datum)finalPointer.getLeft().getElement()).getPrompt());
        System.out.println("Added node #" + ((Datum)finalPointer.getRight().getElement()).getNumber() + " containing " + ((Datum)finalPointer.getRight().getElement()).getPrompt());

        System.out.println(System.lineSeparator() + "Added the " + characteristic + " " + object + " to the " + this.master + " classification tree." + System.lineSeparator());
    }

    private boolean guessObject(Scanner in, BinaryTreeNode pointer, String guessedObjectString){
        // make final guess.
        System.out.print("Is this " + this.master + " " + guessedObjectString + "? (Y/N) > ");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y") == false && response.equals("N") == false){
            while (((response = in.nextLine().toUpperCase()).equals("Y") == false && response.equals("N") == false)) {
                System.out.print(System.lineSeparator() + "Invalid response. Is this " + this.master + " " + guessedObjectString + "? (Y/N) > ");
            }
        }
        // if correctly guessed
        if (response.equals("Y")) {
            System.out.println(System.lineSeparator() + "Good." + System.lineSeparator());
            return true;
        }
        // else I dont have that object in my tree.
        else if (response.equals("N")) {
            String message = "";
            String suffix = " ";
            BinaryTreeNode pointer2 = pointer.getParent();
            String prefix = " ";
            while (pointer != this.tree.root() && pointer != null){
                String prompt = ((Datum)pointer.getElement()).getPrompt();
                prompt = prompt.trim();
                if (prompt.startsWith("a "))
                    prompt = prompt.replaceFirst("a ", "");
                if (prompt.startsWith("an "))
                    prompt = prompt.replaceFirst("an ", "");
                prompt = prompt.trim();

                if (pointer2 != null && this.tree.right(pointer2) == pointer) prefix = " not ";
                else prefix = " ";
                message = prefix + prompt + suffix + message;
                pointer = pointer.getParent();
                if (pointer2 != null) pointer2 = pointer2.getParent();
                suffix = ",";
            }
            System.out.println(System.lineSeparator() + "I don't know any" + message + this.master + "s that aren't " + guessedObjectString + ".");
            return false;
        }
        else {
            System.out.println("Invalid response. Cancelling.");
            return true;
        }
    }

    private BinaryTreeNode askUserInternalPrompt(Scanner in, BinaryTreeNode pointer){
        // Is this animal furry?
        System.out.print("Is this " + this.master + " " + ((Datum)pointer.getElement()).getPrompt() + "? (Y/N) > ");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y") == false && response.equals("N") == false) {
            while (((response = in.nextLine().toUpperCase()).equals("Y") == false && response.equals("N") == false)) {
                System.out.print(System.lineSeparator() + "Invalid response. Is this " + this.master + " " + ((Datum) pointer.getElement()).getPrompt() + "? (Y/N) > ");
            }
        }
        // if yes
        if (response.equals("Y")) pointer = pointer.getLeft();
            // if no
        else if (response.equals("N")) pointer = pointer.getRight();
        else {
            System.out.println("Invalid response. Cancelling.");
            return null;
        }
        return pointer;
    }

    private String determineObjectWording(String prompt){
        // finished asking characteristic questions. determine wording for final guess.
        prompt = prompt.trim();
        if (prompt.startsWith("a "))
            prompt = prompt.replaceFirst("a ", "");
        if (prompt.startsWith("an "))
            prompt = prompt.replaceFirst("an ", "");
        prompt = prompt.trim();
        boolean startsWithVowel = prompt.startsWith("a") || prompt.startsWith("A") || prompt.startsWith("e") || prompt.startsWith("E") ||
                prompt.startsWith("i") || prompt.startsWith("I") || prompt.startsWith("o") || prompt.startsWith("O") ||
                prompt.startsWith("u") || prompt.startsWith("U");
        String message = "";
        if (startsWithVowel) message = "an ";
        else message = "a ";
        message += prompt;
        return message;
    }

    /**
     * Saves a tree to a file.
     */
    public void save() {
        TreeTraversal<Datum> traversal = new InOrderTraversal<>(this.tree);
        //update the numbering
        traversal.setCommand(new EnumerationCommand());
        traversal.traverse();

        // save tree to file
        traversal = new BreadthFirstTraversal<>(this.tree);
        PrintWriter writer;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\" + this.file, false)));
            if (!this.file.canWrite()) { System.out.println("Cant write to file."); return; }
            if (!this.file.exists()) this.file.createNewFile();
            traversal.setCommand(new EnumeratedSaveCommand(writer));
        } catch (FileNotFoundException e) {
            System.out.println("File not found, unable to save classifications.");
            return;
        } catch (IOException e ){
            System.out.println("Issue with FileWriter, unable to save classifications.");
            return;
        }
        traversal.traverse();
        writer.close();
        System.out.println("Saved classifications to " + this.file + ".");
    }

    public void createNewClassificationTree(Scanner input){
        System.out.print(System.lineSeparator() + "Creating a new classification tree." + System.lineSeparator() + "What will you be classifying(ie. animal, food)? > ");
        this.master = "";
        while(this.master.hashCode() == 0) this.master = input.nextLine().replace("\"", "").trim();

        try {
            this.file = new File(this.master.replace(" ", "_") + ".txt");
            this.file.createNewFile();
        } catch (IOException e) {
            System.out.println("Problem creating a save file.");
        }


        System.out.print(System.lineSeparator() + "Creating a new " + this.master + " classification tree." + System.lineSeparator() + "Enter the primary " +
                "differentiating attribute. > ");
        String attribute = "";
        while(attribute.hashCode() == 0) attribute = input.nextLine().replace("\"", "").trim();
        this.tree.setRoot(new Datum(attribute, 0));

        System.out.print(System.lineSeparator() + "Name something that is " + attribute + ". > ");
        String object = "";
        while(object.hashCode() == 0) object = input.nextLine().replace("\"", "").trim();
        this.tree.addLeft(this.tree.root(), new Datum(object));

        System.out.print(System.lineSeparator() + "Name something that is NOT " + attribute + ". > ");
        object = "";
        while(object.hashCode() == 0) object = input.nextLine().replace("\"", "").trim();
        this.tree.addRight(this.tree.root(), new Datum(object));

        System.out.println(System.lineSeparator() + "New classification tree created." + System.lineSeparator());
    }

    /**
     * Loads a tree from the given file, if an exception occurs during file
     * operations, a hardcoded basic tree will be loaded instead.
     * @implNote This is only set up for reading a save file that was written in breadth-first traversal.
     * @param input The scanner being used to collect user input.
     */
    public void load(Scanner input) {
        TreeTraversal<Datum> traversal = new BreadthFirstTraversal<>(tree);
        this.file = null;
        BufferedReader fileReader;
        try {
            System.out.print(System.lineSeparator() + "Run:" + System.lineSeparator() + "Provide a file for loading, or enter -1 " +
                    "to make a new classification tree." + System.lineSeparator() + "Filename: ");
            String fileName = "";
            while(fileName.hashCode() == 0) fileName = input.nextLine().replace("\"", "").trim();
            if (fileName.equals("-1")) {
                createNewClassificationTree(input);
                return;
            }
            if (fileName.equals("test.txt") || fileName.equals("test75.txt")) this.master = "animal";
            this.file = new File(fileName);
            if (this.file.length() == 0) {
                System.out.println("File was empty, using backup tree instead." + System.lineSeparator());
                createBackupTree();
                return;
            }
            fileReader = new BufferedReader(new FileReader(this.file));
            String currentLine;
            try {
                ArrayList<Node<Datum>> list = new ArrayList<>();
                while ((currentLine = (fileReader.readLine())) != null){
                    String[] line = currentLine.split(":");
                    if (this.tree.root() == null && line[0].equals("-1")) {
                        this.tree.setRoot(new Datum(line[3], Integer.parseInt(line[1])));
                        list.add(this.tree.root());
                    }
                    else{
                        int parentNumber = Integer.parseInt(line[0]);
                        // for each of the nodes in the last "row"...
                        for(int i = 0; i < list.size(); i++){
                            // if node in list is the parent of new node...
                            if (list.get(i).getElement().getNumber() == parentNumber){
                                // add new node to correct side of parent node
                                Node<Datum> node;
                                // add left node to parent. unfortunately, if there is no right node, parent will remain in the list.
                                if (line[2].equals("l")) node = this.tree.addLeft(list.get(i), new Datum(line[3], Integer.parseInt(line[1])));
                                // add right node to parent. remove parent node from list, won't need it again.
                                else {
                                    node = this.tree.addRight(list.remove(i), new Datum(line[3], Integer.parseInt(line[1])));
                                }
                                list.add(node);
                                break;
                            }
                        }
                    }
                }
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Had an IO Exception, using backup tree instead." + System.lineSeparator());
                createBackupTree();
                return;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found, using backup tree instead." + System.lineSeparator());
            createBackupTree();
            return;
        }
        System.out.println("Loaded classifications from: " + this.file + "." + System.lineSeparator());
    }

    private void createBackupTree(){
        this.master = "animal";
        this.file = new File("backup.txt");
        this.tree.setRoot(new Datum("furry"));
        this.tree.addLeft(this.tree.root(), new Datum("dog"));
        this.tree.addRight(this.tree.root(), new Datum("snake"));
    }
}
