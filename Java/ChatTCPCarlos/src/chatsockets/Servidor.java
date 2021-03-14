package chatsockets;

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Servidor {

//Palabra para finalizar Conversación
    final String PALABRA_FINALIZAR = "!EXIT";
    Scanner escaner = new Scanner(System.in);
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
   
   
//Main
    public static void main(String[] args) throws IOException {
        Servidor s = new Servidor();
        Scanner sc = new Scanner(System.in);

        mostrarTexto("Ingresa el puerto 8080 por defecto,asegurate de introducir exactamente el mismo puerto para el Servidor como para el Cliente: ");
        String puerto = sc.nextLine();
        if (puerto.length() <= 0) puerto = "8080";
        s.ejecutarConexion(Integer.parseInt(puerto));
        s.escribirDatos();
    }
  //Leo los datos primitivos
    public void flujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Ha ocurrido algo en nuestra conversación");
        }
    }
    //Printo el texto
    public static void mostrarTexto(String s) {
        System.out.print(s);
    }
//Escribo los datos en el escaner y los printo por pantalla 
    public void escribirDatos() {
        while (true) {
            System.out.print("Servidor=> ");
            enviar(escaner.nextLine());   
        }
    }
//Inicia la conexión
    public void levantarConexion(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            mostrarTexto("Esperando conexión entrante en el puerto,ahora introduce en la consola del cliente la IP y el mismo puerto  " + String.valueOf(puerto) + "...");
            socket = serverSocket.accept();
            mostrarTexto("Empieza la conversación,conexión establecida con: : " + socket.getInetAddress().getHostName() + "\n\n\n");
            
        } catch (Exception e) {
            mostrarTexto("Error en iniciar la conexión(): " + e.getMessage());
            System.exit(0);
        }
    }

//Recibo la info y lanzo try catch ,mientras no de a palabra !EXIT seguir procesando
    public void recibirDatos() {
        String st = "";
        try {
            do {
                st = (String) bufferDeEntrada.readUTF();
                mostrarTexto("\nCliente=> " + st);
                System.out.print("\nServidor=> ");
            } while (!st.equals(PALABRA_FINALIZAR));
        } catch (IOException e) {
            cerrarConexion();
        }
    }

//Envio la codificación de los datos
    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Ha ocurrido algo en nuestra conversación: " + e.getMessage());
        }
    }

  //Ejectuo la consexión medianto los puertos
    public void ejecutarConexion(int puerto) {
    	//Lanzo hilo
        Thread Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion(puerto);
                        flujos();
                        recibirDatos();
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        //Inicio hilo
        Thread.start();
    }

//Cierro los sockets y los buffers
    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e) {
          mostrarTexto("Ha ocurrido algo en nuestra conversación: " + e.getMessage());
        } finally {
            mostrarTexto("Conversación finalizada,que vaya bien Toni:)");
            System.exit(0);

        }
    }

   
}