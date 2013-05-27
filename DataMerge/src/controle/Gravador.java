package controle;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import modelo.Dimensao;
import modelo.EstruturaArquivo;
import modelo.Tupla;
import visao.TelaPrincipal;

public class Gravador extends JFrame {

	private File arquivo1;
	private File arquivo2;
	private File novoArquivo;
	private String caminhoArquivo;
	private EstruturaArquivo estrutArq1;
	private EstruturaArquivo estrutArq2;
	private EstruturaArquivo novaEstrutura;
	List<Dimensao> novaListaDimensoes;
	private JPanel statusMerge;
	private JLabel lblStatus;
	private String infoStatus;
	private int tuplaAtual;
	private int totalTuplasParaGravação;
	private Thread threadProgresso;

	private BarraProgresso barraProgresso;

	public Gravador() {

		setSize(new Dimension(400, 200));
		caminhoArquivo = Constantes.caminhoArquivo;
		statusMerge = new JPanel();

		infoStatus = "Tuplas a serem gravas: \n" + totalTuplasParaGravação
				+ "Tupla / Total de tuplas: " + tuplaAtual + " / "
				+ totalTuplasParaGravação;
		lblStatus = new JLabel(infoStatus);

		statusMerge.add(lblStatus);
		add(statusMerge);

		this.setTitle("Estado da gravação");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);

	}

	public void atualizaTxtStatus(Integer tuplaAtual, Integer totalTuplas) {

		this.tuplaAtual = tuplaAtual;
		this.totalTuplasParaGravação = totalTuplas;

		infoStatus = "Tupla / Total de tuplas: " + tuplaAtual + " / "
				+ totalTuplasParaGravação;
		System.out.println(infoStatus);
		lblStatus.setText(infoStatus);

		statusMerge.revalidate();

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
	public File mergeHorizontal(File arquivo1, File arquivo2,
			Boolean temCabecalho, Boolean temIndice, TelaPrincipal tela)
			throws FileNotFoundException {

		Integer[] novosValores;
		StringBuffer novaTupla;
		Tupla tupla1, tupla2;
		BufferedLeitor leitor1 = new BufferedLeitor(arquivo1);
		BufferedLeitor leitor2 = new BufferedLeitor(arquivo2);
		int iVal1, iVal2, numVal1, numVal2;
		Dimensao dimensao;

		estrutArq1 = leitor1.getEstrutura();
		estrutArq2 = leitor2.getEstrutura();

		Integer totalDimensoes = estrutArq1.getDimensoes().size()
				+ estrutArq2.getDimensoes().size();
		Integer totalTuplas = estrutArq1.getNumeroTuplas();

		/*
		 * Criador de nomes automaticos String nomeArquivo = "D" +
		 * totalDimensoes + "_T" + totalTuplas; novoArquivo = new
		 * File(caminhoArquivo + nomeArquivo);
		 */

		novoArquivo = new File(caminhoArquivo);

		numVal1 = estrutArq1.getDimensoes().size();
		numVal2 = estrutArq2.getDimensoes().size();

		iVal1 = iVal2 = 0;
		boolean vezArq1 = true;

		// O arquivo terá cabeçalho?

		novaListaDimensoes = new ArrayList<Dimensao>();
		novaEstrutura = new EstruturaArquivo();

		novaEstrutura.setNumeroTuplas(estrutArq1.getNumeroTuplas());

		Random rdm = new Random();

		for (int i = 0; i < totalDimensoes; i++) {

			if (iVal1 < numVal1 && iVal2 < numVal2) {
				vezArq1 = rdm.nextBoolean();
			} else if (iVal1 >= numVal1) {
				vezArq1 = false;
			} else if (iVal2 >= numVal2) {
				vezArq1 = true;
			}

			if (vezArq1 && iVal1 < numVal1) {
				dimensao = estrutArq1.getDimensoes().get(iVal1);
				dimensao.setArquivoDeOrigem(1);
				novaListaDimensoes.add(dimensao);
				iVal1++;
			}

			if (!vezArq1 && iVal2 < numVal2) {
				dimensao = estrutArq2.getDimensoes().get(iVal2);
				dimensao.setArquivoDeOrigem(2);
				novaListaDimensoes.add(dimensao);
				iVal2++;
			}
		}

		novaEstrutura.setDimensoes(novaListaDimensoes);

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

			// tela.getBarraProgresso().start();
			// tela.getBarraProgresso().setContinua(true);

			barraProgresso = new BarraProgresso(this);

			threadProgresso = new Thread(barraProgresso);

			barraProgresso.setTuplaAtual(0);
			barraProgresso.setTotalTuplas(totalTuplas);

			// threadProgresso.start();

			for (int i = 0; i < totalTuplas; i++) {

				// barraProgresso.setTuplaAtual(i + 1);

				atualizaTxtStatus(i + 1, totalTuplas);

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

				try {
					for (int j = 0; j < totalDimensoes; j++) {
						// try {
						if (novaEstrutura.getDimensoes().get(j)
								.getArquivoDeOrigem().equals(1)) {

							novosValores[j] = tupla1.getValores()[iVal1];
							iVal1++;

						} else if (novaEstrutura.getDimensoes().get(j)
								.getArquivoDeOrigem().equals(2)) {

							novosValores[j] = tupla2.getValores()[iVal2];
							iVal2++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				//
				// if (vezArq1 && iVal1 < numVal1) {
				// novosValores[j] = tupla1.getValores()[iVal1];
				// iVal1++;
				// if (iVal2 < numVal2) {
				// vezArq1 = false;
				// continue;
				// }
				// }
				//
				// if (!vezArq1 && iVal2 < numVal2) {
				// novosValores[j] = tupla2.getValores()[iVal2];
				// iVal2++;
				// if (iVal1 < numVal1) {
				// vezArq1 = true;
				// continue;
				// }
				// }

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

		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		barraProgresso.setTuplaAtual(totalTuplas);
		barraProgresso.setContinua(false);

		return novoArquivo;
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

	public File mergeVertical(File arquivo1, File arquivo2,
			Boolean temCabecalho, Boolean temIndice, TelaPrincipal tela)
			throws FileNotFoundException {

		BufferedLeitor leitor1 = new BufferedLeitor(arquivo1);
		BufferedLeitor leitor2 = new BufferedLeitor(arquivo2);

		estrutArq1 = leitor1.getEstrutura();
		estrutArq2 = leitor2.getEstrutura();

		Integer totalTuplas = estrutArq1.getNumeroTuplas()
				+ estrutArq2.getNumeroTuplas();

		StringBuffer primeiraLinha;
		StringBuffer novaTupla;
		Tupla tupla;

		/*
		 * Criador de nomes automaticos String nomeArquivo = "D" +
		 * totalDimensoes + "_T" + totalTuplas; novoArquivo = new
		 * File(caminhoArquivo + nomeArquivo);
		 */

		novoArquivo = new File(caminhoArquivo);

		Dimensao dimensao;
		List<Dimensao> listaDimensoes = new ArrayList<Dimensao>();

		if (temCabecalho) {
			novaListaDimensoes = new ArrayList<Dimensao>();
			novaEstrutura = new EstruturaArquivo();

			novaEstrutura.setNumeroTuplas(totalTuplas);
			for (int i = 0; i < estrutArq1.getDimensoes().size(); i++) {

				dimensao = new Dimensao();
				dimensao.setCardinalidade(Math.max(estrutArq1.getDimensoes()
						.get(i).getCardinalidade(), estrutArq2.getDimensoes()
						.get(i).getCardinalidade()));

				listaDimensoes.add(dimensao);
			}
			novaEstrutura.setDimensoes(listaDimensoes);
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

			barraProgresso = new BarraProgresso(this);

			threadProgresso = new Thread(barraProgresso);

			barraProgresso.setTuplaAtual(0);
			barraProgresso.setTotalTuplas(totalTuplas);

			// threadProgresso.start();

			for (int i = 0; i < estrutArq1.getNumeroTuplas(); i++) {

				// barraProgresso.setTuplaAtual(i + 1);

				atualizaTxtStatus(i + 1, totalTuplas);

				tupla = leitor1.obterTupla();
				novaTupla = new StringBuffer();

				if (temIndice) {
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

				// barraProgresso.setTuplaAtual(estrutArq1.getNumeroTuplas() + i
				// + 1);

				atualizaTxtStatus(estrutArq1.getNumeroTuplas() + i + 1,
						totalTuplas);

				tupla = leitor2.obterTupla();
				novaTupla = new StringBuffer();

				if (temIndice) {
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
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		barraProgresso.setContinua(false);
		return novoArquivo;
	}

	/***
	 * 
	 * @param arquivo
	 *            Arquivo que será editado
	 * @param temCabecalho
	 *            Se o novo arquivo terá sua estrutura gravada na primeira linha
	 * @param temIndice
	 *            Se o novo arquivo terá o índice das tuplas gravado na primeira
	 *            coluna
	 * @param caminho
	 *            O caminho onde o novo arquivo será salvo, se for null irá
	 *            salvar no mesmo diretório do arquivo de origem com o nome
	 *            modificado para _ (caso for sem cabeçalho e sem índice) ou _c
	 *            (caso o arquivo tenha cabeçalho mas não tenha índice) ou _i
	 *            (caso o arquivo tenha índice mas não tenha cabeçalho) ou _c_i
	 *            (caso o arquivo tenha cabeçalho e índice)
	 */
	public void editarArquivo(File arquivo, Boolean temCabecalho,
			Boolean temIndice, String caminho) {

		EstruturaArquivo estrutura;
		StringBuffer primeiraLinha;
		StringBuffer novaTupla;
		Tupla tupla;
		File novoArquivo;
		try {

			BufferedLeitor leitor = new BufferedLeitor(arquivo);
			estrutura = leitor.getEstrutura();

			if (caminho == null)
				caminho = arquivo.getAbsolutePath();
			if (temCabecalho)
				caminho += "_c";
			if (temIndice)
				caminho += "_i";
			if (!temCabecalho && !temIndice)
				caminho += "_";

			novoArquivo = new File(caminho);

			FileWriter fw = new FileWriter(novoArquivo);
			BufferedWriter gravador = new BufferedWriter(fw);

			if (temCabecalho) {

				primeiraLinha = new StringBuffer();

				primeiraLinha.append(estrutura.getNumeroTuplas());

				for (Dimensao dim : estrutura.getDimensoes()) {
					primeiraLinha.append(" ");
					primeiraLinha.append(dim.getCardinalidade());
				}

				gravador.write(primeiraLinha.toString());
				gravador.newLine();
			}

			Integer indice = 1;
			Integer totalTuplasEd = estrutura.getNumeroTuplas();

			for (int i = 0; i < totalTuplasEd; i++) {

				System.out.println("Editando linha " + (i + 1) + " / "
						+ totalTuplasEd);

				tupla = leitor.obterTupla();
				novaTupla = new StringBuffer();

				if (temIndice) {
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

			JOptionPane.showMessageDialog(null, "Arquivo editado com sucesso");

		} catch (Exception e) {

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

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

}
