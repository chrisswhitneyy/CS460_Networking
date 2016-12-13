/* CS460 Echo Client:

  This program takes in a host name and port number through the
  command line arguments. It then tries to establish a socket with
  the host and if that's succesfull then it prompt the user for
  input. This input will then be sent to the server and the returning
  message will be displayed to the console.

  Last modified by:
  Christopher D. Whitney (cw729@nau.edu) & C. Calderon (cc2768@nau.edu)
  Last modified on: Sep. 29th, 2016
*/
import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {

        // if the number of arg is not 2 exit and display an error
        if (args.length != 2){
          System.err.println("No enough arugments. Host name and port required.");
          System.exit(1);
        }

        // gets the hostName and port# from the args
        String serverHostname = args[0];
        int portNumber = Integer.parseInt(args[1]);

        System.out.println ("Attemping to connect to host " + serverHostname
                            + " on port " + portNumber + ".");

        // init the socket, writer and buffer reader to null
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        // trys to create a socket object using the passed hostName and port #
        // if this is not possible the error is catched and printed to std error
        // also the program exits with code 1.
        try {
            // creates a socket instance
            socket = new Socket(serverHostname, portNumber);
            // creates a PrintWriter instance
            // this is needed to format the message for the server
            out = new PrintWriter(socket.getOutputStream(), true);
            // creates a BufferedReader instance
            // this is needed to format the message from the server
            in = new BufferedReader(new InputStreamReader(
                                        socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Couldn't find host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't connection to: " + serverHostname);
            System.exit(1);
        }

        // creates a BufferedReader instance
        // this is needed to stream the input string for the server
	       BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
         String userInput; // declares userInput strings

         System.out.print ("Enter input: ");
         // loops prompting the user for input
         while ((userInput = stdIn.readLine()) != null) {
           // writes userInput to the socket
           out.println(userInput);
           // prints servers returned message
           System.out.println("Message from server: " + in.readLine());
           System.out.print ("Enter input: ");
	      }
        // closes all the buffers, writers, and the socket
	       out.close();
         in.close();
         stdIn.close();
         socket.close();
    }
}
