package proj2;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class ftpclient {
Socket requestSocket; //socket connect to the server
DataInputStream in;
DataOutputStream out;
String message; //message send to the server
String MESSAGE; //capitalized message read from the server

void run()
{
    try{
        //create a socket to connect to the server
        System.out.println("Enter command 'ftpclient <port>'");
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
	    int inputPort = Integer.parseInt(userInput.split(" ")[1]);
        try{
            requestSocket = new Socket("localhost", inputPort);
        } catch (Exception e){
            System.out.println("Enter correct port no. and try again");
            run();
        }

        System.out.println("Connected to localhost in port " + inputPort);

        out = new DataOutputStream(requestSocket.getOutputStream());
        in = new DataInputStream(requestSocket.getInputStream());

        //get Input from standard input
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true)
        {
            System.out.print("\nEnter command: ");
            //read a sentence from the standard input
            message = bufferedReader.readLine();

            if (message.startsWith("upload")) {
				File file = new File(message.split(" ")[1]);
				if (file.exists() && !file.isDirectory()) {
	            	out.writeUTF("upload " + message.split(" ")[1] + " " + file.length());
                    String response = in.readUTF();
	            	if (response.equals("READY")) {
	                    FileInputStream fileIn = new FileInputStream(file);
	                    byte[] byteArray = new byte[1024];
	                    int inputBytes;
	                    while ((inputBytes = fileIn.read(byteArray)) > 0) {
	                        out.write(byteArray, 0, inputBytes);
	                    }
	                    fileIn.close();
	                    System.out.println("Upload successful!");
					}
	            }else {
					out.writeUTF("no_file_exists");
	                System.out.println("Could not locate the file");
	            }
	        } 

            if(message.startsWith("get")){
                out.writeUTF("get " + message.split(" ")[1]);
	            String response = (String)in.readUTF();
	            if (response.startsWith("FOUND")) {
	                long size = Long.parseLong(response.split(" ")[1]);
	                FileOutputStream fileOut = new FileOutputStream("new"+message.split(" ")[1]);
	                byte[] byteArray = new byte[1024];
	                int inputBytes;
	                while (size > 0 && (inputBytes = in.read(byteArray, 0, (int) Math.min(byteArray.length, size))) != -1) {
	                    fileOut.write(byteArray, 0, inputBytes);
	                    size -= inputBytes;
	                }
	                fileOut.close();
	                System.out.println("Download successful!");
	            } else {
	                System.out.println("Unable to find the file");
	            }
            }

            if(message.equals("exit")){
                in.close();
                out.close();
                requestSocket.close();
                break;
            }
        }
    }
    catch (ConnectException e) {
        System.err.println("Connection refused. You need to initiate a server first.");
    }

    catch(UnknownHostException unknownHost){
        System.err.println("You are trying to connect to an unknown host!");
    }

    catch(IOException ioException){
        ioException.printStackTrace();
    }

    finally{
        //Close connections
        try{
            in.close();
            out.close();
            requestSocket.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}

//send a message to the output stream
void sendMessage(String msg)
{
    try{
    //stream write the message
        out.writeUTF(msg);
        out.flush();
    }
    catch(IOException ioException){
        ioException.printStackTrace();
    }
}

//main method
public static void main(String args[])
    {
        ftpclient client = new ftpclient();
        client.run();
    }
}