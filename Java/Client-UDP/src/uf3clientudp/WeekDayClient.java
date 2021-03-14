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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import lib.ConsoleInterface;

public class WeekDayClient {
	public static final int FIELD_TYPE_OF_DAY = 0;
	public static final int FIELD_TYPE_OF_MONTH = 1;
	public static final int FIELD_TYPE_OF_YEAR = 2;
	public static final String[] STR_WEEK_DAY = {"Diumenge", "Dilluns"
			, "Dimarts", "Dimecres", "Dijous"
			, "Divendres", "Dissabte" };
	ConsoleInterface consoleInterface = new ConsoleInterface();
	InetAddress serverIP;
	int serverPort;
	DatagramSocket socket;
	int attemp=0;

	public void init(String host, int port) throws SocketException,
	UnknownHostException{
		serverIP = InetAddress.getByName(host);
		serverPort = port;
		socket = new DatagramSocket();
	}

	public void runClient() throws IOException{
		int receivedData;
		int [] data;

		data = mostrarmensaje();
		while(continuar(data)){//Atiendo al puerto indefinidamente
			byte[] sendingData = almacenarbytes(data, data.length);
			DatagramPacket packet = new DatagramPacket(sendingData,
					sendingData.length,//Envío los datos
					serverIP,
					serverPort);
			
			socket.send(packet);//Envío la respuesta del paquete

			
			packet = new DatagramPacket(new byte[1024], 1024);//Creo el paquete para que pueda recibir los datos en bytes
			socket.setSoTimeout(5000);//Le pongo que espere los datos 5 segundos
			try{
				socket.receive(packet);//Espero al paquete

				receivedData = datos(packet.getData());//Obtengo el paquete
				data = volverajugar(receivedData);//Obtengo los datos 
			}catch(SocketTimeoutException e){
				data = fueradetiempo(packet);//Mientras no se exceda del tiempo
			}
		}
		close();//Llamo a la función close para que cierre conexión
	}


	public void close(){//Si el socket está vacio o cerrado,cierro conexión
		if(socket!=null && !socket.isClosed()){
			socket.close();
			System.exit(0);//Salgo del programa
		}
	}

	protected int[] volverajugar(int receivedData){
		int[] introdat;
		respuesta(receivedData);
		if(consoleInterface.readYesNo("Quieres volver a jugar?: ", 'S', ConsoleInterface.TO_UPPPER_CASE))//Lanzo petición de si quiere volver a jugar,si me dice que si,lo reinicio y si dice no,paro juego
		{
			System.out.println();
			introdat = peticion();
		}else{
			introdat = new int[] {-1};
		}
		return introdat;
	}

	protected int[] mostrarmensaje(){
		titulo();//Llamo a la función titulo
		return peticion();//Devuelvo función mostrar peticion
	}

	protected boolean continuar(int[] sendingData){
		return (sendingData.length!=1 || sendingData[0]!=-1);
	}

	protected int[] fueradetiempo(DatagramPacket datagram) throws IOException{//Contengo paquete de datos
		int[] introdat;
		if(attemp<3){//Si el número de intentos es menor que 3,ir sumando intentos
			attemp++;
			introdat = tamañodatos(datagram.getData(), 3);
		}else{
			introdat = new int[] {-1};
		}
		return introdat;
	}

	private int[] peticion(){
		int[] introdat = new int[3];
		introdat[FIELD_TYPE_OF_DAY] = consoleInterface.readInt("Dime un día:");//introduzco dia
		System.out.println();
		introdat[FIELD_TYPE_OF_MONTH] = consoleInterface.readInt("Dime un mes: ");//introduzco mes
		System.out.println();
		introdat[FIELD_TYPE_OF_YEAR] = consoleInterface.readInt("Dime un año: ");//introduzco año
		System.out.println();
		return introdat;
	}

	private void respuesta(int iday){
		System.out.print("El dia de la semana era: ");
		System.out.println(STR_WEEK_DAY[iday]);//le printo el iterador de dia
		System.out.println();//le printo espacio
	}

	private void titulo(){
		System.out.println("BUSCADOR DIA SEGÚN FECHA");//titulo juego
	}

	private byte[] almacenarbytes(int[] data, int length){
		byte[] introdat;
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(arrayOut);
		for(int i=0; i<length; i++){
			try {
				dataOut.writeInt(data[i]);
			} catch (IOException ex) {System.out.println("Error");}
		}
		introdat = arrayOut.toByteArray();
		try {
			dataOut.close();
		} catch (IOException ex)
		{
			System.out.println("Error");
		}
		return introdat;
	}

	private byte[] mostrarbytes(int data){
		byte[] introdat;
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();//Buffer crece mientras le escriban datos
		DataOutputStream dataOut = new DataOutputStream(arrayOut);//Implemento un flujo de salida en el que los datos los escribo en una matriz de bytes
		try {
			dataOut.writeInt(data);
		} catch (IOException ex) {
			System.out.println("No hay error");
		}
		introdat = arrayOut.toByteArray();
		try {
			dataOut.close();
		} catch (IOException ex) 
			{
			System.out.println("Error");
			}
		return introdat;
	}

	private int datos(byte[] data) throws IOException{
		int introdat;//Donde le voy a pasar los datos
		DataInputStream datos = new DataInputStream(new ByteArrayInputStream(data));//Le paso un  flujo de entrada de datos que permite  lea tipos de datos primitivos de un flujo de entrada
		introdat = datos.readInt();
		try {
			datos.close();//Cierro datos
		} catch (IOException ex) 
		{
			System.out.println("Error");
		}
		return introdat;
	}

	private int[] tamañodatos(byte[] data, int tamaño) throws IOException{
		int[] introdat = new int[tamaño] ;//Le paso tamaño
		DataInputStream datos = new DataInputStream(new ByteArrayInputStream(data));//creo el canal
		for(int i=0; i<tamaño; i++)
		{
			introdat[i] = datos.readInt();//Si es menor a tamaño leo valores de entrada de datos
		}
		try {
			datos.close();
		} catch (IOException ex) {System.out.println("Error");}
		return introdat;
	}
}