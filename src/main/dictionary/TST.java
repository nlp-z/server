// ====================== TST NODE ============================

import java.util.ArrayList;

class TST_Node {
    char data;
    TST_Node leftNode, midNode, rightNode;
    String content;
    boolean isEnd;
    int weight;

    // Constructor for node given character
    public TST_Node( char data ) {
        this.data = data;
        this.content = "";
        this.isEnd = false;
        this.weight = 1;
        this.leftNode = this.rightNode = this.midNode = null;
    }
}

// ======================= TERNARY SEARCH TREE =======================
public class TST {

    // *********** PRIVATE FIELD *************
    private TST_Node root;

    // *********** CONSTRUCTOR **************
    public TST() {
        this.root = null;
    }

    // ************* PRIVATE METHOD ***************

    private int getWeight( TST_Node node ) {
        if( node == null ) return 0;
        return node.weight;
    }

    private int getBalance( TST_Node node ) {
        if( node == null ) return 0;
        return getWeight( node.leftNode ) - getWeight( node.rightNode );
    }

    private TST_Node leftRotate( TST_Node node ) {
        TST_Node newNode = node.rightNode;
        TST_Node newRight = newNode.leftNode;

        newNode.leftNode = node;
        node.rightNode = newRight;

        node.weight = getWeight( node.leftNode ) >= getWeight( node.rightNode ) ? getWeight( node.leftNode ) + 1 : getWeight( node.rightNode ) + 1;
        newNode.weight = getWeight( newNode.leftNode ) >= getWeight( newNode.rightNode ) ? getWeight( newNode.leftNode ) + 1 : getWeight( newNode.rightNode ) + 1;

        return newNode;
    }

    private TST_Node rightRotate( TST_Node node ) {
        TST_Node newNode = node.leftNode;
        TST_Node newLeft = newNode.rightNode;

        newNode.rightNode = node;
        node.leftNode = newLeft;

        node.weight = getWeight( node.leftNode ) >= getWeight( node.rightNode ) ? getWeight( node.leftNode ) + 1 : getWeight( node.rightNode ) + 1;
        newNode.weight = getWeight( newNode.leftNode ) >= getWeight( newNode.rightNode ) ? getWeight( newNode.leftNode ) + 1 : getWeight( newNode.rightNode ) + 1;

        return newNode;
    }

    // Insert word into TST
    private TST_Node insert( TST_Node currentNode, char[] word, int index, String content ) {
        
        if( currentNode == null ) currentNode = new TST_Node(word[index]);

        if( word[index] < currentNode.data ) currentNode.leftNode = insert( currentNode.leftNode, word, index, content );
        if( word[index] > currentNode.data ) currentNode.rightNode = insert( currentNode.rightNode, word, index, content );
        if( word[index] == currentNode.data ) 
            if( index + 1 < word.length ) currentNode.midNode = insert( currentNode.midNode, word, index + 1, content );
            // This character is the end of word
            else {
                currentNode.isEnd = true;
                currentNode.content = content;
            }

        currentNode.weight = getWeight( currentNode.leftNode ) >= getWeight( currentNode.rightNode ) ? getWeight( currentNode.leftNode ) + 1 : getWeight( currentNode.rightNode ) + 1;
        int balance = getBalance( currentNode );

        if( balance > 1 ) {
            if( word[index] > currentNode.leftNode.data ) currentNode.leftNode = leftRotate(currentNode.leftNode);
            return rightRotate(currentNode);
        }

        if( balance < -1 ) {
            if( word[index] < currentNode.rightNode.data ) currentNode.rightNode = rightRotate(currentNode.rightNode);
            return leftRotate(currentNode);
        } 

        return currentNode;
    }

    // Search word in the TST
    private TST_Node search( TST_Node node, char[] word, int index ) {
        
        if( node == null ) return null;

        if( word[index] < node.data ) return search( node.leftNode, word, index );
        if( word[index] > node.data ) return search( node.rightNode, word, index );
        if( word[index] == node.data ) {
            if( index + 1 < word.length ) return search( node.midNode, word, index + 1 );
            else return node;
        }

        return null;
    }

    // Predict word with given prefix
    private void predict( TST_Node node, String prefix, ArrayList<String> predictedWords ) {
        
        if( node == null ) return;

        predict( node.leftNode, prefix, predictedWords );

        if( node.isEnd ) predictedWords.add( prefix + node.data );
        predict( node.midNode, prefix + node.data, predictedWords );

        predict( node.rightNode, prefix, predictedWords );
    }

    // ************* PUBLIC METHOD ***************
    
    // Insert word into TST
    public void insertWord( String word, String content ) {
        this.root = insert( this.root, word.toCharArray(), 0, content );
    }

    // Search content of the given word
    public String searchWord( String word ) {
        TST_Node result = search( this.root, word.toCharArray(), 0 );
        if( result == null || !result.isEnd ) return "This word is not in dictionary. Your current word: " + word;
        return result.content;
    }

    // Predict word with given prefix 
    public ArrayList<String> predictWord( String prefix ) {
        ArrayList<String> predictedWords = new ArrayList<>();

        TST_Node currentNode = search( this.root, prefix.toCharArray(), 0 );
        if( currentNode == null ) return predictedWords;
        if( currentNode.isEnd ) predictedWords.add( prefix );

        predict( currentNode.midNode, prefix, predictedWords );
        return predictedWords;
    }

    public static void main(String[] args) {
        TST tst = new TST();
        tst.insertWord("cat", "meow meow");
        tst.insertWord("bat", "kak kak kak");
        tst.insertWord("batman", "super herooooo");
        tst.insertWord("basket", "ball");
        tst.insertWord("dog", "gau gau gau");

        System.out.println("Search 'bat': " + tst.searchWord("bat"));
        System.out.println("Search 'man': " + tst.searchWord("man"));
        System.out.println("Search 'batman': " + tst.searchWord("batman"));
        System.out.println("Search 'dog': " + tst.searchWord("dog"));
        System.out.println("Search 'car': " + tst.searchWord("car"));

        System.out.println("\nTernary Search Tree Traversal:");
        ArrayList<String> result = tst.predictWord("ba");
        for (String word : result) {
            System.out.println(word);
        }
    }

}
