package com.sl.galaxy.model;

/**
 * 
 * @author Silvina_Luque
 *
 */
public class WeatherResponse  {
	
	public static final String ERROR_GENERICO = "ERROR_GENERICO";
	
	public static final String ERROR_INPUT_PARAM = "ERROR_INPUT_PARAM";
	
	public static final String OK = "OK";

	public static final String MESSAGE_ERROR_INPUT_DAY = "Día ingresado invalido";
	
	public static final String MESSAGE_ERROR_INPUT_PERIOD = "Periodo ingresado invalido";

	public static final String MESSAGE_ERROR_DB = "Error generico de datos";

	public static final String MESSAGE_ERROR_INPUT_PARAM = "Error con los datos ingresados";
	
	
	private String code;
	
	private String message;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
