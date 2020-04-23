package Projeto.Modos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import Projeto.Main;
import Projeto.Config.*;
import Projeto.Contas.Verificacoes;
import Projeto.Financeiro.*;


@SuppressWarnings("resource")
public class Usuario {

	public Scanner input = new Scanner(System.in);
	
	public Arquivos arq = new Arquivos();
	public Console cs = new Console();
	public Desenvolvedor dev = new Desenvolvedor();
	public Jogo j = new Jogo();
	public Saldo saldo = new Saldo();
	public Deposito deposito = new Deposito();
	public Venda venda = new Venda();
	public Verificacoes verificacao = new Verificacoes();
	
	private String cpf;
	private String nome;
	private String email;
	private String senha;

	public Usuario() {}

	public Usuario(String cpf, String nome, String email, String senha, double saldoUser) {
		this.cpf = cpf;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		saldoUser = saldo.getSaldo(cpf);
	}
	
	public Usuario getUsuarioByCpf(String cpf) {
		if(verificacao.contaJaCriada(cpf, 1)) {
			ArrayList<String> userLinha = new ArrayList<String>();
			arq.transformFileInList("usuarios/" + cpf + ".txt", userLinha);
			String[] infos = userLinha.get(0).split("-");
			Usuario user = new Usuario(infos[0], infos[1], infos[2], infos[3], Double.parseDouble(infos[4]));
			return user;
		} else {
			return null;
		}
	}

	public void bibliotecaDeObtidos(String cpf) {
		String arquivo = "usuarios/" + cpf + ".txt";
		ArrayList<String> jogosObtidos = new ArrayList<String>();
		int index = 1;
		arq.transformFileInList(arquivo, jogosObtidos);
		if (jogosObtidos.size() <= 1) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Você não possui nenhum jogo comprado!"));
		} else {
			while (index < jogosObtidos.size()) {
				String[] infos = jogosObtidos.get(index).split("-");
				String[] data = infos[4].split("/");
				String[] requisitos = infos[10].split("/");

				Jogo jogo = new Jogo(Integer.parseInt(infos[0]), infos[1], infos[2], Double.parseDouble(infos[3]),
						Arrays.asList(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])),
						infos[5], infos[6], infos[7], Double.parseDouble(infos[8]), Integer.parseInt(infos[9]),
						Arrays.asList(requisitos[0], requisitos[1], requisitos[2], requisitos[3], requisitos[4]));

