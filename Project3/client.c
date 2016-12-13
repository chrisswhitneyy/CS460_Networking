//
//  client.c
//  cs460-c-client
//
//  This program connects to a server defined by an address and
//  port in the header. It reads input from the user sends it to
//  the server and then prints out the reply.
//
//  Created by Christopher Whitney & Clarissa Calderon on 10/7/16.
//  Copyright © 2016 Christopher Whitney. All rights reserved.
//
#include "client.h"

int main(int argc , char *argv[])
{
    int sock;
    struct sockaddr_in server;
    char message[BUFFER_SIZE] , server_reply[BUFFER_SIZE];
    
    // creates socket
    sock = socket(AF_INET , SOCK_STREAM , 0);
    if (sock == -1)
    {
        printf("Could not create socket");
    }
    
    // creats sockaddr_in struc with server and protocol info
    server.sin_addr.s_addr = inet_addr(SERVER_ADDR);
    server.sin_family = AF_INET;
    server.sin_port = htons(PORT);
    
    // connects to server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        perror("Connect failed. Error");
        return 1;
    }
    
    // get message from stdin
    printf("Please enter message: ");
    // resets message array to zeros
    bzero(message,BUFFER_SIZE);
    // gets input from stdin
    fgets(message, BUFFER_SIZE, stdin);
        
    // writes message sock fd
    if (write(sock, message, BUFFER_SIZE) < 0)
        printf("Error writing to socket");
    
     // resets server_reply array
     bzero(server_reply,BUFFER_SIZE);
    
    // read message from socket
    if (read(sock, server_reply, BUFFER_SIZE) < 0)
         printf("Error reading from socket");
    
    // prints server reply
    printf("Reply from server: %s\n", server_reply);
    
    // closes socket
    close(sock);
    
    return 0;
}
