package modelo;

import java.util.List;

public class EstruturaArquivo {

	private Integer numeroTuplas;
	private Double skew;
	private List<Dimensao> dimensoes;

	public Integer getNumeroTuplas() {
		return numeroTuplas;
	}

	public void setNumeroTuplas(Integer numeroTuplas) {
		this.numeroTuplas = numeroTuplas;
	}

	public Double getSkew() {
		return skew;
	}

	public void setSkew(Double skew) {
		this.skew = skew;
	}

	public List<Dimensao> getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

}
