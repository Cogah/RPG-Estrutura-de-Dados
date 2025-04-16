import java.io.IOException;
import java.util.Scanner;

public class Arena {
    public int turnoNumero = 0;
    public boolean batalhaAtiva = false;
    public FilaPersonagens ordemBatalha = new FilaPersonagens();
    public PilhaPersonagens ranking = new PilhaPersonagens();
    private Scanner scanner = new Scanner(System.in);

    public Arena() {
        this.turnoNumero = 0;
        this.batalhaAtiva = false;
    }

    public void iniciarBatalha(Personagem jogador1, Personagem jogador2) {
        if (jogador1 == null || jogador2 == null) {
            System.out.println("Jogadores inválidos. Não é possível iniciar a batalha.");
            return;
        }

        System.out.println("A batalha entre " + jogador1.getNome() + " e " + jogador2.getNome() + " começou!");
        ordemBatalha.enfileirar(jogador1);
        ordemBatalha.enfileirar(jogador2);
        this.batalhaAtiva = true;
        this.turnoNumero = 1;
    }

    public void finalizarBatalha(Personagem vencedor, Personagem perdedor) {
        if (vencedor == null || perdedor == null) {
            System.out.println("Vencedor ou perdedor inválido. Não é possível finalizar a batalha.");
            return;
        }

        if (vencedor.isGiveUp()) {
            Personagem temp = vencedor;
            vencedor = perdedor;
            perdedor = temp;
            System.out.println("Como " + perdedor.getNome() + " fugiu, " + vencedor.getNome() + " é o vencedor!");
        } else {
            System.out.println("A batalha terminou! " + vencedor.getNome() + " venceu!");
        }

        System.out.println("Número de turnos da batalha: " + this.turnoNumero);

        ranking.empilhar(perdedor);
        ranking.empilhar(vencedor);

        this.batalhaAtiva = false;
    }

    public void executarTurno(Personagem jogador1, Personagem jogador2, String usuarioLogado) throws IOException {
        System.out.println("\n===== TURNO " + this.turnoNumero + " =====");

        exibirStatusPersonagem(jogador1);
        exibirStatusPersonagem(jogador2);

        System.out.println("\nTurno de " + jogador1.getNome());
        System.out.println("1 -> Ataque Físico (Custo de Stamina: 5)");
        System.out.println("2 -> Ataque Mágico (Custo de Mana: 12)");
        System.out.println("3 -> Usar Item");
        System.out.println("4 -> Fugir");
        System.out.println("Escolha: ");

        int escolha1;
        try {
            escolha1 = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Usando Ataque Físico por padrão.");
            escolha1 = 1;
        }

        if (escolha1 == 3) {
            usarItemEmBatalha(usuarioLogado, jogador1);
        } else if (escolha1 == 4) {
            jogador1.setGiveUp(true);
            System.out.println(jogador1.getNome() + " fugiu da batalha!");
            this.finalizarBatalha(jogador2, jogador1);
            return;
        } else {
            Ataque ataque1;
            if (escolha1 == 1) {
                ataque1 = new Ataque("Ataque Físico", 65, 0, 5);
            } else {
                ataque1 = new Ataque("Ataque Mágico", 85, 12, 0);
            }

            jogador1.ataque(jogador2, ataque1);

            if (!jogador2.isAlive()) {
                this.finalizarBatalha(jogador1, jogador2);
                return;
            }
        }

        if (jogador2.isGiveUp()) {
            System.out.println(jogador2.getNome() + " fugiu da batalha!");
            this.finalizarBatalha(jogador1, jogador2);
            return;
        }

        System.out.println("\n---------------------------");

        System.out.println("\nTurno de " + jogador2.getNome());

        int escolha2;
        if (jogador2.getManaAtual() >= 12 && jogador2.getAtaqueMagico() > jogador2.getAtaqueFisico()) {
            escolha2 = 2;
            System.out.println(jogador2.getNome() + " escolhe Ataque Mágico!");
        } else if (jogador2.getStaminaAtual() >= 5) {
            escolha2 = 1;
            System.out.println(jogador2.getNome() + " escolhe Ataque Físico!");
        } else {
            escolha2 = (jogador2.getManaAtual() > jogador2.getStaminaAtual()) ? 2 : 1;
            System.out.println(jogador2.getNome() + " escolhe " +
                    (escolha2 == 1 ? "Ataque Físico!" : "Ataque Mágico!"));
        }

        Ataque ataque2;
        if (escolha2 == 1) {
            ataque2 = new Ataque("Ataque Físico", 65, 0, 5);
        } else {
            ataque2 = new Ataque("Ataque Mágico", 85, 12, 0);
        }

        jogador2.ataque(jogador1, ataque2);

        if (!jogador1.isAlive()) {
            this.finalizarBatalha(jogador2, jogador1);
            return;
        }

        this.turnoNumero++;

        regenerarRecursos(jogador1);
        regenerarRecursos(jogador2);
    }

    private void exibirStatusPersonagem(Personagem p) {
        int barraVidaTamanho = 20;
        int barraManaStaminaTamanho = 10;

        double percentualVida = (double) p.getVidaAtual() / p.getVidaMaxima();
        double percentualMana = (double) p.getManaAtual() / p.getManaMaxima();
        double percentualStamina = (double) p.getStaminaAtual() / p.getStaminaMaxima();

        int barraVidaCheia = (int) (percentualVida * barraVidaTamanho);
        int barraManaCheia = (int) (percentualMana * barraManaStaminaTamanho);
        int barraStaminaCheia = (int) (percentualStamina * barraManaStaminaTamanho);

        StringBuilder barraVida = new StringBuilder("[");
        for (int i = 0; i < barraVidaTamanho; i++) {
            barraVida.append(i < barraVidaCheia ? "█" : "░");
        }
        barraVida.append("]");

        StringBuilder barraMana = new StringBuilder("[");
        for (int i = 0; i < barraManaStaminaTamanho; i++) {
            barraMana.append(i < barraManaCheia ? "█" : "░");
        }
        barraMana.append("]");

        StringBuilder barraStamina = new StringBuilder("[");
        for (int i = 0; i < barraManaStaminaTamanho; i++) {
            barraStamina.append(i < barraStaminaCheia ? "█" : "░");
        }
        barraStamina.append("]");

        System.out.println("\n" + p.getNome() + " (Nível " + p.getNivel() + ")");
        System.out.println("Vida:    " + barraVida + " " + p.getVidaAtual() + "/" + p.getVidaMaxima());
        System.out.println("Mana:    " + barraMana + " " + p.getManaAtual() + "/" + p.getManaMaxima());
        System.out.println("Stamina: " + barraStamina + " " + p.getStaminaAtual() + "/" + p.getStaminaMaxima());
        System.out.println("Ataque Físico: " + p.getAtaqueFisico() + " | Ataque Mágico: " + p.getAtaqueMagico()
                + " | Defesa: " + p.getDefesa());
    }

    private void regenerarRecursos(Personagem p) {
        int regenMana = (int) (p.getManaMaxima() * 0.05);
        int regenStamina = (int) (p.getStaminaMaxima() * 0.05);

        p.setManaAtual(Math.min(p.getManaMaxima(), p.getManaAtual() + regenMana));
        p.setStaminaAtual(Math.min(p.getStaminaMaxima(), p.getStaminaAtual() + regenStamina));
    }

    private void usarItemEmBatalha(String usuario, Personagem jogador) throws IOException {
        Menu.usarItemEmBatalha(usuario, jogador);
    }
}
