public class App2 {
    public static void main(String[] args) {
        // Corrigindo a ordem dos parâmetros para adequar ao construtor em
        // Personagem.java
        Personagem cavaleiro = new Personagem(100, 50, 30, 20, "Cavaleiro", 1, 70, 70);
        Personagem mago = new Personagem(80, 20, 60, 20, "Mago", 1, 100, 50);

        // Inicializando atributos que não são definidos no construtor
        cavaleiro.setVidaAtual(cavaleiro.getVidaMaxima());
        cavaleiro.setManaAtual(cavaleiro.getManaMaxima());
        cavaleiro.setStaminaAtual(cavaleiro.getStaminaMaxima());

        mago.setVidaAtual(mago.getVidaMaxima());
        mago.setManaAtual(mago.getManaMaxima());
        mago.setStaminaAtual(mago.getStaminaMaxima());

        // Criando um ambiente de batalha simples
        Jogador jogador1 = new Jogador(1, "Carlos", "1234");
        jogador1.cadastrar();

        // Exibindo personagens
        jogador1.adicionarPersonagem(cavaleiro.getNome());
        jogador1.adicionarPersonagem(mago.getNome());
        jogador1.exibirPersonagens();

        // Criando arena e iniciando batalha
        Arena arena = new Arena();
        arena.iniciarBatalha(cavaleiro, mago);

        // Simulando alguns turnos (em uma implementação completa, isso seria
        // interativo)
        while (arena.batalhaAtiva) {
            arena.executarTurno(cavaleiro, mago);
        }

        // Exibindo ranking final
        arena.ranking.exibirPilha();
    }
}