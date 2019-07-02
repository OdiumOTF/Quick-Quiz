package quickquiz;
/****************************************************************
   PROGRAM:   Quick Quiz
   AUTHOR:    Bailey Stephens-Peacock

   FUNCTION:  Program utilized by a teacher to send question to students in a test/exam condition, and handle the answers recieved

   INPUT:     Requires a text file containing questions to be read in

   OUTPUT:    Outputs reports containing a hashmap of the binary tree in different formats (pre-order, in-order, post-order)

   NOTES:     The program relies heavily on the BinaryTree.java, DList.java to handle the data, and LibraryComponents.java to handle gui construction
****************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.table.AbstractTableModel;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class QuickQuiz extends JFrame implements WindowListener, ActionListener {

    private JTextArea txtQN, txtLinkedList, txtBinaryTree;
    private JTextField txtQnum, txtTopic, txtA, txtB, txtC, txtD, txtCorrectAns, txtNumOfStudents;
    private JButton btnExit, btnSend, btnDisplay, btnPreDisplay, btnPreSave, btnInDisplay, btnInSave, btnPostDisplay, btnPostSave, btnBubbleSort, btnInsertSort,
            btnSelectionSort, btnStudentNumSet;
    private JLabel lblQnum, lblCorrectAns, lblLinkedList, lblBinaryTree, lblPreOrder, lblInOrder, lblPostOrder, lblTopic, lblQN, lblA, lblB, lblC, lblD, lblConnected,
            lblNumOfStudents, lblAnswersRecieved, lblNumOfAnswers;
    private JScrollPane spBinaryTree;
    JTable table;
    MyModel quizModel;
    SpringLayout springLayout;

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread1 client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    private String data;
    private String fileName = "C:\\Users\\Public\\Documents\\QuizQuestions.txt";
    private String prehash = "PreHash.txt";    
    private String inhash = "InHash.txt";
    private String posthash = "PostHash.txt";

    String[] dataPacket = new String[7];
    String[] tempData = new String[10]; //temporarily stores student answers while they're processed
    ArrayList<Object[]> al = new ArrayList();
    DList dList = new DList();
    BinaryTree tree = new BinaryTree();
    BTNode focusNode = new BTNode(0, "");
    int numOfStudents;
    int numOfAsked;
    int numOfAnswers;
    int numOfIncorrect;
    String correctAns;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //Hashmap 

    public static void main(String[] args) {
        QuickQuiz quickquiz = new QuickQuiz();
        quickquiz.run();
    }

    static Logger log = Logger.getLogger(QuickQuiz.class.getName());
   
    private void run() {
        setBounds(10, 10, 755, 640);
        setTitle("Quick Quiz");

        getParameters();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        readDataFile("C:\\Users\\Public\\QuizQuestions.txt");
        displayGUI();
        setResizable(false);
        setVisible(true);
        txtLinkedList.setText(dList.toString());
        dList.print();
        connect(serverName, serverPort);
    }
//<editor-fold defaultstate="collapsed" desc="GUI">
/****************************************************************
   FUNCTION:   displayGUI

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      sets up the programs GUI by calling all of the GUI construction functions
****************************************************************/
    private void displayGUI() {
        springLayout = new SpringLayout();
        setLayout(springLayout);
        displayButtons(springLayout);
        displayTextFields(springLayout);
        displayLabels(springLayout);
        displayTextAreas(springLayout);
        displayScrollPanes(springLayout);
        QuickQuizTable(springLayout);
        this.addWindowListener(this);
    }

    //<editor-fold defaultstate="collapsed" desc="Buttons">
    /****************************************************************
   FUNCTION:   displayButtons

   ARGUMENTS:  SpringLayout: the spring layout

   RETURNS:    void

   NOTES:      Displays all buttons required by utilizing the LibraryComponents file
****************************************************************/
    private void displayButtons(SpringLayout layout) {  // Displays all the buttons

        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 600, 570, 130, 25);
        btnSend = LibraryComponents.LocateAJButton(this, this, layout, "Send", 600, 300, 130, 25);
        btnDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 600, 420, 130, 25);
        btnPreDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 10, 570, 75, 25);
        btnPreSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 85, 570, 75, 25);
        btnInDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 200, 570, 75, 25);
        btnInSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 275, 570, 80, 25);
        btnPostDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 400, 570, 75, 25);
        btnPostSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 475, 570, 75, 25);
        btnBubbleSort = LibraryComponents.LocateAJButton(this, this, layout, "Sort by QNum", 20, 300, 120, 25);
        btnInsertSort = LibraryComponents.LocateAJButton(this, this, layout, "Sort by Topic", 175, 300, 120, 25);
        btnSelectionSort = LibraryComponents.LocateAJButton(this, this, layout, "Sort by Question", 325, 300, 120, 25);
        btnStudentNumSet = LibraryComponents.LocateAJButton(this, this, layout, "Set", 655, 10, 55, 25);
    }

    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Labels">
