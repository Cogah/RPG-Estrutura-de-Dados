import java.io.*;
import java.util.*;

public class App {
    private static final String ARQUIVO_USUARIOS = "usuarios.txt";
    private static final String ARQUIVO_PERSONAGENS = "personagens.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String usuarioLogado = null;

        System.out.println("=======================================");
        System.out.println("        RPG BATALHA DE HERÓIS         ");
        System.out.println("=======================================");

        while (usuarioLogado == null) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1) Login");
            System.out.println("2) Cadastrar");
            System.out.println("3) Sair");
            System.out.print("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    usuarioLogado = fazerLogin();
                    break;
                case "2":
                    usuarioLogado = fazerCadastro();
                    break;
                case "3":
                    System.out.println("Saindo do jogo. Até a próxima!");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        System.out.println("\nBem-vindo, " + usuarioLogado + "!");
        menuPrincipal(usuarioLogado);

        scanner.close();
    }

    private static void menuPrincipal(String usuario) throws IOException {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1 - Gerenciar Personagens");
            System.out.println("2 - Iniciar Batalha");
            System.out.println("3 - Visualizar Perfil");
            System.out.println("4 - Sair");
            System.out.print("Opção: ");

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
                    continuar = false;
                    System.out.println("Saindo do jogo. Até a próxima!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuPersonagens(String usuario) throws IOException {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n===== GERENCIAR PERSONAGENS =====");
            System.out.println("1 - Criar novo personagem");
            System.out.println("2 - Listar meus personagens");
            System.out.println("3 - Voltar");
            System.out.print("Opção: ");

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

    private static void menuBatalha(String usuario) throws IOException {
        // Verificar se o usuário tem personagens
        List<String> personagensUsuario = obterPersonagensUsuario(usuario);
        if (personagensUsuario.isEmpty()) {
            System.out.println("Você precisa criar pelo menos um personagem para batalhar!");
            return;
        }

        System.out.println("\n===== MODO DE BATALHA =====");
        System.out.println("1 - PvE (Contra Monstros)");
        System.out.println("2 - PvP (Contra Outros Jogadores)");
        System.out.println("3 - Voltar");
        System.out.print("Opção: ");

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

    private static void batalhaPvE(String usuario, List<String> personagensUsuario) throws IOException {
        // Selecionar personagem do jogador
        Personagem jogador = selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null) return;

        // Criar um monstro para a batalha
        Personagem monstro = criarMonstroAleatorio();
        System.out.println("\nVocê encontrou um " + monstro.getNome() + " nível " + monstro.getNivel() + "!");
        System.out.println("Atributos do monstro: Vida=" + monstro.getVidaMaxima() + ", Ataque Físico=" + monstro.getAtaqueFisico() + ", Ataque Mágico=" + monstro.getAtaqueMagico());
        
        System.out.print("\nDeseja iniciar a batalha? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            iniciarBatalha(jogador, monstro);
            // Após a batalha, se o jogador vencer, ele ganha moedas
            if (jogador.isAlive() && !monstro.isAlive()) {
                int moedasGanhas = 10 + monstro.getNivel() * 5;
                atualizarMoedas(usuario, moedasGanhas);
                System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória!");
            }
        }
    }

    private static void batalhaPvP(String usuario, List<String> personagensUsuario) throws IOException {
        // Lista de outros jogadores
        List<String> outrosJogadores = obterOutrosJogadores(usuario);
        if (outrosJogadores.isEmpty()) {
            System.out.println("Não há outros jogadores disponíveis para batalha!");
            return;
        }

        // Selecionar personagem do jogador
        Personagem jogador = selecionarPersonagemJogador(usuario, personagensUsuario);
        if (jogador == null) return;

        // Selecionar oponente
        System.out.println("\nSelecione o oponente:");
        for (int i = 0; i < outrosJogadores.size(); i++) {
            System.out.println((i + 1) + " - " + outrosJogadores.get(i));
        }
        System.out.print("Opção (0 para voltar): ");
        
        int opcaoOponente;
        try {
            opcaoOponente = Integer.parseInt(scanner.nextLine());
            if (opcaoOponente == 0) return;
            if (opcaoOponente < 1 || opcaoOponente > outrosJogadores.size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um número.");
            return;
        }

        String oponente = outrosJogadores.get(opcaoOponente - 1);
        List<String> personagensOponente = obterPersonagensUsuario(oponente);
        
        if (personagensOponente.isEmpty()) {
            System.out.println("O oponente não tem personagens disponíveis!");
            return;
        }

        // Escolhendo personagem do oponente
        System.out.println("\nPersonagens de " + oponente + ":");
        for (int i = 0; i < personagensOponente.size(); i++) {
            System.out.println((i + 1) + " - " + personagensOponente.get(i));
        }
        System.out.print("Escolha um personagem para enfrentar (0 para voltar): ");
        
        int escolhaPersonagemOponente;
        try {
            escolhaPersonagemOponente = Integer.parseInt(scanner.nextLine());
            if (escolhaPersonagemOponente == 0) return;
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
        iniciarBatalha(jogador, personagemOponente);
        
        // Após a batalha PvP, se o jogador vencer, ele ganha moedas
        if (jogador.isAlive() && !personagemOponente.isAlive()) {
            int moedasGanhas = 20;
            atualizarMoedas(usuario, moedasGanhas);
            System.out.println("\nVocê ganhou " + moedasGanhas + " moedas pela vitória no PvP!");
        }
    }

    private static Personagem selecionarPersonagemJogador(String usuario, List<String> personagensUsuario) {
        System.out.println("\nSelecione seu personagem:");
        for (int i = 0; i < personagensUsuario.size(); i++) {
            System.out.println((i + 1) + " - " + personagensUsuario.get(i));
        }
        System.out.print("Opção (0 para voltar): ");
        
        int opcaoPersonagem;
        try {
            opcaoPersonagem = Integer.parseInt(scanner.nextLine());
            if (opcaoPersonagem == 0) return null;
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

    private static void iniciarBatalha(Personagem jogador, Personagem oponente) {
        System.out.println("\n===== INÍCIO DA BATALHA =====");
        System.out.println(jogador.getNome() + " VS " + oponente.getNome());
        
        Arena arena = new Arena();
        arena.iniciarBatalha(jogador, oponente);
        
        while (arena.batalhaAtiva) {
            arena.executarTurno(jogador, oponente);
            
            System.out.print("\nPressione ENTER para continuar...");
            scanner.nextLine();
        }
        
        System.out.println("\n===== FIM DA BATALHA =====");
        arena.ranking.exibirPilha();
        
        System.out.print("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }

    private static Personagem criarPersonagemAPartirDoNome(String nome) {
        // Criando valores baseados no nome para gerar um personagem único mas consistente
        int hashCode = Math.abs(nome.hashCode());
        Random rand = new Random(hashCode); // Usando o hashCode como seed para consistência
        
        int vida = 80 + rand.nextInt(41); // 80-120
        int ataqueFisico = 30 + rand.nextInt(31); // 30-60
        int ataqueMagico = 20 + rand.nextInt(41); // 20-60
        int defesa = 15 + rand.nextInt(16); // 15-30
        int mana = 50 + rand.nextInt(51); // 50-100
        int stamina = 50 + rand.nextInt(31); // 50-80
        int nivel = 1 + rand.nextInt(5); // 1-5
        
        Personagem p = new Personagem(vida, ataqueFisico, ataqueMagico, defesa, nome, nivel, mana, stamina);
        p.setVidaAtual(vida);
        p.setManaAtual(mana);
        p.setStaminaAtual(stamina);
        
        return p;
    }

    private static Personagem criarMonstroAleatorio() {
        String[] nomesMonstros = {"Goblin", "Ogro", "Dragão", "Esqueleto", "Zumbi", "Bruxa", "Troll", "Slime", "Aranha Gigante", "Fantasma"};
        Random rand = new Random();
        
        String nome = nomesMonstros[rand.nextInt(nomesMonstros.length)];
        int nivel = 1 + rand.nextInt(5); // 1-5
        int vida = 60 + nivel * 10 + rand.nextInt(51); // 70-120 + nível
        int ataqueFisico = 20 + nivel * 3 + rand.nextInt(31); // 25-60 + nível
        int ataqueMagico = 15 + nivel * 2 + rand.nextInt(41); // 15-60 + nível
        int defesa = 10 + nivel + rand.nextInt(16); // 10-30 + nível
        int mana = 40 + nivel * 5 + rand.nextInt(41); // 40-100 + nível
        int stamina = 40 + nivel * 4 + rand.nextInt(31); // 40-80 + nível
        
        Personagem monstro = new Personagem(vida, ataqueFisico, ataqueMagico, defesa, nome, nivel, mana, stamina);
        monstro.setVidaAtual(vida);
        monstro.setManaAtual(mana);
        monstro.setStaminaAtual(stamina);
        
        return monstro;
    }

    private static void criarPersonagem(String usuario) throws IOException {
        System.out.println("\n===== CRIAR NOVO PERSONAGEM =====");
        System.out.print("Nome do personagem: ");
        String nomePersonagem = scanner.nextLine();
        
        // Verificar se já existe um personagem com esse nome para o usuário
        if (personagemJaExiste(usuario, nomePersonagem)) {
            System.out.println("Você já tem um personagem com esse nome!");
            return;
        }
        
        // Salvar o personagem
        FileWriter fw = new FileWriter(ARQUIVO_PERSONAGENS, true);
        fw.write(usuario + ":" + nomePersonagem + "\n");
        fw.close();
        System.out.println("Personagem " + nomePersonagem + " criado com sucesso!");
    }

    private static boolean personagemJaExiste(String usuario, String nomePersonagem) throws IOException {
        List<String> personagens = obterPersonagensUsuario(usuario);
        for (String p : personagens) {
            if (p.equalsIgnoreCase(nomePersonagem)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> obterPersonagensUsuario(String usuario) throws IOException {
        List<String> personagens = new ArrayList<>();
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

    private static List<String> obterOutrosJogadores(String usuarioAtual) throws IOException {
        List<String> jogadores = new ArrayList<>();
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

    private static void exibirPerfil(String usuario) throws IOException {
        System.out.println("\n===== PERFIL DO JOGADOR =====");
        System.out.println("Nome: " + usuario);
        
        // Mostrar quantidade de moedas
        int moedas = obterMoedas(usuario);
        System.out.println("Moedas: " + moedas);
        
        // Mostrar quantidade de personagens
        List<String> personagens = obterPersonagensUsuario(usuario);
        System.out.println("Total de personagens: " + personagens.size());
        
        System.out.print("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }

    private static int obterMoedas(String usuario) throws IOException {
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
                    return Integer.parseInt(dados[1]);
                } catch (NumberFormatException e) {
                    br.close();
                    return 0;
                }
            }
        }
        
        br.close();
        return 0;
    }

    private static void atualizarMoedas(String usuario, int moedasGanhas) throws IOException {
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
                    moedasAtuais = Integer.parseInt(dados[1]);
                } catch (NumberFormatException e) {
                    // Se houver erro de parse, consideramos como 0
                }
                bw.write(usuario + ":" + (moedasAtuais + moedasGanhas));
                if (dados.length > 2) {
                    for (int i = 2; i < dados.length; i++) {
                        bw.write(":" + dados[i]);
                    }
                }
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
            // Substituir o arquivo original pelo temporário
            if (arquivoOriginal.delete()) {
                arquivoTemp.renameTo(arquivoOriginal);
            }
        }
    }

    private static String fazerLogin() throws IOException {
        System.out.print("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Senha: ");
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

    private static String fazerCadastro() throws IOException {
        System.out.print("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        // Verificar se o usuário já existe
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
        
        // Cadastrar o novo usuário com 100 moedas iniciais
        FileWriter fw = new FileWriter(ARQUIVO_USUARIOS, true);
        fw.write(nome + ":" + senha + ":100\n");
        fw.close();
        System.out.println("Cadastro realizado com sucesso! Você recebeu 100 moedas iniciais.");
        return nome;
    }

    private static void exibirPersonagens(String usuario) throws IOException {
        System.out.println("\n===== PERSONAGENS DE " + usuario.toUpperCase() + " =====");
        
        List<String> personagens = obterPersonagensUsuario(usuario);
        
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
        
        System.out.print("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }
}