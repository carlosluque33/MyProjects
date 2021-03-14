
package uf3clientudp;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerRunner {
    public final static int PORT=9994;


    public static void main(String[] args) {
        try {
            WeekDayServer prg = new WeekDayServer();
            prg.init(PORT);
            prg.runServer();
        } catch (IOException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
