import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private String usuarioLogado = null;
    private UsuarioLogica usuarioLogica;
    private PersonagemLogica personagemLogica;
    private BatalhaLogica batalhaLogica;
    private LojaLogica lojaLogica;

    public Menu() {
        this.usuarioLogica = new UsuarioLogica();
        this.personagemLogica = new PersonagemLogica();
        this.batalhaLogica = new BatalhaLogica();
        this.lojaLogica = new LojaLogica();
    }

    public void menuInicio() throws IOException {
        System.out.println("=======================================");
        System.out.println("        RPG BATALHA DE HERÓIS         ");
        System.out.println("=======================================");

        while (usuarioLogado == null) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1) => Login");
            System.out.println("2) => Cadastrar");
            System.out.println("3) => Sair");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    usuarioLogado = usuarioLogica.fazerLogin();
                    break;
                case "2":
                    usuarioLogado = usuarioLogica.fazerCadastro();
                    break;
                case "3":
                    System.out.println("Saindo do jogo. Até a próxima, meu chefe! :)");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        System.out.println("\nBem-vindo, " + usuarioLogado + "!");
        menuPrincipal();
    }

    private void menuPrincipal() throws IOException {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1) => Gerenciar Personagens");
            System.out.println("2) => Iniciar Batalha");
            System.out.println("3) => Visualizar Perfil");
            System.out.println("4) => Loja de Itens");
            System.out.println("5) => Sair");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    personagemLogica.menuPersonagens(usuarioLogado);
                    break;
                case "2":
                    batalhaLogica.menuBatalha(usuarioLogado);
                    break;
                case "3":
                    usuarioLogica.exibirPerfil(usuarioLogado);
                    break;
                case "4":
                    lojaLogica.menuLoja(usuarioLogado);
                    break;
                case "5":
                    continuar = false;
                    System.out.println("Saindo do jogo. Até a próxima, meu chefe! :)");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static Scanner getScanner() {
        return scanner;
    }
}