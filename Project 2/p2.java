import java.util.Scanner;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

/**
 * Main entry to program.
 */
public class p2 {
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
		// BankingSystem.init(argv[0]);
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
		// BankingSystem bank = new BankingSystem();
		Scanner customer = new Scanner(System.in);
		while (true) {
			System.out.println("Welcome to the Self Services Banking System!");
			System.out.println("1. New Customer");
			System.out.println("2. Customer Login");
			System.out.println("3. Exit");

			int login = customer.nextInt();
			if (login == 1) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				String name;
				String gender;
				int age;
				int pin;
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
				
				try {
					age = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID AGE");
					continue;
				}
				
				
				
				while (age < 0) {
					System.out.println("Error, invalid input. Please enter your age: ");
					try {
						age = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID AGE");
						error = true;
						break;
					}
					
				}
				if(error) {
					continue;
				}

				System.out.println("Please enter your pin: ");
				try {
					pin = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
					continue;
				}
				
				while (pin < 0) {
					System.out.println("Error, invalid input. Please enter your pin: ");
					try {
						pin = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
						error = true;
						break;
					}
					
				}
				
				if(error) {
					continue;
				}


				String procCall = "CALL P2.CUST_CRT(?, ?, ?, ?, ?, ?, ?)";

				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					CallableStatement stmt = con.prepareCall(procCall);
					stmt.setString(1, name);
					stmt.setString(2, gender);
					stmt.setInt(3, Integer.valueOf(age));
					stmt.setInt(4, Integer.valueOf(pin));
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.INTEGER);
					stmt.registerOutParameter(7, Types.VARCHAR);

					stmt.execute();

					int newid = stmt.getInt(5);
					int code = stmt.getInt(6);
					String err_msg = stmt.getString(7);
					if (code < 0) {
						System.out.println(err_msg);
					} else {
						System.out.println("Your ID is: " + newid);
					}

