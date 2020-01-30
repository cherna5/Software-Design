package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class NetworkConnectionServer {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    private HashMap<String, String> playerNames = new HashMap<String, String>();

    public NetworkConnectionServer(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);

    }

    public ConnThread getConnThread() {
        return connthread;
    }

    public int getClients() {
        return clients.size();
    }

    public String printClients() {
        String s = new String("Number of Clients: " + String.valueOf(clients.size()));
        return s;
    }

    public ArrayList<ClientThread> getClientList() {
        return clients;
    }
    public void startConn() throws Exception{
        connthread.start();
    }

    public void send(Serializable data) throws Exception{
        for (ClientThread c : clients) {
            c.out.writeObject(data);
        }
    }

    public void sendOne(Serializable data, String s) throws Exception {
        Integer i = Integer.parseInt(s);
        clients.get(i - 1).out.writeObject(data);
    }

    public void closeConn() throws Exception {
        for (ClientThread c : clients ) {
            c.connection.close();
        }

        clients.clear();

        connthread.server.close();

    }

    public void removeExtraClient() {
        clients.remove(4);
    }

    public void closeOne(String s) {
        Integer i = Integer.parseInt(s);
        try {
            clients.get(i - 1).connection.close();
            clients.remove(i - 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addPlayerName(String s1, String s2) {
        playerNames.replace(s1, s2);
    }

    public HashMap<String, String> getPlayerNames() {
        return playerNames;
    }

    abstract protected boolean isServer();
    abstract protected String getIP();
    abstract protected int getPort();

    class ConnThread extends Thread{
        ServerSocket server;

        public ServerSocket getServer() {
            return server;
        }
        public void run() {
            try {
                this.server = new ServerSocket(getPort());
                while(true) {
                    ClientThread t1 = new ClientThread(server.accept());
                    clients.add(t1);
                    playerNames.put(String.valueOf(clients.size()), "");
                    // Sends the updated size of the client list to all clients
                    t1.start();
                    send("NewClientSize " + String.valueOf(clients.size()));
                    //t1.start();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class ClientThread extends Thread{
        private Socket connection;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientThread (Socket s) throws IOException{
            this.connection = s;
            this.out = new ObjectOutputStream(connection.getOutputStream());
            this.in = new ObjectInputStream(connection.getInputStream());
        }

        public void run() {
            try {
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