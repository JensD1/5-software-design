## Lab 4b — Sokoban Game - Abstract Factory + Builder pattern

> **Course:** 5–Software Design — Lab
>
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git
>
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning outcomes

* Apply **Abstract Factory** to create families of related objects that work together (theme kits).
* Use the **Builder** pattern to assemble a themed palette (tiles, colors, sprites) for levels.
* Keep calling code free of knowledge about concrete product classes.
* Keep game logic decoupled from concrete implementations (open/closed principle).

## Context
You will complete a tiny Mini-Sokoban game. 
The game has two game modes; a warehouse level with the classic sokoban game rules, and a glacier level, where ice cubes slide until they reach the wall.
The goal is to ensure all boxes (warehouse boxes or ice cubes) are positioned on the goal state.

More concretely:
* An Abstract Factory (the “theme kit”) produces a consistent set of collaborating objects for a theme (movement strategy, coverage strategy, palette/assets).
* A builder pattern assembles the themed palette

## What you have to do

Implement the Abstract Factory and Builder pattern

1. **Implement the Builder Pattern** (`PaletteBuilder`, `ThemedPaletteBuilder`)
2. **Implement the Builder Director** (`PaletteDirector`)
3. **Implement the Abstract Factory** (`LevelKit`)
4. **Implement the Concrete Factories**
5. **Implement the concrete products**

## Where to Code

The starter project is a standard Maven + IntelliJ Java project. The methods and classes you must implement are *
*already declared** and marked with `TODO` comments. Use one of these approaches to locate them:

- In IntelliJ: **View → Tool Windows → TODO** to list all TODOs in the project.
- Use **Navigate → Symbol** (⌘⌥O / Ctrl+Alt+Shift+N) and search for the method names below.

Keep your implementation simple and readable so it maps cleanly to the class diagrams.

## How to run:
### Intellij:
select `Lab4b JavaFX` from your run configurations.

### VSCode
```bash
mvn -f labs/lab4b/pom.xml clean org.openjfx:javafx-maven-plugin:0.0.8:run
```