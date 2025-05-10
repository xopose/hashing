package lab4;

import com.lazer.lab4.InsertOperation;
import com.lazer.lab4.RGA;
import com.lazer.lab4.RandomEditor;

import java.util.*;

public class TestScenarios {
    public static void main(String[] args) {
        testSequentialAndRandomEdits();
        testMultipleReplicasRandomEdits();
        testConcurrentReplaceConflict();
    }

    private static void testSequentialAndRandomEdits() {
        System.out.println("Test 1");
        TestRGA.NetworkSimulator network = new TestRGA.NetworkSimulator();
        RandomEditor editor = new RandomEditor();

        RGA replicaA = new RGA("A");
        RGA replicaB = new RGA("B");

        network.register("A", replicaA);
        network.register("B", replicaB);

        String text = "HelloWorld";
        String parent = "ROOT";
        for (char c : text.toCharArray()) {
            InsertOperation op = replicaA.localInsert(parent, c);
            parent = op.elementId;
            network.broadcast(op);
        }

        for (int i = 0; i < 5; i++) {
            if (Math.random() < 0.5) {
                String randomParent = editor.getRandomElementId(replicaB);
                InsertOperation op = replicaB.localInsert(randomParent, editor.randomChar());
                network.broadcast(op);
            } else {
                String randomElem = editor.getRandomElementId(replicaB);
                if (!randomElem.equals("ROOT"))
                    network.broadcast(replicaB.localDelete(randomElem));
            }
        }

        System.out.println("replicaA: " + replicaA.getText());
        System.out.println("replicaB: " + replicaB.getText());
    }

    private static void testMultipleReplicasRandomEdits() {
        System.out.println("Test 2");
        TestRGA.NetworkSimulator network = new TestRGA.NetworkSimulator();
        RandomEditor editor = new RandomEditor();

        RGA replicaA = new RGA("A");
        RGA replicaB = new RGA("B");
        RGA replicaC = new RGA("C");

        network.register("A", replicaA);
        network.register("B", replicaB);
        network.register("C", replicaC);

        String text = "CRDT";
        String parent = "ROOT";
        for (char c : text.toCharArray()) {
            InsertOperation op = replicaA.localInsert(parent, c);
            parent = op.elementId;
            network.broadcast(op);
        }

        List<RGA> replicas = Arrays.asList(replicaA, replicaB, replicaC);
        for (int i = 0; i < 10; i++) {
            RGA randomReplica = replicas.get(new Random().nextInt(replicas.size()));
            if (Math.random() < 0.5) {
                String randomParent = editor.getRandomElementId(randomReplica);
                InsertOperation op = randomReplica.localInsert(randomParent, editor.randomChar());
                network.broadcast(op);
            } else {
                String randomElem = editor.getRandomElementId(randomReplica);
                if (!randomElem.equals("ROOT"))
                    network.broadcast(randomReplica.localDelete(randomElem));
            }
        }

        System.out.println("replicaA: " + replicaA.getText());
        System.out.println("replicaB: " + replicaB.getText());
        System.out.println("replicaC: " + replicaC.getText());
    }

    private static void testConcurrentReplaceConflict() {
        System.out.println("Test 3");
        TestRGA.NetworkSimulator network = new TestRGA.NetworkSimulator();

        RGA replicaA = new RGA("A");
        RGA replicaB = new RGA("B");

        network.register("A", replicaA);
        network.register("B", replicaB);

        String word = "apple";
        String parent = "ROOT";
        Map<Integer, String> positions = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            InsertOperation op = replicaA.localInsert(parent, word.charAt(i));
            positions.put(i, op.elementId);
            parent = op.elementId;
            network.broadcast(op);
        }

        for (int i = 0; i < word.length(); i++) {
            network.broadcast(replicaA.localDelete(positions.get(i)));
        }
        parent = "ROOT";
        String replacementA = "banana";
        for (char c : replacementA.toCharArray()) {
            InsertOperation op = replicaA.localInsert(parent, c);
            parent = op.elementId;
            network.broadcast(op);
        }

        for (int i = 0; i < word.length(); i++) {
            network.broadcast(replicaB.localDelete(positions.get(i)));
        }
        parent = "ROOT";
        String replacementB = "cherry";
        for (char c : replacementB.toCharArray()) {
            InsertOperation op = replicaB.localInsert(parent, c);
            parent = op.elementId;
            network.broadcast(op);
        }

        System.out.println("replicaA: " + replicaA.getText());
        System.out.println("replicaB: " + replicaB.getText());
    }
}
