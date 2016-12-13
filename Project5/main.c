//
//  main.c
//  cs460_timeserver
//
//  This program uses the IP and port number defined in the header to connect to a time server,
//  remove the OTM character from the response and is a server to a client that servers them
//  there request information.
//
//  Created by Christopher Whitney and Clarissa Calderon on 11/1/16.
//  Copyright © 2016 Christopher Whitney. All rights reserved.
//

#include <stdio.h>
#include "client.h"
#include "server.h"

pthread_mutex_t lock; // declaration of global mutex lock

void* handle_client(void* in_client_socket);
char* read_line(int fd);
char* communicateWithTimeserver();
char* get_info(char*,char);

int main(int argc, const char * argv[]) {
    int server_socket;                 // descriptor of server socket
    struct sockaddr_in server_address; // for naming the server's listening socket
    int client_socket;
    int num_clients = 0;               // count of number of clients
    pthread_t clients[MAX_NUM_CLIENTS];// array of threads for clients

    pthread_mutex_init(&lock, NULL);   // init mutex

    // create unnamed network socket for server to listen on
    if ((server_socket = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("Error creating socket");
        exit(EXIT_FAILURE);
    }

    // name the socket (making sure the correct network byte ordering is observed)
    server_address.sin_family      = AF_INET;           // accept IP addresses
    server_address.sin_addr.s_addr = htonl(INADDR_ANY); // accept clients on any interface
    server_address.sin_port        = htons(CLIENTSERVER_PORT);       // port to listen on

    // binding unnamed socket to a particular port
    if (bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1) {
        perror("Error binding socket");
        exit(EXIT_FAILURE);
    }

    // listen for client connections (pending connections get put into a queue)
    if (listen(server_socket, NUM_CONNECTIONS) == -1) {
        perror("Error listening on socket");
        exit(EXIT_FAILURE);
    }
    printf("Client server timeserver is running... \n");
    // server loop
    while (TRUE) {

        // accept connection to client
        if ((client_socket = accept(server_socket, NULL, NULL)) == -1) {
            perror("Error accepting client");
        } else {
            printf("\nAccepted client\n");

            if(num_clients<MAX_NUM_CLIENTS){
                // lock mutex
                pthread_mutex_lock(&lock);
                printf("before handle socket %i \n",client_socket);
                // create threads of handle_client
                if(pthread_create(&clients[num_clients],NULL,handle_client,(void *) &client_socket)){
                    fprintf(stderr, "Error creating thread\n");
                    exit(EXIT_FAILURE);
                }
                num_clients ++;
            }else{
                perror("Max number of clients already accepted.\n");
                break;
            }

        }

    }
} // main

/*
 * handle client
 */
void* handle_client(void* in_client_socket) {
    int client_socket = *((int*)in_client_socket); // convert void point back to int
    char input;
    char* response;
    char* timeserv_response = communicateWithTimeserver();;

    // unlock mutex
    pthread_mutex_unlock(&lock);


    // read char from client
    read(client_socket, &input, sizeof(char));

    printf("Client message: %c\n",input);
    printf("Time server: %s\n",timeserv_response);

    response = get_info(timeserv_response, input);
    printf("Response: %s \n",response);

    write(client_socket, response, strlen(response)*sizeof(char));
    printf("Message to client: %s \n",response);

    // cleanup
    if (close(client_socket) == -1) {
        perror("Error closing socket\n");
        exit(EXIT_FAILURE);
    } else {
        printf("Closed socket to client, exit\n");
    }
    return NULL;
}

char* communicateWithTimeserver(){
    int sock;
    struct sockaddr_in server;
    char* line = malloc(sizeof(BUFFER_SIZE));

    // creates socket
    sock = socket(AF_INET , SOCK_STREAM , 0);
    if (sock == -1){
        perror("Could not create socket\n");
        exit(EXIT_FAILURE);
    }

    // creates sockaddr_in struc with server and protocol info
    server.sin_addr.s_addr = inet_addr(SERVER_ADDR);
    server.sin_family = AF_INET;
    server.sin_port = htons(PORT);

    // connects to server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0){
        perror("Connect failed. Error\n");
        exit(EXIT_FAILURE);
    }

    // writes message sock fd
    if (write(sock, " ", BUFFER_SIZE) < 0){
        perror("Error writing to socket\n");
        exit(EXIT_FAILURE);
    }

    // calls read line
    line = read_line(sock);

    // closes socket
    close(sock);

    return line;
}
//@read_line: This function takes in a file descriptor and returns
//  a character pointer which points to the string received but it no
//  delimited by the OTM char defined in the header.
char* read_line(int socket){
    // located space for the line, needed because it's a local variable
    char* line = (char *) malloc(sizeof(BUFFER_SIZE));
    char* pt; // pointer used to increment through the line

    // read message from socket, error if it can't
    if (read(socket, line, BUFFER_SIZE) < 0)
        perror("Error reading from socket.\n");

    // sets the beginning pointer of line to pt
    pt = line;

    // increments through pt until OTM char found
    while (*pt != OTM) {
        pt ++;
    }
    // sends end of pt* to null
    *pt = '\0';
    // adds null terminator to the line
    strcat(line, "\0");

    return line;
} // read_line

char* get_info(char* full_time,char id){

    char* token;
    int i = 0;
    char* tokens [9];
    char* date_tokens [3];
    char* time_tokens [3];

    // parses full time server response
    token = strtok(full_time, " ");
    while (token != NULL) {
        tokens[i] = token;
        i++;
        token = strtok(NULL, " ");
    }

    i = 0; // resets i
    // parses date information
    token = strtok(tokens[1], "-");
    while (token!=NULL) {
        date_tokens[i] = token;
        i++;
        token = strtok(NULL, "-");
    }

    i=0; // resets i
    // parses time information
    token = strtok(tokens[2], ":");
    while (token != NULL) {
        time_tokens[i] = token;
        i++;
        token = strtok(NULL, ":");
    }

    // returns proper info based on message passed by client
    if(id == 'Y'){
        return date_tokens[0];
    }else if (id == 'M'){
        return date_tokens[1];
    }else if (id == 'D'){
        return date_tokens[2];
    }else if (id == 'H'){
        return time_tokens[0];
    }else if (id == 'I'){
        return time_tokens[1];
    }else if (id == 'S'){
        return time_tokens[2];
    }
    // if message not found returns full time
    return full_time;
}




