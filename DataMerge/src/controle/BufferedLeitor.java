package controle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import modelo.Dimensao;
import modelo.EstruturaArquivo;
import modelo.Tupla;

public class BufferedLeitor {

	private EstruturaArquivo estrutura = new EstruturaArquivo();
	private FileReader flReader;
	private BufferedReader bFileReader;
	File arquivo;

	public BufferedLeitor(File arquivo) throws FileNotFoundException {
		this.arquivo = arquivo;
		flReader = new FileReader(arquivo);
		bFileReader = new BufferedReader(flReader);
		estrutura = obterEstruturaArquivo();

	}

	public EstruturaArquivo obterEstruturaArquivo() {

		String strPrimeiraLinha;
		String[] elementosLinha;
		List<Dimensao> dimensoes = new ArrayList<Dimensao>();
		Dimensao dimensao;

		try {

			strPrimeiraLinha = bFileReader.readLine();

			elementosLinha = strPrimeiraLinha.split(" ");

			for (int i = 0; i < elementosLinha.length; i++) {

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return estrutura;
	}

	public Tupla obterTupla() {

		String strLeitura;
		String[] elementosLinha;
		Tupla tupla = null;
		Integer[] valores = new Integer[estrutura.getDimensoes().size()];

		try {

			strLeitura = bFileReader.readLine();

			if (strLeitura != null) {

				elementosLinha = strLeitura.split(" ");

				for (int i = 0; i < elementosLinha.length; i++) {

					valores[i] = Integer.parseInt(elementosLinha[i]);

				}

				tupla = new Tupla(valores);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tupla;
	}

	public EstruturaArquivo getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(EstruturaArquivo estrutura) {
		this.estrutura = estrutura;
	}

	@Override
	protected void finalize() throws Throwable {
		flReader.close();
		bFileReader.close();
		super.finalize();
	}
}
