package Projeto.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Arquivos {

	public Arquivos() {}

	public void criarArquivo(String nome, String tipo) {
		File file = new File(nome + "." + tipo);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void criarPasta(String nome) {
		File file = new File(nome);
		if(!file.exists()) {
			file.mkdir();
		}
	}

	public int gerarID(String arquivo) {
		int id;
		ArrayList<String> ids = new ArrayList<String>();
		int rotacao = 0;
		int ultimo = 0;
		transformFileInList(arquivo, ids);
		if (ids.size() > 0) {
			while(rotacao < ids.size()) {
				String lastID = ids.get(rotacao).split("-")[0];
				if(Integer.parseInt(lastID) > ultimo) {
					ultimo = Integer.parseInt(lastID);
				}
				rotacao++;
			}
			id = ultimo + 1;
			return id;
		} else {
			return 1;
		}
	}
	
	public void transformFileInList(String arquivo, ArrayList<String> lista) {
		FileReader ler;
		try {
			ler = new FileReader(arquivo);
			BufferedReader leitor = new BufferedReader(ler);
			String linha;
			try {
				while((linha = leitor.readLine()) != null) {
					lista.add(linha);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getDateTime(Date date) {
	    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    return dateFormat.format(date);
	}
	
	public String formatarMetodo(int metodo) {
		if (metodo == 1) {
			return "Cartão de crédito";
		}
		if (metodo == 2) {
			return "Boleto";
		}
		return null;
	}
	
	public void limparArquivo(String arquivo) {
		try {
			FileWriter fw = new FileWriter(arquivo, false);
			PrintWriter pw = new PrintWriter(fw, false);
			pw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
