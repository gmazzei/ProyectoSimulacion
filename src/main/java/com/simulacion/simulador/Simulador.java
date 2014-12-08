package com.simulacion.simulador;

public class Simulador {
	
	private final Integer CONEXIONES_POR_APACHE = 1000;
	private final Double PROBABILIDAD_CAIDA_APACHE = 0.0004;
	
	private static final Simulador INSTANCE = new Simulador();
	
	
	private Simulador() {
		
	}
	
	public static Simulador getInstance() {
		return Simulador.INSTANCE;
	}
	
	
	public Resultados realizarSimulacion(Integer s, Integer n, Integer p, Long tf) {
		
		System.out.println("Simulacion a Realizar: ");
		System.out.println("S = " + s);
		System.out.println("N = " + n);
		System.out.println("P = " + p);
		System.out.println("TF = " + tf);
		
		
		//Condiciones iniciales
		long time = 0;
		long st = s * n * CONEXIONES_POR_APACHE;
		long ster = 0;
		long iter = 0;
		long cReset = 0;
		long cr = 0;
		long scr = 0;
		long de = 0;
		long cl = 0;
		long cs = 0;
		long scs = 0;
		Double pcbd = 0.0;
		Double scbd = 0.0;
		long sto = 0;
		Double pcr = 0.0;
		Double pto = 0.0; 
		Double pter = 0.0;
		Double pccm = 0.0;
		long mrs = -1;
		Double random;
		
		while (time <= tf) {
			
			time = time + 1;
			
			//Reseteo del sistema
			if (time == mrs) {
				
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
		
		pcr = (scr * 100.0) / scs;
		pto = (sto * 100.0) / time;
		pter = (double) ster / cReset;
		pccm = scbd.doubleValue() / time;
		
		System.out.println("PCR: " + pcr);
		System.out.println("PTO: " + pto);
		System.out.println("PTER: " + pter);
		System.out.println("PCCM: " + pccm);
		
		
		Resultados resultados = new Resultados();
		resultados.setPorcentajeDeConexionesRechazadas(pcr);
		resultados.setPorcentajeDeTiempoOcioso(pto);
		resultados.setPromedioDeTiempoEntreReseteos(pter);
		resultados.setPromedioConexionesClusterPorMinuto(pccm);
		
		return resultados;
		
	}

	



	//FDPs
	
	private Double generarPorcentajeDeConexionesBD() {
		
		//Por falta de exactitud del numero PI, se incluye un control para que el porcentaje
		//este en el intervalo [0;1]
		
		Double porcentaje = null;
		
		while (porcentaje == null || porcentaje < 0 || porcentaje > 1) {
			Double random = Math.random();
			porcentaje = ((Math.tan(Math.PI * (random - 0.5)) * 5.953) + 59.725) / 100.0;			
		}
		
		return porcentaje;
	}

	private Integer generarConexionesSolicitadas() {

		Double random = Math.random(); 
		
		while (random.equals(0.0) || random.equals(1.0)) {
			random = Math.random();
		}
		
		Double conexiones = 5312.7 - (639.14 * Math.log(-Math.log(random)));
		return conexiones.intValue();
	}

	private Integer generarConexionesLiberadas() {
		
		Double random = Math.random(); 
		
		while (random.equals(1.0)) {
			random = Math.random();
		}
		
		Double conexiones = ((-Math.log(1 - random)) / 0.00032254) + 2344;
		
		return conexiones.intValue();
		

	}

	public Integer generarDemora(Integer p) {
		
		
		Double minutos = null;
		Double a, b, c, d; /* 'c' es alfa 1 y 'd' es alfa 2*/
		
		while (minutos == null || minutos.intValue() <= 0) {
			
			Double random = Math.random();
			
			if (p.equals(1)) {
				a = 3.19;
				b = 32.481;
				c = 1.0;
				d = 0.99993;
				
				minutos = a + (b - a) * Math.pow((1.0 - Math.pow((1.0 - random), (1.0 / d))), (1.0 / c)) ; 
				
			} else if (p.equals(2)) {
				
				while (random.equals(0.0)) {
					random = Math.random();
				}
				
				a = 2.9619;
				b = 10.3;
				
				minutos = b - (a * Math.log(-Math.log(random))); 
				
			} else if (p.equals(3)) {
				
				a = 0.40364;
				b = 9.878;
				c = 1.0679;
				d = 0.92411;
				
				minutos = a + (b - a) * Math.pow((1.0 - Math.pow((1.0 - random), (1.0 / d))), (1.0 / c)) ;
				
			} else if (p.equals(4)) {
				
				a = 0.93261;
				b = 7.48;
				c = 1.163;
				d = 0.8987;
				
				minutos = a + (b - a) * Math.pow((1.0 - Math.pow((1.0 - random), (1.0 / d))), (1.0 / c)) ; 
				
				
			} else if (p.equals(5)) {
				
				a = 2.4394;
				b = 3.6445;
				
				minutos = b * Math.pow(Math.log(1.0 / (1 - random)), 1.0 / a);
				
			}
			
		}
		
		
		
		return minutos.intValue();
	}

}
