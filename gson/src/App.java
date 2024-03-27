import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.sql.*;
import java.util.Scanner;


public class App {
    public static void main(String[] args) throws SQLException, FileNotFoundException {

        String path = "\\C:\\Users\\User\\Desktop\\config.json\\";

        FileReader reader = new FileReader(path);

        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);

        String password = jsonObject.get("password").getAsString();
        String username = jsonObject.get("username").getAsString();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w aplikacji do łączenia się z bazą danych MSSQL!");
        System.out.println("Podaj najpierw potrzebne informacje: ");
        System.out.println("Adres: ");
        String adress = scanner.nextLine();
        System.out.print("Podaj numer portu (naciśnij Enter, aby użyć domyślnego portu 1433): ");
        String portStr = scanner.nextLine();

        String port;
        if (portStr.isEmpty()) {
            port = "1433"; // Domyślny port 1433
        } else {
            port = portStr;
        }

//        System.out.println("Podaj nazwę użytkownika: ");
//        String username = scanner.nextLine();
//        System.out.println("Podaj hasło: ");
//        String password = scanner.nextLine();

        String sql = "select nazwisko from Pracownicy where id=7";

        String url = "jdbc:sqlserver://" + adress + ":" + port + ";databaseName=dbad_s490128";
        StringBuilder phrase1 = new StringBuilder("select ");

        //pętla zapytań
        while (true) {
            System.out.println("Tworzenie zapytań.");
            String exit;

            do {
                System.out.println("Podaj nazwę atrybutu, którego chcesz wyświetlić, lub wpisz 'x' aby zakończyć:");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("x")) {
                    break;
                }

                phrase1.append(input).append(", ");

            } while (true);

            // Usunięcie ostatniego przecinka
            phrase1.deleteCharAt(phrase1.length() - 2);

            phrase1.append(" from ");

            do {
                System.out.println("Podaj nazwę tabeli, z której chcesz wybrać dane, lub wpisz 'x' aby zakończyć:");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("x")) {
                    break;
                }

                phrase1.append(input).append(" ");

            } while (true);

            System.out.println("Czy chcesz dodać warunki? y/n");
            String condition = scanner.nextLine();
            if(condition.equalsIgnoreCase("Y"))
            {
                phrase1.append(" where ");
                do{
                    System.out.println("Podaj proszę warunek.");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("x")) {
                        break;
                    }
                    phrase1.append(input).append(" ");

                }while (true);
            }

            System.out.println("Wciśnij 'q' aby wyjść lub dowolny inny klawisz, aby kontynuować.");
            exit = scanner.nextLine();
            if (exit.equalsIgnoreCase("q")) {
                break;
            }
        }

        scanner.close();

        String queryString = phrase1.toString();


        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(queryString);

            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) { // Iterowanie po wynikach zapytania
                for (int i = 1; i <= columns; i++) {
                    String value = rs.getString(i);
                    System.out.print(value + "\t");
                }
                System.out.println();
            }

            con.close(); // Zamknięcie połączenia
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
//Serwer:      mssql-2017.labs.wmi.amu.edu.pl
//Użytkownik:  dbad_s490128
//Hasło:       icP4qF20wj
//Baza:        dbad_s490128