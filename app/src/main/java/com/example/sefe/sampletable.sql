CREATE TABLE FileHistory (
                             TransactionKey int NOT NULL AUTO_INCREMENT,
                             SenderIP varchar(20) NOT NULL,
                             ReceiverIP varchar(20) NOT NULL,
                             FileSizeInMegabytes double,
                             DateSent DateTime,
                             FileName varchar(100),
                             PRIMARY KEY (TransactionKey)
);

CREATE TABLE LoginTable (
                             UserName varchar(20) NOT NULL,
                             Password varchar(20) NOT NULL,
                             PRIMARY KEY (UserName)
);


INSERT INTO LoginTable(UserName, Password) VALUES ("khutjo","1234a%");
INSERT INTO LoginTable(UserName, Password) VALUES ("kerane","5678b%");
INSERT INTO LoginTable(UserName, Password) VALUES ("tshiamo","1234c%");
INSERT INTO LoginTable(UserName, Password) VALUES ("armin","5678d%");
