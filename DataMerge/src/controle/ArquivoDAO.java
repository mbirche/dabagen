package controle;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import modelo.EstruturaArquivo;
import modelo.Tupla;

public class ArquivoDAO {

	private Connection conn;
	private EstruturaArquivo estrutura;
	BufferedLeitor leitor;

	public ArquivoDAO(Connection conn, File arquivo) {
		this.conn = conn;
		try {

			leitor = new BufferedLeitor(arquivo);

			estrutura = leitor.getEstrutura();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void armazenarArquivo(Integer skew, Integer cardinalidade) {

		StringBuffer sql = new StringBuffer();
		StringBuffer sqlInsercao;
		StringBuffer nomeTabela = new StringBuffer();
		PreparedStatement ps;

		nomeTabela.append("D");
		nomeTabela.append(Util.obterValorComPrefixo(estrutura.getDimensoes()
				.size()));
		nomeTabela.append("_T");
		nomeTabela
				.append(Util.obterValorComPrefixo(estrutura.getNumeroTuplas()));
		nomeTabela.append("_S");
		nomeTabela.append(Util.obterValorComPrefixo(skew));
		nomeTabela.append("_C");
		nomeTabela.append(Util.obterValorComPrefixo(cardinalidade));

		System.out.println(nomeTabela.toString());

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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
