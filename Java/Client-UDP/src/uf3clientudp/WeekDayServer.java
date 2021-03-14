
package uf3clientudp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeekDayServer{
    public final static int STOP=-1;
    DatagramSocket socket;
    
    public void init(int port) throws SocketException{
        socket = new DatagramSocket(port);
    }
    
    public void runServer() throws IOException{
        boolean finish=false;
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        //El servidor aten el port indefinidament
        while(!finish){
            //creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData, 1024);
            //Espera de les dades
            socket.receive(packet);
            //procesament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData(), packet.getLength());
            
            if(isFinishedSignal(sendingData)){
                finish=true;
            }else{
                //obtenció de l'adreça del client
                clientIP = packet.getAddress();
                //obtenció del port del client
                clientPort = packet.getPort();
                //Creació del paquet per enviar la resposta
                packet = new DatagramPacket(sendingData, sendingData.length, 
                                               clientIP, clientPort);
                //enviament de la resposta
                socket.send(packet);
            }
        }
        close();
    }
    
    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }

    protected byte[] processData(byte[] data, int length){
        byte introdat[] ={-1, -1, -1, -1}; //error
        int year;
        int month;
        int day;
        int respuesta=STOP; //close server
        DataInputStream in = new DataInputStream(
                            new ByteArrayInputStream(data, 0, length));
        try {
            year = in.readInt();
            if(year!=STOP){
                month = in.readInt()-1;
                day = in.readInt();
                Calendar calendar = new GregorianCalendar(year, month, month);
                respuesta = calendar.get(Calendar.DAY_OF_WEEK)-1;
            }
        } catch (IOException ex) {
            respuesta = 10; //error of data format
        }
        
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(arrayOut);
        try {
            dataOut.writeInt(respuesta);           
        } catch (IOException ex) {processError(ex);}
        introdat = arrayOut.toByteArray();
        try {
            dataOut.close();
        } catch (IOException ex) {processError(ex);}
        return introdat;
    }

    private boolean isFinishedSignal(byte[] sendingData) {
        return sendingData[0]==STOP;
    }
    
    private void processError(IOException ex) {
            Logger.getLogger(WeekDayServer.class.getName()).log(Level.SEVERE, null, ex);        
    }
}
