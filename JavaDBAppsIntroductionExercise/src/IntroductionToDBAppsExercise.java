import java.sql.*;
import java.util.*;


public class IntroductionToDBAppsExercise {
    private static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";
    private static Scanner scanner = new Scanner(System.in);
    private static final Properties properties = new Properties();
    private static Connection connection;
    private static PreparedStatement preparedStatement;


        public static void main(String[] args) throws SQLException {

            connection = getConnection();

            System.out.println("To exit the program, type 'end'");
            String exercise = "";

            while (!exercise.equals("end")) {
                System.out.println("Choose exercise from 2 to 9: ");
                exercise = scanner.nextLine();
                int ID ;

                switch (exercise) {
                    case "2" -> exerciseTwo();
                    case "3" -> {
                        System.out.println("Choose villain id:");
                        ID = Integer.parseInt(scanner.nextLine());
                        exerciseThree(ID);
                    }
                    case "4" -> {
                        System.out.println("Input:");
                        String minion = scanner.nextLine().split(": ")[1];
                        String villain = scanner.nextLine().split(": ")[1];
                        exerciseFour(minion, villain);
                    }
                    case "5" -> {
                        System.out.println("Enter country name: ");
                        String country = scanner.nextLine();
                        exerciseFive(country);
                    }
                    case "6" -> {
                        System.out.println("Enter villain ID: ");
                        ID = Integer.parseInt(scanner.nextLine());
                        exerciseSix(ID);
                    }
                    case "7" -> exerciseSeven();
                    case "8" -> exerciseEight();

//TODO Before testing the 9th exercise, don't forget to create a procedure.

/*  USE minions_db;
DROP procedure IF EXISTS usp_get_older;

DELIMITER $$
USE minions_db$$
CREATE PROCEDURE usp_get_older (minion_id INT)
BEGIN
UPDATE minions SET age= age+1  WHERE id = minion_id;
END$$

DELIMITER ;*/
                    case "9" -> {
                        System.out.println("Enter minion ID: ");
                        ID = Integer.parseInt(scanner.nextLine());
                        exerciseNine(ID);
                    }
                    default -> {
                        if (!exercise.equals("end") && !exercise.equals("")) {
                            System.out.println("No such exercise found");
                        }
                    }
                }

            }
            connection.close();
        }

        private static void exerciseNine(int ID) throws SQLException {
            CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?) ");
            callableStatement.setInt(1, ID);
            callableStatement.execute();

