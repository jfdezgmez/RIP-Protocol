

import java.net.InetAddress;


/**
		 * 
		 * @author Jose Fernandez 
		 * @version 05/2016
		 * https://github.com/jfdezgmez
		 */


public class Vecino extends Enlace {
	
	public Vecino(InetAddress x, int masc, InetAddress z,int coste){
		super(x,masc,coste,null);
	}

	public Vecino(InetAddress x, int puerto, int coste,int masc,InetAddress z){
		super(x,puerto,masc,coste,null);
	}

}
