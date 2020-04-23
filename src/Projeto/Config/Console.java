package Projeto.Config;

import java.io.IOException;

import Projeto.Main;

public class Console {

	public Console() {}

    private String lastLine = "";
	public final String RESET = "\u001B[0m";
	public final String PRETO = "\u001B[30m";
	public final String VERMELHO = "\u001B[31m";
	public final String VERDE = "\u001B[32m";
	public final String AMARELO = "\u001B[33m";
	public final String AZUL = "\u001B[34m";
	public final String ROXO = "\u001B[35m";
	public final String CINZA = "\u001B[36m";
	public final String BRANCO = "\u001B[37m";

	public void clear() {
		if (System.getProperty("os.name").contains("Windows")) {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Runtime.getRuntime().exec("clear");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void restartMenu() {
		System.out.println("\nAperte ENTER para voltar ao menu!");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String coloredText(String cor, String texto) {
		return cor + texto + RESET;
	}

	public String coloredOption(int num, String texto) {
		return AMARELO + "[" + num + "] " + RESET + texto;
	}

	public String formatPrice(double preco) {
		if (preco <= 0.0) {
			return "Gratuito";
		} else {
			return Double.toString(preco);
		}
	}
	
	public boolean isDigit(String str) {
		if (str.matches("^[0-9]*$")) {
			return true;
		} else {
			return false;
		}
	}
    
    public void print(String line) {
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }
    
    public void animarMenu(String texto) {
    	int c = 0;
    	while(Main.carregando == true) {
    		if(c == 1) {
    			print(this.coloredText(VERDE, "[ \\ ] ") + texto);
    		} else if(c == 2) {
    			print(this.coloredText(VERDE, "[ | ] ") + texto);
    		} else if(c == 3) {
    			print(this.coloredText(VERDE, "[ / ] ") + texto);
    		} else if(c == 4) {
    			print(this.coloredText(VERDE, "[ - ] ") + texto);
    		} else {
    			c = 0;
    		}
    		try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		c++;
    	}
    }
}
