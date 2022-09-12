import java.awt.*;

public class CSE214TreeSet<E extends Comparable<E>> extends BinaryTree implements CSE214Set{



    /*
    I am unsure how to implement a Red-Black Tree without a node class as I am unsure how to
    do it without the use of a root node. I can set a private root node however this means
    that every node must be updated once the root node is changed, but again I am unsure what significance it has.
    I tried this and it does not work.

    I referred to the lecture slides however the rotation methods shown access a root.

    I have tried it with a node class however the BinaryTree toString method becomes obsolete.

    I have left it as a binary search tree with the size and contain methods.
    I apologize however I was super confused with the instructions and lack knowledge.
     */
    //Color class is already implemented in java

    private Color color;
    private E value;
    private CSE214TreeSet<E> left;
    private CSE214TreeSet<E> right;
    private CSE214TreeSet<E> parent;

    public CSE214TreeSet(E e){
        this.value = e;
        this.color = Color.RED;
        //this.root = this;
    }

    public CSE214TreeSet(){}

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void changeColor(){
        if(color == Color.RED)
            this.setColor(Color.BLACK);
        else
            this.setColor(Color.RED);
    }

    public CSE214TreeSet<E> getParent(){
        return this.parent;
    }

    public void setParent(CSE214TreeSet<E> node){
        this.parent = node;
    }

    public E getValue(){return value;}

    public void setValue(Object value){this.value = (E) value;}

    public CSE214TreeSet<E> getLeft(){return left;}

    public void setLeft(CSE214TreeSet left){
        this.left = left;
        this.left.setParent(this);
    }

    public CSE214TreeSet<E> getRight(){return right;}

    public void setRight(CSE214TreeSet<E> right) {
        this.right = right;
        this.right.setParent(this);
    }

    private static <E extends Comparable<E>> String traversePreOrder(CSE214TreeSet<E> root) {
        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getValue());

        String pointerRight = "└──";
        String pointerLeft  = (root.getRight() != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.getLeft(), root.getRight() != null);
        traverseNodes(sb, "", pointerRight, root.getRight(), false);

