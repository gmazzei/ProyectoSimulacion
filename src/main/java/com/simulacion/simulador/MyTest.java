package com.simulacion.simulador;

public class MyTest {

	public static void main(String[] args) {

		Integer p = 5;
		Double minutos = null;
		
		while (true) {
			
			System.out.println(Simulador.getInstance().generarDemora(p));
		}
		
	}

}
