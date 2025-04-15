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
    
    public Personagem(int vidaMaxima, int ataqueFisico,int ataqueMagico, int defesa, String nome, int nivel, int manaMaxima, int staminaMaxima) {
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
        if(this.vidaAtual <= 0) {
            return false;
        }else{
            return true;
        }
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
        int dano = 0;
        if(movimento.getCustoMana() > 0) {
            dano = (int) ((((2 * nivel) / 5.0 + 2) * ataqueMagico * movimento.getDano()) / alvo.getDefesa() + 50 + 2);
        }else if(movimento.getCustoStamina() > 0) {
            dano = (int) ((((2 * nivel) / 5.0 + 2) * ataqueFisico * movimento.getDano()) / alvo.getDefesa() + 50 + 2);
        }
        return dano;
    }

    public void ataque(Personagem alvo, Ataque movimento) {
        ataques[0] = new Ataque("Ataque Físico", 20, 0, 5);
        ataques[1] = new Ataque("Ataque Mágico", 30, 12, 0);

        if (this.isAlive()) {
            int dano = calcularDano(alvo, movimento);
            alvo.setVidaAtual(alvo.getVidaAtual() - dano);

            manaAtual -= movimento.getCustoMana();
            staminaAtual -= movimento.getCustoStamina();

            System.out.println(this.nome + " usou " + movimento.getNome() + " e agora tem " + this.manaAtual + " de mana e " + this.staminaAtual + " de stamina!");
            System.out.println(alvo.getNome() + " agora tem " + alvo.getVidaAtual() + " de vida!");
            
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
