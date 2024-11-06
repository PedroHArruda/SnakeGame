/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snakegame;

import java.awt.Color;

/**
 *
 * @author Pedro
 */
public class Node {
    
    private Quadrado quadrado; 
    private Node nextNode, previousNode;
    
    
    Color nodeColor = new Color(40, 82, 56); 
    

    public Node(int x, int y, Node nextNode, Node previousNode) {
        this.quadrado = new Quadrado(20, 20, nodeColor); // Inicializa o quadrado com largura e altura
        this.quadrado.x=x;
        this.quadrado.y=y;
        this.nextNode = nextNode;
        this.previousNode = previousNode;
    }

    public Quadrado getQuadrado() {
        return quadrado;
    }

    public void setQuadrado(Quadrado quadrado) {
        this.quadrado = quadrado;
    }
   

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }
    
    
    
}
