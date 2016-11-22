#introsde-2016-assignment-2

Stefano Tavonatti

###Folder Structure
- **ServerAssignment2**: this folder contains the source code of the server
- **ClientAssignment2**: this folder contains the source code of the client

###Project Structure:

#####Server:
- int the **tavonatti.stefano.model** package there are the classes used by the persistence provider to map the data on the database to Java objects. The class in this packages is also used to marshall and un-marshall the JSON and XML object.
- in the **tavonatti.stefano.model.variants** there are some classes used to map special XML and JSON objects, for instance the object wich conatins the list of measure types.
- The **tavonatti.stefano.rest** contains the classes used by jersey to handle the client requests.

#####Client:

##Compile and Run:

#####Compile the server
In order to compile the server type the following command:

```shell
cd ServerAssignment2/
ant create.war 
```
The file **ServerAssignment2.war** will be created in the *ServerAssignment2* directory.

#####Compile and run client

The url of my heroku application is: [https://ste-introsde-assignment-2.herokuapp.com](https://ste-introsde-assignment-2.herokuapp.com "")

In order to execute the client with my server as target, type the following command:

```shell
cd ClientAssignment2/
ant execute.client
```

Sometimes, when heroku shutdown the webapplication, the first attempt to launch the client might fail, beacuse the startup phase of the server is very slow. It is necessary to re-run the client in order to obtain the correct result.