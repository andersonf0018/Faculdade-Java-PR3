package Projeto.Contas;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Projeto.Main;
import Projeto.Config.Arquivos;
import Projeto.Config.Console;
import Projeto.Financeiro.Saldo;
import Projeto.Modos.Desenvolvedor;

@SuppressWarnings("serial")
public class Cadastro extends JFrame {

	public Arquivos arq = new Arquivos();
	public Console cs = new Console();
	public Desenvolvedor dev = new Desenvolvedor();
	
	public Verificacoes v = new Verificacoes();
	public Saldo saldo = new Saldo();
	
	private JPanel contentPane;
	private String documento;
	private String nome;
	private String email;
	private String senha;

	private JTextField documentoField;
	private JTextField nomeField;
	private JTextField emailField;
	private JPasswordField senhaField;
	private JButton botao;
	
	public Cadastro() {}

	public Cadastro(int modo) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 300, 275);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titulo = new JLabel("Cadastro");
		titulo.setFont(new Font("Tahoma", Font.PLAIN, 24));
		titulo.setBounds(100, 10, 100, 29);
		contentPane.add(titulo);

		JLabel tituloDocumento = null;
		if(modo == 1) {
			tituloDocumento = new JLabel("CPF");
		} else {
			tituloDocumento = new JLabel("CNPJ");
		}
		tituloDocumento.setBounds(10, 68, 60, 13);
		contentPane.add(tituloDocumento);

		JLabel tituloNome = new JLabel("Nome");
		tituloNome.setBounds(10, 91, 60, 13);
		contentPane.add(tituloNome);

		JLabel tituloEmail = new JLabel("Email");
		tituloEmail.setBounds(10, 114, 60, 13);
		contentPane.add(tituloEmail);

		JLabel tituloSenha = new JLabel("Senha");
		tituloSenha.setBounds(10, 137, 60, 13);
		contentPane.add(tituloSenha);

		documentoField = new JTextField();
		documentoField.setBounds(83, 65, 150, 19);
		contentPane.add(documentoField);
		documentoField.setColumns(10);

		nomeField = new JTextField();
		nomeField.setBounds(83, 88, 150, 19);
		contentPane.add(nomeField);
		nomeField.setColumns(10);

		emailField = new JTextField();
		emailField.setBounds(83, 111, 150, 19);
		contentPane.add(emailField);
		emailField.setColumns(10);

		senhaField = new JPasswordField();
		senhaField.setBounds(83, 134, 150, 19);
		contentPane.add(senhaField);

		botao = new JButton("Criar");
		botao.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if ((documentoField.getText().equals("")) || (nomeField.getText().equals("")
						|| (emailField.getText().equals("")) || (senhaField.getPassword().length == 0))) {
					JOptionPane.showMessageDialog(contentPane, "Preencha todos os campos antes de continuar!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(modo == 1  && !cs.isDigit(documentoField.getText())) {
					JOptionPane.showMessageDialog(contentPane, "Utilize apenas números no campo de CPF!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(modo == 2  && !cs.isDigit(documentoField.getText())) {
					JOptionPane.showMessageDialog(contentPane, "Utilize apenas números no campo de CNPJ!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (modo == 1 && documentoField.getText().length() != 11) {
					JOptionPane.showMessageDialog(contentPane, "Um CPF precisa ter 11 digitos!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (modo == 2 && documentoField.getText().length() != 14) {
					JOptionPane.showMessageDialog(contentPane, "Um CNPJ precisa ter 14 digitos!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (v.contaJaCriada(documentoField.getText(), modo)) {
					JOptionPane.showMessageDialog(contentPane, "Já existe uma conta criada com esse " + v.formatModo(modo) + "!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if((modo == 2) && (dev.desenvolvedorJaExiste(nomeField.getText()))) {
					JOptionPane.showMessageDialog(contentPane, "Já existe um(a) desenvolvedor(a) com esse nome!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					File file = new File(v.getPastaModo(modo) + "/" + documentoField.getText() + ".txt");
					file.createNewFile();
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					if(modo == 1) {
						bw.write(documentoField.getText() + 
								"-" + nomeField.getText() + 
								"-" + emailField.getText() + 
								"-" + senhaField.getText() + 
								"-0\n");
					}
					if(modo == 2) {
						bw.write(documentoField.getText() + 
								"-" + nomeField.getText() + 
								"-" + emailField.getText() + 
								"-" + senhaField.getText() + 
								"-0.0-0-0\n");
					}
					bw.close();
					JOptionPane.showMessageDialog(contentPane, "Conta criada com sucesso!", "Sucesso!",
								JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
					Main.carregando = false;
					Main.logado = documentoField.getText();
				} catch (IOException e1) {
						e1.printStackTrace();
				}
			}
		});botao.setBounds(100,182,85,21);contentPane.add(botao);

	}

	public Cadastro(String documento, String nome, String email, String senha) {
		this.documento = documento;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
