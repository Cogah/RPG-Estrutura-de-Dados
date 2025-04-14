import java.io.*;
import java.util.*;

public class App {
    private static final String ARQUIVO_USUARIOS = "usuarios.txt";
    private static final String ARQUIVO_PERSONAGENS = "personagens.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String usuarioLogado = null;

        System.out.println("---------------------------------------");
        System.out.println("Seja bem-vindo ao RPG, meu chefe!");
        System.out.println("---------------------------------------");

        while (usuarioLogado == null) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1) Login");
            System.out.println("2) Cadastrar");
            System.out.println("3) Sair");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    usuarioLogado = fazerLogin();
                    break;
                case "2":
                    usuarioLogado = fazerCadastro();
                    break;
                case "3":
                    System.out.println("Saindo do jogo");
                    return;
                default:
                    System.out.println("Opção invalida :(");
            }
        }

        System.out.println("\nBem-vindo, " + usuarioLogado + "!");
        exibirPersonagens(usuarioLogado);

        boolean continuar = true;
        while (continuar) {
            System.out.println("\nO que deseja fazer?");
            System.out.println("1 - Adicionar personagem");
            System.out.println("2 - Listar personagens");
            System.out.println("3 - Sair");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    adicionarPersonagem(usuarioLogado);
                    break;
                case "2":
                    exibirPersonagens(usuarioLogado);
                    break;
                case "3":
                    continuar = false;
                    System.out.println("Saindo do jogo!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }

    private static String fazerLogin() throws IOException {
        System.out.println("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.println("Senha: ");
        String senha = scanner.nextLine();

        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            System.out.println("Nenhum usuario cadastrado ainda :(");
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 2 && dados[0].equals(nome) && dados[1].equals(senha)) {
                System.out.println("Login realizado com sucesso!");
                br.close();
                return nome;
            }
        }

        System.out.println("Nome de usuário ou senha incorretos!");
        br.close();
        return null;
    }

    private static String fazerCadastro() throws IOException {
        System.out.println("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.println("Senha: ");
        String senha = scanner.nextLine();

        File arquivo = new File(ARQUIVO_USUARIOS);
        if (arquivo.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(":");
                if (dados.length > 0 && dados[0].equals(nome)) {
                    System.out.println("Nome de usuário já existe :(");
                    br.close();
                    return null;
                }
            }
            br.close();
        }

        FileWriter fw = new FileWriter(ARQUIVO_USUARIOS, true);
        fw.write(nome + ":" + senha + "\n");
        fw.close();

        System.out.println("Cadastro realizado com sucesso xD");
        return nome;
    }

    private static void adicionarPersonagem(String usuario) throws IOException {
        System.out.println("Nome do personagem: ");
        String nomePersonagem = scanner.nextLine();

        FileWriter fw = new FileWriter(ARQUIVO_PERSONAGENS, true);
        fw.write(usuario + ":" + nomePersonagem + "\n");
        fw.close();

        System.out.println("Personagem adicionado com sucesso xD");
    }

    private static void exibirPersonagens(String usuario) throws IOException {
        System.out.println("|------- PERSONAGENS DE " + usuario.toUpperCase() + " -------|");

        File arquivo = new File(ARQUIVO_PERSONAGENS);
        if (!arquivo.exists()) {
            System.out.println("Nenhum personagem encontrado :(");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PERSONAGENS));
        String linha;
        boolean temPersonagem = false;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 2 && dados[0].equals(usuario)) {
                System.out.println("- " + dados[1]);
                temPersonagem = true;
            }
        }

        if (!temPersonagem) {
            System.out.println("Nenhum personagem encontrado :(");
        }

        br.close();
    }
}