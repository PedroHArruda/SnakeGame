
package snakegame;


public class Snake {

    private ListaLigada corpo; // Corpo da cobra
    private int incremento = 20; // Tamanho de cada segmento da cobra
    private boolean crescimentoRecente = false;

    public ListaLigada getCorpo() {
        return corpo;
    }

    public void setCorpo(ListaLigada corpo) {
        this.corpo = corpo;
    }

    public int getIncremento() {
        return incremento;
    }

    public void setIncremento(int incremento) {
        this.incremento = incremento;
    }

    public Snake() {
        corpo = new ListaLigada();
        // Posição inicial da cobra, adicionando um primeiro nó
        corpo.add(200, 200);
    }

    /**
     * Método responsável por aumentar o corpo da cobra.
     */
    public void crescer(String direcao) {
        Node ultimoNode = corpo.getLastNode();
        int novoX = ultimoNode.getQuadrado().x;
        int novoY = ultimoNode.getQuadrado().y;

        // Calcula a posição do novo nó com base na direção anterior
        switch (direcao) {
            case "esquerda":
                novoX += incremento;
                break;
            case "direita":
                novoX -= incremento;
                break;
            case "cima":
                novoY += incremento;
                break;
            case "baixo":
                novoY -= incremento;
                break;
        }

        // Adiciona o novo nó na posição calculada
        corpo.add(novoX, novoY);
        crescimentoRecente = true;

    }

    //Método responsável por diminuir o tamanho da cobra, 
    //o método remove o último nó adicionado ao corpo da cobra.
    public void diminuir() {
        
        corpo.removeLast();
   
    }

    //Método responsável pela movimentação da cobra
    public void mover(String direcao) {
        Node nodeAtual = corpo.getLastNode();
        int novoX = nodeAtual.getQuadrado().x;
        int novoY = nodeAtual.getQuadrado().y;

        // Move a cabeça da cobra na direção especificada
        switch (direcao) {
            case "esquerda":
                novoX -= incremento;
                break;
            case "direita":
                novoX += incremento;
                break;
            case "cima":
                novoY -= incremento;
                break;
            case "baixo":
                novoY += incremento;
                break;
            case "parado":
                novoX = nodeAtual.getQuadrado().x;
                novoY = nodeAtual.getQuadrado().y;
                break;
        }

        // Move cada segmento para a posição do segmento anterior
        Node auxiliar = nodeAtual;
        while (auxiliar != null) {
            int anteriorX = auxiliar.getQuadrado().x;
            int anteriorY = auxiliar.getQuadrado().y;

            // Atualiza o quadrado com as novas posições
            auxiliar.getQuadrado().x = novoX;
            auxiliar.getQuadrado().y = novoY;

            novoX = anteriorX;
            novoY = anteriorY;
            auxiliar = auxiliar.getPreviousNode();
        }
    }
    //O método em questão é responsável por detectar uma colisão da cobra com o próprio corpo. 
    public boolean detectarColisaoComCorpo() {
        // Obtém a cabeça da cobra (último nó)
        Node cabeca = corpo.getLastNode();
        int cabecaX = cabeca.getQuadrado().x;
        int cabecaY = cabeca.getQuadrado().y;
        // Se a cobra acabou de crescer, não verifica colisão
        if (crescimentoRecente) {
            crescimentoRecente = false;
            return false;
        }

        // Percorre o corpo da cobra, exceto a cabeça
        Node atual = corpo.getFirstNode();
        while (atual != null && atual != cabeca) {
            int novoX = atual.getQuadrado().x;
            int novoY = atual.getQuadrado().y;

            // Verifica se a cabeça colidiu com o nó atual
            if (cabecaX == novoX && cabecaY == novoY) {

                return true; // Colisão detectada
            }
            atual = atual.getNextNode();
            cabeca = corpo.getLastNode();

        }

        return false; // Nenhuma colisão detectada
    }
}
