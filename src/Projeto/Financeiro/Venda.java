package Projeto.Financeiro;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Projeto.Config.Arquivos;

public class Venda {

	public Arquivos arq = new Arquivos();
	public String arquivo = "financeiro/vendas.txt";
	
	private int id;
	private Date data;
	private String cpfComprador;
	private String vendedor;
	private String nomeJogo;
	private double valor;
	private boolean possui;
	
	public Venda() {}
	
	public Venda(int id, Date data, String cpfComprador, String vendedor, String nomeJogo, double valor, boolean possui) {
		this.id = id;
		this.data = data;
		this.cpfComprador = cpfComprador;
		this.vendedor = vendedor;
		this.nomeJogo = nomeJogo;
		this.valor = valor;
		this.possui = possui;
	}
	
	
	public Venda getVendaById(int id) {
		FileReader lerVendas;
		try {
			lerVendas = new FileReader(arquivo);
			BufferedReader leitorVendas = new BufferedReader(lerVendas);
			String linha;
			try {
				while ((linha = leitorVendas.readLine()) != null) {
					if (linha.split("-")[0].equalsIgnoreCase(Integer.toString(id))) {
						try {
							String[] infos = linha.split("-");
							Venda venda = new Venda(
									Integer.parseInt(infos[0]), 
									new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(infos[1]), 
									infos[2], infos[3], infos[4], 
									Double.parseDouble(infos[5]),
									Boolean.parseBoolean(infos[6]));
							return venda;
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
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
	
	public Venda getVendaByCpf(String cpf) {
		FileReader lerVendas;
		try {
			lerVendas = new FileReader(arquivo);
			BufferedReader leitorVendas = new BufferedReader(lerVendas);
			String linha;
			try {
				while ((linha = leitorVendas.readLine()) != null) {
					if (linha.split("-")[2].equalsIgnoreCase(cpf)) {
						try {
							String[] infos = linha.split("-");
							Venda venda = new Venda(
									Integer.parseInt(infos[0]), 
									new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(infos[1]), 
									infos[2], infos[3], infos[4], 
									Double.parseDouble(infos[5]),
									Boolean.parseBoolean(infos[6]));
							return venda;
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
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

	public void registrarVenda(Venda venda) {
		try {
			FileWriter fw = new FileWriter(new File(arquivo).getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(venda.getId() + "-" 
					+ arq.getDateTime(venda.getData()) + "-" 
					+ venda.getCpfComprador() + "-" 
					+ venda.getVendedor() + "-" 
					+ venda.getNomeJogo() + "-" 
					+ venda.getValor() +  "-" 
					+ venda.getPossui() + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setRefund(String id) {
		int index = 0;
		ArrayList<String> vendas = new ArrayList<String>();
		arq.transformFileInList("financeiro/vendas.txt", vendas);
		Venda venda = this.getVendaById(Integer.parseInt(id));
		while(index < vendas.size()) {
			if(vendas.get(index).split("-")[0].equals(id)) {
				vendas.remove(index);
				break;
			}
		}
		FileWriter fw;
		try {
			String atualizado = venda.getId() + "-" 
					+ arq.getDateTime(venda.getData()) + "-" 
					+ venda.getCpfComprador() + "-" 
					+ venda.getVendedor() + "-" 
					+ venda.getNomeJogo() + "-" 
					+ venda.getValor() +  "-" 
					+ false;
			vendas.add(atualizado);
			fw = new FileWriter(new File("financeiro/vendas.txt").getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < vendas.size(); i++) {
				bw.write(vendas.get(i) + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String isRefund() {
		if(getPossui() == false) {
			return "REEMBOLSADO";
		} else {
			return "";
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
	
	public String getVendedor() {
		return vendedor;
	}
	
	public String getNomeJogo() {
		return nomeJogo;
	}
	
	public double getValor() {
		return valor;
	}
	
	public boolean getPossui() {
		return possui;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setData(Date data) {
		this.data = data;
	}

	public void setCpfComprador(String cpfComprador) {
		this.cpfComprador = cpfComprador;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public void setNomeJogo(String nomeJogo) {
		this.nomeJogo = nomeJogo;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
}
