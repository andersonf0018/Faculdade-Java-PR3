package Projeto.Financeiro;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import Projeto.Config.Arquivos;

public class Nota {
	
	static Arquivos arq = new Arquivos();
	
	private int idTransacao;
	private String cpf;
	private String produto;
	private double preco;
	private String metodo;
	
	public Nota(int idTransacao, String cpf, String produto, double preco, String metodo) {
		this.idTransacao = idTransacao;
		this.cpf = cpf;
		this.produto = produto;
		this.preco = preco;
		this.metodo = metodo;
	}
	
	public static int gerarIdNota() {
		int m = (int) Math.pow(10, 9 - 1);
	    return m + new Random().nextInt(9 * m);
	}

	public static void gerarNota(Nota nota) {
		Document doc = new Document();
		PdfWriter docWriter = null;
		try {
			String id = Integer.toString(nota.getIdTransacao());
			String pdf = "notas\\nota_"+ id + ".pdf";
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
			doc.setPageSize(PageSize.A5);
			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();

			Paragraph space = new Paragraph(" ");
			Paragraph titulo_p = new Paragraph("Nota fiscal");
			Paragraph id_p = new Paragraph("Transação - " + id);
			Paragraph cpf_p = new Paragraph("CPF - " + nota.getCpf());
			Paragraph produto_p = new Paragraph(nota.getProduto() + " - R$" + nota.getPreco());
			Paragraph metodo_p = new Paragraph("Metodo - " + nota.getMetodo());
			Paragraph data_p = new Paragraph(arq.getDateTime(new Date()));
			Paragraph agradecimentos_p = new Paragraph("Obrigado por comprar conosco ;)");

			Barcode128 code128 = new Barcode128();
			code128.setCode(id);
			code128.setCodeType(Barcode128.CODE128);
			Image code128Image = code128.createImageWithBarcode(cb, null, null);
			code128Image.scalePercent(125);
			
			doc.add(titulo_p);
			doc.add(space);
			doc.add(id_p);
			doc.add(cpf_p);
			doc.add(produto_p);
			doc.add(metodo_p);
			doc.add(space);
			doc.add(data_p);
			doc.add(space);
			doc.add(agradecimentos_p);
			doc.add(code128Image);
			Desktop.getDesktop().open(new File(pdf));
		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
	}

	public int getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(int idTransacao) {
		this.idTransacao = idTransacao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco2) {
		this.preco = preco2;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
}
