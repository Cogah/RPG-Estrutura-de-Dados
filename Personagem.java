public class Personagem {
    private int vidaAtual;
    private int vidaMaxima;
    private int manaAtual;
    private int manaMaxima;
    private int staminaAtual;
    private int staminaMaxima;
    private int ataque;
    private int defesa;
    private String nome;
    private int nivel;
    
    public Personagem(int vidaMaxima, int ataque, int defesa, String nome, int nivel, int manaMaxima, int staminaMaxima) {
        this.vidaMaxima = vidaMaxima;
        this.ataque = ataque;
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

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
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

    public int calcularDano(Personagem alvo, Ataque movimento) {
        return (int) ((((2 * nivel) / 5.0 + 2) * ataque * movimento.getDano()) / alvo.getDefesa() + 50 + 2);
    }

    public void ataque(Personagem alvo, Ataque movimento) {
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
    
}
