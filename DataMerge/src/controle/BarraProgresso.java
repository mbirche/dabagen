package controle;

import javax.swing.JProgressBar;

import visao.TelaPrincipal;

public class BarraProgresso extends Thread {

	private JProgressBar barra;
	private Integer porcentagem;
	private Boolean continua;
	TelaPrincipal tela;

	public BarraProgresso(JProgressBar barra, TelaPrincipal tela) {
		this.barra = barra;
		porcentagem = 0;
		continua = true;
		this.tela = tela;
	}

	@Override
	public void run() {
		while (continua) {
			barra.setValue(porcentagem);
			
			tela.repaint();
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		
		barra.setValue(porcentagem);

	}

	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Boolean getContinua() {
		return continua;
	}

	public void setContinua(Boolean continua) {
		this.continua = continua;
	}

}
