package Application.Client;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private static Socket client;

    public static void main(String[] args){
        String hostname = "localhost";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nickname: ");
        String nickname = scanner.nextLine();
        try(Socket client = new Socket(hostname,2115)){
            DataInputStream dIn = new DataInputStream(client.getInputStream());
            DataOutputStream dOut = new DataOutputStream(client.getOutputStream());
            dOut.writeUTF(nickname);
            while(true){
                String info = dIn.readUTF();
                System.out.println(info);
            }

        }
        catch(IOException ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }
}
