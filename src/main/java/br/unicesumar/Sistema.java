package br.unicesumar;

public class Sistema {

    private BancoDados bancoDados;
    private Usuario usuarioLogado;

    public Sistema() {
        bancoDados = new BancoDados();
        usuarioLogado = null;
    }

    public void registrarUsuario(String nome, String email, String senha) throws Exception {
        Usuario usuario = new Usuario(nome, email, senha);
        bancoDados.salvarUsuario(usuario);
    }

    public boolean login(String email, String senha) throws Exception {
        Usuario usuario = bancoDados.recuperarUsuario(email);
        if (usuario != null && usuario.verificarSenha(senha)) {
            usuarioLogado = usuario;
            return true;
        }
        return false;
    }

    public Usuario getUsuario() {
        return usuarioLogado;
    }

    public void deslogar() {
        usuarioLogado = null;
    }
}
