package org.panda_lang.panda.util;

import org.panda_lang.panda.Panda;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Catcher extends Thread
{

    private final Panda panda;
    private final ServerSocket serverSocket;

    public Catcher(Panda panda, int port) throws IOException
    {
        this.panda = panda;
        this.serverSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress());
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                final Socket socket = serverSocket.accept();
                final DataInputStream input = new DataInputStream(socket.getInputStream());
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String command = input.readUTF();
                            panda.exec(command);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
