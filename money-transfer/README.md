Money Transfer REST API (using Java, Hibernate, embedded Tomcat server & H2 database) 
-------------------------------------------------------------------------------------

A maven project. Build it and use 'mvn tomcat7:run' to launch the application on embedded tomcat server. The app uses in-memory database H2 for persistence.

REST URLs (POST):

http://localhost:8030/money-transfer/createAcc/{initialBalance}

http://localhost:8030/money-transfer/transfer/{senderAccountNum}/{receiverAccountNum}/{amount}

http://localhost:8030/money-transfer/checkBal/{accountNum}


The order in which URLs are given are actually logical in terms of order of the operations in a money transfer app.
