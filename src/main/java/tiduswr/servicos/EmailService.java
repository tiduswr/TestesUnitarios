package tiduswr.servicos;

import tiduswr.entidades.Usuario;

public interface EmailService {
    void notificarAtraso(Usuario usuario);
}
