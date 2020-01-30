package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Client extends Thread{
    private Socket       socket    = null; 
    private Scanner      input     = null; 
    private PrintWriter  output    = null;
    
    private final String address;
    private final int    port;

 // constructor to put ip address and port 
    public Client(String address, int port) 
    {
        this.address = address;
        this.port = port;
    } // end of constructor SecurityClient
    
    public void run(){
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("CLIENT::Connected");
            // sends output to the socket 
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(),true); 
        } 
        catch(IOException e) 
        { 
            System.out.println("CLIENT::"+e.getMessage());
            try {
				System.out.println("Connecting with IP address " + InetAddress.getLocalHost()
				        + " , to server with IP Address " + address
				        + " using port " +  port);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
            return;
        }
        String response;
        KnownCommands command;
        do {
            command = KnownCommands.getCommand(input.nextLine());
            System.out.println("CLIENT:<" + command);
            switch (command) {
                case CONNECTING:
                    output.println("CONNECTED");
                    System.out.println("CLIENT:>CONNECTED");
                    break;
                case GUESS:
                case HIGHER:
                case LOWER:
                    Scanner uInput = new Scanner(System.in);
                    System.out.print("ENTER A NUMBER FROM 1 to 100: ");
                    String out = uInput.nextLine();
                    output.println(out);
                    System.out.println("CLIENT:>"+out);
                    break;
                case WIN:
                    System.out.print("WOULD YOU LIKE TO PLAY AGAIN? (YES/NO):");
                    Scanner scanner = new Scanner(System.in);
                    String str = scanner.nextLine();
                    output.println(str);
                    System.out.println("CLIENT:>"+str);
                    break;
                case EXIT:
                    return;
                default:
                    break;
            }
        } while (true);
    } 
  
    
    public void exit(){
        output.println(KnownCommands.EXIT);
    } // end of method exit()

    public static void main(String args[]) 
    {
        int port     = 7177;
        String IPAddr  = "10.50.0.236";
        Thread client = new Client(IPAddr, port);
        client.start();
    } 
    
}