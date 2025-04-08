
public class App {
    public static void main(String[] args) {
        Jogador jogador1 = new Jogador(1, "Carlos", "1234");
        jogador1.cadastrar();

        if (jogador1.autenticar("1234")) {
            System.out.println("Autenticação bem-sucedida!");
        } else {
            System.out.println("Falha na autenticação.");
        }

        jogador1.adicionarPersonagem("Cavaleiro");
        jogador1.adicionarPersonagem("Mago");
        jogador1.exibirPersonagens();
        jogador1.removerPersonagem();
        jogador1.exibirPersonagens();
    }
}