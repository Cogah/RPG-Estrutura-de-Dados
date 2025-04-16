import java.io.IOException;
import java.util.ArrayList;

public class BatalhaLogica {
    private PersonagemLogica personagemLogica;
    private UsuarioLogica usuarioLogica;

    public BatalhaLogica() {
        this.personagemLogica = new PersonagemLogica();
        this.usuarioLogica = new UsuarioLogica();
    }

    public void menuBatalha(String usuario) throws IOException {
        ArrayList<String> personagensUsuario = PersonagemLogica.obterPersonagensUsuario(usuario);
        if (personagensUsuario.isEmpty()) {
            System.out.println("Você precisa criar pelo menos um personagem para batalhar!");
            return;
        }

        System.out.println("\n===== MODO DE BATALHA =====");
        System.out.println("1 - PvE (Contra Monstros)");
        System.out.println("2 - PvP (Contra Outros Jogadores)");
        System.out.println("3 - Voltar");
        System.out.println("Opção: ");

        String opcao = Menu.getScanner().nextLine();

        switch (opcao) {
            case "1":
                batalhaPvE(usuario, personagensUsuario);
                break;
            case "2":
                batalhaPvP(usuario, personagensUsuario);
                break;
            case "3":
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    public void batalhaPvE(String usuario, ArrayList<String> personagensUsuario) throws IOException {
        Personagem jogador = personagemLogica.selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null)
            return;

        Personagem monstro = PersonagemLogica.criarMonstroAleatorio();
        System.out.println("\nVocê encontrou um " + monstro.getNome() + " nível " + monstro.getNivel() + "!");
        System.out.println("Atributos do monstro: Vida=" + monstro.getVidaMaxima() + ", Ataque Físico="
                + monstro.getAtaqueFisico() + ", Ataque Mágico=" + monstro.getAtaqueMagico());

        System.out.println("\nDeseja iniciar a batalha? (S/N): ");
        String confirmacao = Menu.getScanner().nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            iniciarBatalha(jogador, monstro, usuario);
            if (jogador.isAlive() && !monstro.isAlive()) {
                int moedasGanhas = 10 + monstro.getNivel() * 5;
                usuarioLogica.atualizarMoedas(usuario, moedasGanhas);
                System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória!");
            }
        }
    }

    public void batalhaPvP(String usuario, ArrayList<String> personagensUsuario) throws IOException {
        ArrayList<String> outrosJogadores = usuarioLogica.obterOutrosJogadores(usuario);
        if (outrosJogadores.isEmpty()) {
            System.out.println("Não há outros jogadores disponíveis para batalha!");
            return;
        }

        Personagem jogador = personagemLogica.selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null)
            return;

        System.out.println("\nSelecione o oponente:");
        for (int i = 0; i < outrosJogadores.size(); i++) {
            System.out.println((i + 1) + " - " + outrosJogadores.get(i));
        }
        System.out.println("Opção (0 para voltar): ");

        int opcaoOponente;
        try {
            opcaoOponente = Integer.parseInt(Menu.getScanner().nextLine());
            if (opcaoOponente == 0)
                return;
            if (opcaoOponente < 1 || opcaoOponente > outrosJogadores.size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um número.");
            return;
        }

        String oponente = outrosJogadores.get(opcaoOponente - 1);
        ArrayList<String> personagensOponente = PersonagemLogica.obterPersonagensUsuario(oponente);

        if (personagensOponente.isEmpty()) {
            System.out.println("O oponente não tem personagens disponíveis!");
            return;
        }

        System.out.println("\nPersonagens de " + oponente + ":");
        for (int i = 0; i < personagensOponente.size(); i++) {
            System.out.println((i + 1) + " - " + personagensOponente.get(i));
        }
        System.out.println("Escolha um personagem para enfrentar (0 para voltar): ");

        int escolhaPersonagemOponente;
        try {
            escolhaPersonagemOponente = Integer.parseInt(Menu.getScanner().nextLine());
            if (escolhaPersonagemOponente == 0)
                return;
            if (escolhaPersonagemOponente < 1 || escolhaPersonagemOponente > personagensOponente.size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um número.");
            return;
        }

        String nomePersonagemOponente = personagensOponente.get(escolhaPersonagemOponente - 1);
        Personagem personagemOponente = PersonagemLogica.criarPersonagemAPartirDoNome(nomePersonagemOponente);

        System.out.println("Você irá enfrentar o personagem " + nomePersonagemOponente + " do jogador " + oponente);
        iniciarBatalha(jogador, personagemOponente, usuario);

        if (jogador.isAlive() && !personagemOponente.isAlive()) {
            int moedasGanhas = 20;
            usuarioLogica.atualizarMoedas(usuario, moedasGanhas);
            System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória no PvP!");
        }
    }

    public void iniciarBatalha(Personagem jogador, Personagem oponente, String usuario) throws IOException {
        System.out.println("\n===== INÍCIO DA BATALHA =====");
        System.out.println(jogador.getNome() + " VS " + oponente.getNome());

        Arena arena = new Arena();
        arena.iniciarBatalha(jogador, oponente);

        while (arena.batalhaAtiva) {
            arena.executarTurno(jogador, oponente, usuario);

            System.out.println("\nPressione ENTER para continuar...");
            Menu.getScanner().nextLine();
        }

        System.out.println("\n===== FIM DA BATALHA =====");
        arena.ranking.exibirPilha();

        System.out.println("\nPressione ENTER para voltar ao menu...");
        Menu.getScanner().nextLine();
    }
}