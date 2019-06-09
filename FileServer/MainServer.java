/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author stygianlgdonic
 */
public class MainServer {

    public static void main(String args[]) throws IOException {

        // Creating socket on port 5000
        ServerSocket ss = new ServerSocket(5000);

        String filename;

        while (true) {

            Socket s = null;

            try {
                System.out.println("Waiting for request");

                s = ss.accept();

                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                filename = din.readUTF();
                System.out.println("Connected With " + s.getInetAddress().toString()
                        + "requesting for file:" + filename);

                // Initializing Thread
                Thread ct;
                ct = new ClientThread(s, filename, din, dout);
                ct.start(); // thread start

            } catch (Exception ex) {
                s.close(); // Closing Socket
                ex.printStackTrace();
            }
        }
    }

}
