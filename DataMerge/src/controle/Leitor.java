package controle;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import modelo.Dimensao;
import modelo.EstruturaArquivo;
import modelo.Tupla;

public class Leitor {

	private EstruturaArquivo estrutura;

	public Leitor() {
		estrutura = new EstruturaArquivo();
	}

	public EstruturaArquivo obterEstruturaArquivo(File arquivo) {

		char[] leitura = new char[Constantes.BufferLeitura];
		char[] primeiraLinha = new char[Constantes.BufferLeituraLinha];
		String strPrimeiraLinha;
		String[] elementosLinha;
		List<Dimensao> dimensoes = new ArrayList<Dimensao>();
		Dimensao dimensao;

		try {

			FileReader leitor = new FileReader(arquivo);

			leitor.read(leitura);

			for (int i = 0; i < leitura.length; i++) {
				if (leitura[i] != '\n')
					primeiraLinha[i] = leitura[i];
				else
					break;
			}

			//System.out.println(primeiraLinha);

			strPrimeiraLinha = String.valueOf(primeiraLinha);
			elementosLinha = strPrimeiraLinha.split(" ");

			for (int i = 0; i < elementosLinha.length - 1; i++) { // A última
																	// posição é
																	// um \r
				if (i == 0) {
					estrutura.setNumeroTuplas(Integer
							.parseInt(elementosLinha[i]));
				} else {

					dimensao = new Dimensao();

					dimensao.setIndice(i);
					dimensao.setCardinalidade(Integer
							.parseInt(elementosLinha[i]));
					dimensoes.add(dimensao);
				}
			}
			estrutura.setDimensoes(dimensoes);

			leitor.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return estrutura;
	}

	public Tupla[] obterTuplas(File arquivo) {

		
		char[] leitura = new char[Constantes.BufferLeitura];
		String strLeitura;
		String[] elementosLinha;
		String[] linhas;
		EstruturaArquivo estrutura = obterEstruturaArquivo(arquivo);
		Tupla[] tuplas = new Tupla[estrutura.getNumeroTuplas()];
		Integer[] valores;

		try {
			
			FileReader leitor = new FileReader(arquivo);
			
			leitor.read(leitura);
			
			strLeitura = String.valueOf(leitura);
			
			linhas = strLeitura.split("\n");
			
			for (int i = 1; i < linhas.length -1; i++){ // O último \n cria uma linha vazia no arry de linhas
//				System.out.println(linhas[i]);
				elementosLinha = linhas[i].split(" ");
				valores = new Integer[estrutura.getDimensoes().size()];
				
				for( int j = 0; j < elementosLinha.length - 1; j++){
					
					valores[j] = Integer.parseInt(elementosLinha[j]);
					
				}

				tuplas[i-1] = new Tupla(valores);
			}
			
			leitor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tuplas;
	}
}
