package controle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Dimensao;
import modelo.EstruturaArquivo;

public class BufferedLeitor {

	private EstruturaArquivo estrutura = new EstruturaArquivo();
	private FileReader flReader;
	private BufferedReader bFileReader;
	File arquivo;
	private Map<Integer, ArrayList<String>> mapa = new HashMap<Integer, ArrayList<String>>();

	FileReader novoFlReader;
	BufferedReader bReader;

	Boolean achouAspas = false;
	Boolean campoIncompleto = false;

	public BufferedLeitor(File arquivo, Boolean lerEstrutura)
			throws FileNotFoundException {
		this.arquivo = arquivo;
		flReader = new FileReader(arquivo);
		bFileReader = new BufferedReader(flReader);

		if (lerEstrutura)
			estrutura = obterEstruturaArquivo();

	}

	public String lerTupla() {

		String retorno = null;
		try {

			retorno = bFileReader.readLine();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;

	}

	public void fecharReader() {
		try {
			bFileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EstruturaArquivo obterEstruturaArquivo() {

		Gravador gravador = new Gravador("havaianas.csv");
		String strPrimeiraLinha;
		String strLeitura;
		String[] elementosPrimeiraLinha;
		String[] elementosLinha;
		String[] elementosLinhaResto;
		List<String> linha = new ArrayList<String>();
		List<Dimensao> dimensoes = new ArrayList<Dimensao>();
		Dimensao dimensao;
		Integer numeroDimensoes;
		Integer numeroTuplas = 0;
		Integer linhaLida = 0;

		try {

			strPrimeiraLinha = tratarString(bFileReader.readLine());
			linhaLida++;
			elementosPrimeiraLinha = strPrimeiraLinha.split(";");
			numeroDimensoes = elementosPrimeiraLinha.length;

			for (int i = 0; i < numeroDimensoes; i++) {

				mapa.put(i, new ArrayList<String>());
			}

			strLeitura = tratarString(bFileReader.readLine());
			linhaLida++;

			if (strLeitura.endsWith(";"))
				strLeitura = strLeitura + "null";

			while (strLeitura != null) {

				numeroTuplas++;

				elementosLinha = strLeitura.split(";");

				for (int i = 0; i < elementosLinha.length; i++) {
					if (i == elementosLinha.length - 1) {
						linha.add((!elementosLinha[i].equals("") ? elementosLinha[i]
								: String.valueOf(0)));
					} else {
						linha.add((!elementosLinha[i].equals("") ? elementosLinha[i]
								: "null"));
					}
				}

				if (linha.size() != numeroDimensoes) {
					while (linha.size() < numeroDimensoes - 1) {

						strLeitura = tratarString(bFileReader.readLine());
						linhaLida++;

						if (strLeitura.endsWith(";"))
							strLeitura = strLeitura + "null";

						while (strLeitura.equals("")) {
							strLeitura = tratarString(bFileReader.readLine());
							linhaLida++;

							if (strLeitura.endsWith(";"))
								strLeitura = strLeitura + "null";
						}

						elementosLinhaResto = strLeitura.split(";");
						for (int i = 0; i < elementosLinhaResto.length; i++) {

							String ultimaString = linha.get(linha.size() - 1);
							String parte = null;
							if (linha.size() >= numeroDimensoes - 1) {

								parte = (!elementosLinhaResto[i].equals("") ? elementosLinhaResto[i]
										: String.valueOf(0));
								if (campoIncompleto) {

									linha.remove(linha.size() - 1);
									linha.add(ultimaString + parte);
									campoIncompleto = false;
									continue;
								}
								linha.add(parte);
							} else {

								parte = (!elementosLinhaResto[i].equals("") ? elementosLinhaResto[i]
										: "null");

								if (campoIncompleto) {

									linha.remove(linha.size() - 1);
									linha.add(ultimaString + parte);
									campoIncompleto = false;
									continue;
								}
								
								linha.add(parte);
							}
						}
					}
				}

				gravador.gravarLinha(linha);
				System.out.println("Gravando linha " + numeroTuplas);
				System.out.println("Linha para gravar " + linha);

				for (int i = 0; i < (linha.size() <= 41 ? linha.size() : 41); i++) {

					if (!mapa.get(i).contains(linha.get(i))) {

						mapa.get(i).add(linha.get(i));
					}
				}
				linha.clear();

				strLeitura = tratarString(bFileReader.readLine());
				linhaLida++;
				if (strLeitura != null && strLeitura.endsWith(";"))
					strLeitura = strLeitura + "null";
				System.out.println("Lido arquivo até a linha " + linhaLida);
				System.out.println("Linha lida " + strLeitura);

			}// Fim do While de leitura

			gravador.closeGravador();

			for (int i = 0; i < numeroDimensoes; i++) {

				dimensao = new Dimensao();
				dimensao.setIndice(i);
				dimensao.setCardinalidade(mapa.get(i).size());
				dimensoes.add(dimensao);

			}

			estrutura.setNumeroTuplas(numeroTuplas);
			estrutura.setDimensoes(dimensoes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return estrutura;
	}

	private String tratarString(String linha) {

		StringBuilder retorno = null;

		if (linha != null) {

			retorno = new StringBuilder(linha);

			for (int i = 0; i < linha.length(); i++) {

				if (linha.charAt(i) == '\"') {
					achouAspas = !achouAspas;
				}

				if (achouAspas) {
					if (linha.charAt(i) == ';') {
						retorno.setCharAt(i, ':');
					}
				}

			}

			if (achouAspas)
				campoIncompleto = true;

			return retorno.toString();
		}
		return null;
	}

	public EstruturaArquivo getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(EstruturaArquivo estrutura) {
		this.estrutura = estrutura;
	}

	public Map<Integer, ArrayList<String>> getMapa() {
		return mapa;
	}

	public void setMapa(Map<Integer, ArrayList<String>> mapa) {
		this.mapa = mapa;
	}

}