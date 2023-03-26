package tiduswr.builder;

import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.utils.DataUtils;

import java.util.Date;
import java.util.List;

import static tiduswr.builder.FilmeBuilder.umFilme;
import static tiduswr.builder.UsuarioBuilder.umUsuario;

public class LocacaoBuilder {
    private Locacao locacao;

    private LocacaoBuilder(){}

    public static LocacaoBuilder umaLocacao(){
        LocacaoBuilder builder = new LocacaoBuilder();
        builder.locacao = sampleData();
        return builder;
    }

    private static Locacao sampleData(){
        Locacao locacao = new Locacao();
        locacao.setDataLocacao(new Date());
        locacao.setFilmes(List.of(umFilme().agora()));
        locacao.setUsuario(umUsuario().agora());
        locacao.setValor(4.0);
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
        return locacao;
    }

    public LocacaoBuilder comDataRetorno(Date date){
        locacao.setDataRetorno(date);
        return this;
    }

    public Locacao agora(){
        return locacao;
    }

    public LocacaoBuilder comUsuario(Usuario usuario) {
        locacao.setUsuario(usuario);
        return this;
    }

    public LocacaoBuilder atrasado(){
        locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
        return this;
    }

}
