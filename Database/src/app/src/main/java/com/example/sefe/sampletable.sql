CREATE TABLE FileHistory (
                             TransactionKey int NOT NULL AUTO_INCREMENT,
                             SenderIP varchar(20) NOT NULL,
                             ReceiverIP varchar(20) NOT NULL,
                             FileSizeInMegabytes double,
                             DateSent DateTime,
                             FileName varchar(100),
                             PRIMARY KEY (TransactionKey)
);


SELECT * FROM FileHistory;
