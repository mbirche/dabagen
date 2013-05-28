package visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modelo.EstruturaArquivo;
import modelo.MyFile;
import controle.ArquivoDAO;
import controle.BufferedLeitor;
import controle.Conexao;
import controle.Gravador;
import controle.TestesVelocidade;
import controle.Util;

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
	private JTextArea txtArStatusMerge;

	private JList<MyFile> listaArquivos;
	private DefaultListModel<MyFile> modeloListaArquivo;

	private JButton btnArquivo1;
	private JButton btnArquivo2;
	private JButton btnProcurarArquivoLista;
	private JButton btnRemoverArquivoLista;

	private JButton btnMesclar;
	private JButton btnEditarArquivo;
	private JButton btnBDArquivo;
	private JButton btnProcurarArquivoEdit;
	private JButton btnProcurarArquivoBD;
	private JButton btnTesteVelocidade;

	private ButtonGroup grupoRadios;
	private JRadioButton rdMergeHorizontal;
	private JRadioButton rdMergeVertical;
	private JRadioButton rdNone;

	private JCheckBox ckCriarArquivo1;
	private JCheckBox ckCriarArquivo2;
	private JCheckBox ckCabecalho;
	private JCheckBox ckIndice;
	private JCheckBox ckCabecalho2;
	private JCheckBox ckIndice2;
	private JCheckBox ckCabecalhoEdit;
	private JCheckBox ckIndiceEdit;
	private JCheckBox ckManterCaminhoEdit;

	private JProgressBar pBarMescla;
	private JProgressBar pBarEdição;
	private JProgressBar pBarBD;

	private LayoutManager layoutArquivos;
	private LayoutManager layoutAviso;

	private JScrollPane spnlListaArquivos;

	private JPanel pnlTopo;
	private JPanel pnlCentro;
	private JPanel pnlBaixo;

	private JPanel pnlArquivos;
	private JPanel pnlArquivo1;
	private JPanel pnlArquivo2;
	private JPanel pnlListaArquivos;
	private JPanel pnlBotoesLista;
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
	private File arquivoAdicionado;

	private Boolean temArquivos;
	private Boolean ePossivel;
	private Boolean preencheBarra;

	private String strPossibilidade;

	private Integer porcentagemBarra;

	public TelaPrincipal() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		temArquivos = false;
		ePossivel = false;
		preencheBarra = true;

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
		txtArStatusMerge = new JTextArea(2, 30);

		modeloListaArquivo = new DefaultListModel<MyFile>();
		listaArquivos = new JList<MyFile>(modeloListaArquivo);
		spnlListaArquivos = new JScrollPane(listaArquivos);

		btnArquivo1 = new JButton("Procurar");
		btnArquivo2 = new JButton("Procurar");

		btnProcurarArquivoLista = new JButton("Adicionar arquivo");
		btnRemoverArquivoLista = new JButton("Remover arquivo");
		btnMesclar = new JButton("Mesclar");
		btnMesclar.setEnabled(ePossivel);
		btnEditarArquivo = new JButton("Editar");
		btnEditarArquivo.setEnabled(false);
		btnProcurarArquivoEdit = new JButton("Procurar");
		btnProcurarArquivoBD = new JButton("Procurar");
		btnBDArquivo = new JButton("Armazenar");
		btnBDArquivo.setEnabled(false);
		btnTesteVelocidade = new JButton("Teste Velocidade");
		btnTesteVelocidade.setEnabled(false);

		grupoRadios = new ButtonGroup();
		rdMergeHorizontal = new JRadioButton("Mescla Horizontal");
		rdMergeHorizontal.setEnabled(temArquivos);
		rdMergeVertical = new JRadioButton("Mescla Vertical");
		rdMergeVertical.setEnabled(temArquivos);
		rdNone = new JRadioButton();

		ckCriarArquivo1 = new JCheckBox("Criar Arquivo 1");
		ckCriarArquivo2 = new JCheckBox("Criar Arquivo 2");
		ckCabecalho = new JCheckBox("Cabeçalho");
		ckIndice = new JCheckBox("Índice");
		ckCabecalho2 = new JCheckBox("Cabeçalho");
		ckIndice2 = new JCheckBox("Índice");
		ckCabecalhoEdit = new JCheckBox("Cabeçalho");
		ckIndiceEdit = new JCheckBox("Índice");
		ckManterCaminhoEdit = new JCheckBox("Salvar no mesmo local");

		porcentagemBarra = 0;

		pBarMescla = new JProgressBar();
		pBarMescla.setStringPainted(true);

		grupoRadios.add(rdMergeHorizontal);
		grupoRadios.add(rdMergeVertical);
		grupoRadios.add(rdNone);

		layoutArquivos = new FlowLayout();
		layoutAviso = new FlowLayout();

		pnlTopo = new JPanel(new GridLayout(0, 2));
		pnlTopo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlTopo.setPreferredSize(new Dimension(200, 450));

		pnlCentro = new JPanel();
		pnlCentro.setBorder(BorderFactory.createLineBorder(Color.RED));

		pnlBaixo = new JPanel();
		pnlBaixo.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		pnlBaixo.setPreferredSize(new Dimension(200, 100));

		pnlArquivos = new JPanel(layoutArquivos);
		pnlArquivo1 = new JPanel();
		pnlArquivo2 = new JPanel();
		pnlListaArquivos = new JPanel(new GridLayout(0, 1));
		pnlBotoesLista = new JPanel();
		pnlOpcaoMerge = new JPanel();
		pnlOpcoesEstrutura = new JPanel(new GridLayout(0, 3));

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

		pnlBotoesLista.add(btnProcurarArquivoLista);
		pnlBotoesLista.add(btnRemoverArquivoLista);

		pnlListaArquivos.add(spnlListaArquivos);
		pnlListaArquivos.add(pnlBotoesLista);

		pnlOpcaoMerge.add(rdMergeHorizontal);
		pnlOpcaoMerge.add(rdMergeVertical);

		pnlOpcoesEstrutura.add(ckCriarArquivo1);
		pnlOpcoesEstrutura.add(ckCabecalho);
		pnlOpcoesEstrutura.add(ckIndice);

		pnlOpcoesEstrutura.add(ckCriarArquivo2);
		pnlOpcoesEstrutura.add(ckCabecalho2);
		pnlOpcoesEstrutura.add(ckIndice2);

		pnlOpcoesEstrutura.add(btnMesclar);

		// pnlArquivos.add(pnlArquivo1);
		// pnlArquivos.add(pnlArquivo2);
		pnlArquivos.add(pnlListaArquivos);

		pnlArquivos.add(pnlOpcaoMerge);
		pnlArquivos.add(pnlOpcoesEstrutura);

		pnlInfoArq1.add(txtArArq1);
		// pnlInfoArq2.add(txtArArq2);
		pnlPossivel.add(txtArPossivel);

		pnlAvisos.add(pnlInfoArq1);
		pnlAvisos.add(pnlInfoArq2);
		pnlAvisos.add(pnlPossivel);
		pnlAvisos.add(pBarMescla);
		pnlAvisos.add(txtArStatusMerge);

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
		pnlBaixo.add(btnTesteVelocidade);

		setLayout(new BorderLayout());

		pnlTopo.add(pnlArquivos);
		pnlTopo.add(pnlAvisos);

		add(pnlTopo, BorderLayout.NORTH);
		add(pnlCentro, BorderLayout.CENTER);
		add(pnlBaixo, BorderLayout.SOUTH);

		listaArquivos.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				MyFile myFile = listaArquivos.getSelectedValue();
				if (myFile != null)
					montaInfoArq1(myFile.getFile());
				else
					limpaInfoArq1();

			}
		});
		txtBDArquivo.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}
		});
		txtSkew.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!txtSkew.getText().equals("")
						&& !txtCardinalidade.getText().equals("")
						&& !txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(true);
				}
				if (txtSkew.getText().equals("")
						|| txtCardinalidade.getText().equals("")
						|| txtBDArquivo.getText().equals("")) {
					btnBDArquivo.setEnabled(false);
				}

			}
		});
		txtCardinalidade.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("")
								&& !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("")
								|| txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("")
								&& !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("")
								|| txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						if (!txtSkew.getText().equals("")
								&& !txtCardinalidade.getText().equals("")
								&& !txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(true);
						}
						if (txtSkew.getText().equals("")
								|| txtCardinalidade.getText().equals("")
								|| txtBDArquivo.getText().equals("")) {
							btnBDArquivo.setEnabled(false);
						}

					}
				});

		btnTesteVelocidade.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TestesVelocidade teste = new TestesVelocidade(arquivoBD);

				teste.testeLeituraTupla();

			}
		});
		btnBDArquivo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Integer skew = Integer.parseInt(txtSkew.getText());
				Integer cardinalidade = Integer.parseInt(txtCardinalidade
						.getText());

				ArquivoDAO dao = new ArquivoDAO(Conexao.getConexao(), arquivoBD);

				dao.armazenarArquivo(skew, cardinalidade);

			}
		});
		btnProcurarArquivoBD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());
				chooser.showOpenDialog(null);
				arquivoBD = chooser.getSelectedFile();
				if (arquivoBD != null)
					Util.setCaminhoVisitado(arquivoBD.getParentFile());
				txtBDArquivo.setText(arquivoBD.getAbsolutePath());

				btnTesteVelocidade.setEnabled(true);
			}
		});
		btnProcurarArquivoEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());
				chooser.showOpenDialog(null);
				arquivoEdit = chooser.getSelectedFile();

				if (arquivoEdit != null)
					Util.setCaminhoVisitado(arquivoEdit.getParentFile());

				txtEditArquivo.setText(arquivoEdit.getAbsolutePath());
				btnEditarArquivo.setEnabled(true);

			}
		});

		btnEditarArquivo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String caminho = null;
				Gravador gravador = new Gravador();
				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());

				if (!ckManterCaminhoEdit.isSelected()) {
					if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						caminho = chooser.getSelectedFile().getAbsolutePath();

					} else {
						caminho = null;
					}
				}
				gravador.editarArquivo(arquivoEdit,
						ckCabecalhoEdit.isSelected(),
						ckIndiceEdit.isSelected(), caminho);
			}
		});

		btnRemoverArquivoLista.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!listaArquivos.isSelectionEmpty()) {
					Integer index = listaArquivos.getSelectedIndex();
					modeloListaArquivo.remove(index);

				} else {
					JOptionPane.showMessageDialog(null,
							"Selecione um arquivo para removê-lo");
				}

				ePossivel = false;
				strPossibilidade = "";
				atualizaPossibilidade(strPossibilidade);
				atualizaBotoes();
				limpaOpcoesMerge();

			}
		});
		btnProcurarArquivoLista.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());
				chooser.showOpenDialog(null);
				arquivoAdicionado = chooser.getSelectedFile();

				if (arquivoAdicionado != null) {
					Util.setCaminhoVisitado(arquivoAdicionado.getParentFile());

					modeloListaArquivo
							.addElement(new MyFile(arquivoAdicionado));
					listaArquivos.setSelectedIndex(modeloListaArquivo.size() - 1);
					montaInfoArq1(arquivoAdicionado);
				}

				if (modeloListaArquivo.size() > 1) {
					temArquivos = true;
				}

				ePossivel = false;
				strPossibilidade = "";
				atualizaPossibilidade(strPossibilidade);
				atualizaBotoes();
				limpaOpcoesMerge();

			}
		});
		btnArquivo1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());
				chooser.showOpenDialog(null);
				arquivo1 = chooser.getSelectedFile();
				if (arquivo1 != null)
					Util.setCaminhoVisitado(arquivo1.getParentFile());
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
				JFileChooser chooser = new JFileChooser(Util
						.getCaminhoVisitado());
				chooser.showOpenDialog(null);
				arquivo2 = chooser.getSelectedFile();
				if (arquivo2 != null)
					Util.setCaminhoVisitado(arquivo2.getParentFile());
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

				BufferedLeitor bfLeitor = null;

				List<EstruturaArquivo> estruturas = new ArrayList<EstruturaArquivo>();

				for (int i = 0; i < modeloListaArquivo.size(); i++) {

					try {
						bfLeitor = new BufferedLeitor(modeloListaArquivo.get(i)
								.getFile());
					} catch (Exception e) {
						e.printStackTrace();
					}

					estruturas.add(bfLeitor.getEstrutura());
				}

				Integer numeroTuplas = estruturas.get(0).getNumeroTuplas();

				ePossivel = true;

				for (EstruturaArquivo est : estruturas) {
					if (!est.getNumeroTuplas().equals(numeroTuplas))
						ePossivel = false;
				}
				if (ePossivel) {
					strPossibilidade = "A mescla é possível.";
					atualizaPossibilidade(strPossibilidade);
				} else {
					strPossibilidade = "Não é possivel fazer a mescla horizontal de arquivos\nque possuem número de tuplas diferentes.";
					atualizaPossibilidade(strPossibilidade);
				}

				atualizaBotoes();

				// Leitor leitor = new Leitor();
				// EstruturaArquivo estrtArq1 = leitor
				// .obterEstruturaArquivo(arquivo1);
				// EstruturaArquivo estrtArq2 = leitor
				// .obterEstruturaArquivo(arquivo2);
				//
				// ePossivel = false;
				//
				// if (estrtArq1.getNumeroTuplas().equals(
				// estrtArq2.getNumeroTuplas())) {
				// ePossivel = true;
				// strPossibilidade = "A mescla é possível.";
				// atualizaPossibilidade(strPossibilidade);
				// } else {
				// strPossibilidade =
				// "Não é possivel fazer a mescla horizontal de arquivos\nque possuem número de tuplas diferentes.";
				// atualizaPossibilidade(strPossibilidade);
				// }
				// atualizaBotoes();
			}
		});

		rdMergeVertical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				BufferedLeitor bfLeitor = null;

				List<EstruturaArquivo> estruturas = new ArrayList<EstruturaArquivo>();

				for (int i = 0; i < modeloListaArquivo.size(); i++) {

					try {

						bfLeitor = new BufferedLeitor(modeloListaArquivo.get(i)
								.getFile());
						
					} catch (Exception er) {
						er.printStackTrace();
					}

					estruturas.add(bfLeitor.getEstrutura());
				}

				Integer numeroDimensoes = estruturas.get(0).getDimensoes()
						.size();

				ePossivel = true;

				for (EstruturaArquivo est : estruturas) {
					if (!numeroDimensoes.equals(est.getDimensoes().size()))
						ePossivel = false;
				}

				if (ePossivel) {
					strPossibilidade = "A mescla é possível.";
					atualizaPossibilidade(strPossibilidade);
				} else {
					strPossibilidade = "Não é possivel fazer a mescla vertical de arquivos \nque possuem número de dimensões diferentes.";
					atualizaPossibilidade(strPossibilidade);
				}

				atualizaBotoes();

				// Leitor leitor = new Leitor();
				// EstruturaArquivo estrtArq1 = leitor
				// .obterEstruturaArquivo(arquivo1);
				// EstruturaArquivo estrtArq2 = leitor
				// .obterEstruturaArquivo(arquivo2);
				//
				// ePossivel = false;
				// if (estrtArq1.getDimensoes().size() ==
				// estrtArq2.getDimensoes()
				// .size()) {
				// ePossivel = true;
				// strPossibilidade = "A mescla é possível.";
				// atualizaPossibilidade(strPossibilidade);
				// } else {
				// strPossibilidade =
				// "Não é possivel fazer a mescla vertical de arquivos \nque possuem número de dimensões diferentes.";
				// atualizaPossibilidade(strPossibilidade);
				// }
				// atualizaBotoes();

			}
		});

		btnMesclar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!ckCriarArquivo1.isSelected()
						&& !ckCriarArquivo2.isSelected()) {
					JOptionPane.showMessageDialog(null,
							"Selecione a criação de pelo menos um arquivo");
				} else {
					String caminho = null;
					JFileChooser chooser = new JFileChooser(Util
							.getCaminhoVisitado());
					if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						caminho = chooser.getSelectedFile().getAbsolutePath();

						if (ckCriarArquivo1.isSelected()) {
							Gravador gravador = new Gravador();

							gravador.setCaminhoArquivo(caminho + "1");

							if (rdMergeHorizontal.isSelected()) {
								try {

									File resultadoMerge;
									File parcial;

									if (modeloListaArquivo.size() == 2) {
										resultadoMerge = gravador
												.mergeHorizontal(
														modeloListaArquivo.get(
																0).getFile(),
														modeloListaArquivo.get(
																1).getFile(),
														ckCabecalho
																.isSelected(),
														ckIndice.isSelected(),
														getTela());
									} else {

										resultadoMerge = gravador
												.mergeHorizontal(
														modeloListaArquivo.get(
																0).getFile(),
														modeloListaArquivo.get(
																1).getFile(),
														true, false, getTela());
									}

									for (int i = 2; i < modeloListaArquivo
											.size(); i++) {

										if (i == modeloListaArquivo.size() - 1)
											gravador.setCaminhoArquivo(caminho
													+ "1");
										else
											gravador.setCaminhoArquivo(caminho
													+ "_1_" + i);

										if (i == modeloListaArquivo.getSize() - 1) {

											parcial = gravador.mergeHorizontal(
													resultadoMerge,
													modeloListaArquivo.get(i)
															.getFile(),
													ckCabecalho.isSelected(),
													ckIndice.isSelected(),
													getTela());

											resultadoMerge = parcial;

										} else {
											parcial = gravador.mergeHorizontal(
													resultadoMerge,
													modeloListaArquivo.get(1)
															.getFile(), true,
													false, getTela());

											resultadoMerge = parcial;
										}
									}

									// gravador.mergeHorizontal(arquivo1,
									// arquivo2, ckCabecalho.isSelected(),
									// ckIndice.isSelected(), getTela());
								} catch (FileNotFoundException e) {
									JOptionPane
											.showMessageDialog(null,
													"Problemas com o arquivo selecionado.");
								}
							}

							if (rdMergeVertical.isSelected()) {
								try {

									File resultadoMerge;
									File parcial;

									if (modeloListaArquivo.size() == 2) {
										resultadoMerge = gravador
												.mergeVertical(
														modeloListaArquivo.get(
																0).getFile(),
														modeloListaArquivo.get(
																1).getFile(),
														ckCabecalho
																.isSelected(),
														ckIndice.isSelected(),
														getTela());
									} else {

										resultadoMerge = gravador
												.mergeVertical(
														modeloListaArquivo.get(
																0).getFile(),
														modeloListaArquivo.get(
																1).getFile(),
														true, false, getTela());
									}

									for (int i = 2; i < modeloListaArquivo
											.size(); i++) {

										if (i == modeloListaArquivo.size() - 1)
											gravador.setCaminhoArquivo(caminho
													+ "1");
										else
											gravador.setCaminhoArquivo(caminho
													+ "_1_" + i);

										if (i == modeloListaArquivo.getSize() - 1) {

											parcial = gravador.mergeVertical(
													resultadoMerge,
													modeloListaArquivo.get(i)
															.getFile(),
													ckCabecalho.isSelected(),
													ckIndice.isSelected(),
													getTela());

											resultadoMerge = parcial;

										} else {
											parcial = gravador.mergeVertical(
													resultadoMerge,
													modeloListaArquivo.get(1)
															.getFile(), true,
													false, getTela());

											resultadoMerge = parcial;
										}
									}

									// gravador.mergeVertical(arquivo1,
									// arquivo2,
									// ckCabecalho.isSelected(),
									// ckIndice.isSelected());

								} catch (FileNotFoundException e) {
									JOptionPane
											.showMessageDialog(null,
													"Problemas com o arquivo selecionado.");
								}
							}

						}

					}
					if (ckCriarArquivo2.isSelected()) {
						Gravador gravador2 = new Gravador();

						gravador2.setCaminhoArquivo(caminho + "2");

						if (rdMergeHorizontal.isSelected()) {
							try {

								File resultadoMerge;
								File parcial;

								if (modeloListaArquivo.size() == 2) {
									resultadoMerge = gravador2
											.mergeHorizontal(modeloListaArquivo
													.get(0).getFile(),
													modeloListaArquivo.get(1)
															.getFile(),
													ckCabecalho2.isSelected(),
													ckIndice2.isSelected(),
													getTela());
								} else {

									resultadoMerge = gravador2
											.mergeHorizontal(modeloListaArquivo
													.get(0).getFile(),
													modeloListaArquivo.get(1)
															.getFile(), true,
													false, getTela());
								}

								for (int i = 2; i < modeloListaArquivo.size(); i++) {

									if (i == modeloListaArquivo.size() - 1)
										gravador2.setCaminhoArquivo(caminho
												+ "2");
									else
										gravador2.setCaminhoArquivo(caminho
												+ "_2_" + i);

									if (i == modeloListaArquivo.getSize() - 1) {

										parcial = gravador2.mergeHorizontal(
												resultadoMerge,
												modeloListaArquivo.get(i)
														.getFile(),
												ckCabecalho2.isSelected(),
												ckIndice2.isSelected(),
												getTela());

										resultadoMerge = parcial;

									} else {
										parcial = gravador2.mergeHorizontal(
												resultadoMerge,
												modeloListaArquivo.get(1)
														.getFile(), true,
												false, getTela());

										resultadoMerge = parcial;
									}
								}

								// gravador.mergeHorizontal(arquivo1,
								// arquivo2, ckCabecalho.isSelected(),
								// ckIndice.isSelected(), getTela());
							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(null,
										"Problemas com o arquivo selecionado.");
							}
						}

						if (rdMergeVertical.isSelected()) {
							try {

								File resultadoMerge;
								File parcial;

								if (modeloListaArquivo.size() == 2) {
									resultadoMerge = gravador2
											.mergeVertical(modeloListaArquivo
													.get(0).getFile(),
													modeloListaArquivo.get(1)
															.getFile(),
													ckCabecalho2.isSelected(),
													ckIndice2.isSelected(),
													getTela());
								} else {

									resultadoMerge = gravador2
											.mergeVertical(modeloListaArquivo
													.get(0).getFile(),
													modeloListaArquivo.get(1)
															.getFile(), true,
													false, getTela());
								}

								for (int i = 2; i < modeloListaArquivo.size(); i++) {

									if (i == modeloListaArquivo.size() - 1)
										gravador2.setCaminhoArquivo(caminho
												+ "2");
									else
										gravador2.setCaminhoArquivo(caminho
												+ "_2_" + i);

									if (i == modeloListaArquivo.getSize() - 1) {

										parcial = gravador2.mergeVertical(
												resultadoMerge,
												modeloListaArquivo.get(i)
														.getFile(),
												ckCabecalho2.isSelected(),
												ckIndice2.isSelected(),
												getTela());

										resultadoMerge = parcial;

									} else {
										parcial = gravador2.mergeVertical(
												resultadoMerge,
												modeloListaArquivo.get(1)
														.getFile(), true,
												false, getTela());

										resultadoMerge = parcial;
									}
								}

								// gravador.mergeVertical(arquivo1,
								// arquivo2,
								// ckCabecalho.isSelected(),
								// ckIndice.isSelected());

							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(null,
										"Problemas com o arquivo selecionado.");
							}

						}

					}

				}

				JOptionPane.showMessageDialog(null,
						"O(s) arquivo(s) foi(ram) salvo(s) com sucesso!");

			}

		});

	}

	private void atualizaPossibilidade(String strPossibilidade) {
		txtArPossivel.setText(strPossibilidade);
	}

	private void limpaInfoArq1() {
		String info = "Número de tuplas: \n" + "Número de dimensões: ";
		txtArArq1.setText(info);
	}

	private void montaInfoArq1(File arquivo1) {

		BufferedLeitor leitor = null;

		try {

			leitor = new BufferedLeitor(arquivo1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		EstruturaArquivo estrtArq1 = leitor.getEstrutura();

		String info = "Número de tuplas: " + estrtArq1.getNumeroTuplas() + "\n"
				+ "Número de dimensões: " + estrtArq1.getDimensoes().size();
		txtArArq1.setText(info);
	}

	private void montaInfoArq2(File arquivo2) {

		BufferedLeitor leitor = null;

		try {

			leitor = new BufferedLeitor(arquivo2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		EstruturaArquivo estrtArq2 = leitor.getEstrutura();

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

	public Integer getPorcentagemBarra() {
		return porcentagemBarra;
	}

	public void setPorcentagemBarra(Integer porcentagemBarra) {
		this.porcentagemBarra = porcentagemBarra;
	}

	private TelaPrincipal getTela() {
		return this;
	}

	public Boolean getPreencheBarra() {
		return preencheBarra;
	}

	public void setPreencheBarra(Boolean preencheBarra) {
		this.preencheBarra = preencheBarra;
	}

	public JTextArea getTxtArStatusMerge() {
		return txtArStatusMerge;
	}

	public void setTxtArStatusMerge(JTextArea txtArStatusMerge) {
		this.txtArStatusMerge = txtArStatusMerge;
	}

}
