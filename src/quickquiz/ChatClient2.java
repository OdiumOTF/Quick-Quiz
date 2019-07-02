package quickquiz;
//  Creating a simple Chat Client/Server Solution 
//  http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html

//CHAT RELATED ---------------------------
import java.net.*;
import java.io.*;
//----------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.SpringLayout;
import org.apache.log4j.Logger;

public class ChatClient2 extends Frame implements ActionListener, WindowListener
{

    //CHAT RELATED ---------------------------
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread2 client2 = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    //----------------------------------------
    Label lblQnum ,lblTopic, lblQuestion, lblA, lblB, lblC, lblD, lblAns, lblConnected;
    TextField txtQnum, txtTopic, txtQ, txtA, txtB, txtC, txtD, txtAnswer;
    Button btnSend, btnExit;
    String correctAns;
    
    public static void main(String[] args)
    {
        Frame myFrame = new ChatClient2();
        myFrame.setSize(375, 320);
        myFrame.setLocation(400, 200);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
    }
    static Logger log = Logger.getLogger(ChatClient2.class.getName());
    public ChatClient2()
    {
        setTitle("Quick Quiz: Student");
        setBackground(Color.gray);

        SpringLayout myLayout = new SpringLayout();
        setLayout(myLayout);

        LocateLabels(myLayout);
        LocateTextFields(myLayout);
        LocateButtons(myLayout);

        this.addWindowListener(this);
        connect(serverName, serverPort);
        //CHAT RELATED ---------------------------
        getParameters(); 
        //----------------------------------------
    }

//<editor-fold defaultstate="collapsed" desc="GUI Construction">
    public void LocateLabels(SpringLayout myLabelLayout)
    {
        lblQnum = LocateALabel(myLabelLayout, lblQnum, "Qnum: ", 10, 25);
        lblTopic = LocateALabel(myLabelLayout, lblTopic, "Topic: ", 10, 50);
        lblQuestion = LocateALabel(myLabelLayout, lblQuestion, "Question: ", 10, 75);
        lblA = LocateALabel(myLabelLayout, lblA, "A: ", 10, 100);
        lblB = LocateALabel(myLabelLayout, lblB, "B: ", 10, 125);
        lblC = LocateALabel(myLabelLayout, lblC, "C: ", 10, 150);
        lblD = LocateALabel(myLabelLayout, lblD, "D: ", 10, 175);
        lblAns = LocateALabel(myLabelLayout, lblAns, "Answer: ", 10, 225);
        lblConnected = LocateALabel(myLabelLayout, lblConnected, "Status:                                      ", 10, 3);
    }

    public Label LocateALabel(SpringLayout myLabelLayout, Label myLabel, String LabelCaption, int x, int y)
    {
        myLabel = new Label(LabelCaption);
        add(myLabel);
        myLabelLayout.putConstraint(SpringLayout.WEST, myLabel, x, SpringLayout.WEST, this);
        myLabelLayout.putConstraint(SpringLayout.NORTH, myLabel, y, SpringLayout.NORTH, this);
        return myLabel;
    }

    public void LocateTextFields(SpringLayout myTextFieldLayout)
    {
        txtQnum = LocateATextField(myTextFieldLayout, txtQnum, 20, 90, 25);
        txtTopic = LocateATextField(myTextFieldLayout, txtTopic, 20, 90, 50);
        txtQ = LocateATextField(myTextFieldLayout, txtQ, 20, 90, 75);
        txtA = LocateATextField(myTextFieldLayout, txtA, 20, 90, 100);
        txtB = LocateATextField(myTextFieldLayout, txtB, 20, 90, 125);
        txtC = LocateATextField(myTextFieldLayout, txtC, 20, 90, 150);
        txtD = LocateATextField(myTextFieldLayout, txtD, 20, 90, 175);
        txtAnswer = LocateATextField(myTextFieldLayout, txtAnswer, 20, 90, 225);
    }

    public TextField LocateATextField(SpringLayout myTextFieldLayout, TextField myTextField, int width, int x, int y)
    {
        myTextField = new TextField(width);
        add(myTextField);
        myTextFieldLayout.putConstraint(SpringLayout.WEST, myTextField, x, SpringLayout.WEST, this);
        myTextFieldLayout.putConstraint(SpringLayout.NORTH, myTextField, y, SpringLayout.NORTH, this);
        return myTextField;
    }

    public void LocateButtons(SpringLayout myButtonLayout)
    {
        btnSend = LocateAButton(myButtonLayout, btnSend, "Send", 275, 50, 80, 25);
        btnExit = LocateAButton(myButtonLayout, btnExit, "Exit", 275, 75, 80, 25);
    }

