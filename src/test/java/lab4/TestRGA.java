package lab4;

import com.google.gson.Gson;
import com.lazer.lab4.DeleteOperation;
import com.lazer.lab4.InsertOperation;
import com.lazer.lab4.Operation;
import com.lazer.lab4.RGA;

import java.util.*;

public class TestRGA {
    public static void main(String[] args) {
        Gson gson = new Gson();
        NetworkSimulator network = new NetworkSimulator();

        RGA replicaA = new RGA("A");
        RGA replicaB = new RGA("B");
        RGA replicaC = new RGA("C");

        network.register("A", replicaA);
        network.register("B", replicaB);
        network.register("C", replicaC);


        InsertOperation op1 = replicaA.localInsert("ROOT", 'H');
        network.broadcast(op1);

        InsertOperation op2 = replicaB.localInsert("ROOT", 'e');
        network.broadcast(op2);

        InsertOperation op3 = replicaA.localInsert(op1.elementId, 'i');
        network.broadcast(op3);

        DeleteOperation op4 = replicaC.localDelete(op2.elementId);
        network.broadcast(op4);

        System.out.println("replicaA: " + replicaA.getText());
        System.out.println("replicaB: " + replicaB.getText());
        System.out.println("replicaC: " + replicaC.getText());
    }

    static class NetworkSimulator {
        private final Map<String, RGA> replicas = new HashMap<>();
        private final Gson gson = new Gson();

        public void register(String id, RGA rga) {
            replicas.put(id, rga);
        }

        public void broadcast(Operation op) {
            String json = serialize(op);
            for (RGA replica : replicas.values()) {
                applyFromJson(replica, json);
            }
        }

        private String serialize(Operation op) {
            if (op instanceof InsertOperation) {
                return gson.toJson(op);
            } else if (op instanceof DeleteOperation) {
                return gson.toJson(op);
            }
            return null;
        }

        private void applyFromJson(RGA rga, String json) {
            if (json.contains("\"parentId\"")) {
                InsertOperation op = gson.fromJson(json, InsertOperation.class);
                rga.applyInsert(op);
            } else {
                DeleteOperation op = gson.fromJson(json, DeleteOperation.class);
                rga.applyDelete(op);
            }
        }
    }
}