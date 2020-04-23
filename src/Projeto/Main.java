package Projeto;

//import java.util.Arrays;
import java.util.Scanner;
import Projeto.Config.*;
import Projeto.Contas.*;
import Projeto.Financeiro.*;
import Projeto.Modos.*;

public class Main {

	//Classes
	static Arquivos arq = new Arquivos();
	static Console cs = new Console();
	static Saldo dinheiro = new Saldo();
	static Usuario user = new Usuario();
	static Desenvolvedor dev = new Desenvolvedor();
	
	//Controle
	public static String logado = null;
	public static boolean carregando = true;
	public static boolean sucesso = true;

	//Menus
	public static boolean principal = true;
	public static boolean usuario = true;
	public static boolean desenvolvedor = true;

	public static void main(String[] args) {
		arq.criarPasta("usuarios");
		arq.criarPasta("desenvolvedores");
		arq.criarPasta("financeiro");
		arq.criarPasta("notas");
		arq.criarArquivo("jogos", "txt");
		arq.criarArquivo("financeiro/depositos", "txt");
		arq.criarArquivo("financeiro/vendas", "txt");
		
		Scanner input = new Scanner(System.in);
		while (principal) {
			cs.clear();
			logado = null;
			carregando = true;
			sucesso = true;
			usuario = true;
			desenvolvedor = true;
			System.out.println("  _          _       \r\n" +
							  	" | |    ___ (_) __ _ \r\n" +
							  	" | |   / _ \\| |/ _` |\r\n" +
							  	" | |__| (_) | | (_| |\r\n" +
							  	" |_____\\___// |\\__,_|\r\n" +
							  	"          |__/       \n");

			System.out.println("Escolha a opção desejada:");
			System.out.println(cs.coloredOption(1, "Modo usuário"));
			System.out.println(cs.coloredOption(2, "Modo desenvolvedor"));
			System.out.println(cs.coloredOption(0, "Sair"));

			String opcao = input.nextLine();
			switch (opcao) {
			case "1":
				cs.clear();
				System.out.println(
								"  _   _               \r\n" +
								" | | | |___  ___ _ __ \r\n" +
								" | | | / __|/ _ \\ '__|\r\n" +
								" | |_| \\__ \\  __/ |   \r\n" +
								"  \\___/|___/\\___|_|   \r\n");

				System.out.println(cs.coloredOption(1, "Logar"));
				System.out.println(cs.coloredOption(2, "Registrar"));
				System.out.println(cs.coloredOption(0, "Voltar para o inicio"));
				String opcaoUser = input.nextLine();
				switch (opcaoUser) {
				case "1":
					Login lg = new Login(1);
					lg.setVisible(true);
					cs.animarMenu(cs.coloredText(cs.VERDE, "Aguardando..."));
					break;
				case "2":
					Cadastro cd = new Cadastro(1);
					cd.setVisible(true);
					cs.animarMenu(cs.coloredText(cs.VERDE, "Aguardando..."));
					break;
				case "0":
					sucesso = false;
					break;
				}
				if(sucesso == true) {
					user.menuUsuario();
				}
				break;
			case "2":
				cs.clear();
				System.out.println(
		        		"  ____             \r\n" + 
		        		" |  _ \\  _____   __\r\n" + 
		        		" | | | |/ _ \\ \\ / /\r\n" + 
		        		" | |_| |  __/\\ V / \r\n" + 
		        		" |____/ \\___| \\_/  \r\n");

				System.out.println(cs.coloredOption(1, "Logar"));
				System.out.println(cs.coloredOption(2, "Registrar"));
				System.out.println(cs.coloredOption(0, "Voltar para o inicio"));
				String opcaoDev = input.nextLine();
				switch (opcaoDev) {
				case "1":
					Login lg = new Login(2);
					lg.setVisible(true);
					cs.animarMenu(cs.coloredText(cs.VERDE, "Aguardando..."));
					break;
				case "2":
					Cadastro cd = new Cadastro(2);
					cd.setVisible(true);
					cs.animarMenu(cs.coloredText(cs.VERDE, "Aguardando..."));
					break;
				case "0":
					sucesso = false;
					break;
				}
				if(sucesso == true) {
					dev.menuDev();
				}
			}
		}
		input.close();
	}
}
