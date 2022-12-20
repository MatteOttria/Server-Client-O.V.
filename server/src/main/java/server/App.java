package server;
import java.net.*;
import java.util.ArrayList;

public class App 
{
    public static void main(String [] args) throws Exception
    {
        try(ServerSocket serverSocket = new ServerSocket(1723)){
            System.out.println("Server opened.");
            ArrayList<ClientHandler> handlerList = new ArrayList<ClientHandler>();
            String serverSocketName = "JServer_OV";
            while(true){
                Socket socket = serverSocket.accept();
                User user = new User();
                ClientHandler clientHandler = new ClientHandler(socket, serverSocketName, handlerList, user);
                handlerList.add(clientHandler);
                clientHandler.start();
            }   
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
