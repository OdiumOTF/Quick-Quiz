/**
 * **************************************************************************
 */
/*                                                                           */
/*                    Doubly-Linked List Manipulation                        */
/*                                                                           */
/*                     January 1998, Toshimi Minoura                         */
/*                                                                           */
/**
 * **************************************************************************
 */
// Filename: Doubly-LinkedList_ToshimiMinoura
// Source:   TBA
package quickquiz;

// A Node is a node in a doubly-linked list.
class Node
{              // class for nodes in a doubly-linked list

    Node prev;              // previous Node in a doubly-linked list
    Node next;              // next Node in a doubly-linked list
    Words myWords;
    //public char data;       // data stored in this Node

    Node()
    {                // constructor for head Node 
        prev = this;           // of an empty doubly-linked list
        next = this;
        myWords = new Words();
        myWords.QNum = "QNum";
        myWords.Topic = "Wors";
        myWords.Ans = "D";
        myWords.CorrectAns = "X";
        // data = 'H';           // not used except for printing data in list head
    }

    Node(String QNum, String Topic, String Ans, String CorrectAns)
    {       // constructor for a Node with data
        prev = null;
        next = null;
        myWords = new Words(QNum, Topic, Ans, CorrectAns);
        //this.data = data;     // set argument data to instance variable data
    }

    public void append(Node newNode)
    {  // attach newNode after this Node
        newNode.prev = this;
        newNode.next = next;
        if (next != null)
        {
            next.prev = newNode;
        }
        next = newNode;
        System.out.println("Node with data " + newNode.myWords.QNum
                + " appended after Node with data " + myWords.QNum);
    }

    public void insert(Node newNode)
    {  // attach newNode before this Node
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;;
        prev = newNode;
        System.out.println("Node with data " + newNode.myWords.QNum
                + " inserted before Node with data " + myWords.QNum);
    }

    public void remove()
    {              // remove this Node
        next.prev = prev;                 // bypass this Node
        prev.next = next;
        System.out.println("Node with data " + myWords.QNum + " removed");
    }
    public String toString(){
        return this.myWords.QNum + " - " + this.myWords.Topic;
    }
}

class DList
{

    Node head;

    public DList()
    {
        head = new Node();
    }

    public DList(String QNum, String Topic, String Ans, String CorrectAns)
    {
        head = new Node(QNum, Topic, Ans, CorrectAns);
    }

    public Node find(String wrd1)
    {          // find Node containing x
        for (Node current = head.next; current != head; current = current.next)
        {
            if (current.myWords.QNum.compareToIgnoreCase(wrd1) == 0)
            {        // is x contained in current Node?
                System.out.println("Data " + wrd1 + " found");
                return current;               // return Node containing x
            }
        }
        System.out.println("Data " + wrd1 + " not found");
        return null;
    }

    //This Get method Added by Matt C
    public Node get(int i)
    {
        Node current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    public String toString()
    {
        String str = "";
        if (head.next == head)
        {             // list is empty, only header Node
            return "List Empty";
        }
        str = "list content = ";
        for (Node current = head.next; current != head && current != null; current = current.next)
        {
            str = str + current.myWords.QNum + ", " + current.myWords.Topic +  current.myWords.Ans + ", " + current.myWords.CorrectAns + "\r\n ";
        }
        return str;
    }

    public void print()
    {                  // print content of list
        if (head.next == head)
        {             // list is empty, only header Node
            System.out.println("list empty");
            return;
        }
        System.out.print("list content = ");
        for (Node current = head.next; current != head; current = current.next)
        {
            System.out.print(" " + current.myWords.QNum + ", " + current.myWords.Topic + ", " + current.myWords.Ans + ", " + current.myWords.CorrectAns + "\r\n ");
        }
        System.out.println("");
    }

  public static void main(String[] args) {
    DList dList = new DList();              // create an empty dList
    dList.print();

//    dList.head.append(new Node("1","2"));       // add Node with data '1'
//    dList.print();
//    dList.head.append(new Node("3", "4"));       // add Node with data '2'
//    dList.print();
//    dList.head.append(new Node("5","6"));       // add Node with data '3'
//    dList.print();
//    dList.head.insert(new Node("A","B"));       // add Node with data 'A'
//    dList.print();
//    dList.head.insert(new Node("C","D"));       // add Node with data 'B'
//    dList.print();
//    dList.head.insert(new Node("E","F"));       // add Node with data 'C'
//    dList.print();
//
//    Node nodeA = dList.find("A");           // find Node containing 'A'
//    nodeA.remove();                         // remove that Node
//    dList.print();
//
//    Node node2 = dList.find("3");           // find Node containing '2'
//    node2.remove();                           // remove that Node
//    dList.print();
//
//    Node nodeB = dList.find("5");            // find Node containing 'B'
////    nodeB.append(new Node("Linked","List"));   // add Node with data X
//    dList.print();
  }
}
