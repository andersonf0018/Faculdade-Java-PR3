package Projeto.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Projeto.Contas.Verificacoes;

@SuppressWarnings("resource")
public class Jogo {

	public Arquivos arq = new Arquivos();
	public Console cs = new Console();
	public Verificacoes v = new Verificacoes();

	private int id;
	private String nome;
	private String desc;
	private double preco;
	private List<Integer> dataLancamento;
	private String desenvolvedor;
	private String distribuidora;
	private String genero;
	private double avaliacao;
	private int qtVotos;
	private List<String> requisitos;
	
	public Jogo() {}

	public Jogo(int id, String nome, String desc, double preco, List<Integer> dataLancamento, String desenvolvedor,
			String distribuidora, String genero, double avaliacao, int qtVotos, List<String> requisitos) {
		this.id = id;
		this.nome = nome;
		this.desc = desc;
		this.preco = preco;
		this.dataLancamento = dataLancamento;
		this.desenvolvedor = desenvolvedor;
		this.distribuidora = distribuidora;
		this.genero = genero;
		this.avaliacao = avaliacao;
		this.qtVotos = qtVotos;
		this.requisitos = requisitos;
	}

	public Jogo getJogoById(int id, String arquivo) {
		FileReader lerJogos;
		try {
			lerJogos = new FileReader(arquivo);
			BufferedReader leitorJogos = new BufferedReader(lerJogos);
			String linha;
			try {
				while ((linha = leitorJogos.readLine()) != null) {
					if (linha.split("-")[0].equalsIgnoreCase(Integer.toString(id))) {
						String[] infos = linha.split("-");
						String[] data = infos[4].split("/");
						String[] requisitos = infos[10].split("/");

						Jogo jogo = new Jogo(Integer.parseInt(infos[0]), infos[1], infos[2],
								Double.parseDouble(infos[3]),
								Arrays.asList(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
										Integer.parseInt(data[2])),
								infos[5], infos[6], infos[7], Double.parseDouble(infos[8]), Integer.parseInt(infos[9]),
								Arrays.asList(requisitos[0], requisitos[1], requisitos[2], requisitos[3],
										requisitos[4]));
						return jogo;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Jogo getJogoByName(String nome, String arquivo) {
		FileReader lerJogos;
		try {
			lerJogos = new FileReader(arquivo);
			BufferedReader leitorJogos = new BufferedReader(lerJogos);
			String linha;
			try {
				while ((linha = leitorJogos.readLine()) != null) {
					if (linha.split("-")[1].equalsIgnoreCase(nome)) {
						String[] infos = linha.split("-");
						String[] data = infos[4].split("/");
						String[] requisitos = infos[10].split("/");

						Jogo jogo = new Jogo(Integer.parseInt(infos[0]), infos[1], infos[2],
								Double.parseDouble(infos[3]),
								Arrays.asList(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])),
								infos[5], infos[6], infos[7], Double.parseDouble(infos[8]), Integer.parseInt(infos[9]),
								Arrays.asList(requisitos[0], requisitos[1], requisitos[2], requisitos[3],
										requisitos[4]));
						return jogo;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void adicionarJogo(Jogo jogo, String documento, int modo) {
		String arquivo = v.getPastaModo(modo) + "/" + documento + ".txt";
		String registro = jogo.getId() + "-" + jogo.getNome() + "-" + jogo.getDescricao() + "-" + jogo.getPreco() + "-"
				+ jogo.getLancamento() + "-" + jogo.getDesenvolvedor() + "-" + jogo.getDistribuidora() + "-"
				+ jogo.getGenero() + "-" + jogo.getAvalicao() + "-" + jogo.getQuantidadeVotos() + "-"
				+ jogo.getRequisitos() + "\n";
		try {
			FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(registro);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removerJogo(int id, String documento, int modo) {
		int index = 0;
		String arquivo = v.getPastaModo(modo) + "/" + documento + ".txt";
		ArrayList<String> listaJogos = new ArrayList<String>();
		arq.transformFileInList(arquivo, listaJogos);
		Jogo jogo = this.getJogoById(id, arquivo);
		try {
			if(jogo != null) {
				String jogoRemovido = jogo.getId() + "-" + jogo.getNome() + "-" + jogo.getDescricao() + "-" + jogo.getPreco() + "-"
						+ jogo.getLancamento() + "-" + jogo.getDesenvolvedor() + "-" + jogo.getDistribuidora() + "-"
						+ jogo.getGenero() + "-" + jogo.getAvalicao() + "-" + jogo.getQuantidadeVotos() + "-"
						+ jogo.getRequisitos();
				
				listaJogos.remove(jogoRemovido);
				
				FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				while (index < listaJogos.size()) {
					bw.write(listaJogos.get(index) + "\n");
					index++;
				}
				bw.close();
			}
		} catch (Exception e) {
			System.out.println();
		}
	}
	
	public boolean contemJogo(int id, String arquivo) {
		int index = 0;
		boolean existe = false;
		ArrayList<String> jogos = new ArrayList<String>();
		arq.transformFileInList(arquivo, jogos);
		while(index < jogos.size()) {
			String idSearch = jogos.get(index).split("-")[0];
			if(idSearch.equals(Integer.toString(id))) {
				existe = true;
				break;
			}
			index++;
		}
		return existe;
	}

	public void atualizarJogo(Jogo jogo, String arquivo) {
		int index = 0;
		List<String> listaJogos = new ArrayList<String>();
		FileReader lerJogos;
		try {
			lerJogos = new FileReader(arquivo);
			BufferedReader leitorJogos = new BufferedReader(lerJogos);
			String linha;
			try {
				while ((linha = leitorJogos.readLine()) != null) {
					if (!linha.split("-")[0].equalsIgnoreCase(Integer.toString(jogo.getId()))) {
						listaJogos.add(linha);
					}
				}
				String jogoAlterado = jogo.getId() + "-" + jogo.getNome() + "-" + jogo.getDescricao() + "-"
						+ jogo.getPreco() + "-" + jogo.getLancamento() + "-" + jogo.getDesenvolvedor() + "-"
						+ jogo.getDistribuidora() + "-" + jogo.getGenero() + "-" + jogo.getAvalicao() + "-"
						+ jogo.getQuantidadeVotos() + "-" + jogo.getRequisitos();
				listaJogos.add(jogoAlterado);
				FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				while (index < listaJogos.size()) {
					bw.write(listaJogos.get(index) + "\n");
					index++;
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void listarJogos() {
		ArrayList<String> jogos = new ArrayList<String>();
		arq.transformFileInList("jogos.txt", jogos);
		if(jogos.size() == 0) {
			System.out.println("\n" + cs.coloredText(cs.VERMELHO, "Não existe nenhum jogo publicado!"));
		} else {
			for(int index = 0; index < jogos.size(); index++) {
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

	public int getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	public String getDescricao() {
		return this.desc;
	}

	public double getPreco() {
		return this.preco;
	}

	public String getLancamento() {
		return this.dataLancamento.get(0).toString() + "/" + this.dataLancamento.get(1).toString() + "/"
				+ this.dataLancamento.get(2).toString();
	}

	public String getDesenvolvedor() {
		return this.desenvolvedor;
	}

	public String getDistribuidora() {
		return this.distribuidora;
	}

	public String getGenero() {
		return this.genero;
	}

	public double getAvalicao() {
		return this.avaliacao;
	}

	public int getQuantidadeVotos() {
		return this.qtVotos;
	}

	public String getRequisitos() {
		return this.requisitos.get(0) + "/" + this.requisitos.get(1) + "/" + this.requisitos.get(2) + "/"
				+ this.requisitos.get(3) + "/" + this.requisitos.get(4);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescricao(String descricao) {
		this.desc = descricao;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public void setLancamento(int dia, int mes, int ano) {
		List<Integer> data = new ArrayList<Integer>();
		data.clear();
		data.add(dia);
		data.add(mes);
		data.add(ano);
		this.dataLancamento = data;
	}

	public void setDesenvolvedor(String desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}

	public void setDesenvolvedora(String distribuidora) {
		this.distribuidora = distribuidora;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public void setAvaliacao(double nota) {
		this.avaliacao = nota;
	}

	public void setQuantidadeVotos(int qt_votos) {
		this.qtVotos = qt_votos;
	}

	public void setRequisitos(String processador, String ram, String placa_de_video, String armazenamento,
			String sistema_operacional) {
		List<String> requisitos = new ArrayList<String>();
		requisitos.clear();
		requisitos.add(processador);
		requisitos.add(ram);
		requisitos.add(placa_de_video);
		requisitos.add(armazenamento);
		requisitos.add(sistema_operacional);
		this.requisitos = requisitos;
	}

	public String formatarRequisitos() {
		return cs.coloredText(cs.AMARELO, "Processador: ") + this.requisitos.get(0) + "\n"
				+ cs.coloredText(cs.AMARELO, "Memória RAM: ") + this.requisitos.get(1) + "\n"
				+ cs.coloredText(cs.AMARELO, "Placa de vídeo: ") + this.requisitos.get(2) + "\n"
				+ cs.coloredText(cs.AMARELO, "Armazenamento: ") + this.requisitos.get(3) + "\n"
				+ cs.coloredText(cs.AMARELO, "Sistema Operacional: ") + this.requisitos.get(4);
	}
}
