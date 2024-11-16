# SC2002 Project (HMS)

## Introduction
This repository hosts the codebase of the HMS group assignment of SC2002 Object Oriented Design & Programming.

## Setup Environment
If you want to try on this project, please refer to the following environment requirements we used during development:
1. [JDK 17.0.12](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. [VSCode](https://code.visualstudio.com/Download)
3. [VSCode Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

The required initial data and third-party jar libraries are already included in this repository.

## Project Structure
```
.
│  HMS.java                 // The entry point class
│  LICENSE
│  README.md
├─.github
├─.vscode
├─controller                // controller package
├─data                      // The initial data loaded into system
├─html                      // The JavaDoc auto-generated HTML files
├─lib                       // Third-party jar libraries
│  └─commons-csv-1.10.0
├─model                     // model package
├─observer                  // observer package
├─store                     // store package
└─view                      // view package
```

### 1. `model` package
Contains all information (entities / data) tracked by the application.

### 2. `store` package
Contains the backing stores for all `model`s and handles the process of loading initial data from csv files to the application.

### 3. `view` package
Contains the UIs that interact with users and dispatches user's commands to `controller`s.

### 3. `controller` Package
Contains the `XXXController` classes that abstract the data management (tracking) operations from the user classes.

### 4. `observer` Package
A simple implementation of the observer design pattern which is primarily used in dispatching notifications.

### 5. `HMS` Class
The system (application) class where the `main` method resides.