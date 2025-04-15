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

        // Verificar se o vencedor fugiu (não deve ser possível vencer ao fugir)
        if (vencedor.isGiveUp()) {
            // Inverter vencedor e perdedor
            Personagem temp = vencedor;
            vencedor = perdedor;
            perdedor = temp;
            System.out.println("Como " + perdedor.getNome() + " fugiu, " + vencedor.getNome() + " é o vencedor!");
        } else {
            System.out.println("A batalha terminou! " + vencedor.getNome() + " venceu!");
        }

        System.out.println("Número de turnos da batalha: " + this.turnoNumero);

        // Registrando o ranking
        ranking.empilhar(perdedor);
        ranking.empilhar(vencedor);

        this.batalhaAtiva = false;
    }

    public void executarTurno(Personagem jogador1, Personagem jogador2, String usuarioLogado) throws IOException {
        System.out.println("\n===== TURNO " + this.turnoNumero + " =====");

        // Exibir barra de vida e status dos personagens
        exibirStatusPersonagem(jogador1);
        exibirStatusPersonagem(jogador2);

        // Turno do jogador 1
        System.out.println("\nTurno de " + jogador1.getNome());
        System.out.println("1. Ataque Físico (Stamina: 5)");
        System.out.println("2. Ataque Mágico (Mana: 12)");
        System.out.println("3. Usar Item");
        System.out.println("4. Fugir");
        System.out.print("Escolha: ");

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
            Ataque ataque1 = new Ataque(
                    escolha1 == 1 ? "Ataque Físico" : "Ataque Mágico",
                    escolha1 == 1 ? 65 : 85, // Valores ajustados
                    escolha1 == 1 ? 0 : 12,
                    escolha1 == 1 ? 5 : 0);

            jogador1.ataque(jogador2, ataque1);

            // Verificar imediatamente se o jogador2 morreu após o ataque
            if (!jogador2.isAlive()) {
                this.finalizarBatalha(jogador1, jogador2);
                return;
            }
        }

        // Se o jogador2 desistiu, encerrar a batalha
        if (jogador2.isGiveUp()) {
            System.out.println(jogador2.getNome() + " fugiu da batalha!");
            this.finalizarBatalha(jogador1, jogador2);
            return;
        }

        // Dar um pequeno intervalo para melhor visibilidade
        System.out.println("\n---------------------------");

        // Turno do oponente (com um pouco de IA)
        System.out.println("\nTurno de " + jogador2.getNome());

        // IA simples para o oponente escolher o melhor ataque
        int escolha2;
        if (jogador2.getManaAtual() >= 12 && jogador2.getAtaqueMagico() > jogador2.getAtaqueFisico()) {
            escolha2 = 2; // Usar ataque mágico se tiver mana e seu ataque mágico for maior
            System.out.println(jogador2.getNome() + " escolhe Ataque Mágico!");
        } else if (jogador2.getStaminaAtual() >= 5) {
            escolha2 = 1; // Usar ataque físico se tiver stamina
            System.out.println(jogador2.getNome() + " escolhe Ataque Físico!");
        } else {
            // Se não tiver recursos, escolher o ataque que consome o recurso que tem mais
            escolha2 = (jogador2.getManaAtual() > jogador2.getStaminaAtual()) ? 2 : 1;
            System.out.println(jogador2.getNome() + " escolhe " +
                    (escolha2 == 1 ? "Ataque Físico!" : "Ataque Mágico!"));
        }

        Ataque ataque2 = new Ataque(
                escolha2 == 1 ? "Ataque Físico" : "Ataque Mágico",
                escolha2 == 1 ? 65 : 85, // Valores ajustados
                escolha2 == 1 ? 0 : 12,
                escolha2 == 1 ? 5 : 0);

        jogador2.ataque(jogador1, ataque2);

        // Verificar imediatamente se o jogador1 morreu após o ataque
        if (!jogador1.isAlive()) {
            this.finalizarBatalha(jogador2, jogador1);
            return;
        }

        // Se ambos ainda estão vivos, continuar
        this.turnoNumero++;

        // Regenerar um pouco de recursos a cada turno
        regenerarRecursos(jogador1);
        regenerarRecursos(jogador2);
    }

    private void exibirStatusPersonagem(Personagem p) {
        // Criar barras visuais para representar vida/mana/stamina
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
        // Regenerar 5% da mana e stamina máxima a cada turno
        int regenMana = (int) (p.getManaMaxima() * 0.05);
        int regenStamina = (int) (p.getStaminaMaxima() * 0.05);

        p.setManaAtual(Math.min(p.getManaMaxima(), p.getManaAtual() + regenMana));
        p.setStaminaAtual(Math.min(p.getStaminaMaxima(), p.getStaminaAtual() + regenStamina));
    }

    private void usarItemEmBatalha(String usuario, Personagem jogador) throws IOException {
        Menu.usarItemEmBatalha(usuario, jogador);
    }
}
