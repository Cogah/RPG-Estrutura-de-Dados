public class Arena {
    
    public void iniciarBatalha(Personagem jogador1, Personagem jogador2) {
        if (jogador1 == null) {
            System.out.println("Jogador 1 está nulo. Não é possível iniciar a batalha.");
            return;
        }else if (jogador2 == null) {
            System.out.println("Jogador 2 está nulo. Não é possível iniciar a batalha.");
            return;
        }

        System.out.println("A batalha entre " + jogador1.getNome() + " e " + jogador2.getNome() + " começou!");
    }

    public void finalizarBatalha(Personagem vencedor, Personagem perdedor) {
        if (vencedor == null) {
            System.out.println("Vencedor está nulo. Não é possível finalizar a batalha.");
            return;
        }else if (perdedor == null) {
            System.out.println("Perdedor está nulo. Não é possível finalizar a batalha.");
            return;
        }

        System.out.println("A batalha terminou! " + vencedor.getNome() + " venceu!");
    }
    
    public void executarTurno(Personagem jogador1, Personagem jogador2) {
        //logica de turno e etc

        if(jogador1.isAlive() == false){
            System.out.println("Jogador 1 perdeu!");
            this.finalizarBatalha(jogador2, jogador1);
        }else if(jogador2.isAlive() == false){
            System.out.println("Jogador 2 perdeu!");
            this.finalizarBatalha(jogador1, jogador2);
        }else{
            System.out.println("Turno executado com sucesso!");
        }
    }

    
}