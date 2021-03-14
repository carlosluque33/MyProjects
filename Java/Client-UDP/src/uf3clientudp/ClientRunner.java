package uf3clientudp;

import java.io.IOException;


public class ClientRunner {

  
    public static void main(String[] args) {
        try {
            WeekDayClient prg = new WeekDayClient();
            prg.init("localhost", ServerRunner.PORT);
            prg.runClient();
        } catch (IOException ex) {
            System.out.println("Error rebent o enviant");
        }
    }
}
