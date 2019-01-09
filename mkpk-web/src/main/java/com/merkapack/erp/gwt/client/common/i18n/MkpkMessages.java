package com.merkapack.erp.gwt.client.common.i18n;

import com.google.gwt.i18n.client.Messages;

public interface MkpkMessages extends Messages {
	// Á --> \u00C1 á --> \u00E1
	// É --> \u00C9 é --> \u00E9
	// Í --> \u00CD í --> \u00ED
	// Ó --> \u00D3 ó --> \u00F3
	// Ú --> \u00DA ú --> \u00FA ... acento
	// Ü --> \u00DC ü --> \u00fc ... diéresis
	// Ñ --> \u00D1 ñ --> \u00F1
	// º --> \u00BA ª --> \u00AA
	// ¿ --> \u00BF

	// ----------------------------------------------------------------- Format
	@DefaultMessage("#,##0")
	String integerPattern();

	@DefaultMessage("#,##0.00")
	String decimalPattern();

	@DefaultMessage("dd/MM/yyyy")
	String datePattern();

	@DefaultMessage("EEEEEEEE")
	String dayPattern();

	@DefaultMessage("Configuraci\u00F3n")
	String configuration();
	
	@DefaultMessage("Aceptar")
	String accept();

	@DefaultMessage("Buscar")
	String search();
	
	@DefaultMessage("Borrar")
	String delete();
	
	@DefaultMessage("Cancelar")
	String cancel();
	
	@DefaultMessage("Referencia")
	String reference();
	
	@DefaultMessage("Descripci\u00F3n")
	String description();
	
	@DefaultMessage("C\u00F3digo")
	String code();

	@DefaultMessage("Composici\u00F3n")
	String composition();

	@DefaultMessage("\u00BFDesea cancelar la operaci\u00F3n?")
	String cancelAction();

	@DefaultMessage("\u00BFContinuar con el borrado?")
	String deleteConfirmation();

	@DefaultMessage("Fabricaci\u00F3n")
	String manufacturing();

	@DefaultMessage("Plan de fabricaci\u00F3n autom\u00E1tica")
	String manufacturingPlanning();

	@DefaultMessage("Estadillo de fabricaci\u00F3n")
	String manufacturingInventory();

	@DefaultMessage("An\u00E1lisis estad\u00EDstico de fabricaci\u00F3n")
	String manufacturingStats();

	@DefaultMessage("Materiales")
	String materials();

	@DefaultMessage("Material")
	String material();

	@DefaultMessage("Art\u00EDculos")
	String products();

	@DefaultMessage("Art\u00EDculo")
	String product();

	@DefaultMessage("Bobinas")
	String rolls();

	@DefaultMessage("Bobina")
	String roll();

	@DefaultMessage("Medida")
	String measure();

	@DefaultMessage("Clientes")
	String clients();
	
	@DefaultMessage("Cliente")
	String client();
	
	@DefaultMessage("M\u00E1quinas")
	String machines();

	@DefaultMessage("M\u00E1quina")
	String machine();

	@DefaultMessage("Grosor")
	String thickness();

	@DefaultMessage("Ancho")
	String width();

	@DefaultMessage("Alto")
	String height();

	@DefaultMessage("Largo")
	String length();

	@DefaultMessage("Golpes")
	String blows();
	
	@DefaultMessage("Golpes por defecto")
	String defaultBlows();

	@DefaultMessage("Fecha inicial")
	String startDate();

	@DefaultMessage("Fecha")
	String date();

	@DefaultMessage("Unidad")
	String unit();

	@DefaultMessage("Un./Golpe")
	String blowUnits();

	@DefaultMessage("U/G")
	String blowUnitsAbbr();

	@DefaultMessage("Metros")
	String meters();
	
	@DefaultMessage("Golpes/Min.")
	String blowsMinutes();
	
	@DefaultMessage("G/M")
	String blowsMinutesAbbrv();

	@DefaultMessage("Tiempo")
	String time();
	
	@DefaultMessage("Comentarios")
	String comments();
	
	@DefaultMessage("Chequear plan")
	String checkPlanning();
	
	@DefaultMessage("Nueva l\u00EDnea")
	String newLine();
	
	@DefaultMessage("Subir fichero")
	String uploadFile();

	@DefaultMessage("No se han encontrado datos")
	String noData();
	
	@DefaultMessage("Golpes minutos por defecto")
	String defaultBlowsMiniute();
	
	@DefaultMessage("Margen de tiempo en la jornada")
	String hoursMargin();
	
	@DefaultMessage("Jornada laboral")
	String workHoursInADay();

	

	
}
