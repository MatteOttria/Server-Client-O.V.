package server;

public class User {
    String user_name;

    public User(String user_name){
        this.user_name = user_name;
    }

    public User() {
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}

