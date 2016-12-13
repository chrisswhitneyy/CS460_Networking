/*CS460: ServerThread

  Author:
  Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
  Date: Nov. 30th, 2016
*/
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;

public class ServerThread implements Runnable{
  // class level variables for the socket of the client, a buffer to read in
  // the clients message and an output stream to send a message to the client
  Socket client;
  int   client_number;
  BufferedReader clientReader;
  DataOutputStream clientWriter;
  Server server;

  public ServerThread(Socket client,int client_number,Server server) {
    this.client = client;
    this.client_number = client_number;
    this.server = server;
  }

  //@override
  public void run(){
    String[] tokens;
    int length;
    String direction;
    String penMode;

    try{
      this.clientReader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

      String buffer = null;
      String message = null;
      String delims = "[.]";

      while ((buffer = clientReader.readLine()) != null) {
          message = buffer;
      }
      System.out.println("Client "+ client_number + " said: " + message);
      tokens = message.split(delims);

      length = Integer.parseInt(tokens[1]);
      direction = tokens[0];
      penMode = tokens[2];

      // Pen is down
      if (direction.equals("N") && penMode.equals("D")){
        server.pannel.drawLine(server.currentLine.x,server.currentLine.y-length);
      }else if (direction.equals("S") && penMode.equals("D")){
        server.pannel.drawLine(server.currentLine.x,server.currentLine.y+length);
      }else if (direction.equals("E") && penMode.equals("D")){
        server.pannel.drawLine(server.currentLine.x+length,server.currentLine.y);
      }else if (direction.equals("W") && penMode.equals("D")){
        server.pannel.drawLine(server.currentLine.x-length,server.currentLine.y);
      }

      // Pen is up
      if (direction.equals("N") && penMode.equals("U")){
        server.currentLine.move(server.currentLine.x,server.currentLine.y-length);
      }else if (direction.equals("S") && penMode.equals("U")){
        server.currentLine.move(server.currentLine.x,server.currentLine.y+length);
      }else if (direction.equals("E") && penMode.equals("U")){
        server.currentLine.move(server.currentLine.x+length,server.currentLine.y);
      }else if (direction.equals("W") && penMode.equals("U")){
        server.currentLine.move(server.currentLine.x-length,server.currentLine.y);
      }

    }catch (IOException e){

    }
  }

}
