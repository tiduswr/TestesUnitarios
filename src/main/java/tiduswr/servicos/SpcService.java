package tiduswr.servicos;

import tiduswr.entidades.Usuario;

public interface SpcService {
    boolean possuiNegativacao(Usuario u) throws Exception;
}
