import java.io.BufferedReader;
import java.io.FileReader;
public class BinaryTree {

    public class Node {

        String name;
        int salary;
        Node right;
        Node left;


        public Node(String name, int salary) {
            this.name = name;
            this.salary = salary;
            right = null;
            left = null;
        }
    }


    Node root;

    private Node addRecursive(Node current, String name, int salary) { // base method to be called upon add method invocation

        if (current == null) {
            return new Node(name,salary);
        }

        if (salary < current.salary) {
            current.left = addRecursive(current.left, name, salary);
        } else if (salary > current.salary) {
            current.right = addRecursive(current.right, name, salary);
        } else {
            // value already exists
            return current;
        }

        return current;
    }


    public void add(String name, int salary) { //add method

        root = addRecursive(root, name, salary);
    }


    private Node containsNodeRecursive(Node current, int salary) { // base method to be called up search method invocation
       Node temp = null;
        if (current == null) {
            System.out.println();
            System.out.println("Sorry, requested salary '" + salary + "' is not found!");
            return temp ;

        }
        if (salary == current.salary) {
            System.out.println();
            System.out.print("Entry Found! ---> ");
            System.out.println("Name:" + current.name + ", Salary:" + current.salary);
            return current;
        }
        if (salary < current.salary) {
            return containsNodeRecursive(current.left, salary);
        }
        else {
            return containsNodeRecursive(current.right, salary);
        }
    }

    public Node containsNode(int salary) {    // search method

        return containsNodeRecursive(root, salary);
    }

    public void traverseInOrder(Node root) { //print method
        if (root != null) {
            traverseInOrder(root.left);
            System.out.printf("%-15s %10s %n", root.name, root.salary);
            traverseInOrder(root.right);
        }
    }

    public void display(Node root) { //displays BST data using traverseInOrder() method
        String heading1 = "Name";
        String heading2 = "Salary";
        System.out.println();
        System.out.printf("%-15s %10s %n", heading1, heading2);
        System.out.println("-------------------------------");
        traverseInOrder(root);

    }


    public void deleteKey(int salary){ // This method mainly calls deleteRec()
        if (containsNode(salary) != null ) {
            root = deleteRec(root, salary);
            System.out.println("Deletion was successful!");
        }

        else {
            System.out.println("Therefore, deletion operation could not be completed!");
        }

    }

   public Node deleteRec(Node root, int salary) {

        if (root == null) {
            System.out.println();
            System.out.println("Error! Empty Binary Tree");
            return root; //Base Case: If the tree is empty

        }
        if (salary < root.salary) {    //Otherwise, recur down the tree
            root.left = deleteRec(root.left, salary);
        }
        else if (salary > root.salary){
            root.right = deleteRec(root.right, salary);
        }

        else{ // if key is same as root's key, then This is the node to be deleted

            if (root.left == null) { // node with only one child or no child

                return root.right;
            }
            else if (root.right == null) {
                return root.left;
            }

            root.salary = minValue(root.right).salary;  // node with two children: Get the inorder successor (smallest in the right subtree)
            root.name = minValue(root.right).name;
            root.right = deleteRec(root.right, root.salary); // Delete the inorder successor

        }

        return root;
    }

    public Node minValue(Node root) //finds the minimum value in the right side of the binary tree for deletion operation
    {

        int minv = root.salary;
        String name = root.name;
        Node temp = new Node(name, minv);
        while (root.left != null)
        {
            minv = root.left.salary;
            name = root.left.name;
            root = root.left;
        }
        return temp;
    }



    public void fileRead(String filename) { //reading data from data file
        int PrevSalary = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            line = reader.readLine() ;//skip first line that contains column names
            while ((line = reader.readLine()) != null) { //continue as long as readLine has something to read

                String[] splitted= line.split(" ");//data is split by character space
                String name = splitted[0]; //employee  name
                int salary = Integer.valueOf(splitted[1]); //salary

                if(PrevSalary  - salary > 3000 || (PrevSalary  - salary)*-1 > 3000) { // don't insert any salary differences that is less than $3000
                    add(name, salary);
                }

                else{
                    System.out.println();
                    System.out.println("Following record could not be inserted as it did not satisfy insertion criteria -->");
                    System.out.println("--> Name: " + name + ", Salary:" + salary);
                }
                PrevSalary = salary;
            }
            reader.close();
        }catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }
    }





    public static void main (String[] args) {

        String fileName = "C:\\Users\\mgokcek\\IdeaProjects\\ quickSort\\src\\data.txt";
        BinaryTree bt = new BinaryTree();

        bt.fileRead(fileName); //reading salary data

        bt.display(bt.root); //print BST data
        bt.containsNode(3500); // search operation, should return: "not found"
        bt.containsNode(98000); // search operation, should return:"found"
        bt.deleteKey(22000); // deletion operation, should return: "unsuccessful"
        bt.deleteKey(44000); // deletion operation, should return: "successful"
        bt.display(bt.root);//print BST data again after to confirm deletion


    }
}
