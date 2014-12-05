package com.simulacion.simulador;

public class Simulador {
	
	private final Integer CONEXIONES_POR_APACHE = 120;
	private final Double PROBABILIDAD_CAIDA_APACHE = 0.0004;
	
	private static final Simulador INSTANCE = new Simulador();
	
	private Simulador() {
		
	}
	
	public static Simulador getInstance() {
		return Simulador.INSTANCE;
	}
	
	
	public Resultados realizarSimulacion(Integer s, Integer n, Integer p, Integer tf) {
		
		System.out.println("Simulacion a Realizar: ");
		System.out.println("S = " + s);
		System.out.println("N = " + n);
		System.out.println("P = " + p);
		System.out.println("TF = " + tf);
		
		
		//Condiciones iniciales
		Integer time = 0;
		Integer st = s * n * CONEXIONES_POR_APACHE;
		Integer ster = 0;
		Integer iter = 0;
		Integer cReset = 0;
		Integer cr = 0;
		Integer scr = 0;
		Integer de = 0;
		Integer cl = 0;
		Integer cs = 0;
		Integer scs = 0;
		Double pcbd = 0.0;
		Double scbd = 0.0;
		Integer sto = 0;
		Double pcr = 0.0;
		Double pto = 0.0; 
		Double pter = 0.0;
		Double pccm = 0.0;
		Integer mrs = -1;
		Double random;
		
		while (time <= tf) {
			
			time = time + 1;
			
			//Reseteo del sistema
			if (time.equals(mrs)) {
				
				st = CONEXIONES_POR_APACHE * n * s;
				ster = ster + (time - iter);
				iter = time;
				cReset++;
			}
			
			
			//Caida del sistema
			random = Math.random();
			
			if ((mrs <= time) && (random <= (n * PROBABILIDAD_CAIDA_APACHE))) {
				
				de = this.generarDemora(p);	
				sto = sto + de;
				mrs = time + de;
				
				cr = (n * s * CONEXIONES_POR_APACHE) - st;
				scr = scr + cr;
				
				
				st = 0;
				
			}
			
			
			//Liberacion de conexiones
			if (mrs <= time) {
				
				cl = this.generarConexionesLiberadas();
				
				if ((st + cl) <= (n * s * CONEXIONES_POR_APACHE)) {
					st = st + cl;
				} else {
					st = n * s * CONEXIONES_POR_APACHE;
				}
				
			}
			
			
			//Solicitud de conexiones
			cs = this.generarConexionesSolicitadas();
			scs = scs + cs;
			
			if (mrs <= time) {
				
				pcbd = this.generarPorcentajeDeConexionesBD();
				
				if (st >= cs) {
					
					st = st - cs;
					scbd = scbd + (cs * pcbd);
					
				} else {
					
					cr = cs - st;
					scr = scr + cr;
					st = 0;
					scbd = scbd + ((cs - cr) * pcbd);
					
				}
				
			} else {
				
				cr = cs;
				scr = scr + cr;
				
			}
			
			
		}
		
		pcr = scr * 100.0 / scs;
		pto = sto * 100.0 / time;
		pter = ster.doubleValue() / cReset;
		pccm = scbd.doubleValue() / time;
		
		
		Resultados resultados = new Resultados();
		resultados.setPorcentajeDeConexionesRechazadas(pcr);
		resultados.setPorcentajeDeTiempoOcioso(pto);
		resultados.setPromedioDeTiempoEntreReseteos(pter);
		resultados.setPromedioConexionesClusterPorMinuto(pccm);
		
		return resultados;
		
	}

	



	//FDPs
	
	private Double generarPorcentajeDeConexionesBD() {
		Double porcentaje = 0.5 + 0.2 * Math.random(); 
		return porcentaje;
	}

	private Integer generarConexionesSolicitadas() {
		Double conexiones = 20 + (40 * Math.random());
		return conexiones.intValue();
	}

	private Integer generarConexionesLiberadas() {
		Double conexiones = 20 + (40 * Math.random());
		return conexiones.intValue();
	}

	private Integer generarDemora(Integer p) {
		Double minutos = 6/p + (30 * Math.random()/p);
		return minutos.intValue();
	}

}