/****************************************************************
   FUNCTION:   displayLabels

   ARGUMENTS:  SpringLayout: the spring layout

   RETURNS:    void

   NOTES:      Displays all labels required by utilizing the LibraryComponents file
****************************************************************/
    private void displayLabels(SpringLayout layout) {   // Displays all the labels

        lblCorrectAns = LibraryComponents.LocateAJLabel(this, layout, "Correct Ans:", 485, 300);
        lblLinkedList = LibraryComponents.LocateAJLabel(this, layout, "Linked List:", 30, 325);
        lblBinaryTree = LibraryComponents.LocateAJLabel(this, layout, "Binary Tree:", 30, 425);
        lblPreOrder = LibraryComponents.LocateAJLabel(this, layout, "Pre-Order", 60, 550);
        lblInOrder = LibraryComponents.LocateAJLabel(this, layout, "In-Order", 250, 550);
        lblPostOrder = LibraryComponents.LocateAJLabel(this, layout, "Post-Order", 450, 550);
        lblQnum = LibraryComponents.LocateAJLabel(this, layout, "Qnum:", 500, 50);
        lblTopic = LibraryComponents.LocateAJLabel(this, layout, "Topic:", 500, 75);
        lblQN = LibraryComponents.LocateAJLabel(this, layout, "Question:", 500, 100);
        lblA = LibraryComponents.LocateAJLabel(this, layout, "A:", 500, 150);
        lblB = LibraryComponents.LocateAJLabel(this, layout, "B:", 500, 175);
        lblC = LibraryComponents.LocateAJLabel(this, layout, "C:", 500, 200);
        lblD = LibraryComponents.LocateAJLabel(this, layout, "D:", 500, 225);
        lblConnected = LibraryComponents.LocateAJLabel(this, layout, "", 10, 525);
        lblNumOfStudents = LibraryComponents.LocateAJLabel(this, layout, "Number of Students:", 500, 10);
        lblAnswersRecieved = LibraryComponents.LocateAJLabel(this, layout, "Answers Recieved:", 575, 350);
        lblNumOfAnswers = LibraryComponents.LocateAJLabel(this, layout, "", 685, 350);
    }
