package robo.barueri.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import robo.barueri.exception.ErrorHandler;
import robo.barueri.exception.GenericException;
import robo.barueri.service.RoboBarueriService;

public class RoboView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static JTextField arquivoTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			// Exibe a tela de erro e registra no log
			ErrorHandler.handleError(throwable, null);
			// Encerra a aplicação de forma controlada, se necessário
			System.exit(1);
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoboView frame = new RoboView();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					ErrorHandler.handleError(e, null);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RoboView() {
		setTitle("Cadastro Automático de NFS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 411);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu jMenuArquivo = new JMenu("Arquivo");
		menuBar.add(jMenuArquivo);

		JSeparator separator = new JSeparator();
		jMenuArquivo.add(separator);

		JMenuItem jMenuItemSair = new JMenuItem("Sair");
		jMenuItemSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 System.exit(0);
			}
		});
		jMenuArquivo.add(jMenuItemSair);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JPanel panelProcessamento = new JPanel();
		panelProcessamento.setBackground(Color.WHITE);
		contentPane.add(panelProcessamento, "processamento");
		panelProcessamento.setLayout(null);

		JLabel txtEcfCexp = new JLabel();
		txtEcfCexp.setForeground(Color.LIGHT_GRAY);
		txtEcfCexp.setText("Processamento");
		txtEcfCexp.setHorizontalAlignment(SwingConstants.RIGHT);
		txtEcfCexp.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtEcfCexp.setBounds(496, 0, 198, 20);
		panelProcessamento.add(txtEcfCexp);

		JLabel lblSelecioneOArquivo = new JLabel();
		lblSelecioneOArquivo.setText("Selecione o arquivo Excel");
		lblSelecioneOArquivo.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelecioneOArquivo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelecioneOArquivo.setBounds(90, 73, 500, 20);
		panelProcessamento.add(lblSelecioneOArquivo);

		arquivoTextField = new JTextField();
		arquivoTextField.setColumns(10);
		arquivoTextField.setBounds(90, 97, 334, 20);
		panelProcessamento.add(arquivoTextField);

		JButton btnProcurarArquivo = new JButton("Procurar Arquivo");
		btnProcurarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				String caminho;
				if (arquivoTextField.getText().isEmpty()) {
					caminho = "user.home";
				} else {
					caminho = "user.home";
				}
				fileChooser.setCurrentDirectory(new File(System.getProperty(caminho)));
				JTextField jt = new JTextField();
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Arquivo Excel (xlsx)", "xlsx"));
				fileChooser.setAcceptAllFileFilterUsed(false);

				int result = fileChooser.showOpenDialog(jt);

				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					arquivoTextField.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		btnProcurarArquivo.setBounds(434, 96, 156, 23);
		panelProcessamento.add(btnProcurarArquivo);

		
		
		/*
		JLabel lblLogoAbmc = new JLabel("");
		lblLogoAbmc.setIcon(new ImageIcon(CexpView.class.getResource("/com/cargill/cexp/files/logos/logoabmc.png")));
		lblLogoAbmc.setBounds(10, 223, 198, 106);
		panelProcessamento.add(lblLogoAbmc);

		JLabel lblLogoCargill = new JLabel("");
		lblLogoCargill
				.setIcon(new ImageIcon(CexpView.class.getResource("/com/cargill/cexp/files/logos/logocargill.png")));
		lblLogoCargill.setBounds(464, 212, 220, 117);
		panelProcessamento.add(lblLogoCargill);
		*/
		
		JLabel txtEcfCexp_1 = new JLabel();
		txtEcfCexp_1.setText("Cadastro Autom\u00E1tico de NFS");
		txtEcfCexp_1.setHorizontalAlignment(SwingConstants.CENTER);
		txtEcfCexp_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtEcfCexp_1.setBounds(100, 31, 513, 20);
		panelProcessamento.add(txtEcfCexp_1);
		
		JButton btnExceutar = new JButton("Exceutar");
		btnExceutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				RoboBarueriService rb = new RoboBarueriService();
				String arquivoExcel; // = "C:\\Users\\agcal\\Documents\\ABMC\\Assurant\\Projetos\\20210316 - Rob� Barueri\\Relat�rio_Quinzenal_Solutions.xlsx";
				arquivoExcel = arquivoTextField.getText();
				if (arquivoExcel.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Informe o arquivo Excel");
					return;
				}
				
				try {
					rb.criarColunaStatusProcessamento(arquivoExcel);
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, "Necess�rio fechar o arquivo");
					return;
				}
				
				try {
					rb.lerExcel(arquivoExcel);
					JOptionPane.showMessageDialog(null, "Processamento conclu�do");
				} catch (GenericException e1) {
					JOptionPane.showMessageDialog(null, e1.toString());
				}
			}
		});
		btnExceutar.setBounds(90, 128, 115, 23);
		panelProcessamento.add(btnExceutar);
		
		JLabel lblLogoNfs = new JLabel("");
		lblLogoNfs.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogoNfs.setIcon(new ImageIcon("icon\\NFSe.png"));
		lblLogoNfs.setBounds(20, 162, 249, 167);
		panelProcessamento.add(lblLogoNfs);
		
		JLabel lblLogoNfs_1 = new JLabel("");
		lblLogoNfs_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogoNfs_1.setIcon(new ImageIcon("icon\\logoabmc.png"));
		lblLogoNfs_1.setBounds(435, 162, 249, 167);
		panelProcessamento.add(lblLogoNfs_1);

	}
	
	public String toDataSql(String str) {
		return str.substring(6, 10) + "-" + str.substring(3, 5) + "-" + str.substring(0, 2);
	}
}
