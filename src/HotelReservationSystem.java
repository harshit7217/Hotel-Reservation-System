import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class HotelReservationSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Anika@Tomar";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                switch(choice) {
                    case 1:
                        reserveRoom(connection, sc);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, sc);
                        break;
                    case 4:
                        updateReservation(connection, sc);
                        break;
                    case 5:
                        deleteReservation(connection, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reserveRoom(Connection connection, Scanner sc){
        try {
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number");
            int roomNumber = sc.nextInt();
            System.out.println("Enter Contact Number");
            String contactNumber = sc.next();
            String sql = "INSERT INTO reservation (guest_name, room_number, guest_number) "+"VALUES('" + guestName +"', "+roomNumber +",'"+contactNumber +"')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows > 0){
                    System.out.println("Reservation Successful!");
                }else {
                    System.out.println("Reservation Failed");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewReservation(Connection connection) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, guest_number, reservation_date FROM reservation";

        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Current Reservation");
            System.out.println("+-------------------------------------------------------------------------------------");
            System.out.println("| Reservation ID  |  Guest      | Room Number   | Guest Number    | Reservation Date|");
            System.out.println("+-------------------------------------------------------------------------------------");
            while(resultSet.next()){
                int id = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("guest_number");
                String date = resultSet.getString("reservation_date");

                System.out.printf("| %14d | %-1s | %-13d | %-20s | %-19s   |\n", id, guestName,roomNumber,contactNumber,date);
            }
            System.out.println("+--------------------------------------------------------------------------------------+");

        }
    }

    private static void getRoomNumber(Connection connection, Scanner sc){
        try {
            System.out.println("Enter Reservation ID: ");
            int id = sc.nextInt();
            System.out.println("Enter guest name: ");
            String name = sc.next();
            sc.nextLine();
            String sql = "SELECT room_number FROM reservation WHERE reservation_id = "+ id +" AND guest_name= '"+name+"'";

            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

                if(resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room Number for reservation ID " + id + " and Guest " + name + "is: "+ roomNumber);
                }else {
                    System.out.println("Reservation not found for the gie=ven ID and guest name");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private  static void updateReservation(Connection connection, Scanner sc){
        try {
            System.out.println("Enter Reservation ID to update");
            int id = sc.nextInt();
            sc.nextLine();

            if(!reservationExists(connection, id)) {
                System.out.println("reservation not found for the given ID");
                return;
            }

            System.out.println("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = sc.next();
            sc.nextLine();

            String sql = "UPDATE reservation SET guest_name = '" + newGuestName +
                    "', guest_number = '" + newContactNumber +
                    "', room_number = '" + newRoomNumber +
                    "' WHERE reservation_id = " + id;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation updated successfully!");
                }else {
                    System.out.println("Reservation update failed");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter reservation ID to delete");
            int id = sc.nextInt();

            if(!reservationExists(connection, id)) {
                System.out.println("Reservation not found for the given ID");
                return;
            }
            String sql = "DELETE FROM reservation WHERE reservation_id = "+id;
            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation deleted successfully");
                }else {
                    System.out.println("Reservation deletion failed");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int id) {
        try {
            String sql = "SELECT reservation_id FROM reservation WHERE reservation_id="+id;

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i= 5;
        while(i!=0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System");
    }
}