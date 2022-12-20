package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static ArrayList<User> users_list = new ArrayList<User>();
    static ObjectMapper objectMapper = new ObjectMapper();
    static User user = new User();
    static boolean loop = true;
    public static void main( String[] args ) throws Exception
    {   
        try(Socket socket = new Socket("localhost", 1723);){
            ServerReader serverReader = new ServerReader(socket, users_list);
            BufferedReader bufferedReaderUsr = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter =  new PrintWriter(socket.getOutputStream(),true);
            Scanner scan = new Scanner(System.in);
            serverReader.start();
            while (loop) {
                ServerReader.sleep(100);
                if(serverReader.getStr() != null){
                    String firstChar = String.valueOf(serverReader.getStr().charAt(0));
                    if(firstChar.equals("/")){
                        if(serverReader.getStr().equals("/accesso")){
                            accesso(scan, printWriter);
                        }else if(serverReader.getStr().equals("/msg")){
                            msg(printWriter, bufferedReaderUsr);
                        }
                    }else if(firstChar.equals("#")) {
                        stampaUser(printWriter, bufferedReaderUsr);
                    }
                }
            }
            System.out.println("+ Disconnessione avvenuta.");
        }catch (Exception e) {
            System.out.println("+ Sei stato disconnesso forzatamente dal server.");
        }
    }

    public static void accesso(Scanner scan, PrintWriter printWriter) throws JsonProcessingException{
        boolean loop = false;
        while (!loop) {
            System.out.println("+ Inserire nome: ");
            String name = scan.nextLine();
            user.setUser_name(name);
            if(controlloUser(name)){
                loop = true;
                printWriter.println(objectMapper.writeValueAsString(user)); 
            }else{
                System.out.println("+ Il nome è già stato preso, inserire un nuovo nome.");
            }
        }
    }

    public static boolean controlloUser(String name){
        for (int i = 0; i < users_list.size(); i++) {
            if(users_list.get(i).getUser_name().equals(name)){
                return false;
            }
        }
        return true;
    }

    public static void msg(PrintWriter printWriter, BufferedReader bufferedReader) throws IOException {
        System.out.println("+ Digitare messagio: ");
        stampaUser(printWriter, bufferedReader);
    }

    public static void stampaUser(PrintWriter printWriter, BufferedReader bufferedReaderUsr) throws IOException{
        printWriter.println(bufferedReaderUsr.readLine());
    }



    //setter&getter

    public static ArrayList<User> getUsers_list() {
        return users_list;
    }

    public static void setUsers_list(ArrayList<User> users_list) {
        App.users_list = users_list;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        App.objectMapper = objectMapper;
    }

    public static User getUser() {
        return user;
    }

    public void setUser(User user) {
        App.user = user;
    }

    public static boolean isLoop() {
        return loop;
    }

    public static void setLoop(boolean loop) {
        App.loop = loop;
    } 
    
}