/*
CS460: Echo Client using UDP

The purpose of this program is to create a simple client that sends a message
to a server and displays back the response. This is accomplished using UDP datagrams.
A datagram packet is created using the passed message string and then is sent
to the passed host. This program takes three arguments through the command line,
the first is the host name, the second is the port number and the third is the message.

Command line arguments:
-args[0]: a string of the IP of the server
-args[1]: a int of the port number of the server
-args[2]: a message string to send to the server

Authors: Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
Date: Oct. 16th, 2016

*/

import java.net.*;
import java.util.*;

public class Client{

  public static void main( String args[] ) throws Exception{
    // checks that there's the correct number of arguments
    if(args.length<2){
      System.err.println("Not enough arguments passed. Server address and message required.");
    }
    // assigns arguments
    String address = args[0];
    int port = Integer.parseInt(args[1]);
    String message = args[2];
    // gets the InetAddress from the passed IP
    InetAddress ip = InetAddress.getByName(address);
    // creates a datagram socket to communicate w/ the server
    DatagramSocket socket = new DatagramSocket();
    // creates a byte array w/ the passed message
    byte message_bytes[] = message.getBytes();
    // creates a datagram packet w/ the message
    DatagramPacket packet = new DatagramPacket(message_bytes, message_bytes.length, ip, port);
    // sends the packet through the socket
    socket.send(packet);
    // receives a packet through the socket and reassigns it to the packet
    socket.receive(packet);
    // creates a string of the server response
    String response = new String(packet.getData( ));
    // displays the response to the standard out
    System.out.println(("Message from server: " + response));
  }
}
