
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Scanner;



/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */



public class Rip {
	static String autenticacion;
	
    static Calendar ahora;
	static long Tmili;
	public static ArrayList<Enlace> listaEnlaces=new ArrayList<Enlace>();
	static InetAddress myIP;
	static int myPort=5512;
	public static void main(String[] args) throws UnknownHostException, SocketException, InterruptedException {
	
		// peticion contraseña
		
		autenticacion="";
		System.out.println("\tEscriba una clave de autenticacion:\n");
        Scanner lectura = new Scanner (System.in); 
        autenticacion = lectura.nextLine ();
 
        
		String x;
		int port=0;
		
		if (args.length!=0){
			x=args[0];
			int tienePuerto=0;
			for(int w=0;w<x.length();w++){
				if(x.charAt(w)==':'){
					 tienePuerto=1;
				}}
				if(tienePuerto==0){
					String h[]=x.split("/");
					myIP=InetAddress.getByName(h[0]);
					x="ripconf-"+h[0]+".topo";
				}
				if(tienePuerto==1){
					String temp[]=args[0].split(":");
					myIP=InetAddress.getByName(temp[0]);
					port=Integer.parseInt(temp[1]);
					myPort=port;
					x="ripconf-"+temp[0]+".topo";
				}			
				
			}
			else{
				Inet4Address ipt4=null;
				Enumeration<InetAddress> enumDirs=(NetworkInterface.getByName("eth0")).getInetAddresses();
				while(enumDirs.hasMoreElements()){
					InetAddress ipt=enumDirs.nextElement();
					if(ipt instanceof Inet4Address){
						ipt4=(Inet4Address)ipt;
						myIP=ipt4;
						break;				
					}
				}
				String h=(ipt4.toString()).substring(1, ipt4.toString().length());
				x="ripconf-"+h+".topo";			
			}
		
//	x=x+".txt";
		
		String tmp;
		try{
			FileInputStream fichero = new FileInputStream(x);
			InputStreamReader reader =new InputStreamReader(fichero,"UTF8");
			BufferedReader fich =new BufferedReader(reader);
			Scanner input = new Scanner(fich);

			while(input.hasNext()){
				int mascarax=0;
				int mascara;
				int puerto=0;
				tmp=input.nextLine();
				for(int i=0;i<tmp.length();i++){
					if(tmp.charAt(i)=='/'){
						mascarax=1;
					}
				}
				for(int j=0;j<tmp.length();j++){
					if(tmp.charAt(j)==':'){
						puerto=1;
					}
				}
	
				if(mascarax==1){
					String[] w=tmp.split("/");
					InetAddress g = InetAddress.getByName(w[0]);
					mascara=Integer.parseInt(w[1]);
						Vecino y=new Vecino(g,mascara,null,1);
						ahora=Calendar.getInstance();
						Tmili=ahora.getTimeInMillis();
						y.setTiempo(Tmili);
						listaEnlaces.add(y);
				}else{
					if(puerto==1){
						int puerto1;
						String[] t=tmp.split(":");
						InetAddress g = InetAddress.getByName(t[0]);
						puerto1=Integer.parseInt(t[1]);
						Vecino y=new Vecino(g,puerto1,16,32,null);
						ahora=Calendar.getInstance();
						Tmili=ahora.getTimeInMillis();
						y.setTiempo(Tmili);
						listaEnlaces.add(y);
					}else{
						if(puerto==0 && mascarax==0){
							InetAddress u=InetAddress.getByName(tmp);
							Vecino y=new Vecino(u,32,null,16);
							ahora=Calendar.getInstance();
							Tmili=ahora.getTimeInMillis();
							y.setTiempo(Tmili);
							listaEnlaces.add(y);
						}
					}
				}
				mascarax=0;
				puerto=0;
			}
		}catch (FileNotFoundException e) { 	
			System.out.println("No se ha podido abrir el fichero"); }
			catch (UnsupportedEncodingException e) {
				System.out.println("Error de lectura");
			}

//iniciar el hilo de entrada

		if(port==0){
			DatagramSocket p=new DatagramSocket(5512); 
			hiloEntrada j= new hiloEntrada(p);
			j.start();
		}
		else{
			DatagramSocket p=new DatagramSocket(port);
			hiloEntrada j= new hiloEntrada(p);
			j.start();
		}

//iniciar comprobacion tiempo 
		
		hiloTiempo time=new hiloTiempo();
		time.start();
	
//iniciar hilo salida

		if(port==0){
			DatagramSocket w=new DatagramSocket();
			hiloSalida f= new hiloSalida(w);
			f.start();
		}
		else{
			DatagramSocket w=new DatagramSocket();
			hiloSalida f= new hiloSalida(w);
			f.start();
		}

//mostrar por pantalla

		hiloActualizar p=new hiloActualizar();
		p.start();

	}

	public static byte[] transInt(int x,int numero){
		byte[] bytes = ByteBuffer.allocate(x).putInt(numero).array();
		return bytes;
	}
	

	public static InetAddress getMyIp(){
		return myIP;
	}
}
