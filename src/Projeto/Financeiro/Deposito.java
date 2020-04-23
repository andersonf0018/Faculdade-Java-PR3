package Projeto.Financeiro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Projeto.Config.Arquivos;
import Projeto.Config.Console;

public class Deposito {

	public Arquivos arq = new Arquivos();
	public Console cs = new Console();
	public Saldo saldo = new Saldo();

	private int id;
	private Date data;
	String cpfComprador;
	private double valor;
	private int metodo;

	public Deposito() {}

	public Deposito(int id, Date data, String cpfComprador, double valor, int metodo) {
		this.id = id;
		this.data = data;
		this.cpfComprador = cpfComprador;
		this.valor = valor;
		this.metodo = metodo;
	}

	@SuppressWarnings({ "resource", "unused" })
	public void depositar(String cpf) {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nDigite o valor que você deseja depositar: ");
		try {
			int valor = sc.nextInt();
			if (valor > 0) {
				System.out.println("Qual método de pagamento você deseja realizar o deposito?\n"
						+ cs.coloredOption(1, "Cartão de crédito") + "\n" + cs.coloredOption(2, "Boleto"));
				int metodo = sc.nextInt();
				//Cartão de crédito
				if (metodo == 1) {
					String numero;
					String nomeTitular;
					int mes;
					int ano;
					String CVV;
					sc.nextLine();
					System.out.println("\nDigite o número do cartão de crédito (16 dígitos): ");
					numero = sc.nextLine();
					if((cs.isDigit(numero)) && (numero.length() == 16)) {
						System.out.println("Digite o nome do titular do cartão: ");
						nomeTitular = sc.nextLine();
						System.out.println("Digite a data de vencimento do cartão");
						try {
							System.out.println("- Mês: ");
							mes = sc.nextInt();
							System.out.println("- Ano: ");
							ano = sc.nextInt();
							Date dataAtual = new Date();
							Date dataVencimento = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + mes + "/" + ano);
							if(dataAtual.compareTo(dataVencimento) > 0) {
								System.out.println(cs.coloredText(cs.VERMELHO, "\nEsse cartão de crédito está vencido!"));
							} else {
								sc.nextLine();
								System.out.println("Digite o CVV do cartão (3 dígitos): ");
								CVV = sc.nextLine();
								if((cs.isDigit(CVV)) && (CVV.length() == 3)) {
									String arquivo = "financeiro/depositos.txt";
									int id = Nota.gerarIdNota();
									Deposito deposito = new Deposito(id, new Date(), cpf, valor, 1);
									Nota nota = new Nota(id, cpf, "Deposito", valor, arq.formatarMetodo(metodo));
									System.out.println(cs.coloredText(cs.VERDE, "\nDeposito de R$" + valor + " realizado com sucesso!"));
									System.out.println(cs.coloredText(cs.AMARELO, "ID da transação - " + nota.getIdTransacao()));
									System.out.println(cs.coloredText(cs.VERDE, "Nota fiscal gerada!\n"));
									saldo.addSaldo(cpf, valor);
									Nota.gerarNota(nota);
									try {
										FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile(), true);
										BufferedWriter bw = new BufferedWriter(fw);
										bw.write(deposito.getId() + "-" + arq.getDateTime(deposito.getData()) + "-"
												+ deposito.getCpfComprador() + "-" + deposito.getValor() + "-" + deposito.getMetodo() + "\n");
										bw.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									System.out.println(cs.coloredText(cs.VERMELHO, "\nCVV inválido!"));
								}
							}
						} catch(Exception e) {
							System.out.println(cs.coloredText(cs.VERMELHO, "Data inválida!"));
							e.printStackTrace();
						}
					} else {
						System.out.println(cs.coloredText(cs.VERMELHO, "Número de cartão inválido"));
					}
				//Boleto
				} else if (metodo == 2) {
					String arquivo = "financeiro/depositos.txt";
					int id = Nota.gerarIdNota();
					Deposito deposito = new Deposito(id, new Date(), cpf, valor, 2);
					Nota nota = new Nota(id, cpf, "Deposito", valor, arq.formatarMetodo(metodo));
					System.out.println(cs.coloredText(cs.VERDE, "\nDeposito de R$" + valor + " realizado com sucesso!"));
					System.out.println(cs.coloredText(cs.AMARELO, "ID da transação - " + nota.getIdTransacao()));
					System.out.println(cs.coloredText(cs.VERDE, "Nota fiscal gerada!\n"));
					saldo.addSaldo(cpf, valor);
					Nota.gerarNota(nota);
					try {
						FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile(), true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(deposito.getId() + "-" + arq.getDateTime(deposito.getData()) + "-"
								+ deposito.getCpfComprador() + "-" + deposito.getValor() + "-" + deposito.getMetodo() + "\n");
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(cs.coloredText(cs.VERMELHO, "\nMetodo de pagamento inválido!"));
				}
			} else {
				System.out.println(cs.coloredText(cs.VERMELHO, "\nValor inválido!"));
			}
		} catch (Exception e) {
			System.out.println(cs.coloredText(cs.VERMELHO, "\nOcorreu um erro!"));
		}
	}

	public int getId() {
		return id;
	}

	public Date getData() {
		return data;
	}

	public String getCpfComprador() {
		return cpfComprador;
	}

	public double getValor() {
		return valor;
	}

	public int getMetodo() {
		return metodo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setCpfComprador(String cpf) {
		this.cpfComprador = cpf;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setMetodo(int metodo) {
		this.metodo = metodo;
	}
}
