package modelo;

import java.util.List;

public class EstruturaArquivo {

	private Integer numeroTuplas;
	private List<Dimensao> dimensoes;

	public Integer getNumeroTuplas() {
		return numeroTuplas;
	}

	public void setNumeroTuplas(Integer numeroTuplas) {
		this.numeroTuplas = numeroTuplas;
	}

	public List<Dimensao> getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

}
