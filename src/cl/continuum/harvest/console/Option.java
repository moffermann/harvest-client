package cl.continuum.harvest.console;

import java.lang.reflect.Method;

public class Option {
	
	private char codigo;
	private String descripcion;
	private String metodo;
	public Option(char codigo, String descripcion, String metodo) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.metodo = metodo;
	}
	
	public void execute() throws Exception {
		Method method = Main.class.getMethod(metodo);
		method.invoke(null);
	}
	
	public String toString() {
		return "(" + codigo + ") " + descripcion;
	}
	

}