    public Button LocateAButton(SpringLayout myButtonLayout, Button myButton, String ButtonCaption, int x, int y, int w, int h)
    {
        myButton = new Button(ButtonCaption);
        add(myButton);
        myButton.addActionListener(this);
        myButtonLayout.putConstraint(SpringLayout.WEST, myButton, x, SpringLayout.WEST, this);
        myButtonLayout.putConstraint(SpringLayout.NORTH, myButton, y, SpringLayout.NORTH, this);
        myButton.setPreferredSize(new Dimension(w, h));
        return myButton;
    }
//</editor-fold>

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnSend)
        {
            String answer = txtAnswer.getText();
            if (answer.equalsIgnoreCase("A"))  {
                send();
            }
            else if (answer.equalsIgnoreCase("B"))  {
                send();
            }
            else if (answer.equalsIgnoreCase("C")) {
                send();
            }
            else if (answer.equalsIgnoreCase("D")) {
                send();
            }
            else {
                lblConnected.setText("Please enter a valid response");
            }            
            txtAnswer.requestFocus();
        }

        if (e.getSource() == btnExit)
        {
            txtAnswer.setText(".bye");
            send();
            System.exit(0);
        }
    }
   /****************************************************************
   FUNCTION:   connect

   ARGUMENTS:  serverName: the name of the server
               serverPort: the server port

   RETURNS:    void

   NOTES:      recieves the servername and serverport and uses these to open a connection between the instructor form and the chatclient
               stuff like that
****************************************************************/
    public void connect(String serverName, int serverPort)
    {
        log.info("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            log.info("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            log.error("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            log.error("Unexpected exception: " + ioe.getMessage());
        }
    }
  /****************************************************************
   FUNCTION:   send

   ARGUMENTS:  Relies on global variables

   RETURNS:    void

   NOTES:      Sends the students answer and notifies the student if the answer was correct or not
****************************************************************/
    private void send()
    {
        try
        {
            if (txtAnswer.getText().equalsIgnoreCase(correctAns)){
                lblConnected.setText("Correct");
            }
            else {
                lblConnected.setText("Incorrect");
            }
            streamOut.writeUTF("#" + "~" + txtQnum.getText() + "~" + txtTopic.getText() + "~" + txtAnswer.getText());
            streamOut.flush();
            clear();
        }
        catch (IOException ioe)
        {
            log.error("Sending error: " + ioe.getMessage());
            close();
        }
    }
/****************************************************************
   FUNCTION:   handle

   ARGUMENTS:  msg: the message recieved from the server

   RETURNS:    void

   NOTES:      handles the message recieved from the server. If it originates from a student it does nothing and hides the message.
               If the message is ".bye" the program is closed. Otherwise, log the message recieved and execute the println function.
****************************************************************/
    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            log.info("Good bye. Press EXIT button to exit ...");
            close();
        }
        if (msg.contains("#"))
        {
            msg = "";
        }
        else
        {
            log.info("Handle: " + msg);
            println(msg);
        }
    }
/****************************************************************
   FUNCTION:   open

   ARGUMENTS:  Relies on global variables

   RETURNS:    void

   NOTES:      see "connect" header
****************************************************************/
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client2 = new ChatClientThread2(this, socket);
        }
        catch (IOException ioe)
        {
            log.error("Error opening output stream: " + ioe);
        }
    }
/****************************************************************
   FUNCTION:   close

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      closes the connection between the instructor form and the server 
****************************************************************/
    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            log.error("Error closing ...");
        }
        client2.close();
        client2.stop();
    }
/****************************************************************
   FUNCTION:   println

   ARGUMENTS:  msg: the message recieved from the server

   RETURNS:    void

   NOTES:      if the message is too short to be an instructer question, update the "Connected" label to display the message
   *           else update the text fields to display the question data
****************************************************************/
    void println(String msg)
    {
        String[] temp = msg.split("~");
        if (temp.length < 2){
            lblConnected.setText(msg);
        }
        else {
            lblConnected.setText(msg);
            txtQnum.setText(temp[1]);
            txtTopic.setText(temp[2]);
            txtQ.setText(temp[3]);
            txtA.setText(temp[4]);
            txtB.setText(temp[5]);
            txtC.setText(temp[6]);
            txtD.setText(temp[7]);
            correctAns = temp[8];
        }
    }
/****************************************************************
   FUNCTION:   getParameters

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      updates the serverName and serverPort variables
****************************************************************/
    public void getParameters()
    {
        serverName = "localhost";
        serverPort = 4444;        
    }
/****************************************************************
   FUNCTION:   clear

   ARGUMENTS:  relies on global variables

   RETURNS:    void

   NOTES:      Clears all text fields
****************************************************************/
    public void clear()
    {
        txtQnum.setText("");
        txtTopic.setText("");
        txtQ.setText("");
        txtA.setText("");
        txtB.setText("");
        txtC.setText("");
        txtD.setText("");      
        txtAnswer.setText("");
    }

//<editor-fold defaultstate="collapsed" desc="WindowListener">
    public void windowClosing(WindowEvent we)
    {
        System.exit(0);
    }

    public void windowIconified(WindowEvent we)
    {
    }

    public void windowOpened(WindowEvent we)
    {
    }

    public void windowClosed(WindowEvent we)
    {
    }

    public void windowDeiconified(WindowEvent we)
    {
    }

    public void windowActivated(WindowEvent we)
    {
    }

    public void windowDeactivated(WindowEvent we)
    {
    }

//</editor-fold>
}
