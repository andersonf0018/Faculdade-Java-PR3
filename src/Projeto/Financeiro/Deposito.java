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
		System.out.println("\nDigite o valor que voc� deseja depositar: ");
		try {
			int valor = sc.nextInt();
			if (valor > 0) {
				System.out.println("Qual m�todo de pagamento voc� deseja realizar o deposito?\n"
						+ cs.coloredOption(1, "Cart�o de cr�dito") + "\n" + cs.coloredOption(2, "Boleto"));
				int metodo = sc.nextInt();
				//Cart�o de cr�dito
				if (metodo == 1) {
					String numero;
					String nomeTitular;
					int mes;
					int ano;
					String CVV;
					sc.nextLine();
					System.out.println("\nDigite o n�mero do cart�o de cr�dito (16 d�gitos): ");
					numero = sc.nextLine();
					if((cs.isDigit(numero)) && (numero.length() == 16)) {
						System.out.println("Digite o nome do titular do cart�o: ");
						nomeTitular = sc.nextLine();
						System.out.println("Digite a data de vencimento do cart�o");
						try {
							System.out.println("- M�s: ");
							mes = sc.nextInt();
							System.out.println("- Ano: ");
							ano = sc.nextInt();
							Date dataAtual = new Date();
							Date dataVencimento = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + mes + "/" + ano);
							if(dataAtual.compareTo(dataVencimento) > 0) {
								System.out.println(cs.coloredText(cs.VERMELHO, "\nEsse cart�o de cr�dito est� vencido!"));
							} else {
								sc.nextLine();
								System.out.println("Digite o CVV do cart�o (3 d�gitos): ");
								CVV = sc.nextLine();
								if((cs.isDigit(CVV)) && (CVV.length() == 3)) {
									String arquivo = "financeiro/depositos.txt";
									int id = Nota.gerarIdNota();
									Deposito deposito = new Deposito(id, new Date(), cpf, valor, 1);
									Nota nota = new Nota(id, cpf, "Deposito", valor, arq.formatarMetodo(metodo));
									System.out.println(cs.coloredText(cs.VERDE, "\nDeposito de R$" + valor + " realizado com sucesso!"));
									System.out.println(cs.coloredText(cs.AMARELO, "ID da transa��o - " + nota.getIdTransacao()));
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
									System.out.println(cs.coloredText(cs.VERMELHO, "\nCVV inv�lido!"));
								}
							}
						} catch(Exception e) {
							System.out.println(cs.coloredText(cs.VERMELHO, "Data inv�lida!"));
							e.printStackTrace();
						}
					} else {
						System.out.println(cs.coloredText(cs.VERMELHO, "N�mero de cart�o inv�lido"));
					}
				//Boleto
				} else if (metodo == 2) {
					String arquivo = "financeiro/depositos.txt";
					int id = Nota.gerarIdNota();
					Deposito deposito = new Deposito(id, new Date(), cpf, valor, 2);
					Nota nota = new Nota(id, cpf, "Deposito", valor, arq.formatarMetodo(metodo));
					System.out.println(cs.coloredText(cs.VERDE, "\nDeposito de R$" + valor + " realizado com sucesso!"));
					System.out.println(cs.coloredText(cs.AMARELO, "ID da transa��o - " + nota.getIdTransacao()));
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
					System.out.println(cs.coloredText(cs.VERMELHO, "\nMetodo de pagamento inv�lido!"));
				}
			} else {
				System.out.println(cs.coloredText(cs.VERMELHO, "\nValor inv�lido!"));
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
