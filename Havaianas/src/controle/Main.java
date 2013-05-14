package controle;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import modelo.EstruturaArquivo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		//Caminho do arquivo csv original
		String caminho = Constantes.caminhoArquivo + "havaianasOriginal.csv";
		//Caminho para o novo arquivo que será gerado, corrigindo o arquivo original e obtendo a estrutura do mesmo
		String caminhoNovo = Constantes.caminhoArquivo + "havaianas.csv";
		
		File arquivo = new File(caminho);
		File arquivoNovo = new File(caminhoNovo);

		List<String> linhaParaGravacao = new ArrayList<String>();
		List<String> linhaParaGravacaoComIndice = new ArrayList<String>();
		List<String> primeiraLinha = new ArrayList<String>();
		List<String> linhaLeitura = new ArrayList<String>();

		String strLeitura;
		String[] elementosTupla;
		String valorNumerico = null;

		try {
			BufferedLeitor leitor = new BufferedLeitor(arquivo, true);
			BufferedLeitor leitorNovo = new BufferedLeitor(arquivoNovo, false);

			Gravador gravador1 = new Gravador("ComCabecalhoSemIndice.csv");
			Gravador gravador2 = new Gravador("ComCabecalhoComIndice.csv");
			Gravador gravador3 = new Gravador("SemCabecalhoComIndice.csv");

			Map<Integer, ArrayList<String>> mapa = leitor.getMapa();
			EstruturaArquivo estrutura = leitor.getEstrutura();

			primeiraLinha.add(estrutura.getNumeroTuplas().toString());

			for (int i = 0; i < mapa.size(); i++) {

				primeiraLinha.add(String.valueOf(mapa.get(i).size() + 2));

			}

			gravador1.gravarArquivoDados(primeiraLinha, true);
			gravador2.gravarArquivoDados(primeiraLinha, true);

			for (int i = 0; i < estrutura.getNumeroTuplas(); i++) {

				strLeitura = leitorNovo.lerTupla();
				elementosTupla = strLeitura.split(";");

				linhaParaGravacaoComIndice.add(String.valueOf(i + 1));

				for (int j = 0; j < mapa.size(); j++) {

					linhaLeitura = mapa.get(j);
					if (linhaLeitura.contains(elementosTupla[j])) {

						if (!elementosTupla[j].equals("null")
								&& !elementosTupla[j].equals("0"))
							valorNumerico = String
									.valueOf((linhaLeitura
											.indexOf(elementosTupla[j]) == 0 ? linhaLeitura
											.size() + 2 : linhaLeitura
											.indexOf(elementosTupla[j])));
						else if (elementosTupla[j].equals("null"))
							valorNumerico = String
									.valueOf(linhaLeitura.size() + 1);
						else if (elementosTupla[j].equals("0"))
							valorNumerico = String
									.valueOf(linhaLeitura.size() + 2);
					}

					linhaParaGravacao.add(valorNumerico);
					linhaParaGravacaoComIndice.add(valorNumerico);
					System.out.println("Gravada linha: " + (i + 1));
				}

				gravador1.gravarArquivoDados(linhaParaGravacao, true); // Com
																		// cabeçalho
																		// e sem
																		// índice
				gravador2.gravarArquivoDados(linhaParaGravacaoComIndice, true); // Com
																				// cabeçalho
																				// e
																				// com
																				// índice

				if (i != estrutura.getNumeroTuplas() - 1)
					gravador3.gravarArquivoDados(linhaParaGravacaoComIndice,
							true); // Enquanto não for a última linha adiciona
									// quebra de linha no final
				else
					gravador3.gravarArquivoDados(linhaParaGravacaoComIndice,
							false); // Última linha sem quebra de linha na
									// gravação do arquivo

				linhaParaGravacao.clear();
				linhaParaGravacaoComIndice.clear();
			}

			leitor.fecharReader();
			leitorNovo.fecharReader();

			System.out.println(estrutura.getNumeroTuplas());
			System.out.println(estrutura.getDimensoes().size());

			gravador1.closeGravador();
			gravador2.closeGravador();
			gravador3.closeGravador();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
