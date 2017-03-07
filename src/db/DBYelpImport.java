package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Create DB tables in MySQL. This doesn't have to be exposed to the font end of
 * user.
 */
public class DBYelpImport {
	// THis is not part of the server
	public static void main(String[] args) {
		try {
			// Ensure the driver is imported.
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = null;

			try {
				System.out.println("Connecting to \n" + DBUtil.URL);
				// This is a static class, take URL as parameter, to connect
				// mySQL and get a connection object
				conn = DriverManager.getConnection(DBUtil.URL);
			} catch (SQLException e) {
				// print error message if connection failed
				System.out.println("SQLException " + e.getMessage());
				System.out.println("SQLState " + e.getSQLState());
				System.out.println("VendorError " + e.getErrorCode());
			}
			if (conn == null) {
				// Connection failed, then return
				return;
			}
			// Step 1 Drop tables in case they exist.
			// Interview Q: Cna the deleting order be reversed?
			// Ans: NO, as reason as the below question
			Statement stmt = conn.createStatement();

			String sql = "DROP TABLE IF EXISTS history";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS restaurants";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql);

			// ********* Create tables for the restaurant******************
			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql);

			// Interview Questions: does the order of the creation matter?
			// Ans: Yes, because SQL requires create the restaurant first, and
			// then history and then user
			sql = "CREATE TABLE restaurants " + "(business_id VARCHAR(255) NOT NULL, " + " name VARCHAR(255), "
					+ "categories VARCHAR(255), " + "city VARCHAR(255), " + "state VARCHAR(255), " + "stars FLOAT,"
					+ "full_address VARCHAR(255), " + "latitude FLOAT, " + " longitude FLOAT, "
					+ "image_url VARCHAR(255)," + "url VARCHAR(255)," + " PRIMARY KEY ( business_id ))";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE users " + "(user_id VARCHAR(255) NOT NULL, " + " password VARCHAR(255) NOT NULL, "
					+ " first_name VARCHAR(255), last_name VARCHAR(255), "
					// This is the ID for the user
					+ " PRIMARY KEY ( user_id ))";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE history "
					// To create a history ID, so that to auto_increament the
					// history ID, to keep track the user visit history
					+ "(visit_history_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, "
					+ " user_id VARCHAR(255) NOT NULL , " + " business_id VARCHAR(255) NOT NULL, "
					+ " last_visited_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, "
					+ " PRIMARY KEY (visit_history_id),"

					// Left is theB-ID in the history and refer to the
					// restaurant ID, if not the schema is wrong. So that it
					// make sure tha tall the visited ID is the restaurant in
					// the database
					+ "FOREIGN KEY (business_id) REFERENCES restaurants(business_id),"
					// This is same as the last line
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id))";
			stmt.executeUpdate(sql);

			// Step 3: insert data
			// Create a fake user
			//
			sql = "INSERT INTO users " + "VALUES (\"1111\", \"3229c1097c00d497a0fd282d586be050\", \"John\", \"Smith\")";
			stmt.executeUpdate(sql);

			System.out.println("DBYelpImport: Nice work Yi! import is done successfully.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
