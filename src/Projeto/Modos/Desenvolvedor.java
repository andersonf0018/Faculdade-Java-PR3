package Projeto.Modos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Projeto.Main;
import Projeto.Config.Arquivos;
import Projeto.Config.Console;
import Projeto.Config.Jogo;
import Projeto.Contas.Verificacoes;

public class Desenvolvedor {
	
	public Scanner input = new Scanner(System.in);
	
	public Arquivos arq = new Arquivos();
	public Console cs = new Console();
	public Jogo j = new Jogo();
	public Verificacoes v = new Verificacoes();
	
	private String cnpj;
	private String nome;
	private String email;
	private String senha;
	private double lucro;
	private int quantidadeVendidos;
	private int quantidadeReembolsado;

	public Desenvolvedor() {}
	
	public Desenvolvedor(String cnpj, String nome, String email, String senha, double lucro, int quantidadeVendidos, int quantidadeReembolsado) {
		this.cnpj = cnpj;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.lucro = lucro;
		this.quantidadeVendidos = quantidadeVendidos;
		this.quantidadeReembolsado = quantidadeReembolsado;
	}
	
	public Desenvolvedor getDevByCnpj(String cnpj) {
		if(v.contaJaCriada(cnpj, 2)) {
			ArrayList<String> devLinha = new ArrayList<String>();
			arq.transformFileInList("desenvolvedores/" + cnpj + ".txt", devLinha);
			String[] infos = devLinha.get(0).split("-");
			Desenvolvedor dev = new Desenvolvedor(infos[0], infos[1], infos[2], infos[3],
						Double.parseDouble(infos[4]), Integer.parseInt(infos[5]),
						Integer.parseInt(infos[6]));
			return dev;
		} else {
			return null;
		}
	}
	
	
	public Desenvolvedor getDevByName(String nome) {
		int index = 0;
		File pasta = new File("desenvolvedores");
		File[] lista = pasta.listFiles();
		ArrayList<String> arquivos = new ArrayList<String>();
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].isFile()) {
				arquivos.add(lista[i].getName());
			}
		} 
		while(index < arquivos.size()) {
			Desenvolvedor dev = this.getDevByCnpj(arquivos.get(index).replaceAll(".txt", ""));
			if(dev.getNome().equalsIgnoreCase(nome)) {
				return dev;
			}
			index++;
		}
		return null;
	}
	
	public boolean desenvolvedorJaExiste(String nome) {
		int index = 0;
		boolean existe = false;
		File pasta = new File("desenvolvedores");
		File[] lista = pasta.listFiles();
		ArrayList<String> arquivos = new ArrayList<String>();
		for(int i = 0; i < lista.length; i++) {
			if(lista[i].isFile()) {
				arquivos.add(lista[i].getName());
			}
		}
		while(index < arquivos.size()) {
			Desenvolvedor dev = this.getDevByCnpj(arquivos.get(index));
			if(dev.getNome().equalsIgnoreCase(nome)) {
				existe = true;
				break;
			}
			index++;
		}
		return existe;
	}

	public void publicarJogo(Jogo jogo) {
		try {
			FileWriter fw = new FileWriter(new File("jogos.txt").getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			String registro = jogo.getId() + "-" + jogo.getNome() + "-" + jogo.getDescricao() + "-" + jogo.getPreco() + "-"
					+ jogo.getLancamento() + "-" + jogo.getDesenvolvedor() + "-" + jogo.getDistribuidora() + "-"
					+ jogo.getGenero() + "-" + jogo.getAvalicao() + "-" + jogo.getQuantidadeVotos() + "-"
					+ jogo.getRequisitos() + "\n";
			bw.write(registro);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void retirarJogo(int id) {
		int index = 0;
		String arquivoGeral = "jogos.txt";
		ArrayList<String> listaJogosGeral = new ArrayList<String>();
		arq.transformFileInList(arquivoGeral, listaJogosGeral);
		Jogo jogo = j.getJogoById(id, arquivoGeral);
		try {
			if(jogo != null) {
				String jogoRemovido = jogo.getId() + "-" + jogo.getNome() + "-" + jogo.getDescricao() + "-" + jogo.getPreco() + "-"
						+ jogo.getLancamento() + "-" + jogo.getDesenvolvedor() + "-" + jogo.getDistribuidora() + "-"
						+ jogo.getGenero() + "-" + jogo.getAvalicao() + "-" + jogo.getQuantidadeVotos() + "-"
						+ jogo.getRequisitos();
				
				listaJogosGeral.remove(jogoRemovido);
				
				FileWriter fw = new FileWriter(new File(arquivoGeral).getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				while (index < listaJogosGeral.size()) {
					bw.write(listaJogosGeral.get(index) + "\n");
					index++;
				}
				bw.close();
            	System.out.println(cs.coloredText(cs.AMARELO, "Jogo '" + jogo.getNome() + "' removido com sucesso!"));
			} else {
				System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Você não possui esse jogo publicado!\n"));
			}
		} catch (Exception e) {
			System.out.println();
		}
	}
	
	public void listarSeusJogos(String cnpj) {
		ArrayList<String> jogos = new ArrayList<String>();
		arq.transformFileInList("desenvolvedores/" + cnpj + ".txt", jogos);
		if(jogos.size() <= 1) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Você não possui nenhum jogo publicado!\n"));
		} else {
			System.out.println(cs.coloredText(cs.BRANCO, "Você possui " + (jogos.size() - 1) + " jogo(s) publicado(s)!"));
			for(int index = 1; index < jogos.size(); index++) {
				String[] infos = jogos.get(index).split("-");
				String[] data = infos[4].split("/");
				String[] requisitos = infos[10].split("/");
				Jogo jogo = new Jogo(Integer.parseInt(infos[0]), infos[1], infos[2], Double.parseDouble(infos[3]),
						Arrays.asList(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])),
						infos[5], infos[6], infos[7], Double.parseDouble(infos[8]), Integer.parseInt(infos[9]),
						Arrays.asList(requisitos[0], requisitos[1], requisitos[2], requisitos[3], requisitos[4]));
				System.out.println("=================================\n" 
						+ cs.coloredText(cs.AMARELO, "ID: ") + jogo.getId()
						+ "\n" + cs.coloredText(cs.AMARELO, "Nome: ") + jogo.getNome() + "\n"
						+ cs.coloredText(cs.AMARELO, "Descrição: ") + jogo.getDescricao() + "\n"
						+ cs.coloredText(cs.AMARELO, "Preço: ") + cs.formatPrice(jogo.getPreco()) + "\n"
						+ cs.coloredText(cs.AMARELO, "Data de lançamento: ") + jogo.getLancamento() + "\n"
						+ cs.coloredText(cs.AMARELO, "Desenvolvedor: ") + jogo.getDesenvolvedor() + "\n"
						+ cs.coloredText(cs.AMARELO, "Distribuidora: ") + jogo.getDistribuidora() + "\n"
						+ cs.coloredText(cs.AMARELO, "Gênero: ") + jogo.getGenero() + "\n"
						+ cs.coloredText(cs.AMARELO, "Avaliações: ") + jogo.getAvalicao() + "\n"
						+ cs.coloredText(cs.AMARELO, "Quantidade de votos: ") + jogo.getQuantidadeVotos() + "\n"
						+ "------ Requisitos ------" + "\n" + jogo.formatarRequisitos() + "\n");
			}
		}
	}

	public void pesquisarJogo(String id) {
		ArrayList<String> jogos = new ArrayList<String>();
		arq.transformFileInList("jogos.txt", jogos);
		boolean encontrado = false;
		for(int index = 0; index < jogos.size(); index++) {
			String nomeProcurado = jogos.get(index).split("-")[0];
			if(nomeProcurado.equalsIgnoreCase(id)) {
				Jogo jogo = j.getJogoById(Integer.parseInt(id), "jogos.txt");
				encontrado = true;
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
						+ "------ Requisitos ------" + "\n" 
						+ jogo.formatarRequisitos() + "\n");
			}
		}
		if(encontrado == false) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Não existe nenhum jogo publicado com esse nome!"));
		}
	}
	
	public void atualizarFinanceiro(String cnpj, double lucro, int qtVendidos, int qtReembolsados) {
		Desenvolvedor desenvolvedor = this.getDevByCnpj(cnpj);
		ArrayList<String> dev = new ArrayList<String>();
		arq.transformFileInList("desenvolvedores/" + cnpj + ".txt", dev);
		FileWriter fw;
		try {
			ArrayList<String> outros = new ArrayList<String>();
			String[] infos = dev.get(0).split("-");
			dev.remove(0);
			for(int i = 0; i < dev.size(); i++) {
				outros.add(dev.get(i));
			}
			fw = new FileWriter(new File("desenvolvedores/" + cnpj + ".txt").getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			infos[4] = Double.toString(desenvolvedor.getLucro() + lucro);
			infos[5] = Integer.toString(desenvolvedor.getQuantidadeVendidos() + qtVendidos);
			infos[6] = Integer.toString(desenvolvedor.getQuantidadeReembolsado() + qtReembolsados);
			bw.write(infos[0] + "-" + infos[1] + "-" + infos[2] + "-" + infos[3] + "-" + infos[4] + "-" + infos[5] + "-" + infos[6] + "\n");
			for(int i = 0; i < outros.size(); i++) {
				bw.write(outros.get(i) + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String pontuacao(int vendas, int reembolsos) {
		int p = vendas - reembolsos;
		if((p >= 0 && p < 5) || (p < 0)) {
			return cs.coloredText(cs.VERMELHO, "Sua pontuação está baixa! Publique mais jogos para aumenta-la.");
		} else if(p >= 5 && p < 10) {
			return cs.coloredText(cs.AZUL, "Sua pontuação está mediana! Se esforce um pouco mais!");
		} else {
			return cs.coloredText(cs.VERDE, "Sua pontuação está excelente! Continue nesse ritmo!");
		}
	}
	
	public void informacoesComerciais(String cnpj) {
		Desenvolvedor dev = this.getDevByCnpj(cnpj);
		int vendas = dev.getQuantidadeVendidos();
		int reembolsos = dev.getQuantidadeReembolsado();
		System.out.println(cs.coloredText(cs.AMARELO, "Pontuação: ") + (vendas - reembolsos));
		System.out.println(pontuacao(vendas, reembolsos));
		System.out.println(cs.coloredText(cs.AMARELO, "Seu lucro: ") + "R$ " + dev.getLucro());
		System.out.println(cs.coloredText(cs.AMARELO, "Quantidade de vendas: ") + vendas);
		System.out.println(cs.coloredText(cs.AMARELO, "Quantidade de reembolsos: ") + reembolsos);
	}
	
	public void menuDev() {
		while(Main.desenvolvedor) {
			cs.clear();  
	        System.out.println(
	        		"  ____             \r\n" + 
	        		" |  _ \\  _____   __\r\n" + 
	        		" | | | |/ _ \\ \\ / /\r\n" + 
	        		" | |_| |  __/\\ V / \r\n" + 
	        		" |____/ \\___| \\_/  \r\n");
	        
	        Desenvolvedor dev = this.getDevByCnpj(Main.logado);
	        System.out.println(cs.coloredText(cs.VERDE, "Bem-Vindo(a), ") + cs.coloredText(cs.AMARELO, dev.getNome()));
	        System.out.println(cs.coloredOption(1, "Adicione jogos"));
	        System.out.println(cs.coloredOption(2, "Remover jogos"));
	        System.out.println(cs.coloredOption(3, "Seus jogos"));
	        System.out.println(cs.coloredOption(4, "Listar jogos"));
	        System.out.println(cs.coloredOption(5, "Pesquisar jogo"));
	        System.out.println(cs.coloredOption(6, "Informações comerciais"));
	        System.out.println(cs.coloredOption(0, "Deslogar"));
	        
	        String opcaoDev = input.nextLine();
	        switch (opcaoDev) {
	            case "1":
	            	cs.clear();
	            	try {
	            		System.out.println(
	            				"     _       _ _      _                        \r\n" + 
	            				"    / \\   __| (_) ___(_) ___  _ __   __ _ _ __ \r\n" + 
	            				"   / _ \\ / _` | |/ __| |/ _ \\| '_ \\ / _` | '__|\r\n" + 
	            				"  / ___ \\ (_| | | (__| | (_) | | | | (_| | |   \r\n" + 
	            				" /_/   \\_\\__,_|_|\\___|_|\\___/|_| |_|\\__,_|_|   \r\n");
	            		System.out.println("Nome do Jogo:");
	                    String nome = input.nextLine();
	                    System.out.println("Descrição do Jogo:");
	                    String descricao = input.nextLine();
	                    System.out.println("Preço do Jogo (R$):");
	                    double preco = input.nextDouble();
	                    System.out.println("===== Data de lançamento =====");
	                    System.out.println("Dia:");                    
	                    int dia = input.nextInt();
	                    System.out.println("Mês:");                    
	                    int mes = input.nextInt();                   
	                    System.out.println("Ano:");
	                    int ano = input.nextInt();
	                    input.nextLine(); //Fix do problema de pular a linha do scanner
	                    System.out.println("Distribuidora:");
	                    String distribuidora = input.nextLine();
	                    System.out.println("Gênero:");
	                    String genero = input.nextLine();
	                    System.out.println("===== Requisitos ===== ");
	                    System.out.println("Processador:");
	                    String processador = input.nextLine();
	                    System.out.println("Memória RAM:");
	                    String RAM = input.nextLine();
	                    System.out.println("Placa de Vídeo:");     
	                    String video = input.nextLine();                    
	                    System.out.println("Armazenamento:");          
	                    String armazenamento = input.nextLine();            
	                    System.out.println("Sistema operacional:");    
	                    String SO = input.nextLine();                      
	                    
	                    Desenvolvedor desenvolvedor = this.getDevByCnpj(Main.logado);
	                    Jogo jogo = new Jogo(arq.gerarID("jogos.txt"), nome, descricao, preco, Arrays.asList(dia, mes, ano), desenvolvedor.getNome(),
	                            distribuidora, genero, 0, 0, Arrays.asList(processador, RAM, video, armazenamento, SO));
	                    System.out.println(cs.coloredText(cs.AMARELO, "\nJogo '" + jogo.getNome() + "'adicionado com sucesso!"));
	                    publicarJogo(jogo);
	                    j.adicionarJogo(jogo, desenvolvedor.getCNPJ(), 2);
	            	} catch(Exception e) {
	            		System.out.println("\nOcorreu um erro durante o processo! Tente novamente!\n");
	            	}
	                cs.restartMenu();
	                break;
	            case "2":
	            	cs.clear();
	            	System.out.println(
	            			"  ____                                    \r\n" + 
	            			" |  _ \\ ___ _ __ ___   _____   _____ _ __ \r\n" + 
	            			" | |_) / _ \\ '_ ` _ \\ / _ \\ \\ / / _ \\ '__|\r\n" + 
	            			" |  _ <  __/ | | | | | (_) \\ V /  __/ |   \r\n" + 
	            			" |_| \\_\\___|_| |_| |_|\\___/ \\_/ \\___|_|   \r\n");
	            	
	            	try {
	            		Desenvolvedor desenvolvedor = this.getDevByCnpj(Main.logado);
	            		System.out.println("Insira o ID do jogo a ser removido: ");
	                	int id = input.nextInt();
	                	retirarJogo(id);
	                	j.removerJogo(id, desenvolvedor.getCNPJ(), 2);
	            	} catch (Exception e) {
	            		System.out.println("\nID inválida! Tente novamente!\n");
	            	}
	            	cs.restartMenu();
	                break;
	            case "3":
	            	cs.clear();
	            	System.out.println(
	            			"  _     _     _        \r\n" + 
	            			" | |   (_)___| |_ __ _ \r\n" + 
	            			" | |   | / __| __/ _` |\r\n" + 
	            			" | |___| \\__ \\ || (_| |\r\n" + 
	            			" |_____|_|___/\\__\\__,_|\r\n");
	            	
	            	Desenvolvedor desenvolvedor = this.getDevByCnpj(Main.logado);
	                listarSeusJogos(desenvolvedor.getCNPJ());
	                cs.restartMenu();
	                break;
	            case "4":
	            	cs.clear();
	            	System.out.println(
	            			"  _     _     _        \r\n" + 
	            			" | |   (_)___| |_ __ _ \r\n" + 
	            			" | |   | / __| __/ _` |\r\n" + 
	            			" | |___| \\__ \\ || (_| |\r\n" + 
	            			" |_____|_|___/\\__\\__,_|\r\n");
	            	
	                j.listarJogos();
	                cs.restartMenu();
	                break;
	            case "5":
	            	cs.clear();
	            	System.out.println(
	            			"  ____                       _                \r\n" + 
	            			" |  _ \\ ___  ___  __ _ _   _(_)___  __ _ _ __ \r\n" + 
	            			" | |_) / _ \\/ __|/ _` | | | | / __|/ _` | '__|\r\n" + 
	            			" |  __/  __/\\__ \\ (_| | |_| | \\__ \\ (_| | |   \r\n" + 
	            			" |_|   \\___||___/\\__, |\\__,_|_|___/\\__,_|_|   \r\n" + 
	            			"                    |_|                       \n");
	            	
	            	System.out.println("Digite o ID do jogo: ");
	            	String id = input.nextLine();
	            	pesquisarJogo(id);
	                cs.restartMenu();
	                break;
	            case "6":
	            	cs.clear();
	            	System.out.println("  _____     _        _   _     _   _               \r\n" + 
	            			" | ____|___| |_ __ _| |_(_)___| |_(_) ___ __ _ ___ \r\n" + 
	            			" |  _| / __| __/ _` | __| / __| __| |/ __/ _` / __|\r\n" + 
	            			" | |___\\__ \\ || (_| | |_| \\__ \\ |_| | (_| (_| \\__ \\\r\n" + 
	            			" |_____|___/\\__\\__,_|\\__|_|___/\\__|_|\\___\\__,_|___/\r\n");
	            	informacoesComerciais(dev.getCNPJ());
	            	cs.restartMenu();
	            	break;
	            case "0":
	            	Main.desenvolvedor = false;
	                break;
	        }
		}
	}

	public String getNome() {
		return nome;
	}

	public String getCNPJ() {
		return cnpj;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}
	
	public double getLucro() {
		return lucro;
	}
	
	public int getQuantidadeVendidos() {
		return quantidadeVendidos;
	}
	
	public int getQuantidadeReembolsado() {
		return quantidadeReembolsado;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCNPJ(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setLucro(double lucro) {
		this.lucro = lucro;
	}
	
	public void setQuantidadeVendidos(int quantidadeVendidos) {
		this.quantidadeVendidos = quantidadeVendidos;
	}
	
	public void setQuantidadeReembolsado(int quantidadeReembolsado) {
		this.quantidadeReembolsado = quantidadeReembolsado;
	}
}
