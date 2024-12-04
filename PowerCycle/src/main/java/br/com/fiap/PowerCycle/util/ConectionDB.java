package br.com.fiap.PowerCycle.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionDB {
	
	private static ConectionDB instancia;
	
	private Connection conn;
	
	private ConectionDB() {
		
		try {
			Class.forName ("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
			conn = DriverManager.getConnection(url, ConnectionProperties.obterPropriedades());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ConectionDB getInstance() {
		if(instancia == null) {
			instancia = new ConectionDB();
		}
		return instancia;
	}

	public Connection getConn() {
		return conn;
	}


}
