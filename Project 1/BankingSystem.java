import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * 
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties(); // Create a new Properties object
			FileInputStream input = new FileInputStream(filename); // Create a new FileInputStream object using our
																	// filename parameter
			props.load(input); // Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver"); // Load the driver
			url = props.getProperty("jdbc.url"); // Load the url
			username = props.getProperty("jdbc.username"); // Load the username
			password = props.getProperty("jdbc.password"); // Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
			e.printStackTrace();
		}
	}

	/**
	 * Create a new customer.
	 * 
	 * @param name   customer name
	 * @param gender customer gender
	 * @param age    customer age
	 * @param pin    customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) {
		System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
		/* insert your code here */
		for(int i = 0; i < pin.length(); i++) {
			char c = pin.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
				return;
			}
		}
		for(int i = 0; i < age.length(); i++) {
			char c = age.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID AGE");
				return;
			}
		}
		String queury = "INSERT INTO P1.CUSTOMER (name, gender, age, pin) " + "VALUES ('" + name + "', '" + gender
				+ "', '" + age + "', '" + pin + "')";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			stmt.execute(queury);
			//System.out.println(rs.getString());
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}

		System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
		
		
		
		
	}

	/**
	 * Open a new account.
	 * 
	 * @param id     customer id
	 * @param type   type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) {
		System.out.println(":: OPEN ACCOUNT - RUNNING");
		
		for(int i = 0; i < id.length(); i++) {
			char c = id.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID ID");
				return;
			}
		}
		
		for(int i = 0; i < amount.length(); i++) {
			char c = amount.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID AMOUNT");
				return;
			}
		}
		
		String queury = "INSERT INTO P1.ACCOUNT (id, balance, type, status) " + "VALUES ('" + id + "', '" + amount
				+ "', '" + type + "', 'A')";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			stmt.execute(queury);
			//System.out.println(rs.getString());
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: OPEN ACCOUNT - SUCCESS");
	}

	/**
	 * Close an account.
	 * 
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) {
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		
		for(int i = 0; i < accNum.length(); i++) {
			char c = accNum.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: CLOSE ACCOUNT - ERROR - INVALID ACCOUNT NUMBER");
				return;
			}
		}
		
		String queury = "UPDATE P1.ACCOUNT SET status = 'I', balance = '0' " + "where number = '" + accNum + "'";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			stmt.execute(queury);
			//System.out.println(rs.getString(1));
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: CLOSE ACCOUNT - SUCCESS");
	}

	/**
	 * Deposit into an account.
	 * 
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) {
		System.out.println(":: DEPOSIT - RUNNING");
		String query = "SELECT balance FROM P1.ACCOUNT where number = '" + accNum + "'";
		
		for(int i = 0; i < accNum.length(); i++) {
			char c = accNum.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: DEPOSIT - ERROR - INVALID ACCOUNT NUMBER");
				return;
			}
		}
		
		for(int i = 0; i < amount.length(); i++) {
			char c = amount.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
				return;
			}
		}
		int num = Integer.valueOf(amount);
		int accAmount = 0;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				accAmount = rs.getInt(1);
				break;
			}
			
			num += accAmount;
			query = "UPDATE P1.ACCOUNT SET balance = '" + num + "' WHERE number = '" + accNum + "' AND status = 'A'";
			
			stmt.execute(query);
			
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: DEPOSIT - SUCCESS");
	}

	/**
	 * Withdraw from an account.
	 * 
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) {
		System.out.println(":: WITHDRAW - RUNNING");
		String query = "SELECT balance FROM P1.ACCOUNT where number = '" + accNum + "'";
		
		for(int i = 0; i < accNum.length(); i++) {
			char c = accNum.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT NUMBER");
				return;
			}
		}
		
		for(int i = 0; i < amount.length(); i++) {
			char c = amount.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
				return;
			}
		}
		int num = Integer.valueOf(amount);
		int accAmount = 0;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				accAmount = rs.getInt(1);
				break;
			}
			
			num = accAmount - num;
			if(num < 0) {
				System.out.println(":: WITHDRAW - ERROR - NOT ENOUGH FUNDS");
				return;
			}else {
				query = "UPDATE P1.ACCOUNT SET balance = '" + num + "' WHERE number = '" + accNum + "' AND status = 'A'";
				
				stmt.execute(query);
			}
			
			
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: WITHDRAW - SUCCESS");
	}

	/**
	 * Transfer amount from source account to destination account.
	 * 
	 * @param srcAccNum  source account number
	 * @param destAccNum destination account number
	 * @param amount     transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) {
		System.out.println(":: TRANSFER - RUNNING");
		
		for(int i = 0; i < srcAccNum.length(); i++) {
			char c = srcAccNum.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT NUMBER");
				return;
			}
		}
		
		for(int i = 0; i < destAccNum.length(); i++) {
			char c = destAccNum.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT NUMBER");
				return;
			}
		}
		
		for(int i = 0; i < amount.length(); i++) {
			char c = amount.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
				return;
			}
		}
		
		String query = "SELECT balance FROM P1.ACCOUNT where number = '" + srcAccNum + "'";
		int numTransfer = Integer.valueOf(amount);
		int srcNum = 0;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				srcNum = rs.getInt(1);
				break;
			}
			
			srcNum = srcNum - numTransfer;
			
			if(srcNum < 0) {
				System.out.println(":: TRANSFER - ERROR - NOT ENOUGH FUNDS");
				rs.close();
				stmt.close();
				con.close();
				return;
			}else {
				query = "UPDATE P1.ACCOUNT SET balance = '" + srcNum + "' WHERE number = '" + srcAccNum + "' AND status = 'A'";
			
				stmt.execute(query);
			}
			
			query = "SELECT balance FROM P1.ACCOUNT where number = '" + destAccNum + "'";
			int destNum = 0;
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				destNum = rs.getInt(1);
				break;
			}
			
			destNum = destNum + numTransfer;
			
			query = "UPDATE P1.ACCOUNT SET balance = '" + destNum + "' WHERE number = '" + destAccNum + "' AND status = 'A'";
			
			stmt.execute(query);
			
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: TRANSFER - SUCCESS");
	}

	/**
	 * Display account summary.
	 * 
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) {
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		
		for(int i = 0; i < cusID.length(); i++) {
			char c = cusID.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: ACCOUNT SUMMARY - ERROR - INVALID ID");
				return;
			}
		}
		
		String query = "SELECT number, balance FROM p1.account WHERE ID = '" + cusID + "' AND status = 'A'";
		int totalBalance = 0;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("NUMBER      BALANCE");
			System.out.println("----------- -----------");
			
			while(rs.next()) {
				int number = rs.getInt(1);
				int balance = rs.getInt(2);
				System.out.println("       " + number + "        " + balance);
				totalBalance += balance;
			}
			
			System.out.println("-----------------------");
			System.out.println("TOTAL             " + totalBalance);
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing
	 * Order.
	 */
	public static void reportA() {
		System.out.println(":: REPORT A - RUNNING");
		String query = "SELECT customer.id, name, gender, age, TOTAL FROM p1.customer as customer "
				+ "JOIN (SELECT id, sum(balance) AS TOTAL from p1.account group by id) as account ON account.id = customer.id ORDER BY TOTAL desc";
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("ID          NAME            GENDER AGE         TOTAL");
			System.out.println("----------- --------------- ------ ----------- -----------");
			
			while(rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String gender = rs.getString(3);
				int age = rs.getInt(4);
				int total = rs.getInt(5);
				System.out.println("       " + id + " " + name + "           " + gender + "               " + age + "       " + total);
				
			}
			
			
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: REPORT A - SUCCESS");
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing
	 * Order.
	 * 
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) {
		System.out.println(":: REPORT B - RUNNING");
		
		for(int i = 0; i < min.length(); i++) {
			char c = min.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: REPORT B - ERROR - INVALID MINIMUM AGE");
				return;
			}
		}
		
		for(int i = 0; i < max.length(); i++) {
			char c = max.charAt(i);
			if(c < '0' || c > '9') {
				System.out.println(":: REPORT B - ERROR - INVALID MAXIMUM AGE");
				return;
			}
		}
		
		String query = "select avg(total) from (select sum(balance) as total from p1.account, p1.customer where p1.account.id = p1.customer.id and (age <= '" + max + "' and age >= '" + min + "') group by p1.customer.id) as b";
		
		// select avg(total) from (select sum(balance) as total from p1.account, p1.customer where
		// p1.account.id = p1.customer.id and (age <= '18' and age >= '10') group by p1.customer.id) as b
		//String query = "select avg(total) from (select sum(balance) as total from p1.account, p1.customer where" + 
		//		" p1.account.id = p1.customer.id and (age <= '" + max + "' and age >= '" + min + '") group by p1.customer.id)";		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Average");
			System.out.println("-----------");
			
			while(rs.next()) {
				int average = rs.getInt(1);
				System.out.println("       " + average);
				
			}
			
			
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Can't Execute!");
			e.printStackTrace();
		}
		System.out.println(":: REPORT B - SUCCESS");
	}
}
