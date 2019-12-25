package sample;

import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import sample.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private Connection dbConnection;
    static private DBHelper instance = null;

    public DBHelper() {
        this.dbConnection = getDbConnection();
    }

    public static DBHelper getInstance(){
        if (instance==null){
            instance = new DBHelper();
        }
        return instance;
    }

    private Connection getDbConnection(){
        String connectionString = "jdbc:mysql://localhost:3306/coursework";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            dbConnection = DriverManager.getConnection(connectionString, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }
    public User getUser(String username,String password){
        try {
            String passwordHex = DigestUtils.sha256Hex(password);
            String select = "SELECT id,type FROM user WHERE username=? AND password=?";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setString(1, username);
            ps.setString(2, passwordHex);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id =  rs.getInt("id");
                UserType type = UserType.values()[rs.getInt("type")];
                return new User(id,username,passwordHex,type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(int userId) {
        try {
            String select = "SELECT id,username,password,type FROM user WHERE id=?";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setString(1, String.valueOf(userId));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id =  rs.getInt("id");
                String username = rs.getString("username");
                String passwordHex = rs.getString("password");
                UserType type = UserType.values()[rs.getInt("type")];
                return new User(id,username,passwordHex,type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        ArrayList<User> list = new ArrayList<>();
        try {
            String select = "SELECT id,username,password,type FROM user WHERE type!=0";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id =  rs.getInt("id");
                String username = rs.getString("username");
                String passwordHex = rs.getString("password");
                UserType type = UserType.values()[rs.getInt("type")];
                list.add(new User(id,username,passwordHex,type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer addNewUser(String username, String passwordHex, UserType type) {
        try {
            String insert = "INSERT INTO user (username,password,type) VALUES (?,?,?)";
            PreparedStatement ps = getDbConnection().prepareStatement(insert);
            ps.setString(1,username);
            ps.setString(2,passwordHex);
            ps.setInt(3,type.ordinal());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer updateUser(User user) {
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(
                    "UPDATE user SET username = ?,password = ?,type = ? WHERE id = ?");

            ps.setString(1, user.username);
            ps.setString(2, user.passwordHex);
            ps.setInt(3, user.type.ordinal());
            ps.setInt(4, user.id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dropUser(User user) {
        try{
            PreparedStatement ps = getDbConnection().prepareStatement("DELETE FROM user WHERE id=?");
            ps.setInt(1, user.id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RetailPurchase> getQuerySalesManager1(Date date1,Date date2) {
        ArrayList<RetailPurchase> list = new ArrayList<>();
        try {
            String select = "SELECT r.id,r.name,srp.quantity,srp.price,rp.date FROM single_reagent_purchase srp JOIN reagent_purchase rp ON rp.id = srp.purchase_id JOIN reagent r ON r.id = srp.reagent_id WHERE rp.date BETWEEN ? AND ?";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setDate(1,date1);
            ps.setDate(2,date2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id =  rs.getInt("r.id");
                String reagentName = rs.getString("r.name");
                Integer quantity = rs.getInt("srp.quantity");
                Integer price = rs.getInt("srp.price");
                Date date = rs.getDate("rp.date");
                list.add(new RetailPurchase(id,date,quantity,price,new Reagent(null,reagentName,null,null)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RetailPurchase> getQuerySalesManager2(Date date1, Date date2) {
        ArrayList<RetailPurchase> list = new ArrayList<>();
        try {
            String select = "SELECT r.id,r.name,srr.quantity,srr.price,rr.date FROM single_reagent_retail srr JOIN retail rr ON rr.id = srr.retail_id JOIN reagent r ON r.id = srr.reagent_id WHERE rr.date BETWEEN ? AND ?";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setDate(1,date1);
            ps.setDate(2,date2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id =  rs.getInt("r.id");
                String reagentName = rs.getString("r.name");
                Integer quantity = rs.getInt("srr.quantity");
                Integer price = rs.getInt("srr.price");
                Date date = rs.getDate("rr.date");
                list.add(new RetailPurchase(id,date,quantity,price,new Reagent(null,reagentName,null,null)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer getQuerySalesManager3() {
        try {
            String select = "SELECT SUM(srr.quantity*srr.price) AS sum FROM single_reagent_retail srr JOIN retail rr ON rr.id = srr.retail_id WHERE MONTH(rr.date)=MONTH(CURDATE())";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("sum");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getQuerySalesManager4() {
        try {
            String select = "SELECT SUM(srp.quantity*srp.price) AS sum FROM single_reagent_purchase srp JOIN reagent_purchase rp ON rp.id = srp.purchase_id WHERE MONTH(rp.date)=MONTH(CURDATE())";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("sum");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pair<Reagent, Integer>> getQuerySalesManager5(Integer activityId) {
        ArrayList<Pair<Reagent,Integer>> list = new ArrayList<>();
        try {
            String select = "SELECT r.id,r.name,SUM(ru.quantity*r.price) AS 'sum' FROM reagent_use ru JOIN reagent r ON r.id = ru.reagent_id WHERE ru.activity_id=? GROUP BY r.id";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setInt(1,activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer reagentId = rs.getInt("r.id");
                String reagentName = rs.getString("r.name");
                Integer sum = rs.getInt("sum");
                Reagent reagent = new Reagent(reagentId,reagentName,null,null);
                list.add(new Pair<>(reagent,sum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pair<Reagent, String>> getQuerySalesManager6() {
        ArrayList<Pair<Reagent,String>> list = new ArrayList<>();
        try {
            //String select = "SELECT id,name,'In stock' AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))>0 GROUP BY id UNION SELECT id,name,CONCAT('Out of stock') AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))<=0 GROUP BY id;";
            String select = "SELECT id,name,info FROM (SELECT id,name,'In stock' AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))>0 GROUP BY id UNION SELECT id,name,CONCAT('Out of stock') AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))<=0 GROUP BY id) as commented WHERE id NOT IN (SELECT srp.reagent_id FROM single_reagent_purchase srp JOIN reagent_purchase rp ON rp.id=srp.purchase_id WHERE MONTH(rp.date)=MONTH(CURDATE()) AND YEAR(rp.date)=YEAR(CURDATE())) ";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer reagentId = rs.getInt("id");
                String reagentName = rs.getString("name");
                String comment = rs.getString("info");
                Reagent reagent = new Reagent(reagentId,reagentName,null,null);
                list.add(new Pair<>(reagent,comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pair<Reagent, String>> getQuerySalesManager7() {
        ArrayList<Pair<Reagent,String>> list = new ArrayList<>();
        try {
            //String select = "SELECT id,name,'In stock' AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))>0 GROUP BY id UNION SELECT id,name,CONCAT('Out of stock') AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))<=0 GROUP BY id;";
            String select = "SELECT id,name,info FROM (SELECT id,name,'In stock' AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))>0 GROUP BY id UNION SELECT id,name,CONCAT('Out of stock') AS info FROM reagent WHERE (@k:=(IFNULL((SELECT SUM(srp.quantity) FROM single_reagent_purchase srp WHERE srp.reagent_id=reagent.id),0) - IFNULL((SELECT SUM(srr.quantity) FROM single_reagent_retail srr WHERE srr.reagent_id=reagent.id),0)))<=0 GROUP BY id) as commented WHERE NOT EXISTS (SELECT r.id,r.name FROM single_reagent_retail srr JOIN retail rr ON rr.id=srr.retail_id JOIN reagent r ON r.id=srr.reagent_id WHERE commented.id=srr.reagent_id AND MONTH(rr.date)=MONTH(CURDATE()) AND YEAR(rr.date)=YEAR(CURDATE()))";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer reagentId = rs.getInt("id");
                String reagentName = rs.getString("name");
                String comment = rs.getString("info");
                Reagent reagent = new Reagent(reagentId,reagentName,null,null);
                list.add(new Pair<>(reagent,comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> getQueryEmployeeManager1(Integer orderId) {
        ArrayList<Employee> list = new ArrayList<>();
        try {
            String select = "SELECT DISTINCT em.id,em.name FROM activity ac JOIN employee em ON em.id=ac.employee_id WHERE ac.order_id=? ORDER BY em.name";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ps.setInt(1,orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer employeeId = rs.getInt("em.id");
                String employeeName = rs.getString("em.name");
                Employee employee = new Employee(employeeId,employeeName,null,null,null,null);
                list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> getQueryEmployeeManager2() {
        ArrayList<Employee> list = new ArrayList<>();
        try {
            String select = "SELECT e.id,e.name,e.position,e.salary FROM employee e WHERE e.salary>=(SELECT AVG(em.salary) FROM employee em WHERE em.position = e.position)";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer employeeId = rs.getInt("e.id");
                String employeeName = rs.getString("e.name");
                Integer employeeSalary = rs.getInt("e.salary");
                String employeePosition = rs.getString("e.position");
                Employee employee = new Employee(employeeId,employeeName,null,null,employeePosition,employeeSalary);
                list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ImmutableTriple<Employee, String, String>> getQueryEmployeeManager3() {
        ArrayList<ImmutableTriple<Employee, String, String>> list = new ArrayList<>();
        try {
            String select = "SELECT em.id,em.name,em.salary,em.position,em.info,'Есть незавершенные заказы' AS orders FROM activity ac JOIN employee em ON em.id=ac.employee_id WHERE ac.done_time IS NULL GROUP BY em.id UNION SELECT em.id,em.name,em.salary,em.position,em.info,'Нет незавершенных заказов' AS orders FROM employee em WHERE em.id NOT IN (SELECT em.id FROM activity ac JOIN employee em ON em.id=ac.employee_id WHERE ac.done_time IS NULL GROUP BY em.id)";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer employeeId = rs.getInt("id");
                String employeeName = rs.getString("name");
                Integer employeeSalary = rs.getInt("salary");
                String employeePosition = rs.getString("position");
                String employeeInfo = rs.getString("info");
                String employeeOrders = rs.getString("orders");

                Employee employee = new Employee(employeeId,employeeName,null,null,employeePosition,employeeSalary);
                list.add(ImmutableTriple.of(employee,employeeOrders,employeeInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateDataEmployeeManager() {
        try {
            String update = "UPDATE employee SET info='Сотрудник месяца' WHERE id = ANY(SELECT employee_id FROM activity WHERE done_time IS NOT NULL AND YEAR(done_time)=YEAR(CURDATE()) AND MONTH(done_time)=MONTH(CURDATE()) GROUP BY employee_id HAVING COUNT(*) >= ALL(SELECT COUNT(*) FROM activity ac WHERE ac.done_time IS NOT NULL AND YEAR(ac.done_time)=YEAR(CURDATE()) AND MONTH(ac.done_time)=MONTH(CURDATE()) GROUP BY ac.employee_id))";
            PreparedStatement ps = getDbConnection().prepareStatement(update);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> getQueryClientManager1() {
        ArrayList<Client> list = new ArrayList<>();
        try {
            String select = "SELECT id,name,phone FROM client WHERE phone LIKE '+380_________'";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                Client client = new Client(id,name,phone,null);
                list.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Activity> getQueryClientManager2() {
        ArrayList<Activity> list = new ArrayList<>();
        try {
            String select = "SELECT order_id,client_id,name FROM activity ac JOIN coursework.order o ON o.id=ac.order_id WHERE ac.done_time IS NULL AND ac.start_time=(SELECT MAX(a.start_time) FROM activity a WHERE a.done_time IS NULL AND ac.order_id = a.order_id)";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer orderId = rs.getInt("order_id");
                Integer clientId = rs.getInt("client_id");
                String name = rs.getString("name");
                Client client = new Client(clientId,null,null,null);
                Order order = new Order(orderId,null,client);
                Activity activity = new Activity(null,name,null,null,order,null);
                list.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ImmutablePair<Client, Integer>> getQueryClientManager3() {
        ArrayList<ImmutablePair<Client, Integer>> list = new ArrayList<>();
        try {
            String select = "SELECT c.id,name,phone,COUNT(o.id) AS 'orderNumber',info FROM coursework.order o JOIN client c ON c.id = o.client_id GROUP BY c.id";
            PreparedStatement ps = getDbConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("c.id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                Integer orderNumber = rs.getInt("orderNumber");
                String info = rs.getString("info");

                Client client = new Client(id,name,phone,info);
                list.add(ImmutablePair.of(client,orderNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateDataClientManager() {
        try {
            String update = "UPDATE client SET info='Постоянный клиент' WHERE id = ANY(SELECT cl.id FROM client cl WHERE\n" +
                    "(SELECT COUNT(*) FROM activity ac JOIN coursework.order o ON o.id=ac.order_id WHERE o.client_id=cl.id AND ac.start_time<=ALL(SELECT a.start_time FROM activity a WHERE a.order_id=ac.order_id) AND DATEDIFF(CURDATE(),ac.start_time)<=30*6)>=5 GROUP BY cl.id)";
            PreparedStatement ps = getDbConnection().prepareStatement(update);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
