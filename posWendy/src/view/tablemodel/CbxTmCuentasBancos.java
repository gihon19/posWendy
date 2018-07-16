package view.tablemodel;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import modelo.CuentasBancos;



public class CbxTmCuentasBancos extends DefaultComboBoxModel{
	
	private Vector<CuentasBancos> cuentas=new Vector<CuentasBancos>();

	@Override
	public int getSize() {
		  return cuentas.size();
		 }
	
	@Override
	public Object getElementAt(int index) {
		  return cuentas.get(index);
		 }
	
	public void setLista(Vector<CuentasBancos> im){
		cuentas=im;
	}
	public void addEmpleado(CuentasBancos m){
		cuentas.addElement(m);
		//this.f
	

	}

	
	public CbxTmCuentasBancos(){
		
	}
	
	public int buscarImpuesto(CuentasBancos m){
		int index=-1;
		
		for(int c=0;c<cuentas.size();c++){
			
			if(cuentas.get(c).getId()==m.getId()){
				
				index=c;
			}
		}
		
		return index;
	}

}
