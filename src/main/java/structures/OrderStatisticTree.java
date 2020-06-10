package structures;

public class OrderStatisticTree{ // get rank of node from left to right
    private ColorSizeNode root = null;
    private ColorSizeNode sentinel = new ColorSizeNode(0, Color.BLACK);
    // sentinel: denote leaf and parent of root
    public static class ColorSizeNode{
        private double key;
        private Color color;
        private ColorSizeNode parent;
        private ColorSizeNode left;
        private ColorSizeNode right;
        private int size; // subtree size

        ColorSizeNode(double key){
            this.key = key;
            this.color = Color.RED;
            int size = 1;
        }

        ColorSizeNode(double key, Color color){
            this.key = key;
            this.color = color;
            int size = 1;
        }

        ColorSizeNode(double key, int size){
            this.key = key;
            this.color = Color.RED;
            this.size = size;
        }

        ColorSizeNode(double key, Color color, int size){
            this.key = key;
            this.color = color;
            this.size = size;
        }

        public int getSize() { return this.size; }

        public boolean isRed(){ return this.color == Color.RED; }

        public boolean isBlack(){ return this.color == Color.BLACK; }

        public Color getColor(){ return this.color; }

        private void setRed(){ this.color = Color.RED; }

        private void setBlack() { this.color = Color.BLACK; }

        public double getKey(){ return this.key; }

        public ColorSizeNode getParent(){ return this.parent; }

        public ColorSizeNode getLeft(){ return this.left; }

        public ColorSizeNode getRight(){ return this.right; }
    }
    enum Color{
        RED,
        BLACK
    }

    public double rankGetKey(int rank){
        ColorSizeNode n = rankSelect(rank);
        return n.key;
    }
    private ColorSizeNode rankSelect(int ith){
        int rank = root.left.size + 1;
        if(rank == ith){
            return root;
        }else if(ith < rank){
            return rankSelectChild(root.left, ith);
        }else{
            return rankSelectChild(root.right, ith -rank);
        }
    }
    private ColorSizeNode rankSelectChild(ColorSizeNode current, int ith){
        int rank = current.left.size + 1;
        if(rank == ith){
            return current;
        }else if(ith < rank){
            return rankSelectChild(current.left, ith);
        }else{
            return rankSelectChild(current.right, ith -rank);
        }
    }

    public int keyGetRank(double key){
        ColorSizeNode n = search(key);
        return getNodeRank(n);
    }
    private int getNodeRank(ColorSizeNode node){
        int rank = node.left.size + 1;
        while(node != root){
            if(node == node.parent.right){
                rank += node.parent.left.size + 1;
            }
            node = node.parent;
        }
        return rank;
    }

    public ColorSizeNode search(double key){
        if(root == null | root == sentinel){
            return null;
        }
        return search(root, key);
    }
    private ColorSizeNode search(ColorSizeNode n, double key){
        if(n.key == key){
            return n;
        }else if(key < n.key & n.left != sentinel){
            return search(n.left, key);
        }else if(key > n.key & n.right != sentinel){
            return search(n.right, key);
        }
        return null;
    }
    public ColorSizeNode getRoot() { return this.root; }
    private void setRoot(ColorSizeNode n){
        root = n;
        n.parent = sentinel;
    }

    public ColorSizeNode getMinimum(){
        if(root != null | sentinel != root) {
            return getMinimum(root);
        }else{
            return null;
        }
    }
    public ColorSizeNode getMaximum(){
        if(root != null | root != sentinel) {
            return getMaximum(root);
        }else{
            return null;
        }
    }

    public void insertKey(double key){
        ColorSizeNode n = new ColorSizeNode(key); // default red
        insertNode(n);
    }
    private void insertNode(ColorSizeNode n){
        if(n == null | n == sentinel) return;

        if(root == null | root == sentinel){
            n.setBlack();
            setRoot(n);
            root.right = sentinel;
            root.left = sentinel;
        }else{
            ColorSizeNode store = sentinel;
            ColorSizeNode ptr = root;
            while(ptr != sentinel){
                store = ptr;
                ptr.size = ptr.size + 1;
                if(n.key < ptr.key){
                    ptr = ptr.left;
                }else{
                    ptr = ptr.right;
                }
            }
            n.parent = store;
            if(n.key < store.key){
                store.left = n;
            }else{
                store.right = n;
            }
            n.left = sentinel;
            n.right = sentinel;
        }
        insertFixUp(n);
    }
    private void insertFixUp(ColorSizeNode ptr){
        while(ptr.parent.isRed()){
            if(ptr.parent == ptr.parent.parent.left){
                ColorSizeNode right = ptr.parent.parent.right;
                if(right.isRed()){ // case1: sibling is red
                    ptr.parent.setBlack();
                    right.setBlack();
                    ptr.parent.parent.setRed();
                    ptr = ptr.parent.parent;
                    continue;
                }else if(ptr == ptr.parent.right){ //case 2 convert to case 3
                    ptr = ptr.parent;
                    leftRotate(ptr);
                }
                ptr.parent.setBlack(); // case3
                ptr.parent.parent.setRed();
                rightRotate(ptr.parent.parent); // ptr.getParent will be black and then break
                ptr = ptr.getParent();
            }else{
                ColorSizeNode left = ptr.parent.parent.left;
                if(left.isRed()){
                    ptr.parent.setBlack();
                    left.setBlack();
                    ptr.parent.parent.setRed();
                    ptr = ptr.parent.parent;
                    continue;
                }else if(ptr == ptr.parent.left){
                    ptr = ptr.parent;
                    rightRotate(ptr);
                }
                ptr.parent.setBlack();
                ptr.parent.parent.setRed();
                leftRotate(ptr.parent.parent);
                ptr = ptr.getParent();
            }
        }
        root.setBlack();
    }

