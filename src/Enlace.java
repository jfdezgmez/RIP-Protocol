

import java.net.InetAddress;



/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */

public class Enlace {
	private InetAddress direccion;
	private int puerto=5512;
	private int mascara;
	private int coste;
	private InetAddress NH=null;
	private long tiempo=0;
	private long inutilizado=0;
	
	public Enlace(InetAddress Direccion,int mascara,int coste,InetAddress SigS ){
		NH=SigS;
		this.direccion=Direccion;
		this.mascara=mascara;
		this.coste=coste;
	}
	
	public Enlace(InetAddress Direccion, int puerto, int Mascara,int coste,InetAddress SigS ){
		NH=SigS;
		this.direccion=Direccion;
		this.mascara=Mascara;
		this.puerto=puerto;
		this.coste=coste;
	}

	public InetAddress getIP(){
		return direccion;
	}
	public int getMascara(){
		return mascara;
	}
	public int getPuerto(){
		return puerto;
	}
	
	public int getCoste(){
		return coste;
	}
	public InetAddress getNH(){
		return NH;
	}
	public void setCoste(int coste){
		this.coste=coste;
	}
	public void setTiempo(long tiempo){
		this.tiempo=tiempo;
	}
	public long getTiempo(){
		return tiempo;
	}
	public void setInutilizado(long inutiliz){
		this.inutilizado=inutiliz;
	}
	public long getInutilizado(){
		return inutilizado;
	}
	public void setNH(InetAddress NH){
		this.NH=NH;
	}
}

