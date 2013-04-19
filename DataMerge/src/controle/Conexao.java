package controle;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class Conexao {
	
	public static Connection getConexao(){
		Connection conn = null;
		
		try{
		conn = DriverManager.getConnection(Constantes.url, Constantes.user, Constantes.password);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if(conn != null)
			JOptionPane.showMessageDialog(null, "Conexão OK!");
		return conn;
	}

}
