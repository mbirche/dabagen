package controle;

import java.io.File;
import java.io.FileNotFoundException;

import modelo.EstruturaArquivo;
import modelo.Tupla;

public class Main {
	public static void main(String[] args) {

		File data = new File(
				"C:/Users/Marcelo/Documents/iCubing/data_generator/bin/data");
		File data1 = new File(
				"C:/Users/Marcelo/Documents/iCubing/data_generator/bin/data1");
		
		BufferedLeitor leitor = null;

		try {
			leitor = new BufferedLeitor(data1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		EstruturaArquivo estrutura = leitor.getEstrutura();

		System.out.println("Número de tuplas: " + estrutura.getNumeroTuplas());
		System.out.println("Número de dimensões: "
				+ estrutura.getDimensoes().size());

		Tupla tupla = leitor.obterTupla();

		while (tupla != null) {
			System.out.println();
			for (int i = 0; i < tupla.getValores().length; i++) {
				System.out.print(tupla.getValores()[i] + " ");
			}
			tupla = leitor.obterTupla();
		}

		Gravador gravador = new Gravador();

		try {
			//gravador.mergeHorizontal(data1, data, true, true); // arquivo com cabeçalho e com indice
			//gravador.mergeHorizontal(data1, data, false, true); // arquivo sem cabeçalho e com indice
			//gravador.mergeHorizontal(data1, data, true, false); // arquivo com cabeçalho e sem indice
			//gravador.mergeHorizontal(data1, data, false, false); // arquivo com cabeçalho e indice
			
			//gravador.mergeVertical(data, data1, true, true);
			//gravador.mergeVertical(data, data1, true, false);
			//gravador.mergeVertical(data, data1, false, true);
			gravador.mergeVertical(data, data1, false, false);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
