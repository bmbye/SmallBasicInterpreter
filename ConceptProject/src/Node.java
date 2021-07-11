public class Node {
    private Token token;
    private Node left, right;

    public Node(Token t)
    {
        token = t;
    }

    public Node(Token t, Node r)
    {
        token = t;
        right = r;
    }

    public Node(Node l, Token t)
    {
        left = l;
        token = t;
    }

    public Node(Node l, Token t, Node r)
    {
        left = l;
        token = t;
        right = r;
    }

    public Token getToken() {
        return token;
    }

    public void setLeft(Node l) {
        left = l;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node r) {
        right = r;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString()
    {
        return token.getValue() + " -> " + token.getType();
    }
}
