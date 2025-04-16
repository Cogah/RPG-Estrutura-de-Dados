import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class PersonagemLogica {
    private static final String ARQUIVO_PERSONAGENS = "personagens.txt";

    public void menuPersonagens(String usuario) throws IOException {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n===== GERENCIAR PERSONAGENS =====");
            System.out.println("1 - Criar novo personagem");
            System.out.println("2 - Listar meus personagens");
            System.out.println("3 - Voltar");
            System.out.println("Opção: ");

            String opcao = Menu.getScanner().nextLine();

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

    public void criarPersonagem(String usuario) throws IOException {
        System.out.println("\n===== CRIAR NOVO PERSONAGEM =====");
        System.out.println("Nome do personagem: ");
        String nomePersonagem = Menu.getScanner().nextLine();

        if (personagemJaExiste(usuario, nomePersonagem)) {
            System.out.println("Você já tem um personagem com esse nome!");
            return;
        }

        FileWriter fw = new FileWriter(ARQUIVO_PERSONAGENS, true);
        fw.write(usuario + ":" + nomePersonagem + "\n");
        fw.close();
        System.out.println("Personagem " + nomePersonagem + " criado com sucesso!");
    }

    public boolean personagemJaExiste(String usuario, String nomePersonagem) throws IOException {
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

    public void exibirPersonagens(String usuario) throws IOException {
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
        Menu.getScanner().nextLine();
    }

    public Personagem selecionarPersonagemJogador(String usuario, ArrayList<String> personagensUsuario) {
        System.out.println("\nSelecione seu personagem:");
        for (int i = 0; i < personagensUsuario.size(); i++) {
            System.out.println((i + 1) + " - " + personagensUsuario.get(i));
        }
        System.out.println("Opção (0 para voltar): ");

        int opcaoPersonagem;
        try {
            opcaoPersonagem = Integer.parseInt(Menu.getScanner().nextLine());
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

    public static Personagem criarPersonagemAPartirDoNome(String nome) {
        int hashCode = Math.abs(nome.hashCode());
        Random rand = new Random(hashCode);

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
                "Aranha Gigante", "Fantasma", "Casseb", "Projeto Integrado", "Amazon Hacking" };
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
}