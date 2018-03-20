import java.util.ArrayList;


/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */


public class hiloActualizar extends Thread{
	
	public void run(){
		while(true){
		ArrayList<Enlace> copiaEnlaces=Rip.listaEnlaces;
		try {
			sleep(10000);
			System.out.println("\n\t\t\tTabla de encaminamiento\n");
			System.out.println("\tDireccion\t\tMascara\t\tNH\t\t\tG\t\tCoste\n");	
			for(int i=0;i<copiaEnlaces.size();i++){
				if(copiaEnlaces.get(i) instanceof Vecino){
					System.out.println("\t"+copiaEnlaces.get(i).getIP().getHostAddress()+"\t\t"+copiaEnlaces.get(i).getMascara()+"\t\t"+"-"+"\t\t\t"+"0"+"\t\t"+copiaEnlaces.get(i).getCoste());
				}
			else{
					System.out.println("\t"+copiaEnlaces.get(i).getIP().getHostAddress()+"\t\t"+copiaEnlaces.get(i).getMascara()+"\t\t"+copiaEnlaces.get(i).getNH().getHostAddress()+"\t"+"        1"+"\t\t"+copiaEnlaces.get(i).getCoste());	
				
						}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}
	}
}