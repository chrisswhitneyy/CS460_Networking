//
//  client.h
//  cs460_timeserver
//
//  Created by Christopher Whitney and Clarissa Calderon on 11/1/16.
//  Copyright © 2016 Christopher Whitney. All rights reserved.
//

#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#define SERVER_ADDR "128.138.140.44"
#define PORT 13
#define BUFFER_SIZE 80
#define OTM '*'


