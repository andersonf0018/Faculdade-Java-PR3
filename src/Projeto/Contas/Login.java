package Projeto.Contas;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Projeto.Main;
import Projeto.Modos.Desenvolvedor;
import Projeto.Modos.Usuario;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Login extends JFrame {

	public Usuario user = new Usuario();
	public Desenvolvedor dev = new Desenvolvedor();
	public Verificacoes v = new Verificacoes();
	
	private JPanel contentPane;
	
	private String documento;
	private String senha;
	
	private JTextField documentoField;
	private JPasswordField senhaField;

	public Login(int modo) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 300, 220);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titulo = new JLabel("Login");
		titulo.setFont(new Font("Tahoma", Font.PLAIN, 24));
		titulo.setBounds(115, 10, 58, 29);
		contentPane.add(titulo);
		
		JLabel tituloDocumento = null;
		if(modo == 1) {
			tituloDocumento = new JLabel("CPF");
		} else {
			tituloDocumento = new JLabel("CNPJ");
		}
		tituloDocumento.setBounds(10, 68, 60, 13);
		contentPane.add(tituloDocumento);
		
		JLabel tituloSenha = new JLabel("Senha");
		tituloSenha.setBounds(10, 91, 60, 13);
		contentPane.add(tituloSenha);
		
		documentoField = new JTextField();
		documentoField.setBounds(80, 65, 150, 19);
		contentPane.add(documentoField);
		documentoField.setColumns(10);
		
		senhaField = new JPasswordField();
		senhaField.setBounds(80, 88, 150, 19);
		contentPane.add(senhaField);
		senhaField.setColumns(10);
		
		JButton botao = new JButton("Logar");
		botao.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if(!v.contaJaCriada(documentoField.getText(), modo)) {
					JOptionPane.showMessageDialog(contentPane, v.formatModo(modo) + " não encontrado!", "OPS!",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if(modo == 1) {
						Usuario usuario = user.getUsuarioByCpf(documentoField.getText());
						if(usuario.getSenha().equals(senhaField.getText())) {
							setVisible(false);
							Main.carregando = false;
							Main.logado = documentoField.getText();
						} else {
							JOptionPane.showMessageDialog(contentPane, "Senha incorreta!", "OPS!",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						Desenvolvedor desenvolvedor = dev.getDevByCnpj(documentoField.getText());
						if(desenvolvedor.getSenha().equals(senhaField.getText())) {
							setVisible(false);
							Main.carregando = false;
							Main.logado = documentoField.getText();
						} else {
							JOptionPane.showMessageDialog(contentPane, "Senha incorreta!", "OPS!",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		botao.setBounds(100, 132, 85, 21);
		contentPane.add(botao);
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
