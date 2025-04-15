public class Jogador {
    private int idJogador;
    private String nome;
    private String senha;
    private int saldoMoedas;
    private FilaPersonagens personagens;

    public int getIdJogador() {
        return idJogador;
    }

    public String getNome() {
        return nome;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }

    public Jogador(int idJogador, String nome, String senha) {
        this.idJogador = idJogador;
        this.nome = nome;
        this.senha = senha;
        this.saldoMoedas = 0;
        this.personagens = new FilaPersonagens();
    }

    public void cadastrar() {
        System.out.println("Jogador cadastrado com sucesso: " + nome);
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    public void adicionarPersonagem(String personagem) {
        personagens.enfileirar(personagem);
        System.out.println("Personagem " + personagem + " adicionado ao jogador " + nome);
    }

    public void adicionarPersonagem(Personagem personagem) {
        personagens.enfileirar(personagem);
        System.out.println("Personagem " + personagem.getNome() + " adicionado ao jogador " + nome);
    }

    public String removerPersonagem() {
        String removido = personagens.desenfileirar();
        if (removido != null) {
            System.out.println("Personagem " + removido + " removido.");
        } else {
            System.out.println("Nenhum personagem para remover.");
        }
        return removido;
    }

    public void exibirPersonagens() {
        System.out.print("Personagens de " + nome + ": ");
        personagens.exibirFila();
    }
}