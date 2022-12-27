import java.util.Scanner;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

/**
 * Main entry to program.
 */
public class p1 {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	public static void main(String argv[]) {
		System.out.println(":: PROGRAM START");
		BankingSystem.init(argv[0]);
		init(argv[0]);
		screen1();

		System.out.println(":: PROGRAM END");
	}

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

	public static void screen1() {
		BankingSystem bank = new BankingSystem();
		Scanner customer = new Scanner(System.in);
		while (true) {
			System.out.println("Welcome to the Self Services Banking System!");
			System.out.println("1. New Customer");
			System.out.println("2. Customer Login");
			System.out.println("3. Exit");

			int login = customer.nextInt();
			if (login == 1) {
				String name;
				String gender;
				String age;
				String pin;
				System.out.println("Please enter your name: ");
				name = customer.nextLine();
				name = customer.nextLine();

				while (name.contentEquals("") == true || name.contentEquals("\n") == true) {
					System.out.println("Error, invalid input. Please entert your name: ");
					name = customer.nextLine();
				}

				System.out.println("Please enter your gender, must enter M or F: ");
				gender = customer.nextLine();
				while (gender.contentEquals("M") == false && gender.contentEquals("F") == false) {
					System.out.println("Error, invalid input. Please entert your gender: ");
					gender = customer.nextLine();

				}

				System.out.println("Please enter your age: ");
				age = customer.nextLine();
				int ageNum = 0;
				ageNum = Integer.valueOf(age);
				while (ageNum < 0) {
					System.out.println("Error, invalid input. Please entert your age: ");
					age = customer.nextLine();
					ageNum = Integer.valueOf(age);
				}

				System.out.println("Please enter your pin: ");
				pin = customer.nextLine();
				int pinNum = 0;
				pinNum = Integer.valueOf(pin);
				while (pinNum < 0) {
					System.out.println("Error, invalid input. Please entert your pin: ");
					pin = customer.nextLine();
					pinNum = Integer.valueOf(pin);
				}

				bank.newCustomer(name, gender, age, pin);
				
				String id = "";
				String queury = "SELECT MAX(id) FROM p1.customer";
				
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(queury);
					while(rs.next()) {
						id = rs.getString(1);
					}
					System.out.println("Your ID is: " + id);
					screen2(id);
					stmt.close();
					con.close();
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
				
			} else if (login == 2) {

				int id;
				int pin;
				String name = "";
				System.out.println("Please enter your ID: ");
				id = customer.nextInt();
				System.out.println("Please enter your PIN: ");
				pin = customer.nextInt();

				if (pin == 0 && id == 0) {
					screen4();
				} else {

					String query = "SELECT NAME FROM p1.customer AS customer WHERE customer.id = " + id
							+ " AND customer.pin = " + pin;

					try {
						Class.forName(driver);
						con = DriverManager.getConnection(url, username, password);
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery(query);
						// System.out.println(rs.getString());
						while (rs.next()) {
							name = rs.getString(1);
							System.out.println(name);
						}
						stmt.close();
						con.close();

						if (name.contentEquals("") == true) {
							System.out.println("Login failed");
						} else {
							screen2(String.valueOf(id));
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				}

			} else if (login == 3) {
				System.out.println("Thanks for using our banking system, Goodbye!");
				break;
			} else {
				System.out.println("Error, invalid input, please try again!");
			}
		}
	}

	public static void screen2(String id) {
		BankingSystem bank = new BankingSystem();
		Scanner user = new Scanner(System.in);

		while (true) {
			System.out.println("Customer Main Menu");
			System.out.println("1. Open Account");
			System.out.println("2. Close Account");
			System.out.println("3. Deposit");
			System.out.println("4. Withdraw");
			System.out.println("5. Transfer");
			System.out.println("6. Account Summary");
			System.out.println("7. Exit");

			int userfunc = user.nextInt();

			if (userfunc == 1) {
				System.out.println("Please enter the type of account you want, C for Checking and S For Savings: ");
				String accountType = user.nextLine();
				accountType = user.nextLine();
				while ((accountType.contentEquals("C") == false && accountType.contentEquals("S") == false)
						|| accountType.contentEquals("") == true) {
					System.out.println("Error invalid input, please enter C for checking or S for savings!");
					accountType = user.nextLine();
				}

				System.out.println("Please enter the inital balance: ");
				String balance = user.nextLine();
				int bal = Integer.valueOf(balance);
				while (bal < 0 || balance.contentEquals("") == true) {
					System.out.println("Error Invalid input, please enter your initial deposit");
					balance = user.nextLine();
					bal = Integer.valueOf(balance);
				}
				
				String accId = "";
				
				System.out.println("Please enter the user ID associated with this account");
				accId = user.nextLine();
				
				bank.openAccount(accId, accountType, balance);
				String bankNumb = "";
				String queury = "SELECT MAX(number) FROM p1.account";
				
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(queury);
					while(rs.next()) {
						bankNumb = rs.getString(1);
					}
					System.out.println("Bank Account Number for " + accId + " is: " + bankNumb);
					
					stmt.close();
					con.close();
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}

			} else if (userfunc == 2) {
				System.out.println("Please enter the account number: ");
				String number = user.nextLine();
				number = user.nextLine();
				String query = "SELECT number FROM p1.account WHERE number = '" + number + "' AND id = '" + id + "'";
				String accId = "";
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()) {
						accId = rs.getString(1);
						break;
					}
					stmt.close();
					con.close();
					rs.close();
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
				if(accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T CLOSE ACCOUNTS NOT ASSOCIATED WITH YOUR ID");
					
				}else {
					bank.closeAccount(number);
				}
				

			} else if (userfunc == 3) {
				System.out.println("Please Enter the account number: ");
				
				String accNum = user.nextLine();
				accNum = user.nextLine();
				
				System.out.println("Please enter the amount you wish to deposit.");
				
				String amount = user.nextLine();
				
				while(Integer.valueOf(amount) < 0) {
					System.out.println("ERROR INCORRECT AMOUNT PLEASE ENTER A VALUE OF 0 OR GREATER.");
					amount = user.nextLine();
				}
				
				bank.deposit(accNum, amount);
			} else if (userfunc == 4) {
				System.out.println("Please Enter the account number: ");
				
				String accNum = user.nextLine();
				accNum = user.nextLine();
				
				System.out.println("Please enter the amount you wish to withdraw.");
				
				String amount = user.nextLine();
				
				while(Integer.valueOf(amount) < 0) {
					System.out.println("ERROR INCORRECT AMOUNT PLEASE ENTER A VALUE OF 0 OR GREATER.");
					amount = user.nextLine();
				}
				
				String query = "SELECT number FROM p1.account WHERE number = '" + accNum + "' AND id = '" + id + "'";
				String accId = "";
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()) {
						accId = rs.getString(1);
						break;
					}
					stmt.close();
					con.close();
					rs.close();
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
				if(accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T WITHDRAW FROM ACCOUNTS NOT ASSOCIATED WITH YOUR ID");
					
				}else {
					bank.withdraw(accNum, amount);
				}
				
			} else if (userfunc == 5) {
				System.out.println("Please enter the account you are transferring from: ");
				String srcAccNum = user.nextLine();
				srcAccNum = user.nextLine();
				
				String query = "SELECT number FROM p1.account WHERE number = '" + srcAccNum + "' AND id = '" + id + "'";
				String accId = "";
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()) {
						accId = rs.getString(1);
						break;
					}
					stmt.close();
					con.close();
					rs.close();
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
				if(accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T WITHDRAW FROM ACCOUNTS NOT ASSOCIATED WITH YOUR ID");
					
				}else {
					
					System.out.println("Please enter the account number for the account you will be transferring funds to: ");
					
					String destAccNum = user.nextLine();
					
					System.out.println("Please enter the amount you wish to transfer");
					
					String amount = user.nextLine();
					
					while(Integer.valueOf(amount) < 0) {
						System.out.println("ERROR! INCVALID AMOUNT!");
						System.out.println("Please enter the amount you wish to transfer");
						amount = user.nextLine();
					}
					
					bank.transfer(srcAccNum, destAccNum, amount);
				}
				
				
			} else if (userfunc == 6) {
				
				bank.accountSummary(id);
			} else if (userfunc == 7) {
				System.out.println("Logging Out");
				break;
			} else {
				System.out.println("Error, invalid input, please try again!");
			}
		}

	}

	public static void screen4() {
		BankingSystem bank = new BankingSystem();
		Scanner user = new Scanner(System.in);

		while (true) {

			System.out.println("Administrator Menu");
			System.out.println("1. Account summary for a customer");
			System.out.println("2. Report A :: Customer Information with Total Balance in Decreasing Order");
			System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups");
			System.out.println("4. Exit");

			int admin = user.nextInt();

			if (admin == 1) {
				
				System.out.println("Please enter the ID Number of the customer whose summary you wish to see: ");
				
				String id = user.nextLine();
				id = user.nextLine();
				
				bank.accountSummary(id);

			} else if (admin == 2) {
								
				bank.reportA();
			} else if (admin == 3) {
				System.out.println("Please enter the lowest possible age to get a report from");
				
				String low = user.nextLine();
				low = user.nextLine();
				
				while(Integer.valueOf(low) < 0) {
					System.out.println("ERROR, INVALID AGE!");
					System.out.println("Please enter the lowest possible age to get a report from");
					low = user.nextLine();
				}
				
				System.out.println("Please enter the highest possible age to get a report from");
				
				String high = user.nextLine();
				
				while(Integer.valueOf(high) < 0) {
					System.out.println("ERROR, INVALID AGE!");
					System.out.println("Please enter the highest possible age to get a report from");
					high = user.nextLine();
				}
				bank.reportB(low, high);
			} else if (admin == 4) {
				System.out.println("Logging Out");
				break;
			}
		}
	}
}