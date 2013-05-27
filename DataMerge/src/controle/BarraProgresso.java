package controle;


public class BarraProgresso implements Runnable {

	private Integer tuplaAtual;
	private Integer totalTuplas;
	private Boolean continua;
	private Gravador gravador;

	public BarraProgresso(Gravador gravador) {

		tuplaAtual = 0;
		totalTuplas = 0;
		continua = true;
		this.gravador = gravador;
	}

	@Override
	public void run() {
		while (continua) {
			
			gravador.atualizaTxtStatus(tuplaAtual, totalTuplas);

			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		
	}

	public Integer getTuplaAtual() {
		return tuplaAtual;
	}

	public void setTuplaAtual(Integer tuplaAtual) {
		this.tuplaAtual = tuplaAtual;
	}

	public Integer getTotalTuplas() {
		return totalTuplas;
	}

	public void setTotalTuplas(Integer totalTuplas) {
		this.totalTuplas = totalTuplas;
	}

	public Boolean getContinua() {
		return continua;
	}

	public void setContinua(Boolean continua) {
		this.continua = continua;
	}

}
