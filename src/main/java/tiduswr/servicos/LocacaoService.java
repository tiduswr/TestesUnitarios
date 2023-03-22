package tiduswr.servicos;

import java.util.Calendar;
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

		double valorTotalLocacao = 0.0;
		double descontoMaximo;
		for (int i = 0; i < filmes.size(); i++) {
			descontoMaximo = 0.0;
			Filme filme = filmes.get(i);
			double preco = filme.getPrecoLocacao();

			if(i >= 2 && i <= 5){
				double filmesComDesconto = i - 1;
				descontoMaximo = 0.25 * filmesComDesconto;
			}

			double precoComDesconto = preco - (preco * descontoMaximo);
			valorTotalLocacao += precoComDesconto;
		}
		locacao.setValor(valorTotalLocacao);

		//Entrega no dia util seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = DataUtils.adicionarDias(dataEntrega,1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {

	}
}