package com.example.demo;



import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class csvtodb
{
    public static void main( String[] args )
    {
        String[] line;
        String jdbcURL = "jdbc:postgresql://localhost:5432/sample";


        try {

            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\athul\\IdeaProjects\\final_assignment\\src\\public\\customers.csv"));
            reader.readNext();

            Connection connection = DriverManager.getConnection(jdbcURL,"postgres","athul");

            String insert_table1 = "INSERT INTO customer_info VALUES(?,?,?,?,?,?,?)";
            PreparedStatement statement_1 = connection.prepareStatement(insert_table1);

            String insert_table2 = "INSERT INTO customer_work_info VALUES(?,?,?,?,?,?,?)";
            PreparedStatement statement_2 = connection.prepareStatement(insert_table2);

            while(( line = reader.readNext()) != null){
                String customer_id = line[1];
                statement_1.setString(1,customer_id);
                String f_name = line[2].toUpperCase();
                statement_1.setString(2,f_name);
                String l_name = line[3].toUpperCase();
                statement_1.setString(3,l_name);
                String city   =  line[5];
                statement_1.setString(4,city);
                String country = line[6];
                statement_1.setString(5,country);
                String personal_phnum = line[7];
                statement_1.setString(6,personal_phnum);
                String personal_email = line[9].toLowerCase();
                statement_1.setString(7,personal_email);
                System.out.println(statement_1);

                statement_1.addBatch();

                statement_2.setString(7,customer_id);
                String full_name = line[2]+" "+line[3];
                statement_2.setString(1,full_name);
                String office_Loc = "Bangalore";
                statement_2.setString(2,office_Loc);
                String subscription_date = line[10];
                statement_2.setString(3,subscription_date);
                String website = line[11];
                statement_2.setString(4,website);
                String work_phnum = line[8];
                statement_2.setString(5,work_phnum);
                String work_email = f_name.toLowerCase()+"_"+l_name.toLowerCase()+"@sample.com";
                statement_2.setString(6,work_email);

                statement_2.addBatch();

            }
            statement_1.executeBatch();
            statement_2.executeBatch();

            statement_1.close();
            statement_2.close();
            connection.close();

            System.out.println("Data has been inserted successfully.");

        } catch (CsvValidationException | IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
