import java.util.concurrent.atomic.AtomicMarkableReference;

public class HarrisAMRLinkedList {

    final Node head;
    final Node tail;

    static class Node {
        final Integer key;
        final AtomicMarkableReference<Node> next;

        Node(Integer key) {
            this.key = key;
            this.next = new AtomicMarkableReference<Node>(null, false);
        }
    }

    static class Window {
        public Node pred;
        public Node curr;

        Window(Node myPred, Node myCurr) {
            pred = myPred;
            curr = myCurr;
        }
    }

    public HarrisAMRLinkedList() {
        tail = new Node(null);
        head = new Node(null);
        head.next.set(tail, false);
    }


    public boolean add(Integer key) {
        final Node newNode = new Node(key);
        while (true) {
            final Window window = find(key);
            if (window == null) {
                return false;
            }
            final Node pred = window.pred;
            final Node curr = window.curr;
            if (curr.key != null && curr.key.equals(key)) {
                return false;
            } else {
                newNode.next.set(curr, false);
                if (pred.next.compareAndSet(curr, newNode, false, false)) {
                    return true;
                }
            }
        }
    }


    public boolean remove(int key) {
        while (true) {
            Window window = find(key);
            if (window == null) {
                return false;
            }
            Node pred = window.pred, curr = window.curr;
            if (curr.key != null && !curr.key.equals(key)) {
                return false;
            } else {
                Node succ = curr.next.getReference();
                if (!curr.next.compareAndSet(succ, succ, false, true))
                    continue;
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }


    public Window find(Integer key) {
        Node pred = null;
        Node curr = null;
        Node succ = null;
        boolean[] marked = {false};

        if (head.next.getReference() == tail) {
            return new Window(head, tail);
        }

        retry:
        while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                if (curr == null) {
                    return null;
                }
                succ = curr.next.get(marked);

                while (marked[0]) {
                    if (!pred.next.compareAndSet(curr, succ, false, false)) {
                        continue retry;
                    }
                    curr = succ;
                    succ = curr.next.get(marked);
                }

                if (curr.equals(tail) || key <= curr.key) {
                    return new Window(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
        }
    }


    public void printSet() {
        int n = 0;
        var reference = this.head.next.getReference();
        while (reference.key != null) {
            System.out.println(reference.key);
            n++;
            reference = reference.next.getReference();
            if (reference == null) {
                break;
            }
        }
        System.out.printf("Count = %d%n", n);
    }

    public void printSetElementsCount() {
        int n = 0;
        var reference = this.head.next.getReference();
        while (reference.key != null) {
            n++;
            reference = reference.next.getReference();
            if (reference == null) {
                break;
            }
        }
        System.out.printf("Count = %d%n", n);
    }
}
