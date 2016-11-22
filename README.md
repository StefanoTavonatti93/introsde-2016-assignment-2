#introsde-2016-assignment-2

Stefano Tavonatti

###Folder Structure
- **ServerAssignment2**: this folder contains the source code of the server;
- **ClientAssignment2**: this folder contains the source code of the client.

###Project Structure

#####Server
- In the **tavonatti.stefano.model** package there are the classes used by the persistence provider to map the data on the database to Java objects. The classes in this packages are also used to marshall and un-marshall the JSON and XML objects.
- In the **tavonatti.stefano.model.variants** package there are some classes used to map special XML and JSON objects, for instance the object which contains the list of measure types.
- The **tavonatti.stefano.rest** package contains the classes used by jersey to handle the client requests. In these classes all the allowed CRUD operations are declared.

#####Client
- In the client project there is only the **tavonatti.stefano.assignment2.client.AssignmentClient** class, which performs all the requested operations. This client uses the classes defined in the **tavonatti.stefano.model** to handle properly the data.

##Compile and Run

#####Compile the server
In order to compile the server, type the following commands:

```shell
cd ServerAssignment2/
ant create.war 
```
The file **ServerAssignment2.war** will be created in the *ServerAssignment2* directory.

#####Compile and run client

The url of my heroku application is: [https://ste-introsde-assignment-2.herokuapp.com](https://ste-introsde-assignment-2.herokuapp.com "")

In order to compile and execute the client with my server as target, type the following commands:

```shell
cd ClientAssignment2/
ant execute.client
```

Sometime, when heroku shutdowns the webapplication, the first attempt to launch the client might fail, beacuse the startup phase of the server is very slow. It is necessary to re-run the client in order to obtain a correct result.