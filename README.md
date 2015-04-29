=============================================================================================================================
                                                GENERAL INFORMATION

GW-SERVER project is used for persisting "Quiz" data: questions, user records etc. This project is supposed to be used together with GW-MANAGER (https://github.com/java-fan/gw-manager), which main role is to upload question data and manage it, and GW-CLIENT (third-party project, I will add link to the branch later), which is Android device client, that uses server as backend.

The project is based on Google App Engine platform and is used as backend for Android client (not necessary, actually it could be used for any client). Generally server consists of two modules: Web Services part and Persistence part. The server is able to:

- recieve, store and manage question information data (possible answers, correct answer, relevant question images)
- store user records, calculate top records and place in the top for concrete user
- manage database: versioning, dowbload/upload backup
- secure requests: AUTHENTICATION (server has system users with different roles) and AUTHORIZATION (READ, WRITE, ADMIN permissions for different requests). Storing hash instead of password itself for users with WRITE and ADMIN roles.

=============================================================================================================================
                                                STARTUP INFORMATION

To run the server follow steps:

Use Eclipse update manager to install the tools for your Eclipse IDE. For example, the update site for Eclipse 4.4 (Luna)  version: https://dl.google.com/eclipse/plugin/4.4 (Insert it in Eclipse: Help -> Install New Software... -> Work with) You need to install:

- Developer Tools
- Google plugin for Eclipse
- SDKs

To launch server: Run as -> Web Application. You should see in console that server started successfully. Also try http://localhost:8888/_ah/admin/datastore in your browser to see if server is working.

=============================================================================================================================
                                                    TESTING

All tests are INTEGRATION TESTS. Thus you need to start server before testing the system. Be careful, each test clears database before run.

=============================================================================================================================
                                                TECHNOLOGY STACK

- Java 7 (Java 8 is not supported by GAE)
- Google App Engine (Server)
- JAX-RS + Jersey (Web Services)
- Spring Framework (IoC/DI)
- Objectify (Persistence)
- JUnit (Testing)
- Maven (Build tool)
