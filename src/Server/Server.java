package Server;

import GameData.ServerInformationStorage;
import GameData.UsersStorage;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private boolean running;
    private int numOfClients;
    private ExecutorService pool;
    private int port;
    private UsersStorage usersStorage;
    private ServerInformationStorage serverInformationStorage;


    public Server (int port, UsersStorage usersStorage,
                   ServerInformationStorage serverInformationStorage)
    {
        pool = Executors.newCachedThreadPool ();
        running = true;
        numOfClients = 0;
        this.port = port;
        this.usersStorage = usersStorage;
        this.serverInformationStorage = serverInformationStorage;
    }


    public void startServer ()
    {
        try (ServerSocket welcomingConnection = new ServerSocket (port)) {
            System.out.println ("Server with port : " + port + ((port == 8083)? " (Load Server)" :
                    " (Save Server)") +
                    " Started \nWaiting for Client .....");
            int i = 1;
            while (running)
            {
                pool.execute (new ClientHandler (welcomingConnection.accept (),i,port,usersStorage,
                        serverInformationStorage));
                System.out.println ("Server with port : " + port + ((port == 8083)? " (Load Server)" :
                        " (Save Server)") +
                        " connected to new Client : Client " + i);
                i++;
            }
            pool.shutdown ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public void setRunning (boolean running) {
        this.running = running;
    }

    public int getNumOfClients () {
        return numOfClients;
    }






}
