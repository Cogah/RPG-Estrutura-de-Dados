public class App {
    public static void main(String[] args) {
        Personagem cavaleiro = new Personagem("Cavaleiro", 100, 50, 30, 20, 10, 70);
        Personagem mago = new Personagem("Mago", 80, 20, 60, 20, 100, 50);
        Jogador jogador1 = new Jogador(1, "Carlos", "1234");
        jogador1.cadastrar();

        if (jogador1.autenticar("1234")) {
            System.out.println("Autenticação bem-sucedida!");
        } else {
            System.out.println("Falha na autenticação.");
        }


        jogador1.adicionarPersonagem(cavaleiro);
        jogador1.adicionarPersonagem(mago);
        jogador1.exibirPersonagens();
        jogador1.removerPersonagem();
        jogador1.exibirPersonagens();
    }
}
