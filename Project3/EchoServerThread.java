/*CS460: EchoServerThread
  Defines the EchoServerThread which only has three properties, the socket of
  the client, a stream to the read a message from the client, and a stream to
  write to the client. It implements Runnable which requires run() which reads
  in the message from the client and outputs it back to them.

  Author:
  Christopher D. Whitney (cw729@nau.edu) & Clarissa Calderon (cc2768@nau.edu)
  Date: Oct. 7th, 2016
*/
import java.io.*;
import java.net.*;

class EchoServerThread implements Runnable{
  // class level variables for the socket of the client, a buffer to read in
  // the clients message and an output stream to send a message to the client
  Socket client;
  int   client_number;
  BufferedReader clientReader;
  DataOutputStream clientWriter;

  public EchoServerThread(Socket client,int client_number) {
    this.client = client;
    this.client_number = client_number;
  }
  //@override
  public void run(){

    try{
      this.clientReader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
      this.clientWriter = new DataOutputStream(this.client.getOutputStream());

      String message = null;
      while ((message = clientReader.readLine()) != null) {
          System.out.println("Client "+ client_number + " said: " + message);
          clientWriter.writeBytes(message);
      }
    }catch (IOException e){

    }
  }
}
