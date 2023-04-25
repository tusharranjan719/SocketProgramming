# SocketProgramming
Client server architecture which supports multiple clients using multithreading in socket programming

## How to run:

1)	Unzip the files to you local directory
2)	Open terminal and navigate to the directory where it was extracted
3)	Start the server using the following command:
java ftpserver.java
4)	Once server starts open a new terminal and navigate to same directory
5)	Start the client using the following command:
java ftpclient.java

## Server side:

1)	After running the file wait for server to start

## Client side:

1)	Once client(s) start wait for the enter command prompt to be displayed, then enter the server’s port no when asked in the format:
ftpclient <portNo>

2)	Once confirmation is received from server, as follows:
Connected to server on port <portNo>

3)	After successful connection you can use below commands:
a.	get <filename> - Downloads the file from server to client
b.	upload <filename> - Uploads the file to server

4)	To terminate client enter “exit”


## Error handling:

1)	If empty command is entered anytime during execution
Enter valid command
2)	If port no doesn’t match with server’s port
Enter valid port no and try again
3)	If file doesn’t exist on server
Unable to locate the file
