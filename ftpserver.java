package proj2;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class ftpserver {
    private static final int sPort = 1108;

    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(sPort);
        int clientNum = 1;
        try {
            while(true) {
                new Handler(listener.accept(),clientNum).start();
                System.out.println("Client " + clientNum + " is connected!");
                clientNum++;
            }
        } finally {
            listener.close();
        }
    }
    /**
    * A handler thread class. Handlers are spawned from the listening
    * loop and are responsible for dealing with a single client's requests.
    */
    private static class Handler extends Thread {
        private String message; //message received from the client
        private Socket connection;
        DataInputStream in;
        DataOutputStream out;
        private int no; //The index number of the client

        public Handler(Socket connection, int no) {
            this.connection = connection;
            this.no = no;
        }

        public void run() {
            try{
                //initialize Input and Output streams
                out = new DataOutputStream(connection.getOutputStream());
                in = new DataInputStream(connection.getInputStream());

                try{
                    while(true)
                    {
                        message = (String)in.readUTF();

                        if (message.startsWith("upload")) {
                            String fileName = message.split(" ")[1];
                            long size = Long.parseLong(message.split(" ")[2]);
                            out.writeUTF("READY");
                            byte[] byteArray = new byte[1024];
                            int inputBytes;  
                            FileOutputStream fileOut = new FileOutputStream(new File("new"+fileName));
                            while (size > 0 && (inputBytes = in.read(byteArray, 0, (int) Math.min(byteArray.length, size))) != -1) {
                                fileOut.write(byteArray, 0, inputBytes);
                                size -= inputBytes;
                            }
                            fileOut.close();  
                        }

                        if (message.startsWith("get")) {
                            String fileName = message.split(" ")[1];
                            File file = new File(fileName);
                            if (file.exists() && !file.isDirectory()) {
                                out.writeUTF("FOUND " + file.length());   
                                FileInputStream fileIn = new FileInputStream(file);
                                byte[] byteArray = new byte[1024];
                                int inputBytes;
                                while ((inputBytes = fileIn.read(byteArray)) > 0) {
                                    out.write(byteArray, 0, inputBytes);
                                }
                                fileIn.close();
                            } else {
                                out.writeUTF("ERROR");
                            }
                        }
                        //show the message to the user
                        System.out.println("Receive message: " + message + " from client " + no);
                    }
                }
                catch(Exception classnot){
                    System.err.println("Disconnect with Client " + no);
                }
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client " + no);
            }
        finally{
                //Close connections
                try{
                    in.close();
                    out.close();
                    connection.close();
                }
                catch(IOException ioException){
                    System.out.println("Disconnect with Client " + no);
                }
            }
        }
    }
}
