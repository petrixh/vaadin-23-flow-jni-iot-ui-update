#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h> 

int main(int argc, char *argv[])
{
    int sockfd = 0, n = 0, connFd = 0;
    char recvBuff[4];
    char sendBuff[4];
    struct sockaddr_in serv_addr;
    int count = 0;

    //printf("\n Starting client... \n");

        printf("\n Starting loop... \n");
        if(argc != 2)
        {
            printf("\n Usage: %s <ip of server> \n",argv[0]);
            return 1;
        }

    while (count < 10){
        memset(recvBuff, '0',sizeof(recvBuff));
        memset(sendBuff, '0',sizeof(sendBuff));
        if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
        {
            printf("\n Error : Could not create socket \n");
            return 1;
        }

        memset(&serv_addr, '0', sizeof(serv_addr));

        serv_addr.sin_family = AF_INET;
        serv_addr.sin_port = htons(5000);

        if(inet_pton(AF_INET, argv[1], &serv_addr.sin_addr)<=0)
        {
            printf("\n inet_pton error occured\n");
            return 1;
        }

        //printf("\n Connect... \n");
        if( (connFd = connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr))) < 0)
        {
           printf("\n Error : Connect Failed \n");
           return 1;
        }

        printf("\n Client sending %d\n", count);
        snprintf(sendBuff, sizeof(sendBuff), "%d\r\n", count);
        //sendBuf[0] = count + '0';
        write(sockfd, sendBuff, strlen(sendBuff));

        //printf("\n Read...\n");
        printf("\n Client reading response: ");
        while ( (n = read(sockfd, recvBuff, sizeof(recvBuff)-1)) > 0)
        {
            recvBuff[n] = 0;
            if(fputs(recvBuff, stdout) == EOF)
            {
                printf("\n Error : Fputs error\n");
            }
        }
        printf("\n");

        if(n < 0)
        {
            printf("\n Read error \n");
        }

        printf("\n Client sleeping for 2 seconds... \n");

        close(connFd);
        count++;
        sleep(2);
    }

    return 0;
}