    public void deleteKey(double key){
        ColorSizeNode target = search(key);
        delete(target);
    }
    private void delete(ColorSizeNode target) {
        if(target == null | target == sentinel){
            return;
        }
        ColorSizeNode ptr = target;
        Color ptr_color = ptr.color;
        ColorSizeNode fix_up;
        if(ptr.left == sentinel){
            fix_up = target.right;
            transplant(target, fix_up);
            fix_up.parent.size = fix_up.parent.size - 1;
        }else if(ptr.right == sentinel){
            fix_up = target.left;
            transplant(target, fix_up);
            fix_up.parent.size = fix_up.parent.size - 1;
        }else{
            ptr = getSuccessor(target);
            ptr_color = ptr.color;
            fix_up = ptr.right;
            if(ptr.parent == target){
                fix_up.parent = ptr; // in case of sentinel refer to target
            }else{
                transplant(ptr, ptr.right);
                target.right.size = target.right.size - 1;
                ptr.right = target.right;
                target.right.parent = ptr;
                ptr.size = ptr.right.size + 1;
            }
            transplant(target, ptr);
            ptr.left = target.left;
            target.left.parent = ptr;
            ptr.size = ptr.left.size + ptr.size;
            ptr.parent.size = ptr.parent.size - 1;
            ptr.color = target.color;
        }
        if(ptr_color == Color.BLACK){ // delete black node may violate property of red-black tree
            deleteFixUp(fix_up);
        }
    }
    private void deleteFixUp(ColorSizeNode fix_up){
        while(fix_up != root & fix_up.isBlack()){
            if(fix_up == fix_up.parent.left){
                ColorSizeNode sibling = fix_up.parent.right;
                if(sibling.isRed()) { // case1:sibling is black, convert to case 2, 3 or 4
                    sibling.setBlack(); // , which denote that sibling is black
                    fix_up.parent.setRed();
                    leftRotate(fix_up.parent);
                    sibling = fix_up.parent.right;
                }
                if(sibling.left.isBlack() & sibling.right.isBlack()){ // case2: sibling children is black
                    sibling.setRed();
                    fix_up = fix_up.parent;
                    continue; // may break while condition
                }else if(sibling.right.isBlack()){ // case3: sibling left red, right black. convert case4
                    sibling.left.setBlack();
                    sibling.setRed();
                    rightRotate(sibling);
                    sibling = fix_up.parent.right;
                }
                sibling.color = fix_up.parent.color; // case4: sibling right red
                fix_up.parent.setBlack();
                sibling.right.setBlack();
                leftRotate(fix_up.parent);
                fix_up = root;
            }else{
                ColorSizeNode sibling = fix_up.parent.left;
                if(sibling.isRed()) {
                    sibling.setBlack();
                    fix_up.parent.setRed();
                    rightRotate(fix_up.parent);
                    sibling = fix_up.parent.left;
                }
                if(sibling.left.isBlack() & sibling.right.isBlack()){
                    sibling.setRed();
                    fix_up = fix_up.parent;
                    continue;
                }else if(sibling.left.isBlack()){
                    sibling.right.setBlack();
                    sibling.setRed();
                    leftRotate(sibling);
                    sibling = fix_up.parent.left;
                }
                sibling.color = fix_up.parent.color;
                fix_up.parent.setBlack();
                sibling.left.setBlack();
                rightRotate(fix_up.parent);
                fix_up = root;
            }
        }
        fix_up.setBlack();
    }
    private void transplant(ColorSizeNode a, ColorSizeNode b){
        if(a.parent == sentinel){
            setRoot(b);
        }else if(a.parent.right == a){
            a.parent.right = b;
            b.parent = a.parent; // permissible if b is sentinel
        }else{
            a.parent.left = b;
            b.parent = a.parent;
        }
    }

