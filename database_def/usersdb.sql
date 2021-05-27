CREATE TABLE logindetails (
  username varchar(15) NOT NULL,
  password varchar(15) NOT NULL,
  PRIMARY KEY (username)
);


CREATE TABLE maintable (
  user_name varchar(20) NOT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  descp varchar(200) NOT NULL,
  cat varchar(50) NOT NULL,
  dat date NOT NULL,
  amt double NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_name) REFERENCES logindetails(username)
);

CREATE TABLE acdetails (
  user_name varchar(20) NOT NULL,
  Fname varchar(30) DEFAULT 'Unavailable',
  mailID varchar(30) DEFAULT 'Unavailable',
  phone varchar(30) DEFAULT 'Unavailable',
  bgt int(11) DEFAULT '-1',
  PRIMARY KEY (user_name),
  FOREIGN KEY (user_name) REFERENCES logindetails(username)
);

CREATE TABLE percstore (
  user_name varchar(20) NOT NULL,
  educ float NOT NULL DEFAULT 0,
  educ_sum double NOT NULL DEFAULT 0,
  food float NOT NULL DEFAULT 0,
  food_sum double NOT NULL DEFAULT 0,
  trans float NOT NULL DEFAULT 0,
  trans_sum double NOT NULL DEFAULT 0,
  paym float NOT NULL DEFAULT 0,
  paym_sum double NOT NULL DEFAULT 0,
  medi float NOT NULL DEFAULT 0,
  medi_sum double NOT NULL DEFAULT 0,
  util float NOT NULL DEFAULT 0,
  util_sum double NOT NULL DEFAULT 0,
  trav float NOT NULL DEFAULT 0,
  trav_sum double NOT NULL DEFAULT 0,
  PRIMARY KEY (user_name),
  FOREIGN KEY (user_name) REFERENCES logindetails(username)
);

DELIMITER $$
CREATE PROCEDURE cal_catperc(user VARCHAR(20))
BEGIN
SET @pe=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Education')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @pf=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Food')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @pt=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Transport')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @pp=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Payment')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @pm=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Medical&Healthcare')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @pu=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Utilities')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
SET @ptr=100*(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Travel')/(SELECT SUM(amt) FROM maintable WHERE user_name=user);
IF @pe IS NULL THEN
UPDATE percstore SET educ=default,educ_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET educ=@pe, educ_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Education') WHERE user_name=user;
END IF;
IF @pf IS NULL THEN
UPDATE percstore SET food=default, food_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET food=@pf, food_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Food') WHERE user_name=user;
END IF;
IF @pt IS NULL THEN
UPDATE percstore SET trans=default, trans_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET trans=@pt, trans_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Transport') WHERE user_name=user;
END IF;
IF @pp IS NULL THEN
UPDATE percstore SET paym=default, paym_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET paym=@pp, paym_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Payment') WHERE user_name=user;
END IF;
IF @pm IS NULL THEN
UPDATE percstore SET medi=default, medi_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET medi=@pm, medi_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Medical&Healthcare') WHERE user_name=user;
END IF;
IF @pu IS NULL THEN
UPDATE percstore SET util=default, util_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET util=@pu, util_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Utilities') WHERE user_name=user;
END IF;
IF @ptr IS NULL THEN
UPDATE percstore SET trav=default, trav_sum=default WHERE user_name=user;
ELSE
UPDATE percstore SET trav=@ptr, trav_sum=(SELECT SUM(amt) FROM maintable WHERE user_name=user and cat='Travel') WHERE user_name=user;
END IF;
END$$


CREATE TRIGGER deleteAction
BEFORE DELETE ON logindetails FOR EACH ROW
BEGIN
delete from maintable where user_name=OLD.username;
delete from acdetails where user_name=OLD.username;
delete from percstore where user_name=OLD.username;
END$$

DELIMITER ;






