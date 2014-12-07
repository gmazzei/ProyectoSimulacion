package com.simulacion;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.Model;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import com.simulacion.behavior.NoInputBehavior;
import com.simulacion.simulador.Resultados;
import com.simulacion.simulador.Simulador;

public class AwesomePage extends WebPage {
	
	
	private final Long TIME_INIT_VALUE = 1000000l;
	private final Long TIME_MIN_VALUE = 1000000l;
	private final Long TIME_STEP = 1000000l;
	private final Long TIME_MAX_VALUE = 100000000l;

	private final Integer CONTROL_VAR_INIT_VALUE = 1;
	private final Integer CONTROL_VAR_MIN_VALUE = 1;
	private final Integer CONTROL_VAR_MAX_VALUE = 99;

	private final Integer CANT_CONEXIONES_NODO = 120;
	
	public AwesomePage() {
		
		//Form
		
		Form form = new Form("form");
		this.add(form);
		
		final NumberTextField<Integer> servidoresFisicos = new NumberTextField<Integer>("servidoresFisicos");
		servidoresFisicos.setModel(Model.of(CONTROL_VAR_INIT_VALUE));
		servidoresFisicos.setMinimum(CONTROL_VAR_MIN_VALUE);
		servidoresFisicos.setMaximum(CONTROL_VAR_MAX_VALUE);
		servidoresFisicos.add(new NoInputBehavior());
		form.add(servidoresFisicos);
		
		final NumberTextField<Integer> servidoresApache = new NumberTextField<Integer>("servidoresApache");
		servidoresApache.setModel(Model.of(CONTROL_VAR_INIT_VALUE));
		servidoresApache.setMinimum(CONTROL_VAR_MIN_VALUE);
		servidoresApache.setMaximum(CONTROL_VAR_MAX_VALUE);
		servidoresApache.add(new NoInputBehavior());
		form.add(servidoresApache);
		
		final NumberTextField<Integer> personas = new NumberTextField<Integer>("personas");
		personas.setModel(Model.of(CONTROL_VAR_INIT_VALUE));
		personas.setMinimum(CONTROL_VAR_MIN_VALUE);
		personas.setMaximum(CONTROL_VAR_MAX_VALUE);
		personas.add(new NoInputBehavior());
		form.add(personas);
		
		final NumberTextField<Long> tiempoSimulacion = new NumberTextField<Long>("tiempoSimulacion");
		tiempoSimulacion.setModel(Model.of(TIME_INIT_VALUE));
		tiempoSimulacion.setStep(TIME_STEP);
		tiempoSimulacion.setMinimum(TIME_MIN_VALUE);
		tiempoSimulacion.setMaximum(TIME_MAX_VALUE);
		tiempoSimulacion.add(new NoInputBehavior());
		form.add(tiempoSimulacion);
		
		
		//Resultados
		
		final WebMarkupContainer panelResultados = new WebMarkupContainer("panelResultados");
		panelResultados.setOutputMarkupId(true);
		this.add(panelResultados);
		
		final Label porcentajeConexionesRechazadas = new Label("porcentajeConexionesRechazadas");
		panelResultados.add(porcentajeConexionesRechazadas);
				
		final Label porcentajeTiempoOcioso = new Label("porcentajeTiempoOcioso");
		panelResultados.add(porcentajeTiempoOcioso);
				
		final Label promedioTiempoEntreReseteos = new Label("promedioTiempoEntreReseteos");
		panelResultados.add(promedioTiempoEntreReseteos);
				
		final Label promedioConexionesClusterPorMinuto = new Label("promedioConexionesClusterPorMinuto");
		panelResultados.add(promedioConexionesClusterPorMinuto);
		
		final Label nodos = new Label("nodos");
		panelResultados.add(nodos);
		
		
		//Graficos
		final WebMarkupContainer panelGraficos = new WebMarkupContainer("panelGraficos");
		panelGraficos.setOutputMarkupId(true);
		this.add(panelGraficos);
		
		// 1 - Grafico Conexiones
		Options optionsCon = new Options();
		optionsCon.setTitle(new Title("Conexiones Aceptadas y Rechazadas"));
		optionsCon.setChartOptions(new ChartOptions(SeriesType.PIE));
				
				
		final Point barraConexionesRechazadas = new Point("Conexiones Rechazadas", 50).setColor(new HighchartsColor(1));
		final Point barraConexionesAceptadas = new Point("Conexiones Aceptadas", 50).setColor(new HighchartsColor(2));
				
		optionsCon.addSeries(new PointSeries()
				.setName("Porcentaje")
				.setColor(new HexColor("#000000"))
				.addPoint(barraConexionesRechazadas)
				.addPoint(barraConexionesAceptadas));
		
		
		Chart graficoConexiones = new Chart("graficoConexiones", optionsCon);
		panelGraficos.add(graficoConexiones);
		
		
		// 2 - Grafico Tiempo entre Reseteos
		Options optionsReset = new Options();
		optionsReset.setTitle(new Title("Promedio de Tiempo entre Reseteos"));
		optionsReset.setChartOptions(new ChartOptions(SeriesType.COLUMN));
		optionsReset.setxAxis(new Axis().setCategories("Tiempo"));
		optionsReset.setyAxis(new Axis().setTitle(new Title("Minutos")).setMax(4000).setTickInterval(200.0f));		

						
		final Point barraReseteos = new Point("Tiempo entre Reseteos", 1000).setColor(new HighchartsColor(1));
						
		optionsReset.addSeries(new PointSeries()
				.setName("Tiempo")
				.setColor(new HexColor("#000000"))
				.addPoint(barraReseteos));
				
				
		Chart graficoReseteos = new Chart("graficoReseteos", optionsReset);
		panelGraficos.add(graficoReseteos);
				
		
		
		// 3 - Grafico Tiempo Ocioso
		Options optionsTime = new Options();
		optionsTime.setTitle(new Title("Tiempo de Procesamiento y Tiempo Ocioso"));
		optionsTime.setChartOptions(new ChartOptions(SeriesType.PIE));
				
				
		final Point barraTiempoOcioso = new Point("Tiempo Ocioso", 50).setColor(new HighchartsColor(3));
		final Point barraTiempoProcesamiento = new Point("Tiempo de Procesamiento", 50).setColor(new HighchartsColor(4));
		
		
		optionsTime.addSeries(new PointSeries()
				.setName("Porcentaje")
				.setColor(new HexColor("#000000"))
				.addPoint(barraTiempoOcioso)
				.addPoint(barraTiempoProcesamiento));
		
		
		Chart graficoTiempo = new Chart("graficoTiempo", optionsTime);
		panelGraficos.add(graficoTiempo);
		
		
		// 4 - Promedio de conexiones al Cluster
		Options optionsCluster = new Options();
		optionsCluster.setTitle(new Title("Promedio de Conexiones al Cluster por Minuto"));
		optionsCluster.setChartOptions(new ChartOptions(SeriesType.COLUMN));
		optionsCluster.setxAxis(new Axis().setCategories("Conexiones"));
		optionsCluster.setyAxis(new Axis().setTitle(new Title("Minutos")).setMax(4000).setTickInterval(200.0f));		

								
		final Point barraConexionesCluster = new Point("Conexiones", 2000).setColor(new HighchartsColor(2));
						
		optionsCluster.addSeries(new PointSeries()
				.setName("Cantidad de Conexiones")
				.setColor(new HexColor("#000000"))
				.addPoint(barraConexionesCluster));
						
						
		Chart graficoCluster = new Chart("graficoCluster", optionsCluster);
		panelGraficos.add(graficoCluster);
		
		
		
		
		final AjaxButton comenzarSimulacion = new AjaxButton("comenzarSimulacion") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
								
				Simulador simulador = Simulador.getInstance();
				Resultados resultados = simulador.realizarSimulacion(servidoresFisicos.getModelObject(), servidoresApache.getModelObject(), personas.getModelObject(), tiempoSimulacion.getModelObject());
				

				Double cantidadNodos = resultados.getPromedioConexionesClusterPorMinuto() / CANT_CONEXIONES_NODO;

				porcentajeConexionesRechazadas.setDefaultModel(Model.of(resultados.getPorcentajeDeConexionesRechazadas()));
				porcentajeTiempoOcioso.setDefaultModel(Model.of(resultados.getPorcentajeDeTiempoOcioso()));
				promedioTiempoEntreReseteos.setDefaultModel(Model.of(resultados.getPromedioDeTiempoEntreReseteos()));
				promedioConexionesClusterPorMinuto.setDefaultModel(Model.of(resultados.getPromedioConexionesClusterPorMinuto()));
				nodos.setDefaultModel(Model.of(cantidadNodos.intValue()));				

				barraConexionesRechazadas.setY(resultados.getPorcentajeDeConexionesRechazadas());
				barraConexionesAceptadas.setY(100.0 - resultados.getPorcentajeDeConexionesRechazadas());
				barraReseteos.setY(resultados.getPromedioDeTiempoEntreReseteos());
				barraTiempoOcioso.setY(resultados.getPorcentajeDeTiempoOcioso());
				barraTiempoProcesamiento.setY(100.0 - resultados.getPorcentajeDeTiempoOcioso());
				barraConexionesCluster.setY(resultados.getPromedioConexionesClusterPorMinuto());
				
				target.add(panelResultados);
				target.add(panelGraficos);
			}
			
		};
		form.add(comenzarSimulacion);
		
    }
}








/*
// Grafico
Options options = new Options();
options.setChartOptions(new ChartOptions(SeriesType.COLUMN));
options.setxAxis(new Axis().setCategories("Conexiones Rechazadas", "Tiempo Ocioso"));
options.setyAxis(new Axis().setMax(100).setTickInterval(10.0f));		


final Point barraConexionesRechazadas = new Point(0).setColor(new HighchartsColor(1));
final Point barraTiempoOcioso = new Point(0).setColor(new HighchartsColor(2));

options.addSeries(new PointSeries()
		.setColor(new HexColor("#000000"))
		.addPoint(barraConexionesRechazadas)
		.addPoint(barraTiempoOcioso));
	        

final Chart grafico = new Chart("grafico", options);
grafico.setOutputMarkupId(true);
this.add(grafico);
*/

