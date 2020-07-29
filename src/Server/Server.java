package Server;

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


    public Server (int port)
    {
        pool = Executors.newCachedThreadPool ();
        running = true;
        numOfClients = 0;
        this.port = port;
        loadUserStorage ();
    }


    public void startServer ()
    {
        try (ServerSocket welcomingConnection = new ServerSocket (port)) {
            System.out.println ("Server.Server with port : " + port + ((port == 8083)? " (Load Server.Server)" :
                    " (Save Server.Server)") +
                    " Started \nWaiting for Client .....");
            int i = 1;
            while (running)
            {
                pool.execute (new ClientHandler (welcomingConnection.accept (),i,port,usersStorage));
                System.out.println ("Server.Server with port : " + port + ((port == 8083)? " (Load Server.Server)" :
                        " (Save Server.Server)") +
                        " connected to new Client : Client " + i);
                i++;
            }
            pool.shutdown ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        } finally {
            saveUserStorage ();
        }
    }

    public void setRunning (boolean running) {
        this.running = running;
    }

    public int getNumOfClients () {
        return numOfClients;
    }

    protected void saveUserStorage ()
    {
        try (ObjectOutputStream out = new ObjectOutputStream (
                new FileOutputStream (
                        new File ("./Data/usersData.ser")))){

            out.writeObject (usersStorage);
        } catch (IOException e)
        {
            System.out.println ("some thing went wrong in save");
        }
    }

    private void loadUserStorage ()
    {
        try (ObjectInputStream in = new ObjectInputStream (
                new FileInputStream (
                        new File ("./Data/usersData.ser")))){
            Object o = in.readObject ();
            usersStorage =  (UsersStorage) o;

        } catch (FileNotFoundException e)
        {
            usersStorage = new UsersStorage ();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println ("some thing was wrong in load");
        }
    }




}
