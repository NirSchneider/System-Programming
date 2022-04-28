package bgu.spl.net.impl.BGRS;

public class Admin implements User{

    private String userName;
    private String password;
    private boolean isConnedcted;


    public Admin(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        this.isConnedcted = false;
    }

    //Getters
    public String getUserName(){return this.userName;}
    public String getPassword(){return this.password;}
    public boolean getIsConnected(){return this.isConnedcted;}

    //Setters
    public void setConnedcted(boolean connedcted) {isConnedcted = connedcted;}

}
