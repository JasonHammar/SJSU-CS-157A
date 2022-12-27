--
-- db2 -td"@" -f p2.sql
--
CONNECT TO CS157A@
--
--
DROP PROCEDURE P2.CUST_CRT@
DROP PROCEDURE P2.CUST_LOGIN@
DROP PROCEDURE P2.ACCT_OPN@
DROP PROCEDURE P2.ACCT_CLS@
DROP PROCEDURE P2.ACCT_DEP@
DROP PROCEDURE P2.ACCT_WTH@
DROP PROCEDURE P2.ACCT_TRX@
DROP PROCEDURE P2.ADD_INTEREST@
--
--
CREATE PROCEDURE P2.CUST_LOGIN
(IN p_id INTEGER, IN p_pin INTEGER, OUT Valid INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE exists int;
    DECLARE decrp int;
    DECLARE dec int;
    SET exists = (SELECT COUNT(*) FROM p2.customer WHERE p2.customer.id = p_id);
    SET decrp = (SELECT pin FROM p2.customer WHERE p_id = p2.customer.id);
    SET dec = p2.decrypt(decrp);
    IF p_id < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid ID';
      SET Valid = 0;
    ELSEIF p_pin < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid pin';
      SET Valid = 0;
    ELSEIF exists <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid ID or PIN';
      SET Valid = 0;
    ELSEIF p_pin != dec THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid ID or PIN!';
      SET Valid = 0;
    ELSE
      SET Valid = 1;
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.CUST_CRT
(IN p_name CHAR(15), IN p_gender CHAR(1), IN p_age INTEGER, IN p_pin INTEGER, OUT id INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    SET p_pin = P2.encrypt(p_pin);
    IF p_gender != 'M' AND p_gender != 'F' THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid gender';
    ELSEIF p_age < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid age';
    ELSEIF p_pin < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid pin';
    ELSE
      INSERT INTO P2.CUSTOMER (name, gender, age, pin) VALUES (p_name, p_gender, p_age, p_pin);
      SET sql_code = 0;
      SET id = (SELECT MAX(id) FROM P2.CUSTOMER);
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_OPN
(IN p_id INTEGER, IN p_balance INTEGER, IN p_type CHAR(1), OUT number INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE exists int;
    SET exists = (SELECT COUNT(*) FROM p2.customer WHERE p2.customer.id = p_id);
    IF p_type != 'C' AND p_type != 'S' THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Account Type';
    ELSEIF p_id < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid ID';
    ELSEIF p_balance < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Balance';
    ELSEIF exists <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid ID';
    ELSE
      INSERT INTO P2.ACCOUNT (id, balance, type, status) VALUES (p_id, p_balance, p_type, 'A');
      SET sql_code = 0;
      SET number = (SELECT MAX(number) FROM P2.ACCOUNT);
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_CLS
(IN p_number INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE exists int;
    SET exists = (SELECT COUNT(*) FROM p2.account WHERE p2.account.number = p_number AND status = 'A');
    IF exists <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Account Number';
    ELSEIF p_number < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Number';
    ELSE
      UPDATE P2.ACCOUNT SET status = 'I', balance = '0' WHERE number = p_number;
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_DEP
(IN p_number INTEGER, IN p_amount INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE exists int;
    DECLARE currBalance int;
    DECLARE newBalance int;
    SET currBalance = (SELECT balance FROM p2.account WHERE number = p_number);
    SET newBalance = currBalance + p_amount;
    SET exists = (SELECT COUNT(*) FROM p2.account WHERE p2.account.number = p_number AND status = 'A');
    IF exists <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Account Number';
    ELSEIF p_number < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Number';
    ELSEIF p_amount < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Amount';
    ELSE
      UPDATE P2.ACCOUNT SET balance = newBalance WHERE number = p_number AND status = 'A';
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_WTH
(IN p_number INTEGER, IN p_amount INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE exists int;
    DECLARE currBalance int;
    DECLARE newBalance int;
    SET currBalance = (SELECT balance FROM p2.account WHERE number = p_number);
    SET newBalance = currBalance - p_amount;
    SET exists = (SELECT COUNT(*) FROM p2.account WHERE p2.account.number = p_number AND status = 'A');
    IF exists <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Account Number';
    ELSEIF p_number < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Number';
    ELSEIF p_amount < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Amount';
    ELSEIF newBalance < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid amount, causes negative balance';
    ELSE
      UPDATE P2.ACCOUNT SET balance = newBalance WHERE number = p_number AND status = 'A';
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_TRX
(IN Src_Acct INTEGER, IN Dest_Acct INTEGER, IN p_amount INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE existsSrc int;
    DECLARE existsDest int;
    DECLARE srcBal int;
    SET srcBal = (SELECT balance FROM p2.account WHERE p2.account.number = Src_Acct AND status = 'A');
    SET existsSrc = (SELECT COUNT(*) FROM p2.account WHERE p2.account.number = Src_Acct AND status = 'A');
    SET existsDest = (SELECT COUNT(*) FROM p2.account WHERE p2.account.number = Dest_Acct AND status = 'A');
    IF existsSrc <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Source Account Number';
    ELSEIF existsDest <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Destination Account Number';
    ELSEIF Src_Acct < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Source Account Number';
    ELSEIF Dest_Acct < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Destination Account Number';
    ELSEIF p_amount < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Amount';
    ELSEIF srcBal < p_amount THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid Amount, causes negative balance for source account';
    ELSE
      CALL P2.ACCT_WTH(Src_Acct, p_amount, sql_code, err_msg);
      CALL P2.ACCT_DEP(Dest_Acct, p_amount, sql_code, err_msg);
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.ADD_INTEREST
(IN savings_rate FLOAT, IN checking_rate FLOAT, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    IF savings_rate < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid savings rate';
    ELSEIF checking_rate < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid checking rate';
    ELSE
      UPDATE P2.ACCOUNT SET balance = balance + (balance * checking_rate) WHERE status = 'A' AND type = 'C';
      UPDATE P2.ACCOUNT SET balance = balance + (balance * savings_rate) WHERE status = 'A' AND type = 'S';
    END IF;
END@
--
TERMINATE@
--
--
