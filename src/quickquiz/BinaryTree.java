package quickquiz;

//Source:  http://www.newthinktank.com/2013/03/binary-tree-in-java/
// New Think Tank
public class BinaryTree {

    public String global = "";
    public int count;
    BTNode root;

    public void addBTNode(int qNum, String question) {

        // Create a new BTNode and initialize it
        BTNode newBTNode = new BTNode(qNum, question);

        // If there is no root this becomes root
        if (root == null) {

            root = newBTNode;

        } else {

            // Set root as the BTNode we will start
            // with as we traverse the tree
            BTNode focusBTNode = root;

            // Future parent for our new BTNode
            BTNode parent;

            while (true) {

                // root is the top parent so we start
                // there
                parent = focusBTNode;

                // Check if the new node should go on
                // the left side of the parent node
                if (qNum < focusBTNode.qNum) {

                    // Switch focus to the left child
                    focusBTNode = focusBTNode.leftChild;

                    // If the left child has no children
                    if (focusBTNode == null) {

                        // then place the new node on the left of it
                        parent.leftChild = newBTNode;
                        return; // All Done

                    }

                } else { // If we get here put the node on the right

                    focusBTNode = focusBTNode.rightChild;

                    // If the right child has no children
                    if (focusBTNode == null) {

                        // then place the new node on the right of it
                        parent.rightChild = newBTNode;
                        return; // All Done

                    }

                }

            }
        }

    }

    // All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public void inOrderTraverseTree(BTNode focusBTNode) {

        if (focusBTNode != null) {

            // Traverse the left node
            inOrderTraverseTree(focusBTNode.leftChild);

            // Visit the currently focused on node
            global = global + focusBTNode.toString();
            count++;

            // Traverse the right node
            inOrderTraverseTree(focusBTNode.rightChild);

        }

    }

    public void preorderTraverseTree(BTNode focusBTNode) {

        if (focusBTNode != null) {

            global = global + focusBTNode.toString();
            count++;

            preorderTraverseTree(focusBTNode.leftChild);
            preorderTraverseTree(focusBTNode.rightChild);

        }

    }

    public void postOrderTraverseTree(BTNode focusBTNode) {

        if (focusBTNode != null) {

            postOrderTraverseTree(focusBTNode.leftChild);
            postOrderTraverseTree(focusBTNode.rightChild);

            global = global + focusBTNode.toString();
            count++;

        }

    }

    public String nodeList() {
        String nodePath = global;
        global = "";
        return nodePath;
    }

    public BTNode findBTNode(int qNum) {

        // Start at the top of the tree
        BTNode focusBTNode = root;

        // While we haven't found the BTNode
        // keep looking
        while (focusBTNode.qNum != qNum) {

            // If we should search to the left
            if (qNum < focusBTNode.qNum) {

                // Shift the focus BTNode to the left child
                focusBTNode = focusBTNode.leftChild;

            } else {

                // Shift the focus BTNode to the right child
                focusBTNode = focusBTNode.rightChild;

            }

            // The node wasn't found
            if (focusBTNode == null) {
                return null;
            }

        }

        return focusBTNode;

    }

    public static void main(String[] args) {

        BinaryTree theTree = new BinaryTree();

        theTree.addBTNode(50, "Boss");

        theTree.addBTNode(25, "Vice President");

        theTree.addBTNode(15, "Office Manager");

        theTree.addBTNode(30, "Secretary");

        theTree.addBTNode(75, "Sales Manager");

        theTree.addBTNode(85, "Salesman 1");

        // Different ways to traverse binary trees
        // theTree.inOrderTraverseTree(theTree.root);
        // theTree.preorderTraverseTree(theTree.root);
        // theTree.postOrderTraverseTree(theTree.root);
        // Find the node with qNum 75
        System.out.println("\nBTNode with the qNum 75");

        System.out.println(theTree.findBTNode(75));

    }
}

class BTNode {

    int qNum;
    String question;

    BTNode leftChild;
    BTNode rightChild;

    BTNode(int qNum, String question) {

        this.qNum = qNum;
        this.question = question;

    }

    public String toString() {

        return qNum + " " + question + "|";

        /*
		 * return question + " has the qNum " + qNum + "\nLeft Child: " + leftChild +
		 * "\nRight Child: " + rightChild + "\n";
         */
    }

}
