package br.com.fiap.PowerCycle.response;

import br.com.fiap.PowerCycle.exception.TripException;

public class TripNotFoundException extends TripException{
	
	  public TripNotFoundException(String message) {
	        super(message);
	    }
	
}
