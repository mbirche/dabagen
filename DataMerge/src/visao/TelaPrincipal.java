package visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import modelo.EstruturaArquivo;
import controle.ArquivoDAO;
import controle.Conexao;
import controle.Gravador;
import controle.Leitor;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblArquivo1;
	private JLabel lblArquivo2;
	private JLabel lblEditarArquivo;
	private JLabel lblBDArquivo;
	private JLabel lblSkew;
	private JLabel lblCardinalidade;

	private JTextField txtArquivo1;
	private JTextField txtArquivo2;
	private JTextField txtEditArquivo;
	private JTextField txtBDArquivo;
	private JTextField txtSkew;
	private JTextField txtCardinalidade;

	private JTextArea txtArArq1;
	private JTextArea txtArArq2;
	private JTextArea txtArPossivel;

	private JButton btnArquivo1;
	private JButton btnArquivo2;
	private JButton btnMesclar;
	private JButton btnEditarArquivo;
	private JButton btnBDArquivo;
	private JButton btnProcurarArquivoEdit;
	private JButton btnProcurarArquivoBD;

	private ButtonGroup grupoRadios;
	private JRadioButton rdMergeHorizontal;
	private JRadioButton rdMergeVertical;
	private JRadioButton rdNone;

	private JCheckBox ckCabecalho;
	private JCheckBox ckIndice;
	private JCheckBox ckCabecalhoEdit;
	private JCheckBox ckIndiceEdit;
	private JCheckBox ckManterCaminhoEdit;

	private LayoutManager layoutArquivos;
	private LayoutManager layoutAviso;

	private JPanel pnlTopo;
	private JPanel pnlCentro;
	private JPanel pnlBaixo;

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
	private File arquivoEdit;
	private File arquivoBD;

	private Boolean temArquivos;
	private Boolean ePossivel;

	private String strPossibilidade;

	public TelaPrincipal() {

		temArquivos = false;
		ePossivel = false;

		lblArquivo1 = new JLabel("Arquivo 1");
		lblArquivo2 = new JLabel("Arquivo 2");
		lblEditarArquivo = new JLabel("Arquivo para edição");
		lblBDArquivo = new JLabel("Arquivo para armazenamento");
		lblSkew = new JLabel("Skew");
		lblCardinalidade = new JLabel("Cardinalidade");

		txtArquivo1 = new JTextField();
		txtArquivo1.setPreferredSize(new Dimension(200, 20));
		txtArquivo2 = new JTextField();
		txtArquivo2.setPreferredSize(new Dimension(200, 20));
		txtEditArquivo = new JTextField();
		txtEditArquivo.setPreferredSize(new Dimension(400, 20));
		txtBDArquivo = new JTextField();
		txtBDArquivo.setPreferredSize(new Dimension(500, 20));
		txtSkew = new JTextField();
		txtSkew.setPreferredSize(new Dimension(50, 20));
		txtCardinalidade = new JTextField();
		txtCardinalidade.setPreferredSize(new Dimension(50, 20));

		txtArArq1 = new JTextArea("Número de tuplas:\n"
				+ "Número de dimensões:", 2, 30);
		txtArArq2 = new JTextArea("Número de tuplas:\n"
				+ "Número de dimensões:", 2, 30);
		txtArPossivel = new JTextArea(3, 30);

		btnArquivo1 = new JButton("Procurar");
		btnArquivo2 = new JButton("Procurar");
		btnMesclar = new JButton("Mesclar");
		btnMesclar.setEnabled(ePossivel);
		btnEditarArquivo = new JButton("Editar");
		btnEditarArquivo.setEnabled(false);
		btnProcurarArquivoEdit = new JButton("Procurar");
		btnProcurarArquivoBD = new JButton("Procurar");
		btnBDArquivo = new JButton("Armazenar");
		btnBDArquivo.setEnabled(false);

		grupoRadios = new ButtonGroup();
		rdMergeHorizontal = new JRadioButton("Mescla Horizontal");
		rdMergeHorizontal.setEnabled(temArquivos);
		rdMergeVertical = new JRadioButton("Mescla Vertical");
		rdMergeVertical.setEnabled(temArquivos);
		rdNone = new JRadioButton();

		ckCabecalho = new JCheckBox("Cabeçalho");
		ckIndice = new JCheckBox("Índice");
		ckCabecalhoEdit = new JCheckBox("Cabeçalho");
		ckIndiceEdit = new JCheckBox("Índice");
		ckManterCaminhoEdit = new JCheckBox("Salvar no mesmo local");

		grupoRadios.add(rdMergeHorizontal);
		grupoRadios.add(rdMergeVertical);
		grupoRadios.add(rdNone);

		layoutArquivos = new FlowLayout();
		layoutAviso = new FlowLayout();

		pnlTopo = new JPanel(new GridLayout(0, 2));
		pnlTopo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		pnlCentro = new JPanel();
		pnlCentro.setBorder(BorderFactory.createLineBorder(Color.RED));

		pnlBaixo = new JPanel();
		pnlBaixo.setBorder(BorderFactory.createLineBorder(Color.BLUE));

		pnlArquivos = new JPanel(layoutArquivos);
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

		pnlCentro.add(lblEditarArquivo);
		pnlCentro.add(txtEditArquivo);
		pnlCentro.add(btnProcurarArquivoEdit);
		pnlCentro.add(ckManterCaminhoEdit);
		pnlCentro.add(ckCabecalhoEdit);
		pnlCentro.add(ckIndiceEdit);
		pnlCentro.add(btnEditarArquivo);

		pnlBaixo.add(lblBDArquivo);
		pnlBaixo.add(txtBDArquivo);
		pnlBaixo.add(btnProcurarArquivoBD);
		pnlBaixo.add(lblSkew);
		pnlBaixo.add(txtSkew);
		pnlBaixo.add(lblCardinalidade);
		pnlBaixo.add(txtCardinalidade);
		pnlBaixo.add(btnBDArquivo);

		setLayout(new GridLayout(0, 1));

		pnlTopo.add(pnlArquivos);
		pnlTopo.add(pnlAvisos);

		add(pnlTopo);
		add(pnlCentro);
		add(pnlBaixo);

		
		txtBDArquivo.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}
		});
		txtSkew.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}
		});
		txtCardinalidade.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("") && !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("") || txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}
				});
		
		btnBDArquivo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Integer skew = Integer.parseInt(txtSkew.getText());
				Integer cardinalidade = Integer.parseInt(txtCardinalidade.getText());
				
				ArquivoDAO dao = new ArquivoDAO(Conexao.getConexao(), arquivoBD);
				
				dao.armazenarArquivo(skew, cardinalidade);
				
			}
		});
		btnProcurarArquivoBD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				arquivoBD = chooser.getSelectedFile();
				txtBDArquivo.setText(arquivoBD.getAbsolutePath());
				
			}
		});
		btnProcurarArquivoEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				arquivoEdit = chooser.getSelectedFile();
				txtEditArquivo.setText(arquivoEdit.getAbsolutePath());
				btnEditarArquivo.setEnabled(true);

			}
		});

		btnEditarArquivo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String caminho = null;
				Gravador gravador = new Gravador();
				JFileChooser chooser = new JFileChooser();

				if (!ckManterCaminhoEdit.isSelected()) {
					if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						caminho = chooser.getSelectedFile().getAbsolutePath();
					}
				}

				gravador.editarArquivo(arquivoEdit,
						ckCabecalhoEdit.isSelected(),
						ckIndiceEdit.isSelected(), caminho);

			}
		});
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

				if (estrtArq1.getNumeroTuplas().equals(
						estrtArq2.getNumeroTuplas())) {
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

				JOptionPane.showMessageDialog(null, "O arquivo " + caminho
						+ " foi salvo com sucesso!");

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
