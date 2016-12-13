/*CS460: Echo Server using UDP

The purpose of this program is to create a simple server that echoes back any
message a client sends using datagrams aka UDP. This is accomplished using a datagram
socket and packet provided by the Java net library. This program is passed a port
number through the command line, creates a socket on that port number and continuously
loops receiving packets from the client.

Authors: Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
Date: Oct. 16th, 2016
*/

import java.net.*;
import java.util.*;

public class Server{

  public static void main( String args[]) throws Exception {
    // checks that there's the correct number of arguments
    if(args.length<1){
      System.err.println("Not enough arguments passed. Port required");
    }
    // assigns arguments
    int port = Integer.parseInt(args[0]);
    // creates a datagram socket using the passed port number
    DatagramSocket socket = new DatagramSocket(port);
    // creates an array of bytes for the receiving message
    byte message[] = new byte[256];
    // creates a packet where the receiving message will be stored
    DatagramPacket packet = new DatagramPacket(message, message.length);
    // server loop
    while(true){
      // receives a datagram from the socket
      socket.receive(packet);
      // instantiation of a string w/ the message from the packet
      String receivedMessage = new String(packet.getData(), 0, packet.getLength());
      // sends the message back through the socket
      socket.send(packet);
      // prints the message to standard out
      System.out.println(("Message from client:" + receivedMessage));
    }
  }
}
