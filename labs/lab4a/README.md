## Lab 4a — Factory Method: Reforestation Planner

> **Course:** 5–Software Design — Lab
> 
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git
> 
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning outcomes

* Apply the **Factory Method** pattern to centralize object creation responsibilities.
* Keep calling code free of knowledge about concrete product classes.
* Create a class diagram of the factory method.

## Context

You will build a small “reforestation planner” that uses the Factory Method pattern:

* A `ReforestationPlanner` is responsible for producing a `PlantingPlan`. Its `plan(...)` method computes spacing, counts and produces a short note describing the plan. The concrete plant objects that provide name/spacing/soil information are produced by a factory method `createPlant()` used by `plan(...)`.
* Concrete planners such as `OakPlanner`, `MaplePlanner`, … implement the factory method to return the correct `Plant` product. `Main` demonstrates how planners are used.
* A `Plant` interface defines the product contract (`commonName()`, `spacingMeters()`, `soilPreference()`), and concrete plant classes (e.g., `Oak`, `Maple`, `Willow`) implement it.

## What you have to do

Implement the Factory Method solution and the product classes so that the planner objects produce correct planting plans.

1. **Implement the Factory Method** (`ReforestationPlanner`)
2. **Implement the product classes (`Plant`)** (can be found in `plants/`)
3. **Implement the factory classes (`ReforestationPlanner`)** (can be found in `planners/`)
4. **Implement the main method**
5. **Create a class diagram**

## Where to Code

The starter project is a standard Maven + IntelliJ Java project. The methods and classes you must implement are *
*already declared** and marked with `TODO` comments. Use one of these approaches to locate them:

- In IntelliJ: **View → Tool Windows → TODO** to list all TODOs in the project.
- Use **Navigate → Symbol** (⌘⌥O / Ctrl+Alt+Shift+N) and search for the method names below.

Keep your implementation simple and readable so it maps cleanly to the class diagrams.
