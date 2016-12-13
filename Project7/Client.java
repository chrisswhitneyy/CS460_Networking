/*
  Turtle Graphics Client:
  The purpose of this program is a create a gui where the user enters a servers
  ip and port number to connect to for a distrubted white board. If the connection
  is successful then a pannel appears where the user can choice to put the "pen"
  up or down, enter the length, and specify the direction. A message containing
  all that information is then sent to the server to draw on the board.

  Created by: Clarissa Calderon & Christopher D. Whitney
  Last Modified: Nov. 19th, 2016
*/


import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;

public class Client {
  private String ip;
  private int port;
  private final JFrame frame = new JFrame();

  public static void main(String[] args){
    new Client();
  }

  public Client() {

    initFrame();
    setupPannel();

    //make sure the JFrame is visible
    frame.setVisible(true);
  }

  private void initFrame(){
    //make sure the program exits when the frame closes
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Turtle Graphics");

    //This will center the JFrame in the middle of the screen
    frame.setLocationRelativeTo(null);
  }
  private void setupPannel(){
    //The first JPanel contains a JLabel and JCombobox
    final JPanel pannel = new JPanel();
    final JLabel adLabel = new JLabel("Server Address");
    final JLabel portLabel = new JLabel("Server Port");
    final JTextField serverField = new JTextField("",4);
    final JTextField portField = new JTextField("",4);
    final JButton setupButton = new JButton("Setup");

    pannel.setLayout(new GridLayout(3,4));
    pannel.add(adLabel);
    pannel.add(serverField);
    pannel.add(portLabel);
    pannel.add(portField);
    pannel.add(setupButton);


    frame.add(pannel);

    //The ActionListener class is used to handle the
    //event that happens when the user clicks the button.
    //As there is not a lot that needs to happen we can
    //define an anonymous inner class to make the code simpler.
    setupButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent event)
    {
      String addressText = serverField.getText();
      String portText = portField.getText();

      if (portText.equals("") || addressText.equals("")){
        JOptionPane.showMessageDialog(frame, "Please enter server ip and port.",
              "INPUT ERROR ",JOptionPane.WARNING_MESSAGE);
      }else{
        ip = addressText;
        port = Integer.parseInt(portText);
        pannel.setVisible(false);
        frame.remove(pannel);
        frame.add(pannel);
        drawPannel();
      }

      frame.add(pannel);
    }
    });
    frame.repaint();
    frame.pack();
  }

  private void drawPannel(){
    final JPanel pannel = new JPanel();
    final JButton northButton = new JButton ("^");
    final JButton southButton = new JButton("v");
    final JButton westButton = new JButton("<");
    final JButton eastButton = new JButton(">");
    final JLabel lengthLabel = new JLabel("Enter length");
    final JTextField lengthField = new JTextField();
    final JButton upButton = new JButton("UP");
    final JButton downButton = new JButton("DOWN");
    String message;

    downButton.setEnabled(false);

    // Event listeners
    downButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        downButton.setEnabled(false);
        upButton.setEnabled(true);
      }

    });
    upButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        upButton.setEnabled(false);
        downButton.setEnabled(true);
      }

    });
    northButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if(isFieldEmpty(lengthField)){
          JOptionPane.showMessageDialog(frame, "A length is needed.",
                "INPUT ERROR ",JOptionPane.WARNING_MESSAGE);
                return;
        }
        final String message = formatMessage("N",lengthField,upButton);
        handleServer(message);

      }
    });
    southButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if(isFieldEmpty(lengthField)){
          JOptionPane.showMessageDialog(frame, "A length is needed.",
                "INPUT ERROR ",JOptionPane.WARNING_MESSAGE);
                return;
        }
        final String message = formatMessage("S",lengthField,upButton);
        handleServer(message);
      }
    });
    eastButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if(isFieldEmpty(lengthField)){
          JOptionPane.showMessageDialog(frame, "A length is needed.",
                "INPUT ERROR ",JOptionPane.WARNING_MESSAGE);
                return;
        }
        final String message = formatMessage("E",lengthField,upButton);
        handleServer(message);
      }
    });
    westButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if(isFieldEmpty(lengthField)){
          JOptionPane.showMessageDialog(frame, "A length is needed.",
                "INPUT ERROR ",JOptionPane.WARNING_MESSAGE);
          return;
        }
        final String message = formatMessage("W",lengthField,upButton);
        handleServer(message);
      }
    });

    pannel.setLayout(new GridLayout(4,2));
    pannel.add(northButton);
    pannel.add(eastButton);
    pannel.add(southButton);
    pannel.add(westButton);
    pannel.add(upButton);
    pannel.add(downButton);
    pannel.add(lengthLabel);
    pannel.add(lengthField);

    frame.setSize(400,400);
    frame.add(pannel);
    frame.pack();
    frame.repaint();

  }

  public void handleServer(String request){
    String response = null;
    // init the socket, writer and buffer reader to null
    Socket socket = null;
    PrintWriter out = null;

    // trys to create a socket object using the passed hostName and port #
    // if this is not possible the error is catched and printed to std error
    // also the program exits with code 1.
    try {
        // creates a socket instance
        socket = new Socket(this.ip, this.port);
        out = new PrintWriter(socket.getOutputStream(),true);

        out.println(request);
        // closes all the buffers, writers, and the socket
        out.close();
        socket.close();
    } catch (UnknownHostException e) {
        System.err.println("Couldn't find host: " + this.ip + "\n");
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't connection to: " + this.ip + "\n");
        System.exit(1);
    }

  }

  private boolean isFieldEmpty(JTextField field){
    String text = field.getText();
    if(text.equals("")){
      return true;
    }
    return false;
  }
  private String formatMessage (String dir, JTextField lengthField, JButton upButton){
    String message = dir + "." + lengthField.getText();
    if (upButton.isEnabled()){
      message = message + "." + "D.:)";
    }else{
      message = message + "." + "U.:)";
    }
    return message;
  }

}
