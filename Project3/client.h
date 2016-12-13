//
//  client.h
//  cs460-c-client
//
//  Created by Christopher Whitney & Clarissa Calderon on 10/7/16.
//  Copyright © 2016 Christopher Whitney. All rights reserved.
//


#include<arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#define SERVER_ADDR "127.0.0.1"
#define PORT 2594             
#define BUFFER_SIZE 1024
