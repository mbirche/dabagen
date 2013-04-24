package controle;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.swing.JOptionPane;

import modelo.EstruturaArquivo;
import modelo.Tupla;

public class TestesVelocidade {

	File arquivo;
	Connection conn;
	BufferedLeitor leitor;
	PreparedStatement ps;
	String nomeTabela;
	Date inicioArquivo, fimArquivo, inicioBD, fimBD;
	Long milisegundosArquivo, milisegundosBD;
	EstruturaArquivo estrutura;
	Tupla tupla;
	ArquivoDAO dao;
	Integer numeroTuplas;
	
	public TestesVelocidade(File arquivo) {
		
		this.arquivo = arquivo;
		conn = Conexao.getConexao();
		try{
		leitor = new BufferedLeitor(arquivo);
		}catch (Exception e){
			e.printStackTrace();
		}
		estrutura = leitor.getEstrutura();		
		nomeTabela = Util.obterNomeTabela(estrutura, 0, 20);
		dao = new ArquivoDAO(conn, arquivo);
		numeroTuplas = estrutura.getNumeroTuplas();
	}
	
	public void testeLeituraTupla(){
		
		
		inicioArquivo = new Date();
		
		for(int i = 0; i < numeroTuplas; i++){
			
			tupla = leitor.obterTupla();
		}
		
		fimArquivo = new Date();
		
		inicioBD = new Date();
		
		for(int i = 0; i < numeroTuplas; i++){
			
			tupla = dao.obterProximaTupla();
		}
		
		fimBD = new Date();
		
		
		milisegundosArquivo = fimArquivo.getTime() - inicioArquivo.getTime();
		milisegundosBD = fimBD.getTime() - inicioBD.getTime();
		
		JOptionPane.showMessageDialog(null, "Tempo de leitura do arquivo: " + milisegundosArquivo + "\n"+
									   "Tempo de leitura do BD: " + milisegundosBD);
	}
	
}
