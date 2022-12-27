-- PROG 2
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason

In order to run this program you must first run P2_CREATE.sql. 

Run docker and go into the db2 environment and use the command: db2 -tvf P2_CREATE.sql. 

This will create all of the necessary tables that will be used for this project, as well as the decrypt and encrypt functions.

In order to create the procedures, you will need to run the command: db2 -td"@" -f p2.sql. This will create the procedures that p2.java will call for the UI.
Use only the command listed above, all other commands will not work!

To run the test, you will need to use whatever test file you plan on using. For example, I used a file called p2test.sql, 
so run the file with this command: db2 -tvf p2test.sql. 

Afterwards, I reccomend running P2_CREATE.sql again to get ready to run p2.java. Run it using the command mentioned above when you ran it the first time.

Aftwards, exit the docker environment and go to whichever directory the java files and jar files are located.

Afterwards, go back to the directory in which you have the java files and the jar file and compile p2.java.
Use the command: javac p2.java to compile p2.java.

Use the following command to run p2.java on windows: java -cp ";./db2jcc4.jar" p2 ./db.properties

Use the following command to run p2.java on MAC: java -cp ":./db2jcc4.jar" p2 ./db.properties

IT IS VERY IMPORTANT THAT YOU REMEMBER TO USE db.properties IN ORDER TO RUN THE FILE. IF YOU DO NOT USE db.properties, THE UI WILL NOT WORK. 
USE THE db.properties FROM P1, AS THE FILE WILL CONTAIN THE SAME INFORMATION TO GET INTO THE DATABASE.

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
6. Add Interest
7. Exit

If you enter 1, you will be able to open an account for yourself or another user. You will be prompted for the User Id for the person the account is for, as well as the inital amount, and what type of account it is. The account number will then be displayed.
If you enter 2, you will be able to close only accounts that belong to the current user. All they need to do is enter the account number, and if the account belongs to the current user, the account will close.
If you enter 3, the current user can deposit money into any account. You will need to enter the account number and the amount to deposit.
If you enter 4, you can withdraw money only from the accounts of the current user logged in. You will need to enter the account number and the money to withdraw.
If you enter 5, you can transfer money from the account of the person logged in to any other account. You will need to enter the account you are transferring money from, the account the money is being transferred to, and the amount being transferred.
If you enter 6, you will be able to add interest to all of the accounts. You will be prompted for a percent, and it will specify that you need to enter a number that will later be divided by 100. So if you want 5% interest, enter 5. You will need to enter the interest rate for both the checking accounts and savings accounts.
If you enter 7, you will log out and be taken back to screen 1.

REMEMBER TO RUN p2.java WITH THE db.properties FILE.
