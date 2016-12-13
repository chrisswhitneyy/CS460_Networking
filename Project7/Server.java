/* CS460: Turtle Graphics Server


  Author:
  Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
  Date: Nov. 30th, 2016
*/
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.Thread;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

public class Server{
  // class level variables for the server socket and the port number
  private static ServerSocket serverSocket;
  private static int port = 2594;
  // GUI variables
  private JFrame frame = new JFrame("Server Drawing Window");
  public BasicPanel pannel = new BasicPanel();
  public Point currentLine = new Point(0,0);
  // constructor
  public Server(){
    setupDrawFrame();

  }

  public static void main(String[] args) throws Exception {
    Server inst = new Server();

    int num_client = 0;
    // creates an instance of a server socket object
    Server.serverSocket = new ServerSocket(port);
    System.out.println( "Sever has been started...");

    // server loop: infinitely loops and accepting clients
    while (true) {

      // nesting of instantiation makes it impossible for race conditions
      (new Thread(new ServerThread(serverSocket.accept(),num_client,inst))).start();
      num_client++;
    }
  }
  public void setupDrawFrame(){
    frame.setLayout(new GridLayout(4,2));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(pannel);
    frame.setSize(800, 600);
    frame.setLocation(220, 150);
    frame.setContentPane(pannel);
    frame.setVisible(true);
  }


  public class BasicPanel extends JPanel{
    public int x = 0;
    public int y = 0;
    // Create a constructor method
    public BasicPanel(){
      super();
    }
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      g.drawLine(currentLine.x,currentLine.y,this.x,this.y); // Draw a line from (10,10) to (150,150)
      currentLine.move(x,y);
    }
    public void drawLine (int x, int y){
      Graphics g = this.getGraphics();
      this.x = x;
      this.y = y;
      this.paintComponent(g);

    }
  }


}
