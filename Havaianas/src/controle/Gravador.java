package controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import modelo.Dimensao;

public class Gravador {

	private FileWriter fw;
	private BufferedWriter gravador;
	private File novoArquivo;
	private String caminhoArquivo;

	List<Dimensao> novaListaDimensoes;

	public Gravador(String nomeArquivo) {

		try {

			caminhoArquivo = Constantes.caminhoArquivo;
			novoArquivo = new File(caminhoArquivo + nomeArquivo);
			fw = new FileWriter(novoArquivo);
			gravador = new BufferedWriter(fw);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void gravarLinha(List<String> linha) {

		StringBuffer linhaParaGravacao = new StringBuffer();

		for (int i = 0; i < linha.size(); i++) {

			linhaParaGravacao.append(linha.get(i));

			if (i != linha.size() - 1) {
				linhaParaGravacao.append(";");
			}
		}

		try {
			
			gravador.write(linhaParaGravacao.toString());
			gravador.newLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void closeGravador(){
		try{
		gravador.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gravarArquivoDados(List<String> linha, Boolean pulaLinha){
		StringBuffer linhaParaGravacao = new StringBuffer();

		for (int i = 0; i < linha.size(); i++) {

			linhaParaGravacao.append(linha.get(i));

			if (i != linha.size() - 1) {
				linhaParaGravacao.append(" ");
			}
		}

		try {
			
			gravador.write(linhaParaGravacao.toString());
			if(pulaLinha)
				gravador.newLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
