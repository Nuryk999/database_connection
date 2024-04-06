package org.example;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final Lock lock = new ReentrantLock();

    private String hostname;
    private String username;
    private String password;
    private String database;

    private DatabaseConnection() {

        loadConfig();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private void loadConfig() {

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/resources/config.json")) {
            JSONObject config = (JSONObject) parser.parse(reader);
            this.hostname = (String) config.get("hostname");
            this.username = (String) config.get("username");
            this.password = (String) config.get("password");
            this.database = (String) config.get("database");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public JSONObject getConnectionInfo() {
        JSONObject connectionInfo = new JSONObject();
        connectionInfo.put("hostname", this.hostname);
        connectionInfo.put("username", this.username);
        connectionInfo.put("password", this.password);
        connectionInfo.put("database", this.database);
        return connectionInfo;
    }


    public static void main(String[] args) {

        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        System.out.println(instance1 == instance2);

        System.out.println(instance1.getConnectionInfo());
    }
}

