import java.util.Random;
import java.util.Scanner;

public class Personagem {
    private int vidaAtual;
    private int vidaMaxima;
    private int manaAtual;
    private int manaMaxima;
    private int staminaAtual;
    private int staminaMaxima;
    private int ataqueFisico;
    private int ataqueMagico;
    private int defesa;
    private String nome;
    private int nivel;
    private Ataque[] ataques = new Ataque[4];
    private boolean giveUp = false;

    public Personagem(int vidaMaxima, int ataqueFisico, int ataqueMagico, int defesa, String nome, int nivel,
            int manaMaxima, int staminaMaxima) {
        this.vidaMaxima = vidaMaxima;
        this.ataqueFisico = ataqueFisico;
        this.ataqueMagico = ataqueMagico;
        this.defesa = defesa;
        this.nome = nome;
        this.nivel = nivel;
        this.manaMaxima = manaMaxima;
        this.staminaMaxima = staminaMaxima;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public void setVidaAtual(int vidaAtual) {
        this.vidaAtual = vidaAtual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public int getAtaqueFisico() {
        return ataqueFisico;
    }

    public void setAtaqueFisico(int ataqueFisico) {
        this.ataqueFisico = ataqueFisico;
    }

    public int getAtaqueMagico() {
        return ataqueMagico;
    }

    public void setAtaqueMagico(int ataqueMagico) {
        this.ataqueMagico = ataqueMagico;
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean isAlive() {
        return vidaAtual > 0 && !giveUp;
    }

    public int getManaAtual() {
        return manaAtual;
    }

    public void setManaAtual(int manaAtual) {
        this.manaAtual = manaAtual;
    }

    public int getManaMaxima() {
        return manaMaxima;
    }

    public void setManaMaxima(int manaMaxima) {
        this.manaMaxima = manaMaxima;
    }

    public int getStaminaAtual() {
        return staminaAtual;
    }

    public void setStaminaAtual(int staminaAtual) {
        this.staminaAtual = staminaAtual;
    }

    public int getStaminaMaxima() {
        return staminaMaxima;
    }

    public void setStaminaMaxima(int staminaMaxima) {
        this.staminaMaxima = staminaMaxima;
    }

    public boolean isGiveUp() {
        return giveUp;
    }

    public void setGiveUp(boolean giveUp) {
        this.giveUp = giveUp;
    }

    public int calcularDano(Personagem alvo, Ataque movimento) {
        Random rand = new Random();
        boolean critico = rand.nextInt(100) < 10;
        boolean esquiva = rand.nextInt(100) < 5; 

        if (esquiva) {
            System.out.println(alvo.getNome() + " esquivou do ataque!");
            return 0;
        }

        int danoBase;
        if (movimento.getCustoMana() > 0) {
            danoBase = (int) (this.ataqueMagico * 0.8 * movimento.getDano() / 100.0);
        } else {
            danoBase = (int) (this.ataqueFisico * 0.8 * movimento.getDano() / 100.0);
        }

        double reducaoDano = alvo.getDefesa() / 100.0;
        if (reducaoDano > 0.8)
            reducaoDano = 0.8; 

        int danoFinal = (int) (danoBase * (1.0 - reducaoDano) * (0.8 + rand.nextDouble() * 0.4));

        danoFinal = (int) (danoFinal * (1 + (this.nivel - 1) * 0.05));

        if (critico) {
            danoFinal *= 1.5;
            System.out.println("CRÍTICO! O ataque causou dano extra!");
        }

        return Math.max(1, danoFinal);
    }

    public void ataque(Personagem alvo, Ataque movimento) {
        ataques[0] = new Ataque("Ataque Físico", 65, 0, 5);
        ataques[1] = new Ataque("Ataque Mágico", 85, 12, 0); 

        if (this.isAlive()) {
            if (this.manaAtual < movimento.getCustoMana()) {
                System.out.println(this.nome + " não tem mana suficiente para usar " + movimento.getNome() + "!");
                return;
            }
            if (this.staminaAtual < movimento.getCustoStamina()) {
                System.out.println(this.nome + " não tem stamina suficiente para usar " + movimento.getNome() + "!");
                return;
            }

            int dano = calcularDano(alvo, movimento);

            if (dano > 0) {
                int vidaAnterior = alvo.getVidaAtual();
                alvo.setVidaAtual(Math.max(0, alvo.getVidaAtual() - dano));

                manaAtual -= movimento.getCustoMana();
                staminaAtual -= movimento.getCustoStamina();

                System.out.println(this.nome + " usou " + movimento.getNome() + " causando " + dano + " de dano!");
                System.out.println(alvo.getNome() + " perdeu " + (vidaAnterior - alvo.getVidaAtual()) +
                        " pontos de vida e agora tem " + alvo.getVidaAtual() + "/" + alvo.getVidaMaxima() + " HP");

                if (alvo.getVidaAtual() <= 0) {
                    System.out.println(alvo.getNome() + " foi derrotado!");
                }
            }
        } else {
            System.out.println(this.nome + " está incapacitado e não pode atacar!");
        }
    }

    public Ataque escolherAtaque() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escolha um ataque: ");
        System.out.println("1. Ataque físico" + "\n2. Ataque mágico\n" + "3. Fugir\\n");
        String opcao = sc.nextLine();
        Ataque ataqueEscolhido = null;
        switch (opcao) {
            case "1":
                ataqueEscolhido = ataques[0];
                break;
            case "2":
                ataqueEscolhido = ataques[1];
                break;
            case "3":
                this.setGiveUp(true);
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
        if (ataqueEscolhido != null) {
            System.out.println("Ataque escolhido: " + ataqueEscolhido.getNome());
        } else {
            System.out.println("Nenhum ataque escolhido.");
        }
        sc.close();

        return ataqueEscolhido;
    }
}
