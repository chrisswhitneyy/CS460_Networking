/*
  GUI: The following program uses JFrame to create a GUI that communicates
  with the specified server, using the custom made protocol to ask for
  information about the date and time.
  Created by Christopher Whitney and Clarissa Calderon on 11/1/16.
  Copyright © 2016 Christopher Whitney. All rights reserved.
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;

public class Gui {
  private String ip = "127.0.0.1";
  private int port = 2593;

  public static void main(String[] args){
    new Gui();
  }

  public Gui() {

    final JFrame guiFrame = new JFrame();

    //make sure the program exits when the frame closes
    guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    guiFrame.setTitle("Daytime Client");
    guiFrame.setSize(300,250);

    //This will center the JFrame in the middle of the screen
    guiFrame.setLocationRelativeTo(null);

    //Options for the JComboBox
    String[] timeFormats = {"Year","Month","Day","Hour","Minute","Second","Full"};


    //The first JPanel contains a JLabel and JCombobox
    final JPanel comboPanel = new JPanel();
    final JLabel comboLbl = new JLabel("Time format:");
    final JComboBox formats = new JComboBox(timeFormats);

    comboPanel.add(comboLbl,BorderLayout.NORTH);
    comboPanel.add(formats,BorderLayout.SOUTH);


    final JButton requestButton = new JButton( "Request");

    //The ActionListener class is used to handle the
    //event that happens when the user clicks the button.
    //As there is not a lot that needs to happen we can
    //define an anonymous inner class to make the code simpler.
    requestButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent event)
    {
      String selectedLabel = formats.getSelectedItem().toString();
      String request = labelToRequest(selectedLabel);
      String response = handleServer(request);

      comboPanel.remove(formats);
      comboPanel.remove(comboLbl);
      guiFrame.remove(requestButton);

      comboPanel.add( new JLabel(response));
      comboPanel.revalidate();
      guiFrame.repaint();
    }
    });

    //The JFrame uses the BorderLayout layout manager.
    //Put the two JPanels and JButton in different areas.
    guiFrame.add(comboPanel,BorderLayout.NORTH);
    guiFrame.add(requestButton,BorderLayout.SOUTH);

    //make sure the JFrame is visible
    guiFrame.setVisible(true);
  }

  public String handleServer(String request){
    String response = null;
    // init the socket, writer and buffer reader to null
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    // tries to create a socket object using the passed hostName and port #
    // if this is not possible the error is caught and printed to std error
    // also the program exits with code 1.
    try {
        // creates a socket instance
        socket = new Socket(this.ip, this.port);
        out = new PrintWriter(socket.getOutputStream(),true);

        out.println(request);


        // creates a BufferedReader instance
        // this is needed to format the message from the server
        in = new BufferedReader(new InputStreamReader(
                                    socket.getInputStream()));
        response = in.readLine();

        // closes all the buffers, writers, and the socket
        out.close();
        in.close();
        socket.close();
    } catch (UnknownHostException e) {
        System.err.println("Couldn't find host: " + this.ip + "\n");
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't connection to: " + this.ip + "\n");
        System.exit(1);
    }

     return response;
  }

  //@labelToRequest: Takes in a string of the displaying label format and
  // converts it to the request needed for the protocol.
  public String labelToRequest(String label){

    if (label == "Year"){
      return "Y.";
    }else if (label == "Month"){
      return "M.";
    }else if (label == "Day"){
      return "D.";
    }else if (label == "Hour"){
      return "H.";
    }else if (label == "Minute"){
      return "I.";
    }else if (label == "Second"){
      return "S.";
    }else if (label == "Full"){
      return "F.";
    }else{
      return"NOT VALID";
    }
  }
}
