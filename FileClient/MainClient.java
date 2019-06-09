/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author stygianlgdonic
 */
public class MainClient {

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

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        dout.writeUTF(filename1);

        String filename = "";

        try {
            dout.flush();

            byte check = din.readByte();
            if (check == 1) {
                // if file found Start recieving
                
                filename = din.readUTF();
                System.out.println("Receving file: " + filename);
                filename = "client" + filename;
                System.out.println("Saving as file: " + filename);

                long sz = Long.parseLong(din.readUTF());
                System.out.println("File Size: " + (sz / (1024 * 1024)) + " MB");

                byte b[] = new byte[1024];
                System.out.println("Receving file..");
                
                // Client's save directory
                FileOutputStream fos = new FileOutputStream(new File(
                        "C:\\Users\\mbird\\Desktop\\" + filename), true);

                long bytesRead;

                // Saving file to Client's save directory
                do {
                    bytesRead = din.read(b, 0, b.length);
                    fos.write(b, 0, b.length);
                    System.out.println(".");
                } while (!(bytesRead < 1024));

                System.out.println("Completed");
                System.out.println("Closing Connection");

                fos.close();
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

        } catch (EOFException e) {
            System.out.println("Some Error Has occured");
            System.out.println("Closing Connection");
            dout.close();
            din.close();
            s.close();
        }
    }

}
