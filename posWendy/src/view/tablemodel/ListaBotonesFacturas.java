package view.tablemodel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import modelo.Factura;
import view.botones.BotonesApp;

public class ListaBotonesFacturas extends AbstractListModel {
	private static final int ancho=128;
	private static final int alto=45;
	private List<JToggleButton> btnGuardados= new ArrayList<>();
	private List<Factura> facturas=new ArrayList<>();
	private ButtonGroup grupoOpciones;
	private Font myFon;
	
	public ListaBotonesFacturas(){
		
		grupoOpciones = new ButtonGroup(); // crea ButtonGroup//para el grupo de botones
		
		Factura nueva=new Factura();
		facturas.add(nueva);
		
		JToggleButton nuevo=new JToggleButton("Nueva factura");
		nuevo.setSelected(true);
		btnGuardados.add(nuevo);
		grupoOpciones.add(nuevo);
		
		nuevo.setSize(ancho, alto);
		myFon=new Font("Georgia", Font.PLAIN, 13);
		nuevo.setFont(myFon);
		Color color1 =new Color(60, 179, 113);
		nuevo.setBackground(color1);
	}



	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return btnGuardados.size();
	}

	@Override
	public JToggleButton getElementAt(int index) {
		// TODO Auto-generated method stub
		return btnGuardados.get(index);
	}
	public Factura getFacturaBoton(int index){
		return facturas.get(index);
	}
	public void addBoton(Factura fact){
		facturas.add(fact);
		
		JToggleButton nuevo=new JToggleButton(fact.getCliente().getNombre());
		btnGuardados.add(nuevo);
		grupoOpciones.add(nuevo);
		
		nuevo.setSize(ancho, alto);
		myFon=new Font("Georgia", Font.PLAIN, 13);
		nuevo.setFont(myFon);
		Color color1 =new Color(60, 179, 113);
		nuevo.setBackground(color1);
		
	}
	public Factura buscarFactura(int id){
		for(Factura fac: facturas){
			if(fac.getIdFactura()==id){
				return fac;
			}
			
		}
		return null;
	}
	public int buscarFacturaIndex(int id){
		for(int x=0;x<facturas.size();x++){
			
			if(facturas.get(x).getIdFactura()==id){
				return x;
			}
			
		}
		return -1;
	}
	public void deleteAll(){
		
			this.facturas.removeAll(facturas);
			this.btnGuardados.removeAll(btnGuardados);
			

			Factura nueva=new Factura();
			facturas.add(nueva);
			
			JToggleButton nuevo=new JToggleButton("Nueva factura");
			grupoOpciones.add(nuevo);
			nuevo.setSelected(true);
			btnGuardados.add(nuevo);
			
			nuevo.setSize(ancho, alto);
			myFon=new Font("Georgia", Font.PLAIN, 13);
			nuevo.setFont(myFon);
			Color color1 =new Color(60, 179, 113);
			nuevo.setBackground(color1);
			
	}
	public void setFactura(Factura f){
		
		//facturas.remove(buscarFacturaIndex(f.getIdFactura()));
		this.addBoton(f);
		
		//facturas.get(buscarFacturaIndex(f.getIdFactura())).set;
		
		//facturas.set(buscarFacturaIndex(f.getIdFactura()), f);
	}
	
	public Factura getFacturaSeleted(){
		for(int x=0; x<btnGuardados.size();x++){
			if(btnGuardados.get(x).isSelected()){
				return facturas.get(x);
			}
		}
		return null;
	}
	

}
