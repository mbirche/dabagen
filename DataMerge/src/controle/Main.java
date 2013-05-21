package controle;

import java.awt.Dimension;

import javax.swing.JFrame;

import visao.TelaPrincipal;

public class Main {
	public static void main(String[] args) {
		

		TelaPrincipal tela = new TelaPrincipal();
		
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setTitle("Mesclador de Arquivos do Database Generator");
		tela.setSize(new Dimension(800, 700));
		tela.setResizable(false);
		tela.setLocationRelativeTo(null);
		tela.setVisible(true);
		
		
	}
}
