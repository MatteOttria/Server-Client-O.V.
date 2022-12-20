package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread{
    
    static private ArrayList<User> users_list = new ArrayList<User>();
    private Socket socket;
    private PrintWriter printWriter;
    private User user;
    static private String serverName;
    static private ArrayList<ClientHandler> handlerList = new ArrayList<>();
    static private ObjectMapper objectMapper = new ObjectMapper();
    static private String str = ""; 

    public ClientHandler(Socket socket, String serverName ,ArrayList<ClientHandler> handlerList, User user){
        this.socket = socket;
        ClientHandler.handlerList = handlerList;
        ClientHandler.serverName = serverName;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferReader = new BufferedReader(streamReader);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Client connected.");
            gestore(printWriter, bufferReader);
            disconnessione();
            invioLista(printWriter);
        } catch (Exception e) {
            System.out.println("La socket " + getSocket() + " si è disconnessa forzatamente.");
        }
    }

    public void gestore(PrintWriter printWriter, BufferedReader bufferedReader)throws Exception{
        accesso(printWriter, bufferedReader);
        printWriter.println("> Benvenuto, sei connesso al server " + serverName);
        printWriter.println("> I comandi disponibili sono: ");
        printWriter.println("> /<nome_utente>, Per inviare un messaggio in privato.");
        printWriter.println("> /all, Per inviare un messaggio a tutti.");
        printWriter.println("> /list, Per stamapre tutta la lista degli utenti connessi.");
        printWriter.println("> /rename, Per cambiare nome.");
        printWriter.println("> /disconnect, Per disconnettersi.");
        while (true) {
            printWriter.println("#");
            str = bufferedReader.readLine();
            str = str.replaceAll("[/]", "");
            if (controlloUtenti(str)) {
                invioMsgPrivato(str, printWriter, bufferedReader);
            }else if (str.equals("all")) {
                invioMsgBroadcast(printWriter, bufferedReader);
            }else if (str.equals("list")) {
                printWriter.println("-");
            }else if(str.equals("disconnect")){
                printWriter.println("x");
                System.out.println("> User disconnected : " + getUser().getUser_name());
                return;
            }else if(str.equals("rename")){
                disconnessione();
                accesso(printWriter, bufferedReader);
            }else{
                printWriter.println("> Comando non riconosciuto.");
            }
        }
    }

    public void disconnessione(){
        for (ClientHandler  client : handlerList) {
            String name = client.getUser().getUser_name();
            if (name == getUser().getUser_name()) {
                users_list.remove(getUser());
            }
        }
        return;
    }

    public void invioMsgBroadcast(PrintWriter printWriter, BufferedReader bufferedReader){
        try {
            printWriter.println("/msg");
                str = bufferedReader.readLine();
            for (ClientHandler  client : handlerList) {
                client.printWriter.println("> "+ user.getUser_name() + " ti ha inviato: " +str);
            }
            return;    
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void accesso(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        str = objectMapper.writeValueAsString(users_list);
        printWriter.println(str);
        printWriter.println("/accesso");
        str = bufferedReader.readLine();
        User us = objectMapper.readValue(str, User.class);
        setUser(us);
        users_list.add(user);
        invioLista(printWriter);
        printWriter.println("> Il nome è stato impostato come: " + getUser().getUser_name());
        return;
    }


    public  void invioLista(PrintWriter printWriter){
        try {
            for (ClientHandler  client : handlerList) {
                str = objectMapper.writeValueAsString(users_list);
                client.printWriter.println(str);
            }
            return;    
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean controlloUtenti(String str){
        for (int i = 0; i < users_list.size(); i++) {
            if (users_list.get(i).getUser_name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void invioMsgPrivato(String user_name, PrintWriter printWriter, BufferedReader bufferedReader) throws IOException{
        printWriter.println("/msg");
        posizioneUtente(user_name, printWriter, bufferedReader);
    }


    public void posizioneUtente(String user_name, PrintWriter printWriter, BufferedReader bufferedReader) throws IOException{
        str = bufferedReader.readLine();
        for (ClientHandler  client : handlerList) {
            String name = client.getUser().getUser_name();
            if (name.equals(user_name)) {
                client.printWriter.println("> "+ user.getUser_name() + " ti ha inviato: " +str);
            }
        }
        return;
    }


    //setter&getter

    public  ArrayList<User> getUsers_list() {
        return users_list;
    }

    public  void setUsers_list(ArrayList<User> users_list) {
        ClientHandler.users_list = users_list;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public  String getServerName() {
        return serverName;
    }

    public  void setServerName(String serverName) {
        ClientHandler.serverName = serverName;
    }

    public  ArrayList<ClientHandler> getHandlerList() {
        return handlerList;
    }

    public  void setHandlerList(ArrayList<ClientHandler> handlerList) {
        ClientHandler.handlerList = handlerList;
    }

    public  ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public  void setObjectMapper(ObjectMapper objectMapper) {
        ClientHandler.objectMapper = objectMapper;
    }

    public  String getStr() {
        return str;
    }

    public  void setStr(String str) {
        ClientHandler.str = str;
    }

    
}