    private void leftRotate(ColorSizeNode left_node){
        ColorSizeNode right_node = left_node.right;
        //exchange
        left_node.right = right_node.left;
        if(right_node.left != sentinel){ // remember to double link
            right_node.left.parent = left_node;
        }
        //exchange
        right_node.parent = left_node.parent; // double link right_node to left_node parent
        if(left_node.parent == sentinel){
            setRoot(right_node);
        }else if(left_node.parent.left == left_node){
            left_node.parent.left = right_node;
        }else{
            left_node.parent.right = right_node;
        }
        //exchange
        right_node.left = left_node;
        left_node.parent = right_node;
        right_node.size = left_node.size;
        left_node.size = left_node.left.size + left_node.right.size + 1;
    }
    private void rightRotate(ColorSizeNode right_node){ // mirror of leftRotate
        ColorSizeNode left_node = right_node.left;
        //exchange
        right_node.left = left_node.right;
        if(left_node.right != sentinel){ // remember to double link
            left_node.right.parent = right_node;
        }
        //exchange
        left_node.parent = right_node.parent; // double link right_node to left_node parent
        if(right_node.parent == sentinel){
            setRoot(left_node);
        }else if(right_node.parent.right == right_node){
            right_node.parent.right = left_node;
        }else{
            right_node.parent.left = left_node;
        }
        //exchange
        left_node.right = right_node;
        right_node.parent = left_node;
        left_node.size = right_node.size;
        right_node.size = right_node.left.size + right_node.right.size + 1;
    }

    private ColorSizeNode getMinimum(ColorSizeNode current){
        ColorSizeNode target = null;
        ColorSizeNode ptr = current;
        while(ptr != sentinel){
            target = ptr;
            ptr = ptr.left;
        }
        return target;
    }
    private ColorSizeNode getMaximum(ColorSizeNode current){
        ColorSizeNode target = null;
        ColorSizeNode ptr = current;
        while(ptr != sentinel){
            target = ptr;
            ptr = ptr.right;
        }
        return target;
    }
    private ColorSizeNode getSuccessor(ColorSizeNode current){
        if(current.right != sentinel){
            return getMinimum(current.right);
        }else{
            ColorSizeNode target = current.parent;
            ColorSizeNode target_right = current;
            while(target != sentinel & target.right == target_right){
                target_right = target;
                target = target.parent;
            }
            return target;
        }
    }
    private ColorSizeNode getPredecessor(ColorSizeNode current){
        if(current.left != null){
            return getMaximum(current.left);
        }else{
            ColorSizeNode target = current.parent;
            ColorSizeNode target_left = current;
            while(target != null & target.left == target_left){
                target_left = target;
                target = target.parent;
            }
            return target;
        }
    }

    public void printTree(){ // inorder print
        if(root == null | sentinel == root){
            return;
        }
        inorderTreeWalk(root.left);
        System.out.print(root.key);
        System.out.print(' ');
        inorderTreeWalk(root.right);
        System.out.print('\n');
    }
    public int getNodesNumber(){
        if(root == sentinel){
            return 0;
        }
        return inorderTreeWalk(root, "sum");
    }
    public int getHeight(){
        if(root == null | root == sentinel){
            return 0;
        }
        int count = 1;
        int left_max = inorderTreeWalk(root.left, count);
        int right_max = inorderTreeWalk(root.right, count);
        return Math.max(left_max, right_max);
    }
    private void inorderTreeWalk(ColorSizeNode n){
        if(n != sentinel){
            inorderTreeWalk(n.left);
            System.out.print(n.key);
            System.out.print(' ');
            inorderTreeWalk(n.right);
        }
    }
    private int inorderTreeWalk(ColorSizeNode n, int count){
        if(n != sentinel){
            int left_max = inorderTreeWalk(n.left, count + 1);
            int right_max = inorderTreeWalk(n.right, count + 1);
            return Math.max(left_max, right_max);
        }
        return count;
    }
    private int inorderTreeWalk(ColorSizeNode n, String way){ //overload trick
        if(!way.equals("sum")) throw new IllegalArgumentException();
        if(n.right != sentinel & n.left == sentinel){
            return inorderTreeWalk(n.right, "sum") + 1;
        }else if(n.right == sentinel & n.left != sentinel){
            return inorderTreeWalk(n.left, "sum") + 1;
        }else if(n.right != sentinel & n.left != sentinel){
            return inorderTreeWalk(n.left, "sum") + inorderTreeWalk(n.right, "sum") + 1;
        }else{
            return 1;
        }
    }
}