package controle;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import modelo.EstruturaArquivo;
import modelo.Tupla;

public class ArquivoDAO {

	private Connection conn;
	private EstruturaArquivo estrutura;
	BufferedLeitor leitor;
	int indiceTupla = 1;
	String nomeTabela;
	Integer numeroDimensoes;

	public ArquivoDAO(Connection conn, File arquivo) {
		this.conn = conn;
		try {

			leitor = new BufferedLeitor(arquivo);

			estrutura = leitor.getEstrutura();
			numeroDimensoes = estrutura.getDimensoes().size();
			nomeTabela = Util.obterNomeTabela(estrutura, 0, 20);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void armazenarArquivo(Integer skew, Integer cardinalidade) {

		StringBuffer sql = new StringBuffer();
		StringBuffer sqlInsercao;
		String nomeTabela = Util.obterNomeTabela(estrutura, skew, cardinalidade);
		PreparedStatement ps;

		sql.append("CREATE TABLE ");
		sql.append(nomeTabela);
		sql.append(" (id INTEGER PRIMARY KEY");

		for (int i = 0; i < estrutura.getDimensoes().size(); i++) {
			sql.append(",\"");
			sql.append(i + 1);
			sql.append("\" INTEGER");
		}

		sql.append(")");

		try {
			
			ps = conn.prepareStatement(sql.toString());

			ps.execute();

			JOptionPane.showMessageDialog(null, "Tabela " + nomeTabela
					+ " criada com sucesso. Populando a tabela.");

			Tupla tupla;
			
			int id = 1;
			for (int i = 0; i < estrutura.getNumeroTuplas(); i++) {
				
				sqlInsercao = new StringBuffer();
				sqlInsercao.append("INSERT INTO ");
				sqlInsercao.append(nomeTabela);
				sqlInsercao.append(" (id,");
				for (int k = 0; k < estrutura.getDimensoes().size(); k++) {
					sqlInsercao.append("\"");
					sqlInsercao.append(k + 1);
					sqlInsercao.append("\"");
					if (k != estrutura.getDimensoes().size() - 1)
						sqlInsercao.append(",");
				}
				sqlInsercao.append(") VALUES (");
				sqlInsercao.append(id);
				sqlInsercao.append(",");

				tupla = leitor.obterTupla();
				
				for(int j = 0; j < tupla.getValores().length; j++){
					
					sqlInsercao.append(tupla.getValores()[j]);
					
					if(j != tupla.getValores().length - 1)
						sqlInsercao.append(",");
				}
				
				sqlInsercao.append(")");
				
				ps = conn.prepareStatement(sqlInsercao.toString());
				
				ps.execute();
				
				id++;
			}
			
			ps.close();
			
			JOptionPane.showMessageDialog(null, "Dados inseridos com sucesso");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Tupla[] obterArrayTuplas(){
		Tupla[] tuplas = new Tupla[estrutura.getNumeroTuplas()];
		
		
		return tuplas;
	}
	
	public Tupla obterProximaTupla(){
		
		Tupla tupla = null;
		PreparedStatement ps;
		String sql = "SELECT * FROM " + nomeTabela + " WHERE id = " + indiceTupla;
		int[] valores = new int[numeroDimensoes];
		ResultSet rs;
		
		
		try{
			
		ps = conn.prepareStatement(sql);
		
		rs = ps.executeQuery();
		rs.next();
		for(int i = 0; i < numeroDimensoes; i++){
			valores[i] = rs.getInt(i + 2);
		}
		
		
		
		
		tupla = new Tupla(valores);
		
		
		ps.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		indiceTupla++;
		return tupla;
	}

}
