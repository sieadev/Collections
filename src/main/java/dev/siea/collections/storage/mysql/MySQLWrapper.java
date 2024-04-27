package dev.siea.collections.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.siea.collections.collections.*;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.storage.Storage;
import dev.siea.collections.util.Log;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    + "ID          varchar(8) not null,"
                    + "Name        varchar(8) not null,"
                    + "Description varchar(8) not null,"
                    + "Type        varchar(8) not null,"
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
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + id + " ("
                    + "UUID        varchar(8) not null,"
                    + "Score       varchar(8) not null "
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
                            collection = new BreakCollection(name, description, commands ,global, new Task(target,level));
                            break;
                        case PLACE:
                            collection = new PlaceCollection(name, description, commands ,global, new Task(target,level));
                            break;
                        case KILL:
                            collection = new KillCollection(name, description, commands ,global, new Task(target,level));
                            break;
                        case BREED:
                            collection = new BreedCollection(name, description, commands ,global, new Task(target,level));
                            break;
                        case DELIVER:
                            collection = new DeliverCollection(name, description, commands ,global, new Task(target,level));
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
        int id = 0;
        try (Connection connection = dataSource.getConnection()) {
            String countQuery = "SELECT COUNT(*) FROM Collections";
            try (PreparedStatement countStatement = connection.prepareStatement(countQuery)) {
                ResultSet countResultSet = countStatement.executeQuery();
                if (countResultSet.next()) {
                    id = countResultSet.getInt(1); // Assign the ID based on the count
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


    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
