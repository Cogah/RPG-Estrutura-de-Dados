import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Menu {

    private static final String ARQUIVO_USUARIOS = "usuarios.txt";
    private static final String ARQUIVO_PERSONAGENS = "personagens.txt";
    private static Scanner scanner = new Scanner(System.in);
    String usuarioLogado = null;

    public String fazerLogin() throws IOException {
        System.out.println("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.println("Senha: ");
        String senha = scanner.nextLine();

        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            System.out.println("Nenhum usuário cadastrado ainda!");
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 2 && dados[0].equals(nome) && dados[1].equals(senha)) {
                br.close();
                System.out.println("Login realizado com sucesso!");
                return nome;
            }
        }

        br.close();
        System.out.println("Nome de usuário ou senha incorretos!");
        return null;
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
                    usuarioLogado = fazerLogin();
                    break;
                case "2":
                    usuarioLogado = fazerCadastro();
                    break;
                case "3":
                System.out.println("Saindo do jogo. Até a próxima, meu chefe! :)");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        System.out.println("\nBem-vindo, " + usuarioLogado + "!");
        menuPrincipal(usuarioLogado);

        scanner.close();
    }

    public static void menuPrincipal(String usuario) throws IOException {
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
                    menuPersonagens(usuario);
                    break;
                case "2":
                    menuBatalha(usuario);
                    break;
                case "3":
                    exibirPerfil(usuario);
                    break;
                case "4":
                    menuLoja(usuario);
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

    public static void menuPersonagens(String usuario) throws IOException {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n===== GERENCIAR PERSONAGENS =====");
            System.out.println("1 - Criar novo personagem");
            System.out.println("2 - Listar meus personagens");
            System.out.println("3 - Voltar");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    criarPersonagem(usuario);
                    break;
                case "2":
                    exibirPersonagens(usuario);
                    break;
                case "3":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static void menuBatalha(String usuario) throws IOException {
        ArrayList<String> personagensUsuario = obterPersonagensUsuario(usuario);
        if (personagensUsuario.isEmpty()) {
            System.out.println("Você precisa criar pelo menos um personagem para batalhar!");
            return;
        }

        System.out.println("\n===== MODO DE BATALHA =====");
        System.out.println("1 - PvE (Contra Monstros)");
        System.out.println("2 - PvP (Contra Outros Jogadores)");
        System.out.println("3 - Voltar");
        System.out.println("Opção: ");

        String opcao = scanner.nextLine();

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

    public static void batalhaPvE(String usuario, ArrayList<String> personagensUsuario) throws IOException {
        Personagem jogador = selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null)
            return;

        Personagem monstro = criarMonstroAleatorio();
        System.out.println("\nVocê encontrou um " + monstro.getNome() + " nível " + monstro.getNivel() + "!");
        System.out.println("Atributos do monstro: Vida=" + monstro.getVidaMaxima() + ", Ataque Físico="
                + monstro.getAtaqueFisico() + ", Ataque Mágico=" + monstro.getAtaqueMagico());

        System.out.println("\nDeseja iniciar a batalha? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            iniciarBatalha(jogador, monstro, usuario);
            if (jogador.isAlive() && !monstro.isAlive()) {
                int moedasGanhas = 10 + monstro.getNivel() * 5;
                atualizarMoedas(usuario, moedasGanhas);
                System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória!");
            }
        }
    }

    public static void batalhaPvP(String usuario, ArrayList<String> personagensUsuario) throws IOException {
        ArrayList<String> outrosJogadores = obterOutrosJogadores(usuario);
        if (outrosJogadores.isEmpty()) {
            System.out.println("Não há outros jogadores disponíveis para batalha!");
            return;
        }

        Personagem jogador = selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null)
            return;

        System.out.println("\nSelecione o oponente:");
        for (int i = 0; i < outrosJogadores.size(); i++) {
            System.out.println((i + 1) + " - " + outrosJogadores.get(i));
        }
        System.out.println("Opção (0 para voltar): ");

        int opcaoOponente;
        try {
            opcaoOponente = Integer.parseInt(scanner.nextLine());
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
        ArrayList<String> personagensOponente = obterPersonagensUsuario(oponente);

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
            escolhaPersonagemOponente = Integer.parseInt(scanner.nextLine());
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
        Personagem personagemOponente = criarPersonagemAPartirDoNome(nomePersonagemOponente);

        System.out.println("Você irá enfrentar o personagem " + nomePersonagemOponente + " do jogador " + oponente);
        iniciarBatalha(jogador, personagemOponente, usuario);

        if (jogador.isAlive() && !personagemOponente.isAlive()) {
            int moedasGanhas = 20;
            atualizarMoedas(usuario, moedasGanhas);
            System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória no PvP!");
        }
    }

    public static Personagem selecionarPersonagemJogador(String usuario, ArrayList<String> personagensUsuario) {
        System.out.println("\nSelecione seu personagem:");
        for (int i = 0; i < personagensUsuario.size(); i++) {
            System.out.println((i + 1) + " - " + personagensUsuario.get(i));
        }
        System.out.println("Opção (0 para voltar): ");

        int opcaoPersonagem;
        try {
            opcaoPersonagem = Integer.parseInt(scanner.nextLine());
            if (opcaoPersonagem == 0)
                return null;
            if (opcaoPersonagem < 1 || opcaoPersonagem > personagensUsuario.size()) {
                System.out.println("Opção inválida!");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um número.");
            return null;
        }

        String nomePersonagem = personagensUsuario.get(opcaoPersonagem - 1);
        return criarPersonagemAPartirDoNome(nomePersonagem);
    }

    public static void iniciarBatalha(Personagem jogador, Personagem oponente, String usuario) throws IOException {
        System.out.println("\n===== INÍCIO DA BATALHA =====");
        System.out.println(jogador.getNome() + " VS " + oponente.getNome());

        Arena arena = new Arena();
        arena.iniciarBatalha(jogador, oponente);

        while (arena.batalhaAtiva) {
            arena.executarTurno(jogador, oponente, usuario);

            System.out.println("\nPressione ENTER para continuar...");
            scanner.nextLine();
        }

        System.out.println("\n===== FIM DA BATALHA =====");
        arena.ranking.exibirPilha();

        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }

    public static Personagem criarPersonagemAPartirDoNome(String nome) {
        int hashCode = Math.abs(nome.hashCode());
        Random rand = new Random(hashCode); // Isso daqui é muito genial !!!!!

        int nivel = 1 + rand.nextInt(3);
        int vida = 70 + nivel * 10 + rand.nextInt(21); 
        int ataqueFisico = 15 + nivel * 5 + rand.nextInt(11); 
        int ataqueMagico = 15 + nivel * 5 + rand.nextInt(11); 
        int defesa = 10 + nivel * 2 + rand.nextInt(6); 
        int mana = 40 + nivel * 10 + rand.nextInt(31); 
        int stamina = 40 + nivel * 8 + rand.nextInt(21);

        Personagem p = new Personagem(vida, ataqueFisico, ataqueMagico, defesa, nome, nivel, mana, stamina);
        p.setVidaAtual(vida);
        p.setManaAtual(mana);
        p.setStaminaAtual(stamina);

        return p;
    }

    public static Personagem criarMonstroAleatorio() {
        String[] nomesMonstros = { "Goblin", "Ogro", "Dragão", "Esqueleto", "Zumbi", "Bruxa", "Troll", "Slime",
                "Aranha Gigante", "Fantasma, Casseb, Projeto Integrado, Amazon Hacking" };
        Random rand = new Random();

        String nome = nomesMonstros[rand.nextInt(nomesMonstros.length)];
        int nivel = 1 + rand.nextInt(3); 

        int vida = 50 + nivel * 15 + rand.nextInt(31); 
        int ataqueFisico = 12 + nivel * 4 + rand.nextInt(9); 
        int ataqueMagico = 12 + nivel * 4 + rand.nextInt(9); 
        int defesa = 8 + nivel * 3 + rand.nextInt(7); 
        int mana = 30 + nivel * 10 + rand.nextInt(31); 
        int stamina = 30 + nivel * 8 + rand.nextInt(21); 

        Personagem monstro = new Personagem(vida, ataqueFisico, ataqueMagico, defesa, nome, nivel, mana, stamina);
        monstro.setVidaAtual(vida);
        monstro.setManaAtual(mana);
        monstro.setStaminaAtual(stamina);

        return monstro;
    }

    public static void criarPersonagem(String usuario) throws IOException {
        System.out.println("\n===== CRIAR NOVO PERSONAGEM =====");
        System.out.println("Nome do personagem: ");
        String nomePersonagem = scanner.nextLine();

        if (personagemJaExiste(usuario, nomePersonagem)) {
            System.out.println("Você já tem um personagem com esse nome!");
            return;
        }

        FileWriter fw = new FileWriter(ARQUIVO_PERSONAGENS, true);
        fw.write(usuario + ":" + nomePersonagem + "\n");
        fw.close();
        System.out.println("Personagem " + nomePersonagem + " criado com sucesso!");
    }

    public static boolean personagemJaExiste(String usuario, String nomePersonagem) throws IOException {
        ArrayList<String> personagens = obterPersonagensUsuario(usuario);
        for (String p : personagens) {
            if (p.equalsIgnoreCase(nomePersonagem)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> obterPersonagensUsuario(String usuario) throws IOException {
        ArrayList<String> personagens = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PERSONAGENS);

        if (!arquivo.exists()) {
            return personagens;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PERSONAGENS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 2 && dados[0].equals(usuario)) {
                personagens.add(dados[1]);
            }
        }

        br.close();
        return personagens;
    }

    public static ArrayList<String> obterOutrosJogadores(String usuarioAtual) throws IOException {
        ArrayList<String> jogadores = new ArrayList<>();
        File arquivo = new File(ARQUIVO_USUARIOS);

        if (!arquivo.exists()) {
            return jogadores;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 1 && !dados[0].equals(usuarioAtual)) {
                jogadores.add(dados[0]);
            }
        }

        br.close();
        return jogadores;
    }

    public static void exibirPerfil(String usuario) throws IOException {
        System.out.println("\n===== PERFIL DO JOGADOR =====");
        System.out.println("Nome: " + usuario);

        int moedas = obterMoedas(usuario);
        System.out.println("Moedas: " + moedas);

        ArrayList<String> personagens = obterPersonagensUsuario(usuario);
        System.out.println("Total de personagens: " + personagens.size());

        System.out.println("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }

    public static int obterMoedas(String usuario) throws IOException {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            return 0;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 2 && dados[0].equals(usuario)) {
                try {
                    br.close();
                    return Integer.parseInt(dados[2]);
                } catch (NumberFormatException e) {
                    br.close();
                    return 0;
                }
            }
        }

        br.close();
        return 0;
    }

    public static void atualizarMoedas(String usuario, int moedasGanhas) throws IOException {
        File arquivoOriginal = new File(ARQUIVO_USUARIOS);
        File arquivoTemp = new File(ARQUIVO_USUARIOS + ".tmp");

        BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
        BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp));

        String linha;
        boolean usuarioEncontrado = false;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 2 && dados[0].equals(usuario)) {
                int moedasAtuais = 0;
                try {
                    moedasAtuais = Integer.parseInt(dados[2]);
                } catch (NumberFormatException e) {

                }
                bw.write(usuario + ":" + dados[1] + ":" + (moedasAtuais + moedasGanhas));

                bw.newLine();
                usuarioEncontrado = true;
            } else {
                bw.write(linha);
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        if (usuarioEncontrado) {
            if (arquivoOriginal.delete()) {
                arquivoTemp.renameTo(arquivoOriginal);
            }
        }
    }

    public static String fazerCadastro() throws IOException {
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
                if (dados.length >= 1 && dados[0].equals(nome)) {
                    br.close();
                    System.out.println("Nome de usuário já existe!");
                    return null;
                }
            }

            br.close();
        }

        FileWriter fw = new FileWriter(ARQUIVO_USUARIOS, true);
        fw.write(nome + ":" + senha + ":100\n");
        fw.close();
        System.out.println("Cadastro realizado com sucesso! Você recebeu 100 moedas iniciais.");
        return nome;
    }

    public static void exibirPersonagens(String usuario) throws IOException {
        System.out.println("\n===== PERSONAGENS DE " + usuario.toUpperCase() + " =====");

        ArrayList<String> personagens = obterPersonagensUsuario(usuario);

        if (personagens.isEmpty()) {
            System.out.println("Nenhum personagem encontrado!");
        } else {
            for (int i = 0; i < personagens.size(); i++) {
                String nomePersonagem = personagens.get(i);
                Personagem p = criarPersonagemAPartirDoNome(nomePersonagem);
                System.out.println((i + 1) + " - " + nomePersonagem +
                        " (Nível " + p.getNivel() +
                        ", Vida: " + p.getVidaMaxima() +
                        ", Ataque: " + p.getAtaqueFisico() + ")");
            }
        }

        System.out.println("\n    Pressione ENTER para voltar...");
        scanner.nextLine();
    }

    public static void menuLoja(String usuario) throws IOException {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n===== LOJA DE ITENS =====");
            System.out.println("Suas moedas: " + obterMoedas(usuario));
            System.out.println("1 - Ver itens disponíveis");
            System.out.println("2 - Comprar item");
            System.out.println("3 - Ver meu inventário");
            System.out.println("4 - Voltar");
            System.out.println("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    exibirItensDisponiveis();
                    break;
                case "2":
                    comprarItem(usuario);
                    break;
                case "3":
                    exibirInventario(usuario);
                    break;
                case "4":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static void exibirItensDisponiveis() {
        System.out.println("\n===== ITENS DISPONÍVEIS =====");
        System.out.println("1 - Poção de Vida (Custo: 10, Recupera: Vida=30)");
        System.out.println("2 - Poção de Mana (Custo: 10, Recupera: Mana=30)");
        System.out.println("3 - Poção de Stamina (Custo: 10, Recupera: Stamina=30)");
        System.out.println("4 - Poção Grande de Vida (Custo: 30, Recupera: Vida=80)");
        System.out.println("5 - Elixir Mágico (Custo: 50, Recupera: Vida=30, Mana=50)");
        System.out.println("6 - Bebida Energética (Custo: 40, Recupera: Stamina=80, Vida=10)");

        System.out.println("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }

    public static void comprarItem(String usuario) throws IOException {
        System.out.println("\n===== COMPRAR ITEM =====");
        System.out.println("Suas moedas: " + obterMoedas(usuario));
        exibirItensDisponiveis();

        System.out.println("Escolha o número do item que deseja comprar (0 para cancelar): ");
        int escolha;
        try {
            escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return;
            if (escolha < 1 || escolha > 6) {
                System.out.println("Item inválido!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida!");
            return;
        }

        Item itemComprado = null;
        int custoItem = 0;

        switch (escolha) {
            case 1:
                itemComprado = new Item("Poção de Vida", 10, 0, 0, 30);
                custoItem = 10;
                break;
            case 2:
                itemComprado = new Item("Poção de Mana", 10, 30, 0, 0);
                custoItem = 10;
                break;
            case 3:
                itemComprado = new Item("Poção de Stamina", 10, 0, 30, 0);
                custoItem = 10;
                break;
            case 4:
                itemComprado = new Item("Poção Grande de Vida", 30, 0, 0, 80);
                custoItem = 30;
                break;
            case 5:
                itemComprado = new Item("Elixir Mágico", 50, 50, 0, 30);
                custoItem = 50;
                break;
            case 6:
                itemComprado = new Item("Bebida Energética", 40, 0, 80, 10);
                custoItem = 40;
                break;
        }

        int moedasJogador = obterMoedas(usuario);
        if (moedasJogador < custoItem) {
            System.out.println("Você não tem moedas suficientes para comprar esse item!");
            return;
        }

        atualizarMoedas(usuario, -custoItem);

        adicionarItemInventario(usuario, itemComprado);

        System.out.println("Item " + itemComprado.getNome() + " comprado com sucesso!");
    }

    public static void adicionarItemInventario(String usuario, Item item) throws IOException {

        File arquivo = new File("inventario.txt");
        FileWriter fw = new FileWriter(arquivo, true);
        fw.write(usuario + ":" + item.getNome() + ":" + item.getCusto() + ":" +
                item.getRecoverMana() + ":" + item.getRecoverStamina() + ":" +
                item.getRecoverVida() + "\n");
        fw.close();
    }

    public static void exibirInventario(String usuario) throws IOException {
        System.out.println("\n===== SEU INVENTÁRIO =====");

        ArrayList<Item> itens = obterItensUsuario(usuario);

        if (itens.isEmpty()) {
            System.out.println("Você não tem itens no inventário!");
        } else {
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.get(i);
                System.out.println((i + 1) + " - " + item.getNome() +
                        " (Recupera: Vida=" + item.getRecoverVida() +
                        ", Mana=" + item.getRecoverMana() +
                        ", Stamina=" + item.getRecoverStamina() + ")");
            }
        }

        System.out.println("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }

    public static ArrayList<Item> obterItensUsuario(String usuario) throws IOException {
        ArrayList<Item> itens = new ArrayList<>();
        File arquivo = new File("inventario.txt");

        if (!arquivo.exists()) {
            return itens;
        }

        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 6 && dados[0].equals(usuario)) {
                String nome = dados[1];
                int custo = Integer.parseInt(dados[2]);
                int recoverMana = Integer.parseInt(dados[3]);
                int recoverStamina = Integer.parseInt(dados[4]);
                int recoverVida = Integer.parseInt(dados[5]);

                Item item = new Item(nome, custo, recoverMana, recoverStamina, recoverVida);
                itens.add(item);
            }
        }

        br.close();
        return itens;
    }

    public static void usarItemEmBatalha(String usuario, Personagem jogador) throws IOException {
        ArrayList<Item> itens = obterItensUsuario(usuario);

        if (itens.isEmpty()) {
            System.out.println("Você não tem itens para usar!");
            return;
        }

        System.out.println("\n===== USAR ITEM =====");
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            System.out.println((i + 1) + " - " + item.getNome() +
                    " (Recupera: Vida=" + item.getRecoverVida() +
                    ", Mana=" + item.getRecoverMana() +
                    ", Stamina=" + item.getRecoverStamina() + ")");
        }

        System.out.println("Escolha um item para usar (0 para cancelar): ");
        int escolha;
        try {
            escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return;
            if (escolha < 1 || escolha > itens.size()) {
                System.out.println("Item inválido!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida!");
            return;
        }

        Item itemSelecionado = itens.get(escolha - 1);
        itemSelecionado.usar(jogador);

        removerItemInventario(usuario, itemSelecionado);
    }

    public static void removerItemInventario(String usuario, Item itemUsado) throws IOException {
        File arquivoOriginal = new File("inventario.txt");
        File arquivoTemp = new File("inventario.txt.tmp");

        BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
        BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp));

        String linha;
        boolean itemRemovido = false;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 6 && dados[0].equals(usuario) && dados[1].equals(itemUsado.getNome()) && !itemRemovido) {
                itemRemovido = true;
            } else {
                bw.write(linha);
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        if (arquivoOriginal.delete()) {
            arquivoTemp.renameTo(arquivoOriginal);
        }

    }

}
