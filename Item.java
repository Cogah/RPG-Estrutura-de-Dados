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