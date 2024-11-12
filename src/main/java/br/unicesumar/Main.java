package br.unicesumar;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Sistema sistema = new Sistema();
        Usuario usuarioLogado = null;
        BancoDados banco = new BancoDados();
        boolean sair = false;

        while (!sair) {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar");
            System.out.println("2. Fazer Login");
            if (usuarioLogado != null) {
                System.out.println("3. Adicionar Arquivo");
                System.out.println("4. Ver Arquivos");
                System.out.println("5. Deslogar");
                System.out.println("6. Sair");
            } else {
                System.out.println("3. Sair");
            }
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (usuarioLogado == null) {
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Senha: ");
                        String senha = scanner.nextLine();
                        try {
                            sistema.registrarUsuario(nome, email, senha);
                            System.out.println("Cadastro realizado com sucesso.");
                        } catch (Exception e) {
                            System.out.println("Erro ao registrar usuário: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Você já está logado!");
                    }
                    break;

                case 2:
                    if (usuarioLogado == null) {
                        System.out.print("Email: ");
                        String emailLogin = scanner.nextLine();
                        System.out.print("Senha: ");
                        String senhaLogin = scanner.nextLine();
                        try {
                            if (sistema.login(emailLogin, senhaLogin)) {
                                usuarioLogado = sistema.getUsuario();
                                System.out.println("Login bem-sucedido.");
                            } else {
                                System.out.println("Falha no login.");
                            }
                        } catch (Exception e) {
                            System.out.println("Erro no login: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Você já está logado!");
                    }
                    break;

                case 3:
                    if (usuarioLogado != null) {
                        System.out.print("Nome do arquivo: ");
                        String nomeArquivo = scanner.nextLine();
                        System.out.print("Conteúdo do arquivo: ");
                        String conteudo = scanner.nextLine();

                        banco.adicionarArquivo(nomeArquivo, conteudo, usuarioLogado);

                    } else {
                        System.out.println("Você precisa estar logado para adicionar um arquivo.");
                    }
                    break;

                case 4:
                    if (usuarioLogado != null) {
                        banco.exibirArquivos(usuarioLogado);

                    } else {
                        System.out.println("Você precisa estar logado para ver os arquivos.");
                    }
                    break;

                case 5:
                    if (usuarioLogado != null) {
                        sistema.deslogar();
                        usuarioLogado = null;
                        System.out.println("Deslogado com sucesso.");
                    } else {
                        System.out.println("Você já está deslogado.");
                    }
                    break;

                default:
                    sair = true;
                    System.out.println("Saindo...");
                    break;
            }
        }
        scanner.close();
    }
}