            preparedStatement = connection.prepareStatement("SELECT name,age FROM minions WHERE id=?");
            preparedStatement.setInt(1, ID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getInt(2));
            }

        }

        private static void exerciseEight() throws SQLException {
            System.out.println("Input: ");
            int[] minionsIds = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            preparedStatement = connection.prepareStatement("UPDATE minions SET age= age+1, name= LOWER(name)  WHERE id=?;");
            for (int minionsId : minionsIds) {
                preparedStatement.setInt(1, minionsId);
                preparedStatement.execute();
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM minions");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getInt(3));
            }

        }

        private static void exerciseSeven() throws SQLException {
            preparedStatement = connection.prepareStatement("SELECT name FROM minions");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> minionsNames = new ArrayList<>();

            while (resultSet.next()) {
                minionsNames.add(resultSet.getString(1));
            }

            int startIndex = 0;
            int endIndex = minionsNames.size() - 1;

            for (int i = 0; i < minionsNames.size(); i++) {
                if (i % 2 == 0) System.out.println(minionsNames.get(startIndex++)
                );
                else System.out.println(minionsNames.get(endIndex--)
                );
            }
        }

        private static void exerciseSix(int villainId) throws SQLException {
            String villainName = getEntityNameByID(villainId);
            if (villainName.equals("")) {
                System.out.println("No such villain was found");
                return;

            }

            System.out.printf("%s was deleted%n", villainName);

            preparedStatement = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id=?");
            preparedStatement.setInt(1, villainId);

            int affectedRows = preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM villains WHERE id=?");
            preparedStatement.setInt(1, villainId);
            preparedStatement.execute();

            System.out.printf("%d minions released%n", affectedRows);

        }

        private static void exerciseFive(String country) throws SQLException {
            preparedStatement = connection.prepareStatement("Update towns SET name= UPPER(name) WHERE country=?");
            preparedStatement.setString(1, country);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No town names were affected.");
                return;
            }
            System.out.printf("%d town names were affected.%n", affectedRows);

            preparedStatement = connection.prepareStatement("SELECT name FROM towns WHERE country=?");
            preparedStatement.setString(1, country);
            ResultSet resultSet = preparedStatement.executeQuery();
            String[] result = new String[affectedRows];
            int i = 0;

            while (resultSet.next()) {
                result[i++] = resultSet.getString(1);
            }
            System.out.println(Arrays.toString(result));
        }

        private static void exerciseFour(String minion, String villain) throws SQLException {
            String townName = minion.split(" ")[2];
            String minionName = minion.split(" ")[0];
            Integer minionAge = Integer.parseInt(minion.split(" ")[1]);
            Integer minionID = getEntityIDByName("towns", minionName);
            Integer townID = getEntityIDByName("towns", townName);
            Integer villainID = getEntityIDByName("villains", villain);

            if (townID < 0) {
                String query = String.format("INSERT INTO towns (`name`)  VALUES (\"%s\");", townName);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                System.out.printf("Town %s was added to the database.%n", townName);
                townID = getEntityIDByName("towns", townName);
            }
            if (villainID < 0) {
                String query = String.format("INSERT INTO villains (`name`,`evilness_factor`)  VALUES (\"%s\",\"evil\");", villain);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                System.out.printf("Villain %s was added to the database.%n", villain);
                villainID = getEntityIDByName("villains", villain);
            }
            if (minionID < 0) {
                String query = String.format("INSERT INTO minions (`name`,`age`,`town_id`)  VALUES (\"%s\",\"%d\",\"%s\");", minionName, minionAge, townID);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                minionID = getEntityIDByName("minions", minionName);
            }
            String query = String.format("INSERT INTO minions_villains   VALUES (\"%d\",\"%d\");", minionID, villainID);

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villain);

        }

        private static Connection getConnection() throws SQLException {
            scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            username = username.equals("") ? "root" : username;
            properties.setProperty("user", username);
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            password = password.equals("") ? "password" : password;
            properties.setProperty("password", password);

            return DriverManager.getConnection(CONNECTION_PATH + DATABASE_NAME, properties);
        }

        private static String getEntityNameByID(Integer id) throws SQLException {
            String query = String.format("SELECT name FROM %s where id=?;", "villains");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "";
            }
        }

        private static Integer getEntityIDByName(String tableName, String name) throws SQLException {
            String query = String.format("SELECT id FROM %s where name=?;", tableName);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        }

        private static void exerciseThree(int villainID) throws SQLException {
            preparedStatement = connection.prepareStatement("""
                    SELECT v.name, m.name,m.age FROM minions_villains
                    JOIN minions AS m on m.id = minions_villains.minion_id JOIN villains AS v on v.id = minions_villains.villain_id
                    WHERE villain_id= ?;""");
            preparedStatement.setInt(1, villainID);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.printf("Villain: %s%n", getEntityNameByID(villainID));

            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    System.out.printf("%d. %s %d%n", resultSet.getRow(), resultSet.getString(2), resultSet.getInt(3));
                }
            } else {
                System.out.printf("No villain with ID %d exists in the database.%n", villainID);
            }
        }

        private static void exerciseTwo() throws SQLException {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT v.name, COUNT(DISTINCT mv.minion_id) AS minionsCount FROM villains AS v
                    JOIN minions_villains AS mv on v.id = mv.villain_id
                    GROUP BY name
                    HAVING minionsCount > 15
                    ORDER BY minionsCount DESC;""");

            while (resultSet.next()) {
                System.out.printf("%s %d %n", resultSet.getString(1), resultSet.getInt(2));
            }
        }
    }
