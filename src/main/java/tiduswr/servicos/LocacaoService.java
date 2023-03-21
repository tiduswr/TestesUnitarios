package tiduswr.servicos;

import java.util.Date;
import java.util.List;

import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;
import tiduswr.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		if(usuario == null) throw new LocadoraException("Usuário Vázio!");
		if(filmes == null || filmes.isEmpty()) throw new LocadoraException("Filme vázio!");
		boolean anyFilmeWithoutEstoque = filmes.stream()
				.map(Filme::getEstoque)
				.anyMatch(e -> e == 0);
		if(anyFilmeWithoutEstoque)
			throw new FilmeSemEstoqueException("Filme sem estoque!");

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotalLocacao = filmes.stream()
				.map(Filme::getPrecoLocacao)
				.reduce(0.0, Double::sum);
		locacao.setValor(valorTotalLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {

	}
}