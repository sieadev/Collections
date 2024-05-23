package dev.siea.collections.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.siea.collections.collections.*;
import dev.siea.collections.collections.deliver.DeliverCollection;
import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.collections.common.Task;
import dev.siea.collections.collections.common.Type;
import dev.siea.collections.storage.Storage;
import dev.siea.collections.util.Log;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MySQLWrapper implements Storage {
    private final HikariDataSource dataSource;

    public MySQLWrapper(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
        createCollectionsTable();
    }

    private void createCollectionsTable(){
        try (Connection connection = dataSource.getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Collections ("
                    + "ID          varchar(256) not null,"
                    + "Name        varchar(256) not null,"
                    + "Description varchar(256) not null,"
                    + "Type        varchar(256) not null,"
                    + "Target      text       not null,"
                    + "Level       text       not null,"
                    + "Global      tinyint(1) not null,"
                    + "Commands    text       not null "
                    + ")";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    private void createCollectionDataTable(int id){
        try (Connection connection = dataSource.getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS _" + id + " ("
                    + "UUID        varchar(36) not null,"
                    + "Score       INTEGER not null "
                    + ")";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    @Override
    public List<Collection> getCollections() {
        List<Collection> collections = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM Collections";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("ID");

                    String name = resultSet.getString("Name");

                    String description = resultSet.getString("Description");

                    Type type = Type.valueOf(resultSet.getString("Type"));

                    Object target = MySQLUtils.stringToObject(resultSet.getString("Target"));

                    List<Integer> level = MySQLUtils.stringToIntList(resultSet.getString("Level"));

                    boolean global = resultSet.getBoolean("Global");

                    List<List<String>> commands = MySQLUtils.commandsStringToList(resultSet.getString("Commands"));


                    Collection collection;
                    switch (type){
                        case BREAK:
                            collection = new BreakCollection(name, description, commands ,global, new Task(target,level),Integer.parseInt(id));
                            break;
                        case PLACE:
                            collection = new PlaceCollection(name, description, commands ,global, new Task(target,level),Integer.parseInt(id));
                            break;
                        case KILL:
                            collection = new KillCollection(name, description, commands ,global, new Task(target,level),Integer.parseInt(id));
                            break;
                        case BREED:
                            collection = new BreedCollection(name, description, commands ,global, new Task(target,level),Integer.parseInt(id));
                            break;
                        case DELIVER:
                            collection = new DeliverCollection(name, description, commands ,global, new Task(target,level),Integer.parseInt(id));
                            break;
                        default:
                            Log.str("Collection Type " + type + " does not exist");
                            continue;
                    }

                    collections.add(collection);
                }
            }
        } catch (SQLException e) {
            Log.error(e);
        }

        for (Collection collection : collections){
            createCollectionDataTable(collection.getID());
        }

        return collections;
    }

    @Override
    public int saveCollection(Collection collection) {
        int id = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);;
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM Collections";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String resultID = resultSet.getString("ID");
                    if (Integer.parseInt(resultID) == id){ return saveCollection(collection); }
                }
            }

            String insertQuery = "INSERT INTO Collections (ID, Name, Description, Type, Target, Level, Global, Commands) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, String.valueOf(id));
                insertStatement.setString(2, collection.getName());
                insertStatement.setString(3, collection.getDescription());
                insertStatement.setString(4, collection.getType().name());
                insertStatement.setString(5, collection.getTasks().getTarget().toString());
                insertStatement.setString(6, MySQLUtils.intListToString(collection.getTasks().getLevel()));
                insertStatement.setBoolean(7, collection.isGlobal());
                insertStatement.setString(8, MySQLUtils.commandsListToString(collection.getCommands()));

                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        createCollectionDataTable(id);
        return id;
    }

    @Override
    public HashMap<String, Integer> getCollectionScores(Player player) {
        HashMap<String, Integer> scores = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            String idQuery = "SELECT ID FROM Collections";
            try (PreparedStatement idStatement = connection.prepareStatement(idQuery)) {
                ResultSet idResultSet = idStatement.executeQuery();
                while (idResultSet.next()) {
                    String id = idResultSet.getString("ID");
                    String scoreQuery = "SELECT Score FROM _" + id + " WHERE UUID = ?";
                    try (PreparedStatement scoreStatement = connection.prepareStatement(scoreQuery)) {
                        scoreStatement.setString(1, player.getUniqueId().toString());
                        ResultSet scoreResultSet = scoreStatement.executeQuery();
                        if (scoreResultSet.next()) {
                            int score = scoreResultSet.getInt("Score");
                            scores.put(id, score);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.error(e);
        }

        return scores;
    }

    @Override
    public void saveCollectionScores(Player player, HashMap<String, Integer> scores) {
        try (Connection connection = dataSource.getConnection()) {
            for (String id : scores.keySet()) {
                int score = scores.get(id);
                String selectQuery = "SELECT * FROM _" + id + " WHERE UUID = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        String updateQuery = "UPDATE _" + id + " SET Score = ? WHERE UUID = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, score);
                            updateStatement.setString(2, player.getUniqueId().toString());
                            updateStatement.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO _" + id + " (UUID, Score) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, player.getUniqueId().toString());
                            insertStatement.setInt(2, score);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.error(e);
        }
    }


    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
