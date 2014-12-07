package com.simulacion.simulador;

public class Resultados {
	
	private Double porcentajeDeConexionesRechazadas;
	private Double porcentajeDeTiempoOcioso;
	private Double promedioDeTiempoEntreReseteos;
	private Double promedioConexionesClusterPorMinuto;
	
	
	
	public String getValoresResultado() {
		
		String valores = "";
		valores += "Porcentaje de Conexiones Rechazadas: " + this.porcentajeDeConexionesRechazadas + "\n";
		valores += "Porcentaje de Tiempo Ocioso: " + this.porcentajeDeTiempoOcioso + "\n";
		valores += "Promedio de tiempo entre reseteos: " + this.promedioDeTiempoEntreReseteos + "\n";
		valores += "Promedio de conexiones al Cluster por minuto: " + this.promedioConexionesClusterPorMinuto + "\n";
		
		return valores;
	}
	
	
	
	@Override
	public String toString() {
		return this.getValoresResultado();
	}






	public Double getPorcentajeDeConexionesRechazadas() {
		return porcentajeDeConexionesRechazadas;
	}
	
	public void setPorcentajeDeConexionesRechazadas(Double porcentajeDeConexionesRechazadas) {
		this.porcentajeDeConexionesRechazadas = porcentajeDeConexionesRechazadas;
	}
	
	public Double getPorcentajeDeTiempoOcioso() {
		return porcentajeDeTiempoOcioso;
	}
	
	public void setPorcentajeDeTiempoOcioso(Double porcentajeDeTiempoOcioso) {
		this.porcentajeDeTiempoOcioso = porcentajeDeTiempoOcioso;
	}
	
	public Double getPromedioDeTiempoEntreReseteos() {
		return promedioDeTiempoEntreReseteos;
	}
	
	public void setPromedioDeTiempoEntreReseteos(Double promedioDeTiempoEntreReseteos) {
		this.promedioDeTiempoEntreReseteos = promedioDeTiempoEntreReseteos;
	}

	public Double getPromedioConexionesClusterPorMinuto() {
		return promedioConexionesClusterPorMinuto;
	}

	public void setPromedioConexionesClusterPorMinuto(
			Double promedioConexionesClusterPorMinuto) {
		this.promedioConexionesClusterPorMinuto = promedioConexionesClusterPorMinuto;
	}
	
}
