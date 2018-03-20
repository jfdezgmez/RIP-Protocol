

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Enumeration;


/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */



public class hiloSalida extends Thread {
	DatagramSocket SocketSal;
	
	hiloSalida(DatagramSocket x){
		SocketSal=x;
	}
	
	public void run(){
		try{
			while(true){
				
				byte[] my;
				int actualizar=0;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				
				//cabecera constante
				
				byte ocho=(byte)Short.parseShort("11111111",2);
				Integer dos_1=new Integer(2);
				byte dos=dos_1.byteValue();
				outputStream.write( dos);
				outputStream.write( dos);
				Integer cero_1=new Integer(0);
				byte cero=cero_1.byteValue();
		    	outputStream.write(cero);
		     	outputStream.write(cero);
				
				//autenticacion
				
				outputStream.write(ocho);
				outputStream.write(ocho);
				outputStream.write(cero);
				outputStream.write(dos);
				outputStream.write(Rip.autenticacion.getBytes("UTF-8"));
				for(int i=Rip.autenticacion.length();i<16;i++){
					outputStream.write(cero);
				}
			  
				outputStream.write(cero);
				outputStream.write(dos);
				outputStream.write(cero);
				outputStream.write(cero);
				my=Rip.getMyIp().getAddress();
				outputStream.write(my);
				
				outputStream.write(ocho);
				outputStream.write(ocho);
				outputStream.write(ocho);
				outputStream.write(ocho);
				outputStream.write(transInt(4,0));
				outputStream.write(transInt(4,0));
				actualizar=1;
				
				for(int i=0;i<Rip.listaEnlaces.size();i++){
					if(Rip.listaEnlaces.get(i) instanceof Vecino && Rip.listaEnlaces.get(i).getMascara() ==32 ){	
						for(int j=0;j<Rip.listaEnlaces.size();j++){
	
							if(i==j){

								continue;}
							else{
								Calendar ahora=Calendar.getInstance();
								long Tmili=ahora.getTimeInMillis();
								if((Tmili-Rip.listaEnlaces.get(j).getTiempo())>50000 && Rip.listaEnlaces.get(j).getMascara()==32 || (Tmili-Rip.listaEnlaces.get(j).getTiempo())>50000 && Rip.listaEnlaces.get(j).getCoste()!=1){
									break;
									
								}
									if(Rip.listaEnlaces.get(j) instanceof NoVecino){
										if(Rip.listaEnlaces.get(j).getNH().equals(Rip.listaEnlaces.get(i).getIP())){
											outputStream.write(cero);
											outputStream.write(dos);
											outputStream.write(cero);
											outputStream.write(cero);
											InetAddress x=Rip.listaEnlaces.get(j).getIP();
											int y=Rip.listaEnlaces.get(j).getMascara(); 
											String mascaraCompleta="11111111111111111111111111111111";
											String mascaraResul=mascaraCompleta.substring(0, y);
											for(int s=y;s<32;s++){
												mascaraResul=mascaraResul+"0";
											}
											String w[] = new String[4];
											for(int d=0;d<4;d++){
												w[d]=mascaraResul.substring(d*8, (d+1)*8);
											}
											byte uno=(byte)Short.parseShort(w[0],2);
											byte dos_2=(byte)Short.parseShort(w[1],2);
											byte tres=(byte)Short.parseShort(w[2],2);
											byte cuatro=(byte)Short.parseShort(w[3],2);
											byte[] IP = x.getAddress();
											outputStream.write(IP);
											outputStream.write(uno);
											outputStream.write(dos_2);
											outputStream.write(tres);
											outputStream.write(cuatro);
											outputStream.write(transInt(4,0));
											outputStream.write(transInt(4,16));
											actualizar=1;}	 
										else{
											outputStream.write(cero);
											outputStream.write(dos);
											outputStream.write(cero);
											outputStream.write(cero);
											InetAddress x=Rip.listaEnlaces.get(j).getIP();
											int y=Rip.listaEnlaces.get(j).getMascara(); 
											String mascaraCompleta="11111111111111111111111111111111";
											String mascaraResul=mascaraCompleta.substring(0, y);
											for(int s=y;s<32;s++){
												mascaraResul=mascaraResul+"0";
											}
											String w[] = new String[4];
											for(int d=0;d<4;d++){
												w[d]=mascaraResul.substring(d*8, (d+1)*8);
											}
											byte uno=(byte)Short.parseShort(w[0],2);
											byte dos_2=(byte)Short.parseShort(w[1],2);
											byte tres=(byte)Short.parseShort(w[2],2);
											byte cuatro=(byte)Short.parseShort(w[3],2);
											byte[] IP = x.getAddress();
											outputStream.write(IP);
											outputStream.write(uno);
											outputStream.write(dos_2);
											outputStream.write(tres);
											outputStream.write(cuatro);
											outputStream.write(transInt(4,0));
											outputStream.write(transInt(4,Rip.listaEnlaces.get(j).getCoste()));
											actualizar=1;
										}
									}
									else{
										InetAddress x=Rip.listaEnlaces.get(j).getIP();
										int y=Rip.listaEnlaces.get(j).getMascara(); 
										String mascaraCompleta="11111111111111111111111111111111";
										String mascaraResul=mascaraCompleta.substring(0, y);
										for(int s=y;s<32;s++){
											mascaraResul=mascaraResul+"0";
										}
										String w[] = new String[4];
										for(int d=0;d<4;d++){
											w[d]=mascaraResul.substring(d*8, (d+1)*8);
										}
										byte uno=(byte)Short.parseShort(w[0],2);
										byte dos_2=(byte)Short.parseShort(w[1],2);
										byte tres=(byte)Short.parseShort(w[2],2);
										byte cuatro=(byte)Short.parseShort(w[3],2);
										byte[] IP = x.getAddress();
										outputStream.write(cero);
										outputStream.write(dos);
										outputStream.write(cero);
										outputStream.write(cero);
										outputStream.write(IP);
										outputStream.write(uno);
										outputStream.write(dos_2);
										outputStream.write(tres);
										outputStream.write(cuatro);
										outputStream.write(transInt(4,0));
										outputStream.write(transInt(4,Rip.listaEnlaces.get(j).getCoste()));
										actualizar=1;
									}
							}
						}
						
						if(actualizar==1){	
							byte[] mensaje= outputStream.toByteArray();			
							DatagramPacket datosSal=new DatagramPacket(mensaje,mensaje.length,Rip.listaEnlaces.get(i).getIP(),Rip.listaEnlaces.get(i).getPuerto());
							SocketSal.send(datosSal);
							outputStream.reset();
							outputStream.write( dos);
							outputStream.write( dos);
							outputStream.write(cero);
							outputStream.write(cero);		

							//autenticacion
							
							outputStream.write(ocho);
							outputStream.write(ocho);
							outputStream.write(cero);
							outputStream.write(dos);
							outputStream.write(Rip.autenticacion.getBytes("UTF-8"));
							for(int u=Rip.autenticacion.length();u<16;u++){
								outputStream.write(cero);
							}
							  
							outputStream.write(cero);
							outputStream.write(dos);
							outputStream.write(cero);
							outputStream.write(cero);
							outputStream.write(my);
							outputStream.write(ocho);
							outputStream.write(ocho);
							outputStream.write(ocho);
							outputStream.write(ocho);
							outputStream.write(transInt(4,0));
							outputStream.write(transInt(4,0));
							actualizar=0;
						}
					}
				}
				int aleatorio=(int)(Math.random()*(30)+10);		
				sleep(10000+100*aleatorio);	
			}		
		}catch(Exception e){
			System.out.println("error hilo salida");
		}
	}

	public byte[] transInt(int x,int numero){
		byte[] bytes = ByteBuffer.allocate(x).putInt(numero).array();
		return bytes;

	}
}