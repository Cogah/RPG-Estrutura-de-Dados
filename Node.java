class Node {
    Personagem personagem;
    Item item;
    Node next;

    public Node(Personagem personagem) {
        this.personagem = personagem;
        this.next = null;
    }

    public Node(Item item) {
        this.item = item;
        this.next = null;
    }
}
