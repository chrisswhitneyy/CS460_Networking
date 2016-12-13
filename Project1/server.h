//
//  server.h
//  cs460-hw1
//
//  Created by Christopher Whitney on 9/20/16.
//  Copyright © 2016 Christopher Whitney. All rights reserved.
//

#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <unistd.h>
#include <netinet/in.h>
#include <pthread.h>


/* Function prototypes */
void* handle_client(void *client_socket);

/* Preprocessor directives */
#define SERVER_ADDR "127.0.0.1" // for client
#define PORT 2593              // port the server will listen on

#define FALSE 0
#define TRUE !FALSE

#define NUM_CONNECTIONS 1 // number of pending connections in the connection queue
#define MAX_NUM_CLIENTS 4 // max number of clients the service can accept