//</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Text Fields">
    /****************************************************************
   FUNCTION:   displayTexFields

   ARGUMENTS:  SpringLayout: the spring layout

   RETURNS:    void

   NOTES:      Displays all Text fields required by utilizing the LibraryComponents file
****************************************************************/
    private void displayTextFields(SpringLayout layout) {  // Displays all the text fields
        txtQnum = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 50);
        txtTopic = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 75);
        txtA = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 150);
        txtB = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 175);
        txtC = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 200);
        txtD = LibraryComponents.LocateAJTextField(this, null, layout, 15, 575, 225);
        txtCorrectAns = LibraryComponents.LocateAJTextField(this, null, layout, 3, 560, 300);
        txtNumOfStudents = LibraryComponents.LocateAJTextField(this, null, layout, 2, 625, 10);
    }
    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Text Areas">
        /****************************************************************
   FUNCTION:   displayTextAreas

   ARGUMENTS:  SpringLayout: the spring layout

   RETURNS:    void

   NOTES:      Displays all Text Areas required by utilizing the LibraryComponents file
****************************************************************/
    private void displayTextAreas(SpringLayout layout) {  // Displays all the text areas

        txtQN = LibraryComponents.LocateAJTextArea(this, layout, 575, 100, 2, 15);
        txtLinkedList = LibraryComponents.LocateAJTextArea(this, layout, 10, 350, 4, 50);

        txtBinaryTree = LibraryComponents.LocateAJTextArea(this, layout, 10, 450, 4, 50);
    }

    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Scroll Panes">
        /****************************************************************
   FUNCTION:   displayScrollPanes

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      Add all Scroll panes required by utilizing the LibraryComponents file
****************************************************************/
    private void displayScrollPanes(SpringLayout layout) { //Display all the scroll panes
        JScrollPane spLinkedList = new JScrollPane(txtLinkedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout.putConstraint(SpringLayout.WEST, spLinkedList, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, spLinkedList, 345, SpringLayout.NORTH, this);

        this.add(spLinkedList);

        JScrollPane spBinaryTree = new JScrollPane(txtBinaryTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout.putConstraint(SpringLayout.WEST, spBinaryTree, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, spBinaryTree, 445, SpringLayout.NORTH, this);

        this.add(spBinaryTree);
    }
//</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Table">
    /****************************************************************
   FUNCTION:   QuickQuizTable

   ARGUMENTS:  SpringLayout: the spring layout

   RETURNS:    void

   NOTES:      Displays the Jtable, and handles the mouse listener for selecting questions and moving the data to the text fields
****************************************************************/
    public void QuickQuizTable(SpringLayout myPanelLayout) {
        // Create a panel to hold all other components
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        add(topPanel);

        // Create column names
        String columnNames[]
                = {"#", "Topic", "Question"};

        // constructor of JTable model
        quizModel = new MyModel(al, columnNames);

        // Create a new table instance
        table = new JTable(quizModel);

        // Configure some of JTable's paramters
        table.isForegroundSet();
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        add(table);

        // Change the text and background colours
        table.setSelectionForeground(Color.white);
        table.setSelectionBackground(Color.red);

        //add mouse listener to select row
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    txtQnum.setText(table.getModel().getValueAt(row, 0).toString());
                    txtTopic.setText(table.getModel().getValueAt(row, 1).toString());
                    txtQN.setText(table.getModel().getValueAt(row, 2).toString());
                    txtA.setText(table.getModel().getValueAt(row, 3).toString());
                    txtB.setText(table.getModel().getValueAt(row, 4).toString());
                    txtC.setText(table.getModel().getValueAt(row, 5).toString());
                    txtD.setText(table.getModel().getValueAt(row, 6).toString());
                    txtCorrectAns.setText(table.getModel().getValueAt(row, 7).toString());
                }
            }
        });

        // Add the table to a scrolling pane, size and locate
        JScrollPane scrollPane = table.createScrollPaneForTable(table);
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(475, 250));
        myPanelLayout.putConstraint(SpringLayout.WEST, topPanel, 10, SpringLayout.WEST, this);
        myPanelLayout.putConstraint(SpringLayout.NORTH, topPanel, 40, SpringLayout.NORTH, this);
    }
