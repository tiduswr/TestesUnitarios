package tiduswr.builder;

import tiduswr.entidades.Filme;

public class FilmeBuilder {
    private Filme filme;

    private FilmeBuilder(){}

    public static FilmeBuilder umFilme(){
        FilmeBuilder b = new FilmeBuilder();
        Filme f = new Filme();
        f.setEstoque(2);
        f.setNome("Filme 1");
        f.setPrecoLocacao(4.0);
        b.filme = f;
        return b;
    }

    public static FilmeBuilder umFilmeSemEstoque(){
        FilmeBuilder b = new FilmeBuilder();
        Filme f = new Filme();
        f.setEstoque(0);
        f.setNome("Filme 1");
        f.setPrecoLocacao(4.0);
        b.filme = f;
        return b;
    }

    public FilmeBuilder semEstoque(){
        filme.setEstoque(0);
        return this;
    }

    public FilmeBuilder comValor(Double valor){
        filme.setPrecoLocacao(valor);
        return this;
    }

    public Filme agora(){
        return filme;
    }

}
