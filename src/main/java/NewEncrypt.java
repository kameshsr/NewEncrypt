import org.postgresql.core.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import java.sql.*;

public class NewEncrypt {
    @Qualifier("selfTokenRestTemplate")
    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] argv) throws SQLException {
        Statement statement;
        System.out.println("PostgreSQL JDBC Connection Testing ~");


        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println("Unable to find the PostgreSQL JDBC Driver!");
            e.printStackTrace();
            return;
        }

        // default database: postgres
        // JDK 7, auto close connection with try-with-resources

//        Connection conn = DriverManager.getConnection(url, username, password);
//
//// Just pass the connection and the table name to printTable()
//

        try (Connection connection =
                     DriverManager.getConnection("jdbc:postgresql://qa3.mosip.net:30090/mosip_prereg",
                             "postgres", "mosip123")) {
            ResultSet rs = null;
            String query = "SELECT * FROM applicant_demographic";
                statement = connection.createStatement();
                rs = statement.executeQuery(query);
                while (rs.next()) {
                System.out.println((rs.getBinaryStream("demog_detail")));


//            DatabaseMetaData metaData = connection.getMetaData();
//
//            try (ResultSet rs = metaData.getTables(null, null, "%", null)) {
//
//                ResultSetMetaData rsMeta = rs.getMetaData();
//                int columnCount = rsMeta.getColumnCount();
//
//                while (rs.next()) {
//
////                    System.out.println("\n----------");
////                    System.out.println(rs.getString("TABLE_NAME"));
//                    if(rs.getString("TABLE_NAME").equalsIgnoreCase("applicant_demographic")){
//                        DBTablePrinter.printTable(connection, rs.getString("TABLE_NAME"));
//                    }
//
////                    System.out.println("----------");
//
////                    for (int i = 1; i <= columnCount; i++) {
////                        String columnName = rsMeta.getColumnName(i);
////                        System.out.format("%s:%s\n", columnName, rs.getString(i));
////                    }
//
//                }
//            }

//        } catch (SQLException e) {
//            System.err.println("Something went wrong!");
//            e.printStackTrace();
//            return;
//        }

//    } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

    }}}}

//    String query = "SELECT * FROM employee";
//    List<Employee> list = new ArrayList<Employee>();
//    Employee employee = null;
//    ResultSet rs = null;
//        try {
//                connection = ConnectionFactory.getConnection();
//                statement = connection.createStatement();
//                rs = statement.executeQuery(query);
//                while (rs.next()) {
//                employee = new Employee();
//                /*Retrieve one employee details
//                and store it in employee object*/
//                employee.setEmpId(rs.getInt("emp_id"));
//                employee.setEmpName(rs.getString("emp_name"));
//                employee.setDob(rs.getDate("dob"));
//                employee.setSalary(rs.getDouble("salary"));
//                employee.setDeptId((rs.getInt("dept_id")));
//
//                //add each employee to the list
//                list.add(employee);
//                }