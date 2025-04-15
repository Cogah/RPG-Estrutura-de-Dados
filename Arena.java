public class Arena {
    public int turnoNumero = 0;
    public boolean batalhaAtiva = false;

    public void iniciarBatalha(Personagem jogador1, Personagem jogador2) {
        if (jogador1 == null) {
            System.out.println("Jogador 1 está nulo. Não é possível iniciar a batalha.");
            return;
        }else if (jogador2 == null) {
            System.out.println("Jogador 2 está nulo. Não é possível iniciar a batalha.");
            return;
        }

        System.out.println("A batalha entre " + jogador1.getNome() + " e " + jogador2.getNome() + " começou!");
        this.batalhaAtiva = true;
        this.turnoNumero = 1;
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
        System.out.println("Número de turnos da batalha" + this.turnoNumero);
        this.batalhaAtiva = false;
        this.turnoNumero = 0;
    }
    
    public void executarTurno(Personagem jogador1, Personagem jogador2) {
        System.out.println("Turno" + this.turnoNumero + ":");
        System.out.println("Turno de " + jogador1.getNome() + ":" +
                            "Vida: " + jogador1.getVidaAtual() + "/" + jogador1.getVidaMaxima() +
                            "Mana: " + jogador1.getManaAtual() + "/" + jogador1.getManaMaxima() +
                            "Stamina: " + jogador1.getStaminaAtual() + "/" + jogador1.getStaminaMaxima());
        Ataque ataqueEscolhido1 = jogador1.escolherAtaque();

        System.out.println("Turno de " + jogador2.getNome() + ":" +
                            "Vida: " + jogador2.getVidaAtual() + "/" + jogador2.getVidaMaxima() + 
                            "Mana: " + jogador2.getManaAtual() + "/" + jogador2.getManaMaxima() +
                            "Stamina: " + jogador2.getStaminaAtual() + "/" + jogador2.getStaminaMaxima());
        Ataque ataqueEscolhido2 = jogador2.escolherAtaque(); 

        jogador1.ataque(jogador2, ataqueEscolhido1);
        jogador2.ataque(jogador1, ataqueEscolhido2);

        System.out.println("Fim do turno!\n Resultados:");
        if(jogador1.isAlive() == false || jogador1.isGiveUp() == true){
            System.out.println("Jogador 1 perdeu!");
            this.finalizarBatalha(jogador2, jogador1);
        }else if(jogador2.isAlive() == false || jogador2.isGiveUp() == true){
            System.out.println("Jogador 2 perdeu!");
            this.finalizarBatalha(jogador1, jogador2);
        }else{
            System.out.println("Ambos jogadores ainda estão vivos!");
            this.turnoNumero++;
        }
    }   
}
