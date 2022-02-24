import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.applicationService;

import java.sql.SQLException;

//@SpringBootApplication
public class NewEncrypt {


    public static void main(String[] argv) throws SQLException {
        //SpringApplication.run(NewEncrypt.class, argv);
        applicationService.test();
    }
}
