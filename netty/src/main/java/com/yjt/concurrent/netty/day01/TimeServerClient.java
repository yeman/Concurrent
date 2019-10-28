package com.yjt.concurrent.netty.day01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TimeServerClient
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-09-12 11:37
 **/
public class TimeServerClient {

    public void doClient(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter printWriter = null;
        try {
            socket = new Socket("127.0.0.1", port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("sync time");
            String resp = reader.readLine();
            System.out.println(String.format("线程%03d, resp %s",Integer.parseInt(Thread.currentThread().getName()), resp));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        Thread[]  threads = new Thread[100];
        for(int i=0;i<threads.length;i++){
            threads[i] = new Thread(()->{
              TimeServerClient client = new TimeServerClient();
              client.doClient(args);
           },(i+1)+"");
       }
        Arrays.stream(threads).forEach(t -> {
            //try {
                //TimeUnit.SECONDS.sleep(1);
                t.start();
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
           // }

        });

    }
}
