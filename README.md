# How to build and run: 
The following application was developed and run on a VM running Debian 11 (Bullseye x64)
with: 

- OpenJdk 11
- Maven 3.6.3
- build-essentials, g++ etc... installed 
- JAVA_HOME set to `export JAVA_HOME=/usr/lib/jvm/adoptopenjdk-11-hotspot-amd64`

The native c client-server code was copied from:

https://www.thegeekstuff.com/2011/12/c-socket-programming/

and modified. 

## Build JNI native libraries... 
Ensure JAVA home is set: 

`echo $JAVA_HOME` should point to the folder where the JDK has the `include` folder (in case you have multiple)

Run `./compileLinkNative.sh`

(If it's not runnable, `chmod +x compileLinkNative.sh` to make it executable)

This will: 
- remove the `native-lib` folder if it exists
- Build the two native linked libs (Hello World and Socket) under `target/native`
- Finally, it will copy `*.so` files to `native-lib`

## Build and run Vaadin Hello world

Run: 

`mvn clean package -Pproduction` to make the artifact 

(production mode so that the UI debug tools don't run and the UI loads faster, compilation takes a bit longer though)

Run: 

`java -jar -Djava.library.path=./native-lib target/socketjnitest-1.0-SNAPSHOT.jar` 

Open a browser and go to: 

`http://localhost:8080`

Check the console, it should inform you if the socket connection was successful. If it was, you can check the
status also through a terminal using for instance: `sudo lsof -i:5000` (port hardcoded to 5000 in JNI poc)

To run the native client: 

```
cd native
g++ sockclient.cpp -o sockclient.o
./sockclient.o 127.0.0.1
```

The `sockclient` accepts the remote IP as an argument (port hardcoded) and will ping the server 10 times with an 
increasing number.  











#############
# https://start.vaadin.com default description
#############
--------------------

# SocketJNITest

This project can be used as a starting point to create your own Vaadin application with Spring Boot.
It contains all the necessary configuration and some placeholder files to get you started.

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different 
IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/socketjnitest-1.0-SNAPSHOT.jar`

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorials at [vaadin.com/tutorials](https://vaadin.com/tutorials).
- Watch training videos and get certified at [vaadin.com/learn/training](https://vaadin.com/learn/training).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/components](https://vaadin.com/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes). 
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).
