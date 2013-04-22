package controle;

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

}
