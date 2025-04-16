import java.io.*;
import java.util.ArrayList;

public class LojaLogica {
    private UsuarioLogica usuarioLogica;

    public LojaLogica() {
        this.usuarioLogica = new UsuarioLogica();
    }

    public void menuLoja(String usuario) throws IOException {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n===== LOJA DE ITENS =====");
            System.out.println("Suas moedas: " + usuarioLogica.obterMoedas(usuario));
            System.out.println("1 - Ver itens disponíveis");
            System.out.println("2 - Comprar item");
            System.out.println("3 - Ver meu inventário");
            System.out.println("4 - Voltar");
            System.out.println("Opção: ");

            String opcao = Menu.getScanner().nextLine();

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

    public void exibirItensDisponiveis() {
        System.out.println("\n===== ITENS DISPONÍVEIS =====");
        System.out.println("1 - Poção de Vida (Custo: 10, Recupera: Vida=30)");
        System.out.println("2 - Poção de Mana (Custo: 10, Recupera: Mana=30)");
        System.out.println("3 - Poção de Stamina (Custo: 10, Recupera: Stamina=30)");
        System.out.println("4 - Poção Grande de Vida (Custo: 30, Recupera: Vida=80)");
        System.out.println("5 - Elixir Mágico (Custo: 50, Recupera: Vida=30, Mana=50)");
        System.out.println("6 - Bebida Energética (Custo: 40, Recupera: Stamina=80, Vida=10)");

        System.out.println("\nPressione ENTER para voltar...");
        Menu.getScanner().nextLine();
    }

    public void comprarItem(String usuario) throws IOException {
        System.out.println("\n===== COMPRAR ITEM =====");
        System.out.println("Suas moedas: " + usuarioLogica.obterMoedas(usuario));
        exibirItensDisponiveis();

        System.out.println("Escolha o número do item que deseja comprar (0 para cancelar): ");
        int escolha;
        try {
            escolha = Integer.parseInt(Menu.getScanner().nextLine());
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

        int moedasJogador = usuarioLogica.obterMoedas(usuario);
        if (moedasJogador < custoItem) {
            System.out.println("Você não tem moedas suficientes para comprar esse item!");
            return;
        }

        usuarioLogica.atualizarMoedas(usuario, -custoItem);
        adicionarItemInventario(usuario, itemComprado);
        System.out.println("Item " + itemComprado.getNome() + " comprado com sucesso!");
    }

    public void adicionarItemInventario(String usuario, Item item) throws IOException {
        File arquivo = new File("inventario.txt");
        FileWriter fw = new FileWriter(arquivo, true);
        fw.write(usuario + ":" + item.getNome() + ":" + item.getCusto() + ":" +
                item.getRecoverMana() + ":" + item.getRecoverStamina() + ":" +
                item.getRecoverVida() + "\n");
        fw.close();
    }

    public void exibirInventario(String usuario) throws IOException {
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
        Menu.getScanner().nextLine();
    }

    public ArrayList<Item> obterItensUsuario(String usuario) throws IOException {
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

    public void usarItemEmBatalha(String usuario, Personagem jogador) throws IOException {
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
            escolha = Integer.parseInt(Menu.getScanner().nextLine());
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

    public void removerItemInventario(String usuario, Item itemUsado) throws IOException {
        File arquivoOriginal = new File("inventario.txt");
        File arquivoTemp = new File("inventario.txt.tmp");

        BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
        BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp));

        String linha;
        boolean itemRemovido = false;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length == 6 && dados[0].equals(usuario) && dados[1].equals(itemUsado.getNome())
                    && !itemRemovido) {
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