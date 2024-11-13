
package snakegame;

public class ListaLigada {

    private Node firstNode;
    private Node lastNode;
    private int size;

    public ListaLigada() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    public void add(int x, int y) {
        Node newNode;
        if (size == 0) {
            newNode = new Node(x, y, null, null);
            size++;
            firstNode = newNode;
            lastNode = newNode;
        } else {
            newNode = new Node(x, y, null, lastNode);

            size++;
            lastNode.setNextNode(newNode);
            lastNode = newNode;
        }
    }

    public void removeLast() {

        if (this.size == 1) {
            this.firstNode.setQuadrado(null);      
        }
        
        else{
            setLastNode(this.lastNode.getPreviousNode()); 
            this.lastNode.setNextNode(null); 
            this.size--; 
        }
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getLastNode() {
        return lastNode;
    }

    public void setLastNode(Node lastNode) {
        this.lastNode = lastNode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
