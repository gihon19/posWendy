package view.tablemodel;

import java.util.List;
import java.util.ArrayList;

import modelo.CuentasBancos;


public class TmCuentasBanco extends TablaModelo {
	private String []columnNames={"Codigo","Nombre","No Cuenta","Tipo Cuenta"};
	private List<CuentasBancos> cuentas=new ArrayList<CuentasBancos>();
	
	
	public void agregar(CuentasBancos cuenta){
		cuentas.add(cuenta);
		fireTableDataChanged();
	}
	public CuentasBancos getCuenta(int index){
		return cuentas.get(index);
	}
	
	public void cambiarUsuario(int index,CuentasBancos cuenta){
		cuentas.set(index, cuenta);
		fireTableDataChanged();
	}
	
	public void eliminar(int rowIndex) {
		cuentas.remove(rowIndex);
        fireTableDataChanged();
    }
     
    public void limpiar() {
    	cuentas.clear();
        fireTableDataChanged();
    }
	
	 @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
	 
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return cuentas.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
			switch (columnIndex) {
					
					case 0: 
						return cuentas.get(rowIndex).getId();
					case 1:
						return cuentas.get(rowIndex).getNombre();
					case 2:
						return cuentas.get(rowIndex).getNoCuenta();
					case 3:
						return cuentas.get(rowIndex).getTipoCuenta();
					default:
			            return null;
				}
	}
	@Override
    public Class getColumnClass(int columnIndex) {
		//        return getValueAt(0, columnIndex).getClass();
        return String.class;
        
        
        /*switch (columnIndex) {
        case 0:
            return Integer.class;
        case 1:
            return String.class;
        case 2:
        	return String.class;
        
        default:
            return null;
            }*/
    }

}
