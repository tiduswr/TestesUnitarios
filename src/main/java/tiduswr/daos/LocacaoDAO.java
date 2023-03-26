package tiduswr.daos;

import tiduswr.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {
    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
