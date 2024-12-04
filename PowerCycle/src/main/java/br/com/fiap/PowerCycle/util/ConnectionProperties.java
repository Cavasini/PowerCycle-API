package br.com.fiap.PowerCycle.util;

import java.util.Properties;

public class ConnectionProperties {
	
	public static Properties obterPropriedades() {
		Properties props = new Properties();
		props.put("user", "RM97722");
		props.put("password", "120205");

		return props;
	}

}