        return sb.toString();
    }

    private static <E extends Comparable<E>> void traverseNodes(StringBuilder sb, String padding, String pointer, CSE214TreeSet<E> node, boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getValue());
            sb.append(" [").append(node.getColor()).append("]");

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight   = "└──";
            String pointerLeft    = (node.getRight() != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeft(), node.getRight() != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.getRight(), false);
        }
    }

    public boolean is_left_child(){
        return this.equals(parent.getLeft());
    }

    @Override
    public int size() {
        return size(this);
    }

    public int size(CSE214TreeSet<E> node){
        int size = 0;
        if(node == null)
            return 0;
        else {
            size += size(node.getLeft());
            size += size(node.getRight());
            return size+1;
        }
    }

    @Override
    public boolean contains(Object o) {
        //Maybe Wrong Exception
        if(o == null)
            throw new NullPointerException();
        else
            return contains(this, o);
    }

    public boolean contains(CSE214TreeSet<E> node, Object o){
        E value = (E) o;
        while(node != null){
            int compare = value.compareTo(node.getValue());
            if(compare > 0)
                node = node.getRight();
            else if(compare < 0)
                node = node.getLeft();
            else if(compare == 0)
                return true;
        }
        return false;
    }

    @Override
    public boolean add(Object o) {
        //Check for duplicates.
        if(contains(o))
            return false;

        CSE214TreeSet<E> insertnode = new CSE214TreeSet<>((E) o);
        CSE214TreeSet<E> new_node = add(this, insertnode);
        //recolor and rotate
        recolorandrotate(insertnode);
        if(new_node != null) {
            return true;
        }
        return false;

    }

    public CSE214TreeSet<E> add(CSE214TreeSet<E> node, CSE214TreeSet<E> insertnode){
        if(node == null) {
            node = new CSE214TreeSet<E>(insertnode.getValue());
            return node;
        }

        int compare = insertnode.getValue().compareTo(node.getValue());
        if(compare < 0) {
            node.setLeft(add(node.getLeft(), insertnode));
            node.getLeft().setParent(node);
        }
        else if(compare > 0) {
            node.setRight(add(node.getRight(), insertnode));
            node.getRight().setParent(node);
        }

        return node;

    }

    public void handleLeftSituation(CSE214TreeSet<E> node, CSE214TreeSet<E> parent, CSE214TreeSet<E> grandparent){
        if (!node.is_left_child())
            left_rotate(parent);
        parent.changeColor();
        grandparent.changeColor();
        right_rotate(grandparent);
        recolorandrotate(node.is_left_child() ? parent : grandparent);
    }

    public void handleRightSituation(CSE214TreeSet<E> node, CSE214TreeSet<E> parent, CSE214TreeSet<E> grandparent){
        if (node.is_left_child())
            right_rotate(parent);
        parent.changeColor();
        grandparent.changeColor();
        left_rotate(grandparent);
        recolorandrotate(node.is_left_child() ? grandparent : parent);
    }

    //maybe set to static
    public void recolorandrotate(CSE214TreeSet<E> node){
        CSE214TreeSet<E> parent = node.getParent();

        //it's the root node
        if(parent == null) {
            node.setColor(Color.BLACK);
            return;
        }
        else{
            if(parent.getColor() == Color.RED){
                CSE214TreeSet<E> grandparent = node.getParent().getParent();
                CSE214TreeSet<E> uncle = parent.is_left_child() ? grandparent.getRight() : grandparent.getLeft();

                if(uncle != null && uncle.getColor() == Color.RED){
                    change_colors(parent, uncle, grandparent);
                }else if(parent.is_left_child()){
                    handleLeftSituation(node, parent, grandparent);
                }else if(!parent.is_left_child()){
                    handleRightSituation(node, parent, grandparent);
                }
            }
        }
    }

    public void updateChildren(CSE214TreeSet<E> node, CSE214TreeSet<E> new_node){
        if(node.getParent() == null){
            node.setValue(new_node.getValue());
            node.setLeft(new_node.getLeft());
            node.setRight(new_node.getRight());
            node.setColor(new_node.getColor());
            node.setParent(new_node.getParent());
        }
        else if(node.is_left_child()){
            node.getParent().setLeft(new_node);
        }
        else{
            node.getParent().setRight(new_node);
        }
    }

    public void right_rotate(CSE214TreeSet<E> node){
        CSE214TreeSet<E> left = node.getLeft();
        node.setLeft(left.getRight());
        if(node.getLeft() != null){
            node.getLeft().setParent(node);
        }
        left.setRight(node);
        left.setParent(node.getParent());
        updateChildren(node, left);
        node.setParent(left);

    }
    public void left_rotate(CSE214TreeSet<E> node){
        CSE214TreeSet<E> right = node.getRight();
        node.setRight(right.getLeft());
        if(node.getRight() != null){
            node.getRight().setParent(node);
        }
        right.setLeft(node);
        right.setParent(node.getParent());
        /*
        if(node.getParent() == null){
            //root should be changed to right;
        }else{
            if(node == node.getParent().getLeft())
                node.getParent().setLeft(right);
            else
                node.getParent().setRight(right);
        }*/
        updateChildren(node, right);
        node.setParent(right);
    }


    public void change_colors(CSE214TreeSet<E> parent, CSE214TreeSet<E> uncle, CSE214TreeSet<E> grandparent){
        parent.changeColor();
        uncle.changeColor();
        grandparent.changeColor();
        recolorandrotate(grandparent);
    }


    @Override
    public String toString() {
        return traversePreOrder(this);
    }

    public static void main(String... args) {
        CSE214TreeSet<String> root = new CSE214TreeSet<String>("root");

        CSE214TreeSet<String> node1 = new CSE214TreeSet<String>("node1");
        CSE214TreeSet<String> node2 = new CSE214TreeSet<String>("node2");
        root.add("node8");
        root.add("node2");

        CSE214TreeSet<String> node3 = new CSE214TreeSet<String>("node3");
        CSE214TreeSet<String> node4 = new CSE214TreeSet<String>("node4");
        root.add("node3");
        root.add("node4");

        //root.add(new CSE214TreeSet<String>("node5"));
        //root.add(new CSE214TreeSet<String>("node6"));

        CSE214TreeSet<String> node7 = new CSE214TreeSet<String>("node7");
        System.out.println(root.add("node7"));
        root.add("node1");
        //root.add(new CSE214TreeSet<String>("node8"));
        //root.add(new CSE214TreeSet<String>("node9"));

        System.out.println(root.toString());

        CSE214TreeSet<Integer> boot = new CSE214TreeSet<Integer>(4);


        boot.add(1);
        boot.add(3);
        boot.add(2);
        boot.add(5);
        System.out.println(boot.size());
        System.out.println(boot.toString());
    }

}