					stmt.close();
					con.close();
					screen2(String.valueOf(newid));
				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}

			} else if (login == 2) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				int id = 0;
				int pin = 0;
				String name = "";
				System.out.println("Please enter your ID: ");
				try {
					id = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: LOGIN - ERROR - INVALID ID");
					continue;
				}
				
				while (id < 0) {
					System.out.println("Invalid id\nPlease enter your id: ");
					try {
						id = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: LOGIN - ERROR - INVALID ID");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				System.out.println("Please enter your PIN: ");
				try {
					pin = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: LOGIN - ERROR - INVALID PIN");
					continue;
				}
				
				while (pin < 0) {
					System.out.println("Invalid PIN\nPlease enter your PIN: ");
					try {
						pin = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: LOGIN - ERROR - INVALID PIN");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				String procCall = "CALL P2.CUST_LOGIN(?, ?, ?, ?, ?)";

				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					CallableStatement stmt = con.prepareCall(procCall);

					stmt.setInt(1, Integer.valueOf(id));
					stmt.setInt(2, Integer.valueOf(pin));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.INTEGER);
					stmt.registerOutParameter(5, Types.VARCHAR);

					stmt.execute();

					int valid = stmt.getInt(3);
					int code = stmt.getInt(4);
					String err_msg = stmt.getString(5);
					stmt.close();
					con.close();
					if (valid == 0) {
						System.out.println(err_msg);
					} else {
						System.out.println("Logging in!");
						screen2(String.valueOf(id));
					}
				} catch (Exception e) {

					e.printStackTrace();
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
		// BankingSystem bank = new BankingSystem();
		Scanner user = new Scanner(System.in);

		while (true) {
			System.out.println("Customer Main Menu");
			System.out.println("1. Open Account");
			System.out.println("2. Close Account");
			System.out.println("3. Deposit");
			System.out.println("4. Withdraw");
			System.out.println("5. Transfer");
			System.out.println("6. Add Interest");
			System.out.println("7. Exit");

			int userfunc = user.nextInt();

			if (userfunc == 1) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				System.out.println("Please enter the type of account you want, C for Checking and S For Savings: ");
				String accountType = user.nextLine();
				try {
					accountType = user.nextLine();
				}catch(Exception e) {
					System.out.println("Invalid Type");
					continue;
				}
				
				while ((accountType.contentEquals("C") == false && accountType.contentEquals("S") == false)
						|| accountType.contentEquals("") == true) {
					System.out.println("Error invalid input, please enter C for checking or S for savings!");
					accountType = user.nextLine();
				}
				int balance = 0;
				System.out.println("Please enter the inital balance: ");
				try {
					balance = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: ACCOUNT OPEN - ERROR - INVALID BALANCE");
					continue;
				}
				
				
				while (balance < 0) {
					System.out.println("Error, Invalid Balance\nPlease enter the initial balance: ");
					try {
						balance = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: ACCOUNT OPEN - ERROR - INVALID BALANCE");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				int accId = 0;

				System.out.println("Please enter the user ID associated with this account");
				try {
					accId = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: ACCOUNT OPEN - ERROR - INVALID ID");
					continue;
				}
				
				while(accId < 0) {
					System.out.println("Error, Invalid ID\nPlease enter the ID associated with this account: ");
					try {
						accId = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: ACCOUNT OPEN - ERROR - INVALID ID");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				String procCall = "CALL P2.ACCT_OPN(?, ?, ?, ?, ?, ?)";

				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					CallableStatement stmt = con.prepareCall(procCall);

					stmt.setInt(1, Integer.valueOf(accId));
					stmt.setInt(2, Integer.valueOf(balance));
					stmt.setString(3, accountType);
					stmt.registerOutParameter(4, Types.INTEGER);
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.VARCHAR);

					stmt.execute();

					int accnum = stmt.getInt(4);
					int sql_code = stmt.getInt(5);
					String err_msg = stmt.getString(6);
					stmt.close();
					con.close();
					if (sql_code < 0) {
						System.out.println(err_msg);
					} else {
						System.out.println("Account number for user " + accId + " is: " + accnum);
					}

				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}

			} else if (userfunc == 2) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				int number = 0;

				System.out.println("Please enter the number of the account you wish to close");
				try {
					number = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: ACCOUNT CLOSE - ERROR - INVALID ACCOUNT NUMBER");
					continue;
				}
				
				while(number < 0) {
					System.out.println("Error, Invalid ID\nPlease enter the number of the account you wish to close: ");
					try {
						number = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: ACCOUNT CLOSE - ERROR - INVALID ACCOUNT NUMBER");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}
				
				String query = "SELECT number FROM p2.account WHERE number = '" + number + "' AND id = '" + id + "'";
				String accId = "";
				
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
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
				if (accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T CLOSE ACCOUNTS NOT ASSOCIATED WITH YOUR ID");

				} else {
					String procCall = "CALL P2.ACCT_CLS(?, ?, ?)";

					try {
						Class.forName(driver);
						con = DriverManager.getConnection(url, username, password);
						CallableStatement stmt = con.prepareCall(procCall);

						stmt.setInt(1, Integer.valueOf(number));
						stmt.registerOutParameter(2, Types.INTEGER);
						stmt.registerOutParameter(3, Types.VARCHAR);

						stmt.execute();

						int sql_code = stmt.getInt(2);
						String err_msg = stmt.getString(3);
						stmt.close();
						con.close();
						if (sql_code < 0) {
							System.out.println(err_msg);
						} else {
							System.out.println("Account " + number + " is closed.");
						}

					} catch (Exception e) {
						System.out.println("Can't Execute!");
						e.printStackTrace();
					}
				}

			} else if (userfunc == 3) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				System.out.println("Please Enter the account number: ");

				int accNum = 0;
				try {
					accNum = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: DEPOSIT - ERROR - INVALID ACCOUNT NUMBER");
					continue;
				}
				
				while(accNum < 0) {
					System.out.println("Error, invalid account number, please enter the account number: ");
					try {
						accNum = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: DEPOSIT - ERROR - INVALID ACCOUNT NUMBER");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}
				
				System.out.println("Please enter the amount you wish to deposit.");

				int amount = 0;
				try {
					amount = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
					continue;
				}
				
				while (amount < 0) {
					System.out.println("ERROR INCORRECT AMOUNT PLEASE ENTER A VALUE OF 0 OR GREATER.");
					try {
						amount = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				String procCall = "CALL P2.ACCT_DEP(?, ?, ?, ?)";

				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					CallableStatement stmt = con.prepareCall(procCall);

					stmt.setInt(1, Integer.valueOf(accNum));
					stmt.setInt(2, Integer.valueOf(amount));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);

					stmt.execute();

					int sql_code = stmt.getInt(3);
					String err_msg = stmt.getString(4);
					stmt.close();
					con.close();
					if (sql_code < 0) {
						System.out.println(err_msg);
					} else {
						System.out.println("$" + amount + " deposited into account " + accNum);
					}

				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
			} else if (userfunc == 4) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				System.out.println("Please Enter the account number: ");

				int accNum = 0;
				try {
					accNum = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT NUMBER");
					continue;
				}
				
				while(accNum < 0) {
					System.out.println("Error, invalid account number, please enter the account number: ");
					try {
						accNum = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT NUMBER");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}
				System.out.println("Please enter the amount you wish to withdraw.");

				int amount = 0;
				try {
					amount = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
					continue;
				}
				
				while (amount < 0) {
					System.out.println("ERROR INCORRECT AMOUNT PLEASE ENTER A VALUE OF 0 OR GREATER.");
					try {
						amount = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
						error = true;
						break;
					}
				}
				
				if(error) {
					continue;
				}

				String query = "SELECT number FROM p2.account WHERE number = '" + accNum + "' AND id = '" + id + "'";
				String accId = "";
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
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
				if (accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T WITHDRAW FROM ACCOUNTS NOT ASSOCIATED WITH YOUR ID");

				} else {
					String procCall = "CALL P2.ACCT_WTH(?, ?, ?, ?)";

					try {
						Class.forName(driver);
						con = DriverManager.getConnection(url, username, password);
						CallableStatement stmt = con.prepareCall(procCall);

						stmt.setInt(1, Integer.valueOf(accNum));
						stmt.setInt(2, Integer.valueOf(amount));
						stmt.registerOutParameter(3, Types.INTEGER);
						stmt.registerOutParameter(4, Types.VARCHAR);

						stmt.execute();

						int sql_code = stmt.getInt(3);
						String err_msg = stmt.getString(4);
						stmt.close();
						con.close();
						if (sql_code < 0) {
							System.out.println(err_msg);
						} else {
							System.out.println("$" + amount + " withdrawn from account " + accNum);
						}

					} catch (Exception e) {
						System.out.println("Can't Execute!");
						e.printStackTrace();
					}
				}

			} else if (userfunc == 5) {
				boolean error = false;
				Scanner cust = new Scanner(System.in);
				System.out.println("Please enter the account you are transferring from: ");
				int srcAccNum = 0;
				
				try {
					srcAccNum = cust.nextInt();
				}catch(Exception e) {
					System.out.println(":: TRANSFER - ERROR - INVALID SOURCE ACCOUNT NUMBER");
					continue;
				}
				
				while(srcAccNum < 0) {
					System.out.println("Error, invalid source account number, please enter the account you are transferring from: ");
					try {
						srcAccNum = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: TRANSFER - ERROR - INVALID SOURCE ACCOUNT NUMBER");
						error = true;
						break;
					}
				}
				if(error) {
					continue;
				}

				String query = "SELECT number FROM p2.account WHERE number = '" + srcAccNum + "' AND id = '" + id + "'";
				String accId = "";
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
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
				if (accId.contentEquals("") == true) {
					System.out.println("ERROR CAN'T WITHDRAW FROM ACCOUNTS NOT ASSOCIATED WITH YOUR ID");

				} else {

					System.out.println(
							"Please enter the account number for the account you will be transferring funds to: ");

					int destAccNum = 0;
					try {
						destAccNum = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: TRANSFER - ERROR - INVALID DESTINATION ACCOUNT NUMBER");
						continue;
					}

					while(destAccNum < 0) {
						System.out.println("Error, invalid destination account number, please enter the account you are transferring to: ");
						try {
							destAccNum = cust.nextInt();
						}catch(Exception e) {
							System.out.println(":: TRANSFER - ERROR - INVALID DESTINATION ACCOUNT NUMBER");
							error = true;
							break;
						}
					}
					if(error) {
						continue;
					}
					
					System.out.println("Please enter the amount you wish to transfer");

					int amount = 0;
					try {
						amount = cust.nextInt();
					}catch(Exception e) {
						System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
						continue;
					}
					
					while (amount < 0) {
						System.out.println("ERROR INCORRECT AMOUNT PLEASE ENTER A VALUE OF 0 OR GREATER.");
						try {
							amount = cust.nextInt();
						}catch(Exception e) {
							System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
							error = true;
							break;
						}
					}
					
					if(error) {
						continue;
					}

					String procCall = "CALL P2.ACCT_TRX(?, ?, ?, ?, ?)";

					try {
						Class.forName(driver);
						con = DriverManager.getConnection(url, username, password);
						CallableStatement stmt = con.prepareCall(procCall);

						stmt.setInt(1, Integer.valueOf(srcAccNum));
						stmt.setInt(2, Integer.valueOf(destAccNum));
						stmt.setInt(3, Integer.valueOf(amount));
						stmt.registerOutParameter(4, Types.INTEGER);
						stmt.registerOutParameter(5, Types.VARCHAR);

						stmt.execute();

						int sql_code = stmt.getInt(4);
						String err_msg = stmt.getString(5);
						stmt.close();
						con.close();
						if (sql_code < 0) {
							System.out.println(err_msg);
						} else {
							System.out.println("$" + amount + " transferred from account " + srcAccNum + " to account "
									+ destAccNum);
						}

					} catch (Exception e) {
						System.out.println("Can't Execute!");
						e.printStackTrace();
					}
				}

			} else if (userfunc == 6) {
				boolean error = false;
				double checkingRate = 0.0;
				double savingsRate = 0.0;
				Scanner savings = new Scanner(System.in);

				System.out.println(
						"Please enter the percent of interest you would like to add to all active checking accounts. Your percentage will be divided by 100.");
				try {
					checkingRate = savings.nextDouble();
				}catch(Exception e) {
					System.out.println("ERROR INVALID CHECKING RATE");
					continue;
				}
				
				
				while (checkingRate < 0) {
					System.out.println(
							"Error, incorrect input!\nPlease enter the interest you would like to add to all active checking accounts.");
					try {
						checkingRate = savings.nextDouble();
					}catch(Exception e) {
						System.out.println("ERROR INVALID CHECKING RATE");
						error = true;
						break;
					}
				}
				if(error) {
					continue;
				}

				System.out.println(
						"Please enter the percent of interest you would like to add to all active savings accounts. Your percentage will be divided by 100.");
				try {
					savingsRate = savings.nextDouble();
				}catch(Exception e) {
					System.out.println("ERROR INVALID SAVINGS RATE");
					continue;
				}
				while (savingsRate < 0) {
					System.out.println(
							"Error, incorrect input!\nPlease enter the interest you would like to add to all active checking accounts.");
					try {
						savingsRate = savings.nextDouble();
					}catch(Exception e) {
						System.out.println("ERROR INVALID SAVINGS RATE");
						error = true;
						break;
					}
				}
				if(error) {
					continue;
				}

				checkingRate /= 100;
				savingsRate /= 100;

				String procCall = "CALL P2.ADD_INTEREST(?, ?, ?, ?)";

				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, username, password);
					CallableStatement stmt = con.prepareCall(procCall);

					stmt.setDouble(1, checkingRate);
					stmt.setDouble(2, savingsRate);
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);

					stmt.execute();

					int sql_code = stmt.getInt(3);
					String err_msg = stmt.getString(4);
					stmt.close();
					con.close();
					if (sql_code < 0) {
						System.out.println(err_msg);
					} else {
						System.out.println("Interest rate added!");
					}

				} catch (Exception e) {
					System.out.println("Can't Execute!");
					e.printStackTrace();
				}
			} else if (userfunc == 7) {
				System.out.println("Logging Out");
				break;
			} else {
				System.out.println("Error, invalid input, please try again!");
			}
		}

	}
}