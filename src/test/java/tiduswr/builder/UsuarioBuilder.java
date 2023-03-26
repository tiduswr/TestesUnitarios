package tiduswr.builder;

import tiduswr.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario user;

    private UsuarioBuilder(){}

    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.user = new Usuario();
        builder.user.setNome("Usuario 1");
        return builder;
    }

    public Usuario agora(){
        return user;
    }

}
