Money Transfer REST API () 
--------------------------

It is a maven project. Build the project and use 'mvn tomcat7:run' to launch the application on embedded tomcat server. The app uses embedded database H2 for in-memory persistence.

REST URLs:

http://localhost:8030/money-transfer/createAcc/{initialBalance}
http://localhost:8030/money-transfer/transfer/{senderAccountNum}/{receiverAccountNum}/{amount}
http://localhost:8030/money-transfer/checkBal/{accountNum}
