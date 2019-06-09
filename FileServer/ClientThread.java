/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author stygianlgdonic
 */
public class ClientThread extends Thread {

    private Socket s;
    private String filename;
    private DataInputStream din;
    private DataOutputStream dout;

    public ClientThread(Socket s, String filename,
            DataInputStream din, DataOutputStream dout) {
        this.s = s;
        this.filename = filename;
        this.din = din;
        this.dout = dout;
    }

    @Override
    public void run() {
            try {

                System.out.println("Sending File: " + filename);

                byte check;

                //server's directory to search from 
                File f = new File("C:\\Users\\mbird\\Desktop\\FileBank\\"
                        + filename);

                if (f.exists() == true) {

                    // if requested file exists
                    check = 1;
                    dout.writeByte(check);
                    dout.flush();

                } else {

                    // if requested file exists
                    check = 0;
                    dout.writeByte(check);
                    dout.flush();
                    System.out.println("Error: FileNotFound");
                    
                    closeConnection();
    
                }
                
                dout.writeUTF(filename);
                dout.flush();
                
                // For Sending Server's File
                FileInputStream fin = new FileInputStream(f);
                long sz = (int) f.length();
                byte b[] = new byte[1024];
                int read;
                dout.writeUTF(Long.toString(sz));
                dout.flush();
                System.out.println("Size: " + sz);
                System.out.println("Buf size: " + s.getReceiveBufferSize());
                
                while ((read = fin.read(b)) != -1) {
                    dout.write(b, 0, read);
                    dout.flush();
                }
                fin.close();

                System.out.println("..ok");
                dout.flush();

                System.out.println("Send Complete");
                dout.flush();
                closeConnection(); // Closing Connection
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occured");
                
            }

    }

    public void closeConnection() throws IOException {
        System.out.println("Connection has been closed with "
                + s.getInetAddress().toString());
        s.close();
        dout.close();
        din.close();

    }

}
