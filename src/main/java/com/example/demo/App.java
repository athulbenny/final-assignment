
//package org.assignment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class App
{
	public static void main( String[] args )
	{
		String jdbcURL = "jdbc:postgresql://localhost:5432/sample";
		String csvfilename = "customer_details.csv";

		System.out.print("Enter the Name of the Customer :: ");
		Scanner sc = new Scanner(System.in);
		String f_name = sc.next()+"%";

		try{
			Connection connection = DriverManager.getConnection(jdbcURL, "postgres", "athul");
			String query = "SELECT * FROM customer_info,customer_work_info WHERE customer_info.customer_id = customer_work_info.customer_id AND customer_work_info.full_name ILIKE ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1,f_name);
			ResultSet rs = stmt.executeQuery();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate currentDate = LocalDate.now();

			try(FileWriter writer = new FileWriter(csvfilename)){
				writer.append("customer_id,full_name,city,personal_phnum,personal_email,off_loc,subscription_date,pending_days,work_phnum,work_email\n");
			}


			while (rs.next()) {
				String customer_id = rs.getString("customer_id");
				String full_name = rs.getString("full_name");
				String city = rs.getString("city");
				String personal_phnum = rs.getString("personal_phnum");
				String work_phnum = rs.getString("work_phnum");
				String personal_email = rs.getString("personal_email");
				String work_email = rs.getString("work_email");
				String date = rs.getString("subscription_date");
				String office_loc = rs.getString("off_loc");

				long daysDifference = 0;
				try {
					LocalDate inputDate = LocalDate.parse(date, formatter);
					daysDifference = ChronoUnit.DAYS.between(inputDate, currentDate);

				} catch (DateTimeParseException e) {
					System.out.println("Invalid date format: " + date);
				}

				System.out.println(full_name + city + personal_email + personal_phnum + work_email + work_phnum + customer_id + date);
				System.out.println(daysDifference);


				try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvfilename,true))){
					writer.append(customer_id)
							.append(',')
							.append(full_name)
							.append(',')
							.append(city)
							.append(',')
							.append(personal_phnum)
							.append(',')
							.append(personal_email)
							.append(',')
							.append(office_loc)
							.append(',')
							.append(date)
							.append(',')
							.append(String.valueOf(daysDifference))
							.append(',')
							.append(work_phnum)
							.append(',')
							.append(work_email)
							.append('\n');
				}
				catch (IOException e){
					throw new RuntimeException(e);
				}


			}
			System.out.println("CSV file generated: " + csvfilename);
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
