import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;


/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */



public class hiloTiempo  extends Thread{
	
	public void run(){
		while(true){
			try {
				sleep(100*((int)(Math.random()*(10-1)+1)));
				for(int x=0;x<Rip.listaEnlaces.size();x++){
				Calendar ahora=Calendar.getInstance();
				long Tmili=ahora.getTimeInMillis();
				if(Rip.listaEnlaces.get(x).getInutilizado()!=0){
					if((Tmili-Rip.listaEnlaces.get(x).getInutilizado())>20000 ){
						Rip.listaEnlaces.remove(x);
					}
				}
				}
			
				int max=Rip.listaEnlaces.size();
				for(int i=0;i<max;i++){
					if(Rip.listaEnlaces.get(i).getInutilizado()==0){
						if( Rip.listaEnlaces.get(i) instanceof Vecino && (Rip.listaEnlaces.get(i).getMascara()==32)){
							Calendar ahora=Calendar.getInstance();
							long Tmili=ahora.getTimeInMillis();
				
							if((Tmili-Rip.listaEnlaces.get(i).getTiempo())>40000){
								Rip.listaEnlaces.get(i).setCoste(16);
								Rip.listaEnlaces.get(i).setInutilizado(Tmili);
								triggeredUpd(Rip.listaEnlaces.get(i).getIP(),Rip.listaEnlaces.get(i).getMascara(),16);
								for(int j=0;j<max;j++){
									if((Rip.listaEnlaces.get(i).getIP()).equals(Rip.listaEnlaces.get(j).getNH())){
										Rip.listaEnlaces.get(j).setCoste(16);
										Rip.listaEnlaces.get(j).setInutilizado(Tmili);
										triggeredUpd(Rip.listaEnlaces.get(j).getIP(),Rip.listaEnlaces.get(j).getMascara(),16);
									}
								}
							}
						}else{
							if(Rip.listaEnlaces.get(i) instanceof NoVecino){
								Calendar ahora=Calendar.getInstance();
								long Tmili=ahora.getTimeInMillis();
								if((Tmili-Rip.listaEnlaces.get(i).getTiempo())>40000){
									Rip.listaEnlaces.get(i).setCoste(16);
									Rip.listaEnlaces.get(i).setInutilizado(Tmili);
									triggeredUpd(Rip.listaEnlaces.get(i).getIP(),Rip.listaEnlaces.get(i).getMascara(),16);
								}	
							}
						}
					}
				}
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public byte[] transInt(int x,int numero){
		byte[] bytes = ByteBuffer.allocate(x).putInt(numero).array();
		return bytes;

	}
}
