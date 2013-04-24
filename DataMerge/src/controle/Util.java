package controle;

import modelo.EstruturaArquivo;

public class Util {
	
	public static String obterValorComPrefixo(Integer valor){
		
		String valorComPrefixo = String.valueOf(valor);
		
		if(valor >= 1000){
			valorComPrefixo = valor/1000 + "k";
		}
		if(valor >= 1000000){
			valorComPrefixo = valor/1000000 + "M";
		}
		
		return valorComPrefixo;
	}
	
	public static String obterNomeTabela(EstruturaArquivo estrutura, Integer skew, Integer cardinalidade){
		
		StringBuffer nomeTabela = new StringBuffer();
		
		nomeTabela.append("D");
		nomeTabela.append(obterValorComPrefixo(estrutura.getDimensoes()
				.size()));
		nomeTabela.append("_T");
		nomeTabela
				.append(obterValorComPrefixo(estrutura.getNumeroTuplas()));
		nomeTabela.append("_S");
		nomeTabela.append(obterValorComPrefixo(skew));
		nomeTabela.append("_C");
		nomeTabela.append(obterValorComPrefixo(cardinalidade));
		
		return nomeTabela.toString();
	}

}
