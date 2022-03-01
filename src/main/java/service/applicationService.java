package service;

import org.springframework.stereotype.Service;
import util.CryptoUtil;

import java.sql.*;
import java.time.LocalDateTime;

//@Service
public class applicationService{
    public static void test() throws SQLException {
        Statement statement;
        System.out.println("PostgreSQL JDBC Connection Testing ~");


        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println("Unable to find the PostgreSQL JDBC Driver!");
            e.printStackTrace();
            return;
        }


        try (Connection connection =
                     DriverManager.getConnection("jdbc:postgresql://qa3.mosip.net:30090/mosip_prereg",
                             "postgres", "mosip123")) {
            ResultSet rs = null;
            String query = "SELECT * FROM applicant_demographic";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            int i=0;
            while (rs.next()) {
                if (i<218) {
                    System.out.println((rs.getBinaryStream("demog_detail")));
                    byte[] b = rs.getBytes("demog_detail");
                    System.out.println(new String(b));
                    byte[] b1 = rs.getBinaryStream("demog_detail").toString().getBytes();
                    System.out.println(new String(b1));
                    System.out.println(b1);
                    CryptoUtil cryptoUtil = new CryptoUtil();
                    System.out.println("encrypted data" + b);


                    System.out.println("decrypted data" + rs.getString("cr_by"));
                    byte[] decrypted = cryptoUtil.decrypt(b, LocalDateTime.now());
//                if (i++>37) {
//                    break;
//                }
                }
                i++;
            }
            System.out.println(i);
        }

    }
}