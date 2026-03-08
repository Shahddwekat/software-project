/*package edu.najah.software;

/**
 * Hello world!

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
*/
package edu.najah.software;

import java.time.LocalDateTime;
import edu.najah.software.service.AppointmentService;

public class App {

    public static void main(String[] args) {

        AppointmentService service = new AppointmentService();

        service.bookAppointment(
                "A1",
                LocalDateTime.now(),
                60,
                3
        );
    }
}