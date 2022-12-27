-- PROG 1
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason

In order to run this program you must first run P1_CREATE.sql. 

Run docker and go into the db2 environment and use the command: db2 -tvf P1_CREATE.sql. 

This will create all of the necessary tables that will be used for this project.


Aftwards, exit the docker environment and go to whichever directory the java files and jar files are located.
Compile ProgramLauncher.java with the command: javac ProgramLauncher.java. 
Compile BankingSystem.java with the command: javac BankingSystem.java.
Then, you will need to run ProgramLauncher.java. However, there are some differences depending on which environement you run it in.

If you are using windows and want to send the output to a .out file, use the following command to run ProgramLauncher.java: java -cp ";./db2jcc4.jar" ProgramLauncher ./db.properties > test1.out
If you want to see the output on the terminal, use the following command: java -cp ";./db2jcc4.jar" ProgramLauncher ./db.properties

If you are using a Mac, use the following command if you want to use a .out file: java -cp ":./db2jcc4.jar" ProgramLauncher ./db.properties > test1.out
If you want to see the output on the terminal, use the following command: java -cp ":./db2jcc4.jar" ProgramLauncher ./db.properties

IT IS VERY IMPORTANT THAT YOU REMEMBER TO USE db.properties IN ORDER TO RUN THE FILE.

Once you run ProgramLauncher.java, you will get a series of outputs based on tests in which new customers are created, accounts are opened/closed, account summaries etc.

In order to run p1.java, I reccomend going back to the docker and running P1_CREATE.sql again, just like the first instructions state.

Afterwards, go back to the directory in which you have the java files and the jar file and compile p1.java.
Use the command: javac p1.java to compile p1.java.

Use the following command to run p1.java on windows: java -cp ";./db2jcc4.jar" p1 ./db.properties

Use the following command to run p1.java on MAC: java -cp ":./db2jcc4.jar" p1 ./db.properties

IT IS VERY IMPORTANT THAT YOU REMEMBER TO USE db.properties IN ORDER TO RUN THE FILE.

The program will the prompt you with a main menu in which it will have a list of options and a number next to them. 
All you need to do is type in the number and hit enter, and that task will run.
This is what the first menu looks like:

Welcome to the Self Services Banking System!
1. New Customer
2. Customer Login
3. Exit

If you want to sign up as a new customer, you would type in 1 to the command line and hit enter, and it will prompt you for your name, age, gender, with M for male and F for female, and the pin you want to use to log in.
The program will then tell you your ID to use in future reference and take you to the second screen.
If you enter 2, the program will prompt you for your pin and your ID. If successful, you will log in and go to the next screen
If you enter 3, the program will exit. (The program will only exit in screen 1)
Screen 2 Will look like this:

Customer Main Menu
1. Open Account
2. Close Account
3. Deposit
4. Withdraw
5. Transfer
6. Account Summary
7. Exit

If you enter 1, you will be able to open an account for yourself or another user. You will be prompted for the User Id for the person the account is for, as well as the inital amount, and what type of account it is.
If you enter 2, you will be able to close only accounts that belong to the current user. All they need to do is enter the account number, and if the account belongs to the current user, the account will close.
If you enter 3, the current user can deposit money into any account. You will need to enter the account number and the amount to deposit.
If you enter 4, you can withdraw money only from the accounts of the current user logged in. You will need to enter the account number and the money to withdraw.
If you enter 5, you can transfer money from the account of the person logged in to any other account. You will need to enter the account you are transferring money from, the account the money is being transferred to, and the amount being transferred.
If you enter 6, an account summary for the current user logged in will be displayed.
If you enter 7, you will log out and be taken back to screen 1.

For the customer log in, if you want to go to the administrator menu enter 0 for the id and pin to go to the admin menu, which looks as follows.

Administrator Menu
1. Account summary for a customer
2. Report A :: Customer Information with Total Balance in Decreasing Order
3. Report B :: Find the Average Total Balance Between Age Groups
4. Exit

If you enter 1, you will be able to display the account summary for any customer. You will need to enter the ID of the customer whose information you want displayed.
If you enter 2, you will be able to display the total amount of money that each customer has who has an active account.
If you enter 3, you will be able to see the average balance between age groups. You will be prompted for a maximum age and a minimum age, and the average will be displayed on screen.
If you enter 4, you will be taken back to screen 1.

In short, enter the number displayed on screen with the task that can be run. Example: 1. Open Account means enter 1 to open an account. The program will explain everything else.

REMEMBER TO RUN p1.java WITH THE db.properties FILE.