//</editor-fold> 

    private void clear() {
        txtQnum.setText("");
        txtQN.setText("");
        txtTopic.setText("");
        txtA.setText("");
        txtB.setText("");
        txtC.setText("");
        txtD.setText("");
        txtCorrectAns.setText("");
    }
    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Action and Key Listeners">

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnExit) {
            txtTopic.setText(".bye");
            send();
            System.exit(0);
        }
        if (e.getSource() == btnSend) {
            correctAns = txtCorrectAns.getText();
            send();
        }
        if (e.getSource() == btnStudentNumSet) {
            numOfStudents = Integer.parseInt(txtNumOfStudents.getText());
            lblNumOfAnswers.setText(numOfAnswers + "/" + numOfStudents);
        }
        if (e.getSource() == btnBubbleSort) {
            bubbleSort(al);
            quizModel.fireTableDataChanged();
        }
        if (e.getSource() == btnInsertSort) {
            InsertionSort(al);
            quizModel.fireTableDataChanged();
        }
        if (e.getSource() == btnSelectionSort) {
            SelectionSort(al);
            quizModel.fireTableDataChanged();
        }
        if (e.getSource() == btnPreDisplay) {
            tree.global = "";
            tree.preorderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
        }
        if (e.getSource() == btnPreSave) {
            tree.global = "";
            tree.preorderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
            writeFile(hashing(),prehash);
        }
        if (e.getSource() == btnInDisplay) {
            tree.global = "";
            tree.inOrderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
        }
        if (e.getSource() == btnInSave) {
            tree.global = "";
            tree.inOrderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
            writeFile(hashing(),inhash);
        }
        if (e.getSource() == btnPostDisplay) {
            tree.global = "";
            tree.postOrderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
        }
        if (e.getSource() == btnPostSave) {
            tree.global = "";
            tree.postOrderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);
            writeFile(hashing(),posthash);
        }
    }
    //</editor-fold>    
//<editor-fold defaultstate="collapsed" desc="File Management">
    /****************************************************************
   FUNCTION:   readDataFile

   ARGUMENTS:  fileName: the location of the file being read

   RETURNS:    void

   NOTES:      uses a buffered reader to read in a txt file of comma delimited data and adds it to a string array, which is then added to an ArrayList
****************************************************************/
    private void readDataFile(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Public\\QuizQuestions.txt"))) {
            String inputLine;
            br.readLine();       //TEMP FIX: if file does not have column names first line will be skipped
            while ((inputLine = br.readLine()) != null) {
                //Use if statement to get the first line into an array called "columnName" for the table to read in

                String[] questionVars = inputLine.split(",");
                al.add(questionVars);
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
    }
    /****************************************************************
   FUNCTION:   writeFile

   ARGUMENTS:  data: a string containing the data that is to be saved
   *            

   RETURNS:    void

   NOTES:      sorts the data in the Jtable using the bubble sort algorithm by question number
****************************************************************/
    public void writeFile(String data,String fileName) {

       try(FileWriter fileWriter = new FileWriter(fileName)) 
        {
            fileWriter.write(data);
            log.info("File: " + fileName + "Saved");
        } 
         catch (Exception e) 
        {
            log.error(e.getMessage());
        }    
    }

    //</editor-fold> 
//<editor-fold defaultstate="collapsed" desc="Sorts">
    /****************************************************************
   FUNCTION:   bubbleSort

   ARGUMENTS:  arr: ArrayList<Object[]>, the data in the Jtable

   RETURNS:    void

   NOTES:      sorts the data in the Jtable using the bubble sort algorithm by question number
****************************************************************/
    public static void bubbleSort(ArrayList<Object[]> arr) {

        for (int j = 0; j < arr.size(); j++) {
            for (int i = j + 1; i < arr.size(); i++) {
                if ((arr.get(i)[0]).toString().compareToIgnoreCase(arr.get(j)[0].toString()) < 0) {
                    Object[] words = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, words);
                }
            }
            log.info(arr.get(j)[0] + " - " + arr.get(j)[1]);
        }
    }
    /****************************************************************
   FUNCTION:   InsertionSort

   ARGUMENTS:  arr: ArrayList<Object[]>, the data in the Jtable

   RETURNS:    void

   NOTES:      sorts the data in the Jtable using the insertion sort algorithm by topic
****************************************************************/
    public static void InsertionSort(ArrayList<Object[]> arr) {
        int j; // the number of items sorted so far
        Object[] key; // the item to be inserted
        int i;
        for (j = 1; j < arr.size(); j++) // Start with 1 (not 0)
        {
            key = arr.get(j);
            for (i = j - 1; (i >= 0) && ((arr.get(i)[1]).toString().compareToIgnoreCase(key[1].toString()) > 0); i--) {
                arr.set(i + 1, arr.get(i));
            }
            arr.set(i + 1, key);
            log.info(arr.get(j)[1] + " - " + arr.get(j)[1]);
        }
    }
    /****************************************************************
   FUNCTION:   SelectionSort

   ARGUMENTS:  arr: ArrayList<Object[]>, the data in the Jtable

   RETURNS:    void

   NOTES:      sorts the data in the Jtable using the selection sort algorithm by question
****************************************************************/
    public static void SelectionSort(ArrayList<Object[]> arr) {
        int i, j, first;
        Object[] temp;
        for (i = arr.size() - 1; i > 0; i--) {
            first = 0; //initialize to subscript of first element
            for (j = 1; j <= i; j++) //locate smallest element between positions 1 and i.
            {
                if (arr.get(j)[2].toString().compareToIgnoreCase(arr.get(first)[2].toString()) > 0) {
                    first = j;
                }
            }
            temp = arr.get(first); //swap smallest found with element in position i.
            arr.set(first, arr.get(i));
            arr.set(i, temp);
        }
        for (i = 0; i < arr.size(); i++) {
            log.info("arr[i]: " + arr.get(i)[2]);
        }
    }

    //</editor-fold> 
