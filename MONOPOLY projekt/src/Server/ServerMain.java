package Server;

import java.net.*;
import java.io.*;

public class ServerMain {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);

    }
}
