================================================================================================================================
-------------------------------------------------GENERAL INFORMATION------------------------------------------------------------

Backend for GuessWhat client application

To run server follow steps:

Use Eclipse update manager to install the tools in the version for your Eclipse IDE. For example the update site for the Eclipse 4.4. version is the following URL: https://dl.google.com/eclipse/plugin/4.4 (Insert it in Eclipse: Help -> Install New Software... -> Work with) You need to install:

- Developer Tools
- Google plugin for Eclipse (required)
- SDKs

To launch server: Run as -> Web Application Try http://localhost:8888/_ah/admin/datastore in your browser to see if server started

================================================================================================================================
-------------------------------------------------------TESTING-----------------------------------------------------------------

All tests are INTEGRATION TESTS. Thus you need to start server before testing the system. Be careful, each test clears database before run.
================================================================================================================================
