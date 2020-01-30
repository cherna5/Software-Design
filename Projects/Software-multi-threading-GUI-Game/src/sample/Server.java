package sample;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnectionServer {

    private int port;

    public Server(int port, Consumer<Serializable> callback) {
        super(callback);
        // TODO Auto-generated constructor stub
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected String getIP() {
        // TODO Auto-generated method stub
        return "127.0.0.1";
    }

    @Override
    protected int getPort() {
        // TODO Auto-generated method stub
        return port;
    }

}