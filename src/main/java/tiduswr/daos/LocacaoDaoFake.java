package tiduswr.daos;

import tiduswr.entidades.Locacao;

import java.util.List;

public class LocacaoDaoFake implements LocacaoDAO{
    @Override
    public void salvar(Locacao locacao) {

    }

    @Override
    public List<Locacao> obterLocacoesPendentes() {
        return null;
    }
}
