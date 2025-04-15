public class Item {
    private String nome;
    private String descricao;
    private int custo;
    private boolean consumido = false;
    private int recoverMana;
    private int recoverStamina;
    private int recoverVida;

    public Item(String nome, int custo, int recoverMana, int recoverStamina, int recoverVida) {
        this.nome = nome;
        this.custo = custo;
        this.recoverMana = recoverMana;
        this.recoverStamina = recoverStamina;
        this.recoverVida = recoverVida;
    }

    public String getNome() {
        return nome;
    }

    public int getCusto() {
        return custo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public boolean isConsumido() {
        return consumido;
    }

    public void setConsumido(boolean consumido) {
        this.consumido = consumido;
    }

    public int getRecoverMana() {
        return recoverMana;
    }

    public void setRecoverMana(int recoverMana) {
        this.recoverMana = recoverMana;
    }

    public int getRecoverStamina() {
        return recoverStamina;
    }

    public void setRecoverStamina(int recoverStamina) {
        this.recoverStamina = recoverStamina;
    }

    public int getRecoverVida() {
        return recoverVida;
    }

    public void setRecoverVida(int recoverVida) {
        this.recoverVida = recoverVida;
    }

    public void usar(Personagem personagem) {
        if (!consumido) {
            personagem.setVidaAtual(personagem.getVidaAtual() + recoverVida);
            personagem.setManaAtual(personagem.getManaAtual() + recoverMana);
            personagem.setStaminaAtual(personagem.getStaminaAtual() + recoverStamina);

            // Não permitir que atributos excedam o máximo
            if (personagem.getVidaAtual() > personagem.getVidaMaxima()) {
                personagem.setVidaAtual(personagem.getVidaMaxima());
            }
            if (personagem.getManaAtual() > personagem.getManaMaxima()) {
                personagem.setManaAtual(personagem.getManaMaxima());
            }
            if (personagem.getStaminaAtual() > personagem.getStaminaMaxima()) {
                personagem.setStaminaAtual(personagem.getStaminaMaxima());
            }

            consumido = true;
            System.out.println(personagem.getNome() + " usou " + nome + "!");
        } else {
            System.out.println("Este item já foi consumido!");
        }
    }
}