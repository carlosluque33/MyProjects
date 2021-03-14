package chatsockets;

import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Cliente {
	//Palabra para finalizar Conversación
	final String PALABRA_FINALIZAR = "!EXIT";
	Scanner teclado = new Scanner(System.in);
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    
    
  //Main    
    public static void main(String[] argumentos) {
        Cliente cliente = new Cliente();
        Scanner escaner = new Scanner(System.in);
        mostrarTexto("Si ya has introducido el puerto en el servidor,ahora ingresa la IP: [localhost por defecto] ");
        String ip = escaner.nextLine();
        if (ip.length() <= 0) ip = "localhost";

        mostrarTexto("Introduce el mismo puerto que has introducido en el servidor (8080) por defecto ");
        String puerto = escaner.nextLine();
        if (puerto.length() <= 0) puerto = "8080";
        cliente.ejecutarConexion(ip, Integer.parseInt(puerto));
        cliente.escribirDatos();
    }
  //Printo texto
    public static void mostrarTexto(String s) {
        System.out.println(s);
    }
 //Leo los datos primitivos
    public void abrirFlujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Ha ocurrido algo en nuestra conversación:");
        }
    }
  //Inicia la conexión
    public void levantarConexion(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            mostrarTexto("Bienvenido,vamos a establecer una conversación Toni :),a partir de la IP:" + socket.getInetAddress().getHostName());
            System.out.println("CLICA ENTER PARA COMENZAR");
        } catch (Exception e) {
            mostrarTexto("Ha ocurrido algo en nuestra conversación: " + e.getMessage());
            System.exit(0);
        }
    }

  //Envio la codificación de los datos
    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("IOException on enviar");
        }
    }
    //Ejectuo la consexión medianto los puertos
    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    levantarConexion(ip, puerto);
                    abrirFlujos();
                    recibirDatos();
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();
    }
  //Cierro los sockets y los buffers
    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conversación finalizada,que vaya bien Toni:)");
        } catch (IOException e) {
            mostrarTexto("IOException on cerrarConexion()");
        }finally{
            System.exit(0);
        }
    }
    //Escribo los datos en el escaner y los printo por pantalla 
    public void escribirDatos() {
        String entrada = "";
        while (true) {
            System.out.print("Servidor => ");
            entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }

  //Recibo la info y lanzo try catch ,mientras no de a palabra !EXIT seguir procesando
    public void recibirDatos() {
        String st = "";
        try {
            do {
                st = (String) bufferDeEntrada.readUTF();
                mostrarTexto("\nServidor => " + st);
                System.out.print("\nCliente => ");
            } while (!st.equals(PALABRA_FINALIZAR));
        } catch (IOException e) {}
    }
 

    
}