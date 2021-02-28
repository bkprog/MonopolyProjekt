package Application.ClientApplication;

import java.net.*;
import java.io.*;
import java.rmi.UnknownHostException;

public class ClientMain {

    public static void main(String[] args){
        int port = 2556;
        String hostname = "";
        try(Socket socket = new Socket(hostname,port)){
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String diceFirst  = reader.readLine();
            System.out.println("Wyrzucono koscmi: " + diceFirst);
            String diceSecound  = reader.readLine();
            System.out.println("Wyrzucono koscmi: " + diceSecound);
            int sum = Integer.parseInt(diceFirst) + Integer.parseInt(diceSecound);
            System.out.println("Suma oczek z kostek to: " + sum);

        }
        catch(UnknownHostException ex){
            System.out.println("Server not found: " + ex.getMessage());
        }
        catch(IOException ex){
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
