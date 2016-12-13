/* CS460: Java echo server
  This server accpets multiple clients reading in there sent message character
  by character and sends same message back (i.e the name echo server). This
  server is multi thread and creates an instance of the EchoServerThread
  anytime an client is accepted. This is sponing of threads is handled really
  cleverly to deal with race conditions. This is accomplished by nesting
  the instantiate of the EchoServerThread and Thread, this means that it is
  impossible for two threads to be created with the same socket.

  Author:
  Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
  Date: Oct. 7th, 2016
*/
import java.io.*;
import java.net.*;
import java.lang.Thread;

class EchoServer {
  // class level variables for the server socket and the port number
  private static ServerSocket serverSocket;
  private static int port = 2594;

  // constructor
  public EchoServer(int port){
    // if passed a new port# apon instantiation than it reassigns the port
    EchoServer.port = port;
  }

  public static void main(String[] args) throws Exception {
    int num_client = 0;
    // creates an instance of a server socket object
    EchoServer.serverSocket = new ServerSocket(port);
    System.out.println( "Echo sever has been started");

    // server loop: infinitely loops and accepting clients
    while (true) {
      // nesting of instantiation makes it impossible for race conditions
      (new Thread(new EchoServerThread(serverSocket.accept(),num_client))).start();
      num_client++;
    }
  }
}
