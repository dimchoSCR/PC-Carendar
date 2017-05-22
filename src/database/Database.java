package database;

import model.UserManager;

import java.sql.*;

/**
 * Created by DevM on 5/17/2017.
 */
public class Database {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/carendar","carendar","carendar");
            statement = connect.createStatement();
            System.out.println("Connection success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase(){
        try {
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException{
        return statement.executeQuery(query);
    }

    /**
     *
     * @param username - String
     * @param pass - String
     * @param age - pass
     * @return Returns true if user is successfully registered in database, false otherwise
     */
    public boolean createUser(String username,String pass,int age){
        try {
            String sql = "insert into users(username,password,userAge)" + "values(?,?,?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,pass);
            preparedStatement.setInt(3,age);
            int added = preparedStatement.executeUpdate();
            preparedStatement.close();
            if(added >0) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean availableToRegister(String username,String pass){
        String sql = "select username from users where username = ?";
        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1,username);
             resultSet = preparedStatement.executeQuery();
             while (resultSet.next()){
                 System.out.println(resultSet.getString(1) + resultSet.getString(2) + resultSet.getString(3));
                 return false;
             }
             return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks user credentials from db
     * @param username
     * @param pass
     * @return the age of the logged user, 0 if the value in the db is NULL and -1 if the login was unsuccessful
     */
    public int logInUser(String username, String pass){
        String sql = "select username,userAge,isLogged from users where username = ? AND password = ?";

        try {
            preparedStatement = connect.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,pass);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.first()){
                return -1;
            }
            resultSet.updateInt(3,1);
            resultSet.updateRow();

            return resultSet.getInt(2);

        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the user's vehicles from the database
     *
     * @param username the unique id that identifies which vehicles are owned by this user
     * @return the user's vehicles
     */
    public ResultSet getLoggedUserVehicles(String username){
        String sql = "select brand, model,vehicles.type,registration,ownerID, productionYear," +
                " taxes.type, dateFrom, dateTo, price" +
                " from users Inner Join vehicles On (users.username = vehicles.ownerID)" +
                " Left Join taxes On (vehicles.idVehicles=taxes.taxes_vehicleID)" +
                " Where username=?";
        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Finds if there is logged user.
     */
    public String[] findLoggedUser() throws SQLException {
        String sql = "select username,password,userAge from users where isLogged = 1";
        resultSet = statement.executeQuery(sql);
        if (resultSet.first()) {
            return new String[]{resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)};
        }
        else return null;
    }
}
