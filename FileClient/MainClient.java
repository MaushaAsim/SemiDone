/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author stygianlgdonic
 */
public class MainClient {
    
    static FileOutputStream fos;
    static DataInputStream din;
    static DataOutputStream dout;
    
    public static void main(String args[]) throws Exception {

        // Client's file request
        System.out.println("Enter File Name: ");
        Scanner sc = new Scanner(System.in);
        String filename1 = sc.nextLine();

        // Server's address
        String address = "";
        System.out.println("Enter Server Address: ");
        address = sc.nextLine();

        Socket s = new Socket(address, 5000);
        //connection setup

        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

        dout.writeUTF(filename1);

        String filename = "";

            dout.flush();

            byte check = din.readByte();
            if (check == 1) {
                // if file found Start recieving

                startRecieving(filename);
                
                System.out.println("Closing Connection");

                fos.close();
                din.close();
                dout.close();
                s.close();
            } else {
                // if file not found Close Connection
                System.out.println("FileNotFound");
                System.out.println("Closing Connection");
                dout.close();
                din.close();
                s.close();

            }
    }
    
    public static void startRecieving(String filename) throws IOException {
        filename = din.readUTF();
        System.out.println("Receving file: " + filename);
        filename = "client" + filename;
        System.out.println("Saving as file: " + filename);

        long sz = Long.parseLong(din.readUTF());
        System.out.println("File Size: " + (sz / (1024 * 1024)) + " MB");

        byte b[] = new byte[1024];
        System.out.println("Receving file..");
        // Client's save directory
        fos = new FileOutputStream(new File(
                "C:\\Users\\aliik\\Desktop\\" + filename), true);

                long bytesRead;

                // Saving file to Client's save directory
                do {
                    bytesRead = din.read(b, 0, b.length);
                    fos.write(b, 0, b.length);
                    System.out.println(".");
                } while (!(bytesRead < 1024));

                System.out.println("Completed");


    }
    
}
