import java.util.ArrayList;
import java.util.List;

public class GerenciadorJogadores {
    private List<Jogador> jogadores = new ArrayList<>();

    public Jogador cadastrar(String nome, String senha) {
        if (buscarJogadorPorNome(nome) != null) {
            System.out.println("❌ Nome de usuário já cadastrado!");
            return null;
        }
        Jogador novo = new Jogador(0, nome, senha);
        jogadores.add(novo);
        System.out.println("✅ Cadastro realizado com sucesso!");
        return novo;
    }

    public Jogador login(String nome, String senha) {
        Jogador jogador = buscarJogadorPorNome(nome);
        if (jogador != null && jogador.autenticar(senha)) {
            System.out.println("✅ Login bem-sucedido!");
            return jogador;
        }
        System.out.println("❌ Nome ou senha inválidos!");
        return null;
    }

    private Jogador buscarJogadorPorNome(String nome) {
        for (Jogador j : jogadores) {
            if (j.getNome().equalsIgnoreCase(nome)) {
                return j;
            }
        }
        return null;
    }

    public void removerJogador(Jogador jogador) {
        jogadores.remove(jogador);
        System.out.println("✅ Jogador removido com sucesso!");
    }
}
