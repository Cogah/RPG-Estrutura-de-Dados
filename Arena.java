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

        System.out.println("A batalha terminou! " + vencedor.getNome() + " venceu!");
        System.out.println("Número de turnos da batalha: " + this.turnoNumero);

        // Registrando o ranking
        ranking.empilhar(vencedor);
        ranking.empilhar(perdedor);

        this.batalhaAtiva = false;
    }

    public void executarTurno(Personagem jogador1, Personagem jogador2) {
        System.out.println("\nTurno " + this.turnoNumero + ":");

        System.out.println(jogador1.getNome() + ": " +
                "Vida: " + jogador1.getVidaAtual() + "/" + jogador1.getVidaMaxima() + " - " +
                "Mana: " + jogador1.getManaAtual() + "/" + jogador1.getManaMaxima() + " - " +
                "Stamina: " + jogador1.getStaminaAtual() + "/" + jogador1.getStaminaMaxima());

        System.out.println(jogador2.getNome() + ": " +
                "Vida: " + jogador2.getVidaAtual() + "/" + jogador2.getVidaMaxima() + " - " +
                "Mana: " + jogador2.getManaAtual() + "/" + jogador2.getManaMaxima() + " - " +
                "Stamina: " + jogador2.getStaminaAtual() + "/" + jogador2.getStaminaMaxima());

        // Simplificando para evitar problemas com Scanner
        System.out.println("Turno de " + jogador1.getNome());
        System.out.println("1. Ataque Físico");
        System.out.println("2. Ataque Mágico");
        System.out.print("Escolha: ");
        int escolha1 = Integer.parseInt(scanner.nextLine());

        Ataque ataque1 = new Ataque(
                escolha1 == 1 ? "Ataque Físico" : "Ataque Mágico",
                escolha1 == 1 ? 20 : 30,
                escolha1 == 1 ? 0 : 12,
                escolha1 == 1 ? 5 : 0);

        System.out.println("Turno de " + jogador2.getNome());
        System.out.println("1. Ataque Físico");
        System.out.println("2. Ataque Mágico");
        System.out.print("Escolha: ");
        int escolha2 = Integer.parseInt(scanner.nextLine());

        Ataque ataque2 = new Ataque(
                escolha2 == 1 ? "Ataque Físico" : "Ataque Mágico",
                escolha2 == 1 ? 20 : 30,
                escolha2 == 1 ? 0 : 12,
                escolha2 == 1 ? 5 : 0);

        // Executando ataques
        jogador1.ataque(jogador2, ataque1);
        if (jogador2.isAlive()) {
            jogador2.ataque(jogador1, ataque2);
        }

        System.out.println("Fim do turno! Resultados:");
        if (!jogador1.isAlive() || jogador1.isGiveUp()) {
            System.out.println(jogador1.getNome() + " foi derrotado!");
            this.finalizarBatalha(jogador2, jogador1);
        } else if (!jogador2.isAlive() || jogador2.isGiveUp()) {
            System.out.println(jogador2.getNome() + " foi derrotado!");
            this.finalizarBatalha(jogador1, jogador2);
        } else {
            System.out.println("Ambos jogadores ainda estão vivos!");
            this.turnoNumero++;
        }
    }
}