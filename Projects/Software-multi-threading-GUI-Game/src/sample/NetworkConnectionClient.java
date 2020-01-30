package sample;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class NetworkConnectionClient {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    private ArrayList<String> outMessages = new ArrayList<String>();
    private Integer totalClients = 0;
    private Integer clientNum = -1;

    public NetworkConnectionClient(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);


    }

    public void startConn() throws Exception{
        connthread.start();
    }

    public void send(Serializable data) throws Exception{
        connthread.out.writeObject(data);
    }

    public void closeConn() throws Exception{
        totalClients -= 1;
        connthread.socket.close();
    }

    public ConnThread getConn() {
        return connthread;
    }

    public ArrayList<String> getMessages() {
        return outMessages;
    }

    public void updateTotalClients(Integer i) {
        totalClients = i;
    }

    public Integer getTotalClients() {
        return totalClients;
    }

    public void setClientNum(Integer i) {
        clientNum = i;
    }

    abstract protected boolean isServer();
    abstract protected String getIP();
    abstract protected int getPort();

    class ConnThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;

        public Socket getSocket() { return socket; }

        public ObjectOutputStream getOut() { return out; }

        public void run() {
            try {
                Socket socket = new Socket(getIP(), getPort());
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                }

            }
            catch(Exception e) {
                callback.accept("Connection Closed");
            }
        }
    }
}