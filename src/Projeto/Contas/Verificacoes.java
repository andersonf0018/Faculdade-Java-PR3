package Projeto.Contas;

import java.io.File;
import java.util.ArrayList;

public class Verificacoes {

	public Verificacoes() {
	}

	public String getPastaModo(int modo) {
		if (modo == 1) {
			return "usuarios";
		} else if (modo == 2) {
			return "desenvolvedores";
		} else {
			return null;
		}
	}

	public String formatModo(int modo) {
		if (modo == 1) {
			return "CPF";
		} else {
			return "CNPJ";
		}
	}

	public boolean contaJaCriada(String documento, int modo) {
		String modoPasta = getPastaModo(modo);
		File pasta = new File(modoPasta);
		File[] lista = pasta.listFiles();
		ArrayList<String> arquivos = new ArrayList<String>();
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].isFile()) {
				arquivos.add(lista[i].getName());
			}
		}
		if (arquivos.contains(documento + ".txt")) {
			return true;
		} else {
			return false;
		}
	}
}