				System.out.println("=================================\n" 
						+ cs.coloredText(cs.AMARELO, "ID: ") + jogo.getId() + "\n" 
						+ cs.coloredText(cs.AMARELO, "Nome: ") + jogo.getNome() + "\n"
						+ cs.coloredText(cs.AMARELO, "Descrição: ") + jogo.getDescricao() + "\n"
						+ cs.coloredText(cs.AMARELO, "Preço: ") + cs.formatPrice(jogo.getPreco()) + "\n"
						+ cs.coloredText(cs.AMARELO, "Data de lançamento: ") + jogo.getLancamento() + "\n"
						+ cs.coloredText(cs.AMARELO, "Desenvolvedor: ") + jogo.getDesenvolvedor() + "\n"
						+ cs.coloredText(cs.AMARELO, "Distribuidora: ") + jogo.getDistribuidora() + "\n"
						+ cs.coloredText(cs.AMARELO, "Gênero: ") + jogo.getGenero() + "\n"
						+ cs.coloredText(cs.AMARELO, "Avaliações: ") + jogo.getAvalicao() + "\n"
						+ cs.coloredText(cs.AMARELO, "Quantidade de votos: ") + jogo.getQuantidadeVotos() + "\n"
						+ "------ Requisitos ------" + "\n" + jogo.formatarRequisitos() + "\n");
				index++;
			}
		}
	}

	public void comprarJogo(String cpf) {
		String arquivo = "usuarios/" + cpf + ".txt";
		j.listarJogos();
		ArrayList<String> jogos = new ArrayList<String>();
		arq.transformFileInList("jogos.txt", jogos);
		if(jogos.size() > 0) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Digite o ID do jogo que você deseja comprar: ");
			try {
				int id = sc.nextInt();
				if (j.contemJogo(id, "jogos.txt")) {
					if (!j.contemJogo(id, arquivo)) {
						Jogo jogo = j.getJogoById(id, "jogos.txt");
						if (saldo.getSaldo(cpf) < jogo.getPreco()) {
							System.out.println(
									cs.coloredText(cs.VERMELHO, "\nVocê não possui saldo para comprar esse jogo!"));
							System.out.println(cs.coloredText(
									cs.VERDE, "Seu saldo: " + saldo.getSaldo(cpf) + "            Preço do jogo: " + jogo.getPreco()));
						} else {
							Desenvolvedor desenvolvedor = dev.getDevByName(jogo.getDesenvolvedor());
							Venda venda = new Venda(arq.gerarID("financeiro/vendas.txt"), new Date(), this.getCpf(), jogo.getDesenvolvedor(),
									jogo.getNome(), jogo.getPreco(), true);
							j.adicionarJogo(jogo, cpf, 1);
							desenvolvedor.atualizarFinanceiro(desenvolvedor.getCNPJ(), jogo.getPreco(), 1, 0);
							saldo.removerSaldo(cpf, jogo.getPreco());
							System.out.println(cs.coloredText(cs.AMARELO,
									"O jogo '" + jogo.getNome() + "' foi adicionado a sua biblioteca!"));
							System.out.println(cs.coloredText(cs.VERDE, "Seu saldo atual: " + saldo.getSaldo(cpf)));
							venda.registrarVenda(venda);
						}
					} else {
						System.out.println(cs.coloredText(cs.VERMELHO, "\nVocê já possui esse jogo!"));
					}
				} else {
					System.out.println(cs.coloredText(cs.VERMELHO, "\nEsse jogo não existe! Tente novamente!"));
				}
			} catch (Exception e) {
				System.out.println(cs.coloredText(cs.VERMELHO, "ID inválido! Tente novamente!"));
			}
		}
	}

	public void avaliarJogo(String cpf) {
		String arquivo = "usuarios/" + cpf + ".txt";
		ArrayList<String> jogosObtidos = new ArrayList<String>();
		arq.transformFileInList(arquivo, jogosObtidos);
		if (jogosObtidos.size() <= 1) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Você não possui nenhum jogo comprado!"));
		} else {
			this.bibliotecaDeObtidos(cpf);
			Scanner sc = new Scanner(System.in);
			System.out.println("\nDigite o ID do jogo que você deseja avaliar: ");
			try {
				int id = sc.nextInt();
				if (j.contemJogo(id, arquivo)) {
					Jogo jogo = j.getJogoById(id, arquivo);
					System.out.println("Digite um nota de 0-10 para a avaliação do jogo '" + jogo.getNome() + "': ");
					try {
						double nota = sc.nextDouble();	
						if (nota >= 0 && nota <= 10) {
							jogo.setQuantidadeVotos(jogo.getQuantidadeVotos() + 1);
							jogo.setAvaliacao((jogo.getAvalicao() + nota) / jogo.getQuantidadeVotos());

							String[] lancamento = jogo.getLancamento().split("/");
							String[] requisitos = jogo.getRequisitos().split("/");

							// Copia do Jogo que se encontra no File com as alterações necessárias
							Jogo jogoAtualizado = new Jogo(jogo.getId(), jogo.getNome(), jogo.getDescricao(),
									jogo.getPreco(),
									Arrays.asList(Integer.parseInt(lancamento[0]), Integer.parseInt(lancamento[1]),
											Integer.parseInt(lancamento[2])),
									jogo.getDesenvolvedor(), jogo.getDistribuidora(), jogo.getGenero(),
									jogo.getAvalicao(), jogo.getQuantidadeVotos(), Arrays.asList(requisitos[0],
											requisitos[1], requisitos[2], requisitos[3], requisitos[4]));
							Desenvolvedor devJogo = dev.getDevByName(jogo.getDesenvolvedor());
							j.atualizarJogo(jogoAtualizado, "jogos.txt");
							j.atualizarJogo(jogoAtualizado, arquivo);
							j.atualizarJogo(jogoAtualizado, "desenvolvedores/" + devJogo.getCNPJ() + ".txt");
							System.out.println(cs.coloredText(cs.AMARELO,
									"Você a avaliou o jogo '" + jogo.getNome() + "' com a nota " + nota + "!"));
							System.out.println(cs.coloredText(cs.AMARELO, "Nota atual do jogo: " + jogo.getAvalicao()));
						} else {
							System.out.println(cs.coloredText(cs.VERMELHO, "\nNota inválida!"));
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(cs.coloredText(cs.VERMELHO, "\nNota inválida! Tente novamente!"));
					}
				} else {
					System.out.println(cs.coloredText(cs.VERMELHO, "\nVocê não possui esse jogo!"));
				}
			} catch (Exception e) {
				System.out.println(cs.coloredText(cs.VERMELHO, "ID inválido! Tente novamente!"));
			}
		}
	}
	
	public void reembolsarJogo(String cpf) {
		String arquivo = "usuarios/" + cpf + ".txt";
		ArrayList<String> jogosObtidos = new ArrayList<String>();
		arq.transformFileInList(arquivo, jogosObtidos);
		if (jogosObtidos.size() <= 1) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Você não possui nenhum jogo comprado!"));
		} else {
			this.bibliotecaDeObtidos(cpf);
			Scanner sc = new Scanner(System.in);
			System.out.println("\nDigite o ID do jogo que você deseja reembolsar:");
			int id = sc.nextInt();
			if (j.contemJogo(id, arquivo)) {
				Venda reembolso = venda.getVendaByCpf(cpf);
				Jogo jogo = j.getJogoById(id, arquivo);
				Desenvolvedor desenvolvedor = dev.getDevByName(jogo.getDesenvolvedor());
				j.removerJogo(id, cpf, 1);
				saldo.addSaldo(cpf, jogo.getPreco());
				dev.atualizarFinanceiro(desenvolvedor.getCNPJ(), -jogo.getPreco(), 0, 1);
				reembolso.setRefund(Integer.toString(reembolso.getId())); 
				System.out.println(
						"\n" + cs.coloredText(cs.AMARELO, "Jogo '" + jogo.getNome() + "' reembolsado com sucesso!"));
				System.out.println(cs.coloredText(cs.VERDE, "Você recebeu seus R$" + jogo.getPreco() + " de volta."));
				System.out.println(cs.coloredText(cs.VERDE, "Seu saldo atual: " + saldo.getSaldo(cpf)));
			} else {
				System.out.println(cs.coloredText(cs.VERMELHO, "\nVocê não possui esse jogo!"));
			}
		}
	}
	
	public void getExtratoFinanceiro(String cpf, int modo) {
		ArrayList<String> compras = new ArrayList<String>();
		ArrayList<String> depositos = new ArrayList<String>();
		arq.transformFileInList("financeiro/vendas.txt", compras);
		arq.transformFileInList("financeiro/depositos.txt", depositos);
		cs.clear();
		if((modo == 1) || (modo == 3)) {
			System.out.println("   ____                                    \r\n" + 
					"  / ___|___  _ __ ___  _ __  _ __ __ _ ___ \r\n" + 
					" | |   / _ \\| '_ ` _ \\| '_ \\| '__/ _` / __|\r\n" + 
					" | |__| (_) | | | | | | |_) | | | (_| \\__ \\\r\n" + 
					"  \\____\\___/|_| |_| |_| .__/|_|  \\__,_|___/\r\n" + 
					"                      |_|                  \n");
			if(compras.size() > 0) {
				for(int i = 0; i < compras.size(); i++) {
					String[] infos = compras.get(i).split("-");
					if(infos[2].equals(cpf)) {
						try {
							Venda venda = new Venda(Integer.parseInt(infos[0]),
									new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(infos[1]),
									infos[2], infos[3], infos[4], Double.parseDouble(infos[5]),
									Boolean.parseBoolean(infos[6]));
							System.out.println("=================================\n"
									+ cs.coloredText(cs.ROXO, venda.isRefund()) + "\n"
									+ cs.coloredText(cs.AMARELO, "ID da compra: ") + venda.getId() + "\n" 
									+ cs.coloredText(cs.AMARELO, "Data: ") + arq.getDateTime(venda.getData()) + "\n"
									+ cs.coloredText(cs.AMARELO, "Jogo: ") + venda.getNomeJogo() + "\n"
									+ cs.coloredText(cs.AMARELO, "Desenvolvedor: ") + venda.getVendedor() + "\n"
									+ cs.coloredText(cs.AMARELO, "Valor: ") + "R$" + venda.getValor() + "\n");
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				System.out.println(cs.coloredText(cs.VERMELHO, "Você não possui compras registradas!"));
			}
		}
		if((modo == 2) || (modo == 3)) {
			System.out.println("  ____                       _ _            \r\n" + 
					" |  _ \\  ___ _ __   ___  ___(_) |_ ___  ___ \r\n" + 
					" | | | |/ _ \\ '_ \\ / _ \\/ __| | __/ _ \\/ __|\r\n" + 
					" | |_| |  __/ |_) | (_) \\__ \\ | || (_) \\__ \\\r\n" + 
					" |____/ \\___| .__/ \\___/|___/_|\\__\\___/|___/\r\n" + 
					"            |_|                             \n");
			if(depositos.size() > 0) {
				for(int i = 0; i < depositos.size(); i++) {
					String[] infos = depositos.get(i).split("-");
					if(infos[2].equals(cpf)) {
						System.out.println("=================================\n" 
								+ cs.coloredText(cs.AMARELO, "ID: ") + infos[0] + "\n" 
								+ cs.coloredText(cs.AMARELO, "Data: ") + infos[1] + "\n"
								+ cs.coloredText(cs.AMARELO, "Valor: ") + "R$" + infos[3] + "\n"
								+ cs.coloredText(cs.AMARELO, "Método: ") + arq.formatarMetodo(Integer.parseInt(infos[4])) + "\n");
					}
				}
			} else {
				System.out.println(cs.coloredText(cs.VERMELHO, "\nVocê não possui depositos registrados!"));
			}
		}
	}

	public void menuUsuario() {
		while(Main.usuario) {
			cs.clear();
			System.out.println("  _   _               \r\n" +
								" | | | |___  ___ _ __ \r\n" +
								" | | | / __|/ _ \\ '__|\r\n" +
								" | |_| \\__ \\  __/ |   \r\n" +
								"  \\___/|___/\\___|_|   \r\n");
			Usuario user = this.getUsuarioByCpf(Main.logado);
			System.out.println(cs.coloredText(cs.VERDE, "Bem-Vindo(a), ") + cs.coloredText(cs.AMARELO, user.getNome()) + "\n" +
								cs.coloredText(cs.VERDE, "Seu saldo: " + cs.coloredText(cs.AMARELO, "R$ " + Double.toString(saldo.getSaldo(user.getCpf())))));
			System.out.println(cs.coloredOption(1, "Biblioteca de jogos obtidos"));
			System.out.println(cs.coloredOption(2, "Comprar jogo"));
			System.out.println(cs.coloredOption(3, "Avaliar jogo"));
			System.out.println(cs.coloredOption(4, "Reembolsar jogo"));
			System.out.println(cs.coloredOption(5, "Depositar saldo"));
			System.out.println(cs.coloredOption(6, "Extrato financeiro"));
			System.out.println(cs.coloredOption(0, "Deslogar"));
			String opcaoUser = input.nextLine();
			switch (opcaoUser) {
			case "1":
				cs.clear();
				System.out.println("  ____  _ _     _ _       _                 \r\n"
						+ " | __ )(_) |__ | (_) ___ | |_ ___  ___ __ _ \r\n"
						+ " |  _ \\| | '_ \\| | |/ _ \\| __/ _ \\/ __/ _` |\r\n"
						+ " | |_) | | |_) | | | (_) | ||  __/ (_| (_| |\r\n"
						+ " |____/|_|_.__/|_|_|\\___/ \\__\\___|\\___\\__,_|\r\n");

				user.bibliotecaDeObtidos(user.getCpf());
				cs.restartMenu();
				break;
			case "2":
				cs.clear();
				System.out.println("   ____                                     \r\n"
						+ "  / ___|___  _ __ ___  _ __  _ __ __ _ _ __ \r\n"
						+ " | |   / _ \\| '_ ` _ \\| '_ \\| '__/ _` | '__|\r\n"
						+ " | |__| (_) | | | | | | |_) | | | (_| | |   \r\n"
						+ "  \\____\\___/|_| |_| |_| .__/|_|  \\__,_|_|   \r\n"
						+ "                      |_|                   \n");

				System.out.println(cs.coloredText(cs.VERDE, "Seu saldo: " + saldo.getSaldo(user.getCpf())));
				user.comprarJogo(user.getCpf());
				cs.restartMenu();
				break;
			case "3":
				cs.clear();
				System.out.println("     _             _ _            \r\n" +
									"    / \\__   ____ _| (_) __ _ _ __ \r\n" +
									"   / _ \\ \\ / / _` | | |/ _` | '__|\r\n" +
									"  / ___ \\ V / (_| | | | (_| | |   \r\n" +
									" /_/   \\_\\_/ \\__,_|_|_|\\__,_|_|   \r\n");

				user.avaliarJogo(user.getCpf());
				cs.restartMenu();
				break;
			case "4":
				cs.clear();
				System.out.println("  ____                     _           _           \r\n"
						+ " |  _ \\ ___  ___ _ __ ___ | |__   ___ | |___  ___  \r\n"
						+ " | |_) / _ \\/ _ \\ '_ ` _ \\| '_ \\ / _ \\| / __|/ _ \\ \r\n"
						+ " |  _ <  __/  __/ | | | | | |_) | (_) | \\__ \\ (_) |\r\n"
						+ " |_| \\_\\___|\\___|_| |_| |_|_.__/ \\___/|_|___/\\___/ \r\n");

				System.out.println(cs.coloredText(cs.VERDE, "Seu saldo: " + saldo.getSaldo(user.getCpf())));
				user.reembolsarJogo(user.getCpf());
				cs.restartMenu();
				break;
			case "5":
				cs.clear();
				System.out.println("  ____                       _ _        \r\n" + 
						" |  _ \\  ___ _ __   ___  ___(_) |_ ___  \r\n" + 
						" | | | |/ _ \\ '_ \\ / _ \\/ __| | __/ _ \\ \r\n" + 
						" | |_| |  __/ |_) | (_) \\__ \\ | || (_) |\r\n" + 
						" |____/ \\___| .__/ \\___/|___/_|\\__\\___/ \r\n" + 
						"            |_|                         ");
				deposito.depositar(user.getCpf());
				cs.restartMenu();
				break;
			case "6":
				cs.clear();
				System.out.println("  _____      _             _        \r\n" + 
						" | ____|_  _| |_ _ __ __ _| |_ ___  \r\n" + 
						" |  _| \\ \\/ / __| '__/ _` | __/ _ \\ \r\n" + 
						" | |___ >  <| |_| | | (_| | || (_) |\r\n" + 
						" |_____/_/\\_\\\\__|_|  \\__,_|\\__\\___/ \r\n" + 
						"                                    ");
				
				System.out.println(cs.coloredOption(1, "Compras"));
				System.out.println(cs.coloredOption(2, "Depositos"));
				System.out.println(cs.coloredOption(3, "Geral"));
				System.out.println(cs.coloredOption(0, "Voltar ao menu da sua conta"));
				String opcaoExtrato = input.nextLine();
				switch(opcaoExtrato) {
					case "1":
						getExtratoFinanceiro(user.getCpf(), 1);
						cs.restartMenu();
						break;
					case "2":
						getExtratoFinanceiro(user.getCpf(), 2);
						cs.restartMenu();
						break;
					case "3":
						getExtratoFinanceiro(user.getCpf(), 3);
						cs.restartMenu();
						break;
					case "0":
						break;
				}
				break;
			case "0":
				Main.usuario = false;
				break;
			}
		}
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
