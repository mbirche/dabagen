package controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import modelo.Dimensao;
import modelo.EstruturaArquivo;
import modelo.Tupla;

public class Gravador {

	private File arquivo1;
	private File arquivo2;
	private File novoArquivo;
	private String caminhoArquivo;
	private EstruturaArquivo estrutArq1;
	private EstruturaArquivo estrutArq2;
	private EstruturaArquivo novaEstrutura;
	List<Dimensao> novaListaDimensoes;

	public Gravador() {
		caminhoArquivo = Constantes.caminhoArquivo;
	}

	/**
	 * 
	 * @param arquivo1
	 *            Primeiro arquivo que será mesclado, os valores do arquivo
	 *            resultante será composto dos valores da primeira coluna do
	 *            arquivo1 seguido dos valores da primeira coluna do arquivo2 e
	 *            seguindo assim de forma alternada enquanto houver colunas nos
	 *            arquivos 1 e 2
	 * @param arquivo2
	 *            Segundo arquivo que será mesclado, os valores do arquivo
	 *            resultante será composto dos valores da primeira coluna do
	 *            arquivo1 seguido dos valores da primeira coluna do arquivo2 e
	 *            seguindo assim de forma alternada enquanto houver colunas nos
	 *            arquivos 1 e 2
	 * @param temCabecalho
	 *            Opção que determina se a linha do cabeçalho será gravada no
	 *            arquivo. True - Grava a estrutura do arquivo na primeira linha
	 *            False - Não grava o cabeçalho
	 * @param temIndice
	 *            Opção que determina se o índice das tuplas será grava do
	 *            arquivo. True - Grava os índices das tuplas na primeira coluna
	 *            do arquivo False - Não grava os índices das colunas
	 * 
	 * @throws FileNotFoundException
	 *             Lança excessão se houver problemas na abertura dos arquivos
	 */
	public void mergeHorizontal(File arquivo1, File arquivo2,
			Boolean temCabecalho, Boolean temIndice)
			throws FileNotFoundException {

		Integer[] novosValores;
		StringBuffer novaTupla;
		Tupla tupla1, tupla2;
		BufferedLeitor leitor1 = new BufferedLeitor(arquivo1);
		BufferedLeitor leitor2 = new BufferedLeitor(arquivo2);
		int iVal1, iVal2, numVal1, numVal2;

		estrutArq1 = leitor1.getEstrutura();
		estrutArq2 = leitor2.getEstrutura();

		Integer totalDimensoes = estrutArq1.getDimensoes().size()
				+ estrutArq2.getDimensoes().size();
		Integer totalTuplas = estrutArq1.getNumeroTuplas();

		String nomeArquivo = "D" + totalDimensoes + "_T" + totalTuplas;
		novoArquivo = new File(caminhoArquivo + nomeArquivo);

		numVal1 = estrutArq1.getDimensoes().size();
		numVal2 = estrutArq2.getDimensoes().size();

		iVal1 = iVal2 = 0;
		boolean vezArq1 = true;

		// O arquivo terá cabeçalho?

		if (temCabecalho) {
			novaListaDimensoes = new ArrayList<Dimensao>();
			novaEstrutura = new EstruturaArquivo();

			novaEstrutura.setNumeroTuplas(estrutArq1.getNumeroTuplas());

			// Vamos escrever a primeira linha

			for (int i = 0; i < totalDimensoes; i++) {

				if (vezArq1 && iVal1 < numVal1) {
					novaListaDimensoes
							.add(estrutArq1.getDimensoes().get(iVal1));
					iVal1++;
					if (iVal2 < numVal2) {
						vezArq1 = false;
						continue;
					}
				}

				if (!vezArq1 && iVal2 < numVal2) {
					novaListaDimensoes
							.add(estrutArq2.getDimensoes().get(iVal2));
					iVal2++;
					if (iVal1 < numVal1) {
						vezArq1 = true;
						continue;
					}
				}
			}

			novaEstrutura.setDimensoes(novaListaDimensoes);
		}

		try {

			FileWriter fWriter = new FileWriter(novoArquivo);

			BufferedWriter gravador = new BufferedWriter(fWriter);

			// O arquivo terá cabeçalho?
			if (temCabecalho) {

				StringBuffer primeiraLinha = new StringBuffer();

				primeiraLinha.append(novaEstrutura.getNumeroTuplas());

				for (Dimensao dim : novaEstrutura.getDimensoes()) {
					primeiraLinha.append(" ");
					primeiraLinha.append(dim.getCardinalidade());
				}
				gravador.write(primeiraLinha.toString());
				gravador.newLine();
			}

			// Escrita das tuplas no arquivo
			// Vamos escrever as tuplas no arquivo

			for (int i = 0; i < totalTuplas; i++) {

				iVal1 = iVal2 = 0;
				vezArq1 = true;

				novaTupla = new StringBuffer();

				// Somente teremos o índice se a opcao de gravaçao for com
				// índice

				if (temIndice) {
					novaTupla.append(i + 1);
				}
				novosValores = new Integer[totalDimensoes];
				tupla1 = leitor1.obterTupla();
				tupla2 = leitor2.obterTupla();

				// Se não for preciso fazer nada com o vetor de valores da
				// tupla, é possivel mudarmos para ganharmos
				// performance gravando direto no StringBuffer
				for (int j = 0; j < totalDimensoes; j++) {

					if (vezArq1 && iVal1 < numVal1) {
						novosValores[j] = tupla1.getValores()[iVal1];
						iVal1++;
						if (iVal2 < numVal2) {
							vezArq1 = false;
							continue;
						}
					}

					if (!vezArq1 && iVal2 < numVal2) {
						novosValores[j] = tupla2.getValores()[iVal2];
						iVal2++;
						if (iVal1 < numVal1) {
							vezArq1 = true;
							continue;
						}
					}

				}

				for (int k = 0; k < novosValores.length; k++) {
					if (temIndice)
						novaTupla.append(" " + novosValores[k]);
					else if (k == novosValores.length - 1)
						novaTupla.append(novosValores[k]);
					else
						novaTupla.append(novosValores[k] + " ");

				}

				gravador.write(novaTupla.toString());
				gravador.newLine();

			}

			gravador.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param arquivo1
	 *            Primeiro arquivo que será mesclado, os valores do arquivo
	 *            resultante será composto, em cada coluna, pelos valores na
	 *            coluna vindos do arquivo1 e em seguida dos valores vindos do
	 *            arquivo 2.
	 * @param arquivo2
	 *            Primeiro arquivo que será mesclado, os valores do arquivo
	 *            resultante será composto, em cada coluna, pelos valores na
	 *            coluna vindos do arquivo1 e em seguida dos valores vindos do
	 *            arquivo 2.
	 * @param temCabecalho
	 *            Opção que determina se a linha do cabeçalho será gravada no
	 *            arquivo. True - Grava a estrutura do arquivo na primeira linha
	 *            False - Não grava o cabeçalho
	 * @param temIndice
	 *            Opção que determina se o índice das tuplas será grava do
	 *            arquivo. True - Grava os índices das tuplas na primeira coluna
	 *            do arquivo False - Não grava os índices das colunas
	 * @throws FileNotFoundException
	 *             Lança excessão se houver problemas na abertura dos arquivos
	 */

	public void mergeVertical(File arquivo1, File arquivo2,
			Boolean temCabecalho, Boolean temIndice)
			throws FileNotFoundException {

		BufferedLeitor leitor1 = new BufferedLeitor(arquivo1);
		BufferedLeitor leitor2 = new BufferedLeitor(arquivo2);

		estrutArq1 = leitor1.getEstrutura();
		estrutArq2 = leitor2.getEstrutura();

		Integer totalTuplas = estrutArq1.getNumeroTuplas()
				+ estrutArq2.getNumeroTuplas();
		Integer totalDimensoes = estrutArq1.getDimensoes().size();

		StringBuffer primeiraLinha;
		StringBuffer novaTupla;
		Tupla tupla;

		String nomeArquivo = "D" + totalDimensoes + "_T" + totalTuplas;
		novoArquivo = new File(caminhoArquivo + nomeArquivo);

		if (temCabecalho) {
			novaListaDimensoes = new ArrayList<Dimensao>();
			novaEstrutura = new EstruturaArquivo();

			novaEstrutura.setNumeroTuplas(totalTuplas);
			novaEstrutura.setDimensoes(estrutArq1.getDimensoes());
		}
		try {

			FileWriter fWriter = new FileWriter(novoArquivo);

			BufferedWriter gravador = new BufferedWriter(fWriter);

			// Escreveremos a primeira linha se houver cabeçalho
			if (temCabecalho) {

				primeiraLinha = new StringBuffer();

				primeiraLinha.append(totalTuplas);

				for (Dimensao dim : novaEstrutura.getDimensoes()) {
					primeiraLinha.append(" ");
					primeiraLinha.append(dim.getCardinalidade());
				}

				gravador.write(primeiraLinha.toString());
				gravador.newLine();
			}

			// Vamos escrever as tuplas.
			Integer indice = 1;

			for (int i = 0; i < estrutArq1.getNumeroTuplas(); i++) {

				tupla = leitor1.obterTupla();
				novaTupla = new StringBuffer();
				
				if(temIndice){
					novaTupla.append(indice + " ");
					indice++;
				}

				for (int j = 0; j < tupla.getValores().length; j++) {

					if (j == tupla.getValores().length - 1)
						novaTupla.append(tupla.getValores()[j]);
					else
						novaTupla.append(tupla.getValores()[j] + " ");
				}
				gravador.write(novaTupla.toString());
				gravador.newLine();
			}
			for (int i = 0; i < estrutArq2.getNumeroTuplas(); i++) {

				tupla = leitor2.obterTupla();
				novaTupla = new StringBuffer();

				if(temIndice){
					novaTupla.append(indice + " ");
					indice++;
				}
				
				for (int j = 0; j < tupla.getValores().length; j++) {

					if (j == tupla.getValores().length - 1)
						novaTupla.append(tupla.getValores()[j]);
					else
						novaTupla.append(tupla.getValores()[j] + " ");
				}
				gravador.write(novaTupla.toString());
				gravador.newLine();
			}

			gravador.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public File getArquivo1() {
		return arquivo1;
	}

	public void setArquivo1(File arquivo1) {
		this.arquivo1 = arquivo1;
	}

	public File getArquivo2() {
		return arquivo2;
	}

	public void setArquivo2(File arquivo2) {
		this.arquivo2 = arquivo2;
	}

}
