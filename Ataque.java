public class Ataque{
    private String nome;
    private int dano;
    private int custoMana;
    private int custoStamina;
    
    public Ataque(String nome, int dano, int custoMana, int custoStamina) {
        this.nome = nome;
        this.dano = dano;
        this.custoMana = custoMana;
        this.custoStamina = custoStamina;
    }

    public String getNome() {
        return nome;
    }

    public int getDano() {
        return dano;
    }

    public int getCustoMana() {
        return custoMana;
    }

    public int getCustoStamina() {
        return custoStamina;
    }
    
    
}
