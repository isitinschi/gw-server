=============================================================================================================================
                                                GENERAL INFORMATION

Server based on Google App Engine

To run server follow steps:

Use Eclipse update manager to install the tools for your Eclipse IDE. For example, the update site for Eclipse 4.4 (Luna)  version: https://dl.google.com/eclipse/plugin/4.4 (Insert it in Eclipse: Help -> Install New Software... -> Work with) You need to install:

- Developer Tools
- Google plugin for Eclipse
- SDKs

To launch server: Run as -> Web Application Try http://localhost:8888/_ah/admin/datastore in your browser to see if server started successfully.

=============================================================================================================================
                                                    TESTING

All tests are INTEGRATION TESTS. Thus you need to start server before testing the system. Be careful, each test clears database before run.

=============================================================================================================================
                                                TECHNOLOGY STACK

- Google App Engine (Server)
- JAX-RS + Jersey (Web Services)
- Spring Core (IoC)
- Objectify (Persistence)
- JUnit (Testing)
