

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;


/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */



public class hiloEntrada extends Thread {
	DatagramSocket SocketEnt;
	hiloEntrada(DatagramSocket x){
		SocketEnt=x;
	}
	
	public void run(){
		try{ 
			while(true){
				final int max=24*20+4;
				int numeroD;
				int encontrado=0;
			    byte[] Mensaje=new byte[max];
				DatagramPacket datosEntrada=new DatagramPacket(Mensaje,Mensaje.length);	
				SocketEnt.receive(datosEntrada);
				int tt=datosEntrada.getLength();
				byte[] datos=datosEntrada.getData();
				numeroD=((tt)-4-20)/20; 
				byte contrasenha[];
				ByteArrayOutputStream contra= new ByteArrayOutputStream( );
				Integer cero_1=new Integer(0);
				byte cero=cero_1.byteValue();
				contra.write(datos[8]);
				contra.write(datos[9]);
				contra.write(datos[10]);
				contra.write(datos[11]);
				contra.write(datos[12]);
				contra.write(datos[13]);
				contra.write(datos[14]);
				contra.write(datos[15]);
				contra.write(datos[16]);
				contra.write(datos[17]);
				contra.write(datos[18]);
				contra.write(datos[19]);
				contra.write(datos[20]);
				contra.write(datos[21]);
				contra.write(datos[22]);
				contra.write(datos[23]);
				
				contrasenha=contra.toByteArray();
				ByteArrayOutputStream contra2= new ByteArrayOutputStream( );
				contra2.write(Rip.autenticacion.getBytes("UTF-8"));
				for(int i=Rip.autenticacion.length();i<16;i++){
					contra2.write(cero);
				}
if(Arrays.equals(contrasenha,contra2.toByteArray())){
				for(int u=0;u<numeroD;u++){
					int mascara_1=0;
					int coste = 0; 
					ByteArrayOutputStream x= new ByteArrayOutputStream( );
					x.write(datos[28+(u)*20]);
					x.write(datos[29+(u)*20]);
					x.write(datos[30+(u)*20]);
					x.write(datos[31+(u)*20]);			
					
					byte[] IP= x.toByteArray();
					InetAddress IP_net = InetAddress.getByAddress(IP);
				
					
					ByteArrayOutputStream y= new ByteArrayOutputStream( );
					String resul=Integer.toBinaryString(datos[32+(u)*20] & 0xFF)+Integer.toBinaryString(datos[33+(u)*20] & 0xFF)+Integer.toBinaryString(datos[34+(u)*20] & 0xFF)+Integer.toBinaryString(datos[35+(u)*20] & 0xFF);
		        
					for(int p=0;p<resul.length();p++){
						if(resul.charAt(p)=='1'){mascara_1++;}
					}
					ByteArrayOutputStream z= new ByteArrayOutputStream( );
					z.write(datos[40+(u)*20]);
					z.write(datos[41+(u)*20]);
					z.write(datos[42+(u)*20]);
					z.write(datos[43+(u)*20]);			
				
					byte[] coste_1= z.toByteArray();
					coste= coste_1[3] & 0xFF | (coste_1[2] & 0xFF) << 8 |  (coste_1[1] & 0xFF) << 16 | (coste_1[0] & 0xFF) << 24;  
					if(coste!=16) coste++;
					
					for(int i=0; i<Rip.listaEnlaces.size();i++){
						if(IP_net.equals(Rip.listaEnlaces.get(i).getIP())){
						encontrado=1;
						Calendar ahora=Calendar.getInstance();
						long Tmili=ahora.getTimeInMillis();
						switch(coste){
						case 1:
							if(coste<Rip.listaEnlaces.get(i).getCoste()){
								Rip.listaEnlaces.get(i).setCoste(coste);
								Rip.listaEnlaces.get(i).setNH(null);
								Rip.listaEnlaces.get(i).setTiempo(Tmili);
								Rip.listaEnlaces.get(i).setInutilizado(0);
								triggeredUpd(IP_net,mascara_1,coste);		
							}
							if(coste==Rip.listaEnlaces.get(i).getCoste()){
								Rip.listaEnlaces.get(i).setTiempo(Tmili);
								Rip.listaEnlaces.get(i).setNH(null);
								Rip.listaEnlaces.get(i).setInutilizado(0);
							}
							break;
						case 16:
							if(Rip.listaEnlaces.get(i) instanceof NoVecino){
							if(coste>Rip.listaEnlaces.get(i).getCoste() && (datosEntrada.getAddress().getHostAddress()).equals(Rip.listaEnlaces.get(i).getNH().getHostAddress())){
								Rip.listaEnlaces.get(i).setCoste(coste);
								Rip.listaEnlaces.get(i).setTiempo(Tmili-40000);
								Rip.listaEnlaces.get(i).setInutilizado(0);
								triggeredUpd(IP_net,mascara_1,coste);
							}
							}
							break;
							default:
								if(coste<Rip.listaEnlaces.get(i).getCoste()){
									Rip.listaEnlaces.get(i).setNH(datosEntrada.getAddress());
									Rip.listaEnlaces.get(i).setTiempo(Tmili);
									Rip.listaEnlaces.get(i).setInutilizado(0);
									Rip.listaEnlaces.get(i).setCoste(coste);	
									triggeredUpd(IP_net,mascara_1,coste);
								}
								if(coste==Rip.listaEnlaces.get(i).getCoste()){
									Rip.listaEnlaces.get(i).setTiempo(Tmili);
									Rip.listaEnlaces.get(i).setInutilizado(0);	
								}
								if(coste>Rip.listaEnlaces.get(i).getCoste() && (datosEntrada.getAddress()).equals(Rip.listaEnlaces.get(i).getNH()) && Rip.listaEnlaces.get(i) instanceof NoVecino){
									Rip.listaEnlaces.get(i).setTiempo(Tmili);
									Rip.listaEnlaces.get(i).setInutilizado(0);
									Rip.listaEnlaces.get(i).setCoste(coste);
									triggeredUpd(IP_net,mascara_1,coste);
								}
								break;
						}
			
				}
					}	
				if(encontrado==0){
					if(coste==1){
						Vecino nuevo=new Vecino(IP_net,mascara_1,null,coste);
						Calendar ahora=Calendar.getInstance();
						long Tmili=ahora.getTimeInMillis();
					    nuevo.setTiempo(Tmili);
						Rip.listaEnlaces.add(nuevo);
						triggeredUpd(IP_net,mascara_1,coste);
					}else{
						NoVecino nuevo=new NoVecino(IP_net,mascara_1,coste,datosEntrada.getAddress()); 
						Calendar ahora=Calendar.getInstance();
						long Tmili=ahora.getTimeInMillis();
						nuevo.setTiempo(Tmili);
						Rip.listaEnlaces.add(nuevo);
						triggeredUpd(IP_net,mascara_1,coste);
						}
				}
					
					encontrado=0;
				}
		
				
		}		
				
			}		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
}
	

	public static void triggeredUpd(InetAddress net_new, int mascara_new, int coste_new ) throws IOException{
		for(int i=0;i<Rip.listaEnlaces.size();i++){
			if(Rip.listaEnlaces.get(i).getIP().equals(net_new)){ 
				continue;
			}
			if(Rip.listaEnlaces.get(i) instanceof Vecino && Rip.listaEnlaces.get(i).getMascara() ==32 ){
				DatagramSocket SocketSal=new DatagramSocket();
				byte[] my;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				
				//cabecera constante
				
				Integer dos_1=new Integer(2);
				byte dos=dos_1.byteValue();
				outputStream.write( dos);
				outputStream.write( dos);
				Integer cero_1=new Integer(0);
				byte cero=cero_1.byteValue();
				outputStream.write(cero);
				outputStream.write(cero);
				
                //autenticacion
				
				byte ocho=(byte)Short.parseShort("11111111",2);
				outputStream.write(ocho);
				outputStream.write(ocho);
				outputStream.write(cero);
				outputStream.write(dos);
				outputStream.write(Rip.autenticacion.getBytes("UTF-8"));
				for(int w=Rip.autenticacion.length();w<16;w++){
					outputStream.write(cero);
				}
			  
	
				outputStream.write(cero);
				outputStream.write(dos);
				outputStream.write(cero);
				outputStream.write(cero);
				my=net_new.getAddress();
				outputStream.write(my);
				String mascaraCompleta="11111111111111111111111111111111";
				String mascaraResul=mascaraCompleta.substring(0, mascara_new);
				for(int s=mascara_new;s<32;s++){
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
			    outputStream.write(uno);
			    outputStream.write(dos_2);
			    outputStream.write(tres);
			    outputStream.write(cuatro);
			    outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
			    outputStream.write(ByteBuffer.allocate(4).putInt(coste_new).array());
			    byte[] mensaje= outputStream.toByteArray();	
			    DatagramPacket datosSal=new DatagramPacket(mensaje,mensaje.length,Rip.listaEnlaces.get(i).getIP(),Rip.listaEnlaces.get(i).getPuerto());
				SocketSal.send(datosSal);
			}
		} 
	    }
	
}

	
	