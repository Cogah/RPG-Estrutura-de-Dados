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
    
}
