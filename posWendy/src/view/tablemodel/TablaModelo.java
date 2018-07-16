package view.tablemodel;

import javax.swing.table.AbstractTableModel;

public abstract class TablaModelo extends AbstractTableModel{

	private int limiteInferior=0;
	private int limiteSuperior=20;
	private int noPagina=1;
	
	public int getLimiteInferior(){
		return limiteInferior;
	}
	public int getLimiteSuperior(){
		return limiteSuperior;
	}
	public int getNoPagina(){
		return noPagina;
	}
	
	public void netPag(){
		limiteInferior+=20;
		noPagina++;
	}
	public void lastPag(){
		
		if((limiteInferior-20)>0){
			limiteInferior-=20;
			noPagina--;
		}else{
			setPaginacion();
		}
	}
	public void setPaginacion(){
		limiteInferior=0;
		limiteSuperior=20;
		noPagina=1;
	}

}
