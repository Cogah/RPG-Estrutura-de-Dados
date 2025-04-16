import java.io.*;
import java.util.ArrayList;

public class UsuarioLogica {
    private static final String ARQUIVO_USUARIOS = "usuarios.txt";

    public String fazerLogin() throws IOException {
        System.out.println("Nome de usuário: ");
        String nome = Menu.getScanner().nextLine();
        System.out.println("Senha: ");
        String senha = Menu.getScanner().nextLine();

        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            System.out.println("Nenhum usuário cadastrado ainda!");
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 2 && dados[0].equals(nome) && dados[1].equals(senha)) {
                br.close();
                System.out.println("Login realizado com sucesso!");
                return nome;
            }
        }

        br.close();
        System.out.println("Nome de usuário ou senha incorretos!");
        return null;
    }

    public String fazerCadastro() throws IOException {
        System.out.println("Nome de usuário: ");
        String nome = Menu.getScanner().nextLine();
        System.out.println("Senha: ");
        String senha = Menu.getScanner().nextLine();

        File arquivo = new File(ARQUIVO_USUARIOS);
        if (arquivo.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(":");
                if (dados.length >= 1 && dados[0].equals(nome)) {
                    br.close();
                    System.out.println("Nome de usuário já existe!");
                    return null;
                }
            }

            br.close();
        }

        FileWriter fw = new FileWriter(ARQUIVO_USUARIOS, true);
        fw.write(nome + ":" + senha + ":100\n");
        fw.close();
        System.out.println("Cadastro realizado com sucesso! Você recebeu 100 moedas iniciais.");
        return nome;
    }

    public void exibirPerfil(String usuario) throws IOException {
        System.out.println("\n===== PERFIL DO JOGADOR =====");
        System.out.println("Nome: " + usuario);

        int moedas = obterMoedas(usuario);
        System.out.println("Moedas: " + moedas);

        ArrayList<String> personagens = PersonagemLogica.obterPersonagensUsuario(usuario);
        System.out.println("Total de personagens: " + personagens.size());

        System.out.println("\nPressione ENTER para voltar...");
        Menu.getScanner().nextLine();
    }

    public int obterMoedas(String usuario) throws IOException {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            return 0;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 3 && dados[0].equals(usuario)) {
                try {
                    br.close();
                    return Integer.parseInt(dados[2]);
                } catch (NumberFormatException e) {
                    br.close();
                    return 0;
                }
            }
        }

        br.close();
        return 0;
    }

    public void atualizarMoedas(String usuario, int moedasGanhas) throws IOException {
        File arquivoOriginal = new File(ARQUIVO_USUARIOS);
        File arquivoTemp = new File(ARQUIVO_USUARIOS + ".tmp");

        BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
        BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp));

        String linha;
        boolean usuarioEncontrado = false;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 3 && dados[0].equals(usuario)) {
                int moedasAtuais = 0;
                try {
                    moedasAtuais = Integer.parseInt(dados[2]);
                } catch (NumberFormatException e) {
                    // Se não conseguir converter, assume 0
                }
                bw.write(usuario + ":" + dados[1] + ":" + (moedasAtuais + moedasGanhas));
                bw.newLine();
                usuarioEncontrado = true;
            } else {
                bw.write(linha);
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        if (usuarioEncontrado) {
            if (arquivoOriginal.delete()) {
                arquivoTemp.renameTo(arquivoOriginal);
            }
        }
    }

    public ArrayList<String> obterOutrosJogadores(String usuarioAtual) throws IOException {
        ArrayList<String> jogadores = new ArrayList<>();
        File arquivo = new File(ARQUIVO_USUARIOS);

        if (!arquivo.exists()) {
            return jogadores;
        }

        BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(":");
            if (dados.length >= 1 && !dados[0].equals(usuarioAtual)) {
                jogadores.add(dados[0]);
            }
        }

        br.close();
        return jogadores;
    }
}