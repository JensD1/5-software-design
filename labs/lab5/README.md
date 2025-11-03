# Lab 5 — MVC: Time Registration App

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes

- Apply **Model–View–Controller** to separate concerns.
- Use the **Observer** pattern to keep the view in sync with the model.
- Integrate an existing GUI with your controller and model.

## Context
You will implement a time registration platform, where all employees can check in/out. Each check in/out is an entry. You can add employees or remove them, same for entries. Finally, you can also edit persons and entries.

## What you have to do

1. Implement the view (`View` class).
2. Implement the controller (`RegistrationControler` class).
3. Wire the MVC in `ViewApp`.

## Where to Code

The starter project is a standard Maven + IntelliJ Java project. The methods and classes you must implement are *
*already declared** and marked with `TODO` comments. Use one of these approaches to locate them:

- In IntelliJ: **View → Tool Windows → TODO** to list all TODOs in the project.
- Use **Navigate → Symbol** (⌘⌥O / Ctrl+Alt+Shift+N) and search for the method names below.

Keep your implementation simple and readable so it maps cleanly to the class diagrams.

## How to run:
### Intellij:
select `Lab5 JavaFX` from your run configurations.

### VSCode
```bash
mvn -f labs/lab5/pom.xml clean org.openjfx:javafx-maven-plugin:0.0.8:run
```