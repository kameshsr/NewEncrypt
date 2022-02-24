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
            while (rs.next()) {
                System.out.println((rs.getBinaryStream("demog_detail")));
                byte[] b = rs.getBytes("demog_detail");
                System.out.println(new String(b));
                CryptoUtil cryptoUtil = new CryptoUtil();
                System.out.println("encrypted data"+b);
                byte[] decrypted = cryptoUtil.decrypt(b, LocalDateTime.now());
                break;
            }
        }

    }
}