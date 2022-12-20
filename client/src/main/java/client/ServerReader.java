package client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerReader extends Thread{
    Socket socket;
    static String str;
    static ObjectMapper objectMapper = new ObjectMapper();
    static ArrayList<User> users_list = new ArrayList<User>();

    public ServerReader(Socket socket, ArrayList<User> users_list){
        this.socket = socket;
        ServerReader.users_list = users_list;
    }
    
    @Override
    public void run(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                str = bufferedReader.readLine();
                String firstChar = String.valueOf(str.charAt(0));
                if(firstChar.equals(">")){
                    System.out.println(str);
                }else if(firstChar.equals("[")) {
                    users_list = objectMapper.readValue(getStr(), new TypeReference<ArrayList<User>>(){});
                    App.setUsers_list(users_list);
                }else if (firstChar.equals("-")){
                    listaUtenti();
                }else if (firstChar.equals("x")){
                    App.setLoop(false);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void listaUtenti(){
        int count = 0;
        for (int i = 0; i < users_list.size(); i++) {
            if(!users_list.get(i).getUser_name().equals(App.getUser().getUser_name())){
                System.out.println(users_list.get(i).getUser_name());
                count++;
            }
        }
        if(count == 0){
            System.out.println("+ Non ci sono altri utenti.");
        }
    }

    //setter&getter
    String getStr(){
        return str;
    }

    public void setStr(String str) {
        ServerReader.str = str;
    }
    
}
