package Projeto.Financeiro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Projeto.Config.Arquivos;

public class Saldo {

	public Saldo() {}

	public Arquivos arq = new Arquivos();

	public double getSaldo(String cpf) {
		ArrayList<String> usuario = new ArrayList<String>();
		arq.transformFileInList("usuarios/" + cpf + ".txt", usuario);
		String[] infos = usuario.get(0).split("-");
		return Double.parseDouble(infos[4]);
	}

	public void setSaldo(String cpf, double quantidade) {
		ArrayList<String> usuario = new ArrayList<String>();
		arq.transformFileInList("usuarios/" + cpf + ".txt", usuario);
		FileWriter fw;
		try {
			ArrayList<String> outros = new ArrayList<String>();
			String[] infos = usuario.get(0).split("-");
			usuario.remove(0);
			for(int i = 0; i < usuario.size(); i++) {
				outros.add(usuario.get(i));
			}
			fw = new FileWriter(new File("usuarios/" + cpf + ".txt").getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			infos[4] = Double.toString(quantidade);
			bw.write(infos[0] + "-" + infos[1] + "-" + infos[2] + "-" + infos[3] + "-" + infos[4] + "\n");
			for(int i = 0; i < outros.size(); i++) {
				bw.write(outros.get(i) + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addSaldo(String cpf, double quantidade) {
		setSaldo(cpf, getSaldo(cpf) + quantidade);
	}

	public void removerSaldo(String cpf, double quantidade) {
		if((getSaldo(cpf) - quantidade) < 0) {
			setSaldo(cpf, 0);
		} else {
			setSaldo(cpf, getSaldo(cpf) - quantidade);
		}
	}
}
