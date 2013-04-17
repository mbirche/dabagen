package visao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import modelo.EstruturaArquivo;
import controle.Gravador;
import controle.Leitor;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblArquivo1;
	private JLabel lblArquivo2;

	private JTextField txtArquivo1;
	private JTextField txtArquivo2;

	private JTextArea txtArArq1;
	private JTextArea txtArArq2;
	private JTextArea txtArPossivel;

	private JButton btnArquivo1;
	private JButton btnArquivo2;
	private JButton btnMesclar;

	private ButtonGroup grupoRadios;
	private JRadioButton rdMergeHorizontal;
	private JRadioButton rdMergeVertical;
	private JRadioButton rdNone;

	private JCheckBox ckCabecalho;
	private JCheckBox ckIndice;

	private LayoutManager layoutArquivos;
	private LayoutManager layoutAviso;

	private JPanel pnlArquivos;
	private JPanel pnlArquivo1;
	private JPanel pnlArquivo2;
	private JPanel pnlOpcaoMerge;
	private JPanel pnlOpcoesEstrutura;
	private JPanel pnlAvisos;
	private JPanel pnlInfoArq1;
	private JPanel pnlInfoArq2;
	private JPanel pnlPossivel;

	private File arquivo1;
	private File arquivo2;

	private Boolean temArquivos;
	private Boolean ePossivel;

	private String strPossibilidade;

	public TelaPrincipal() {

		temArquivos = false;
		ePossivel = false;

		lblArquivo1 = new JLabel("Arquivo 1");
		lblArquivo2 = new JLabel("Arquivo 2");
		txtArquivo1 = new JTextField();
		txtArquivo1.setPreferredSize(new Dimension(200, 20));
		txtArquivo2 = new JTextField();
		txtArquivo2.setPreferredSize(new Dimension(200, 20));
		txtArArq1 = new JTextArea("Número de tuplas:\n"
				+ "Número de dimensões:", 2, 30);
		txtArArq2 = new JTextArea("Número de tuplas:\n"
				+ "Número de dimensões:", 2, 30);
		txtArPossivel = new JTextArea(3, 30);

		btnArquivo1 = new JButton("Procurar");
		btnArquivo2 = new JButton("Procurar");
		btnMesclar = new JButton("Mesclar");
		btnMesclar.setEnabled(ePossivel);

		grupoRadios = new ButtonGroup();
		rdMergeHorizontal = new JRadioButton("Mescla Horizontal");
		rdMergeHorizontal.setEnabled(temArquivos);
		rdMergeVertical = new JRadioButton("Mescla Vertical");
		rdMergeVertical.setEnabled(temArquivos);
		rdNone = new JRadioButton();

		ckCabecalho = new JCheckBox("Cabeçalho");
		ckIndice = new JCheckBox("Índice");

		grupoRadios.add(rdMergeHorizontal);
		grupoRadios.add(rdMergeVertical);
		grupoRadios.add(rdNone);

		layoutArquivos = new FlowLayout();
		layoutAviso = new FlowLayout();

		pnlArquivos = new JPanel(layoutArquivos);
		pnlArquivos.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlArquivo1 = new JPanel();
		pnlArquivo2 = new JPanel();
		pnlOpcaoMerge = new JPanel();
		pnlOpcoesEstrutura = new JPanel();

		pnlAvisos = new JPanel(layoutAviso);
		pnlInfoArq1 = new JPanel();
		pnlInfoArq2 = new JPanel();
		pnlPossivel = new JPanel();

		pnlArquivo1.add(lblArquivo1);
		pnlArquivo1.add(txtArquivo1);
		pnlArquivo1.add(btnArquivo1);
		pnlArquivo2.add(lblArquivo2);
		pnlArquivo2.add(txtArquivo2);
		pnlArquivo2.add(btnArquivo2);

		pnlOpcaoMerge.add(rdMergeHorizontal);
		pnlOpcaoMerge.add(rdMergeVertical);

		pnlOpcoesEstrutura.add(ckCabecalho);
		pnlOpcoesEstrutura.add(ckIndice);
		pnlOpcoesEstrutura.add(btnMesclar);

		pnlArquivos.add(pnlArquivo1);
		pnlArquivos.add(pnlArquivo2);
		pnlArquivos.add(pnlOpcaoMerge);
		pnlArquivos.add(pnlOpcoesEstrutura);

		pnlInfoArq1.add(txtArArq1);
		pnlInfoArq2.add(txtArArq2);
		pnlPossivel.add(txtArPossivel);

		pnlAvisos.add(pnlInfoArq1);
		pnlAvisos.add(pnlInfoArq2);
		pnlAvisos.add(pnlPossivel);

		setLayout(new GridLayout(0, 2));

		add(pnlArquivos);
		add(pnlAvisos);

		btnArquivo1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				arquivo1 = chooser.getSelectedFile();
				txtArquivo1.setText(arquivo1.getAbsolutePath());
				montaInfoArq1(arquivo1);

				if (arquivo1 != null && arquivo2 != null) {
					temArquivos = true;
				}
				ePossivel = false;
				strPossibilidade = "";
				atualizaPossibilidade(strPossibilidade);
				atualizaBotoes();
				limpaOpcoesMerge();
			}
		});

		btnArquivo2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				arquivo2 = chooser.getSelectedFile();
				txtArquivo2.setText(arquivo2.getAbsolutePath());
				montaInfoArq2(arquivo2);

				if (arquivo1 != null && arquivo2 != null) {
					temArquivos = true;
				}
				ePossivel = false;
				strPossibilidade = "";
				atualizaPossibilidade(strPossibilidade);
				atualizaBotoes();
				limpaOpcoesMerge();
			}
		});

		rdMergeHorizontal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Leitor leitor = new Leitor();
				EstruturaArquivo estrtArq1 = leitor
						.obterEstruturaArquivo(arquivo1);
				EstruturaArquivo estrtArq2 = leitor
						.obterEstruturaArquivo(arquivo2);

				ePossivel = false;

				if (estrtArq1.getNumeroTuplas() == estrtArq2.getNumeroTuplas()) {
					ePossivel = true;
					strPossibilidade = "A mescla é possível.";
					atualizaPossibilidade(strPossibilidade);
				} else {
					strPossibilidade = "Não é possivel fazer a mescla horizontal de arquivos\nque possuem número de tuplas diferentes.";
					atualizaPossibilidade(strPossibilidade);
				}
				atualizaBotoes();
			}
		});

		rdMergeVertical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Leitor leitor = new Leitor();
				EstruturaArquivo estrtArq1 = leitor
						.obterEstruturaArquivo(arquivo1);
				EstruturaArquivo estrtArq2 = leitor
						.obterEstruturaArquivo(arquivo2);

				ePossivel = false;
				if (estrtArq1.getDimensoes().size() == estrtArq2.getDimensoes()
						.size()) {
					ePossivel = true;
					strPossibilidade = "A mescla é possível.";
					atualizaPossibilidade(strPossibilidade);
				} else {
					strPossibilidade = "Não é possivel fazer a mescla vertical de arquivos \nque possuem número de dimensões diferentes.";
					atualizaPossibilidade(strPossibilidade);
				}
				atualizaBotoes();

			}
		});

		btnMesclar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Gravador gravador = new Gravador();
				String caminho = "";
				JFileChooser chooser = new JFileChooser();
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					caminho = chooser.getSelectedFile().getAbsolutePath();
				}
				gravador.setCaminhoArquivo(caminho);

				if (rdMergeHorizontal.isSelected()) {
					try {
						gravador.mergeHorizontal(arquivo1, arquivo2,
								ckCabecalho.isSelected(), ckIndice.isSelected());
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"Problemas com o arquivo selecionado.");
					}
				}

				if (rdMergeVertical.isSelected()) {
					try {
						gravador.mergeVertical(arquivo1, arquivo2,
								ckCabecalho.isSelected(), ckIndice.isSelected());
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"Problemas com o arquivo selecionado.");
					}
				}
				
				JOptionPane.showMessageDialog(null, "O arquivo " + caminho + " foi salvo com sucesso!");

			}
		});

	}

	private void atualizaPossibilidade(String strPossibilidade) {
		txtArPossivel.setText(strPossibilidade);
	}

	private void montaInfoArq1(File arquivo1) {

		Leitor leitor = new Leitor();
		EstruturaArquivo estrtArq1 = leitor.obterEstruturaArquivo(arquivo1);

		String info = "Número de tuplas: " + estrtArq1.getNumeroTuplas() + "\n"
				+ "Número de dimensões: " + estrtArq1.getDimensoes().size();
		txtArArq1.setText(info);
	}

	private void montaInfoArq2(File arquivo2) {

		Leitor leitor = new Leitor();
		EstruturaArquivo estrtArq2 = leitor.obterEstruturaArquivo(arquivo2);

		String info = "Número de tuplas: " + estrtArq2.getNumeroTuplas() + "\n"
				+ "Número de dimensões: " + estrtArq2.getDimensoes().size();
		txtArArq2.setText(info);
	}

	private void atualizaBotoes() {

		rdMergeHorizontal.setEnabled(temArquivos);
		rdMergeVertical.setEnabled(temArquivos);
		btnMesclar.setEnabled(ePossivel);
	}

	private void limpaOpcoesMerge() {
		rdNone.setSelected(true);
	}

}