//<editor-fold defaultstate="collapsed" desc="Server">
    /****************************************************************
   FUNCTION:   connect

   ARGUMENTS:  serverName: the name of the server
               serverPort: the server port

   RETURNS:    void

   NOTES:      recieves the servername and serverport and uses these to open a connection between the instructor form and the chatclient
               
****************************************************************/
    public void connect(String serverName, int serverPort) {
        log.info("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            log.info("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            log.error("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            log.error("Unexpected exception: " + ioe.getMessage());
        }
    }

  /****************************************************************
   FUNCTION:   send

   ARGUMENTS:  Relies on global variables

   RETURNS:    void

   NOTES:      Sends the data of the selected question to the server and adds the question to the binary tree
****************************************************************/
    private void send() {
        try {
            data = "~" + txtQnum.getText() + "~" + txtTopic.getText() + "~" + txtQN.getText() + "~" + txtA.getText() + "~" + txtB.getText() + "~" + txtC.getText() + "~" + txtD.getText() + "~" + txtCorrectAns.getText();
            streamOut.writeUTF(data);
            streamOut.flush();
            numOfAsked++; //track that a question has been asked

            tree.addBTNode(Integer.parseInt(txtQnum.getText().trim()), txtTopic.getText().trim());    //add question to the binary tree
            tree.global = "";
            tree.preorderTraverseTree(tree.root);
            txtBinaryTree.setText(tree.global);    //Display it in the text area
            numOfAnswers = 0;
        } catch (IOException ioe) {
            log.error("Sending error: " + ioe.getMessage());
            close();
        }
    }
/****************************************************************
   FUNCTION:   handle

   ARGUMENTS:  msg: the message recieved from the server

   RETURNS:    void

   NOTES:      handles the message recieved from the server. If it originates from a student it keeps track of the amount of students that have answered
               and if the answer was right. If the amount answered is equal to the total number of students, check if any answered wrong and if so add the 
               question and amount of wrong answers to the 
               linked list and 
****************************************************************/
    public void handle(String msg) { // This is fucked
        if (msg.equals(".bye")) {
            log.info("Good bye. Press EXIT button to exit ...");
            close();
        }
        if (msg.contains("#")) { //if message comes from a student
            tempData = msg.split("~"); //add student answers to tempData string array
            numOfAnswers++;
            if (!(tempData[3].equals(correctAns))) { //check if answer is incorrect,
                numOfIncorrect++; //tally the incorrect answer                
                if (numOfAnswers == numOfStudents) { //once all answers have been collected
                    if (numOfIncorrect > 0) { //If there are 2 or more incorrect answeres
                        dList.head.insert(new Node(tempData[1], tempData[2], " Number of wrong answers: ", Integer.toString(numOfIncorrect))); //add question to the linkedlist
                        txtLinkedList.setText(dList.toString());
                        dList.print();
                        numOfAnswers = 0;
                        numOfIncorrect = 0;
                    }
                }
            } else if (numOfAnswers == numOfStudents) {
                numOfAnswers = 0;
                numOfIncorrect = 0;
            }
            lblNumOfAnswers.setText(numOfAnswers + "/" + numOfStudents);//Update amount of answers label (for no reason other than I thought it would be cool)
            log.info(msg);
        } else {
            log.info(msg);
        }
    }

/****************************************************************
   FUNCTION:   open

   ARGUMENTS:  Relies on global variables

   RETURNS:    void

   NOTES:      see "connect" header
****************************************************************/
    public void open() {
        try {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ChatClientThread1(this, socket);
        } catch (IOException ioe) {
            log.error("Error opening output stream: " + ioe);
        }
    }
    
/****************************************************************
   FUNCTION:   close

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      closes the connection between the instructor form and the server 
****************************************************************/
    public void close() {
        try {
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            log.error("Error closing ...");
        }
        client.close();
        client.stop();
    }
/****************************************************************
   FUNCTION:   println

   ARGUMENTS:  msg: the message recieved from the server

   RETURNS:    void

   NOTES:      updates the "connected" label to display the message recieved from the server
****************************************************************/
    void println(String msg) {
        lblConnected.setText(msg);
    }
/****************************************************************
   FUNCTION:   getParameters

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      updates the serverName and serverPort variables
****************************************************************/
    public void getParameters() {
        serverName = "localhost";
        serverPort = 4444;
    }

    //</editor-fold> 
//<editor-fold defaultstate="collapsed" desc="Data Management">
    /****************************************************************
   FUNCTION:   hashing

   ARGUMENTS:  relies on global variables

   RETURNS:    string containing the hashmap

   NOTES:      converts the binary tree to a hashmap, and returns the hashmap as a string
****************************************************************/
    public String hashing() {
        String[] data = new String(txtBinaryTree.getText()).split("|");

        for (String key : data) {
            Integer freq = hashMap.get(key);
            if (freq == null) {
                freq = 1;
            } else {
                freq++;
            }
            hashMap.put(key, freq);
        }
        log.info("HashMap: " + hashMap);
        return hashMap.toString();
    }
//</editor-fold> 
//<editor-fold defaultstate="collapsed" desc="AbstractTableModel">

    class MyModel extends AbstractTableModel {

        // the headers
        String[] header;

        // constructor 
        MyModel(ArrayList<Object[]> obj, String[] header) {
            // save the header
            this.header = header;
            // and the data
            al = obj;
        }

        // method that needs to be overload. The row count is the size of the ArrayList
        public int getRowCount() {
            return al.size();
        }

        // method that needs to be overload. The column count is the size of our header
        public int getColumnCount() {
            return header.length;
        }

        // method that needs to be overload. The object is in the arrayList at rowIndex
        public Object getValueAt(int rowIndex, int columnIndex) {
            return al.get(rowIndex)[columnIndex];
        }

        // a method to return the column name 
        public String getColumnName(int index) {
            return header[index];
        }

        // a method to add a new line to the table
        void add(String word1, String word2, String word3) {
            // make it an array[2] as this is the way it is stored in the ArrayList
            // (not best design but we want simplicity)
            String[] str = new String[2];
            str[0] = word1;
            str[1] = word2;
            str[2] = word3;
            al.add(str);
            // inform the GUI that I have change
            fireTableDataChanged();
        }
    }
    //</editor-fold> 
}