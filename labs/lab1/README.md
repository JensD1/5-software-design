# Lab 1 — UML: Sequence & Use Case + Hotel Booking Starter

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes

- Draw **Use Case** and **Sequence** diagrams for a small system.
- Translate between diagrams and code.
- _optional: testing_

## Context

You’ll model and implement parts of a **simple hotel booking system** with three features:

1) **Find cheapest hotel** for a given **date range** (`startDate`, `endDate`).
2) **Book** a room in a **specific hotel** (by `hotelID`) for a given **date**.
2) **Book** a room in a **specific hotel** (by `hotelID`) for a given **date range**.
3) **Book** a room in the **cheapest hotel** for a given **date range**.

## What You Have To Do

By the end of this lab you should:

1. **Implement the code** for booking a room given the sequence diagram (open the `.xml` file from BB in Visual Paradigm)
2. **Create a full Sequence diagram** given the code for finding the cheapest hotel
3. Provide **the code** for:
   4. Booking a room in a hotel for a range of dates
   5. Booking a room in the cheapest hotel for a range of dates 
   6. _Bonus_: Provide Sequence Diagrams for both.
7. _Bonus:_ add tests to verify your implementation.
8. **Draw a Use Case diagram** of all the features.

## Where to Code

The starter project is a standard Maven + IntelliJ Java project. The methods you must implement are **already declared**
and marked with `TODO` comments. Use one of these approaches to locate them:

- In IntelliJ: **View → Tool Windows → TODO** to list all TODOs in the project.
- Use **Navigate → Symbol** (⌘⌥O / Ctrl+Alt+Shift+N) and search for the method names below.

**Target methods**:

- `bookRoomInHotel(long date, int hotelID)`
- `findCheapestHotel(long startDate, long endDate)`
- `bookRoomInHotel(long startDate, long endDate, int hotelID)`
- `bookRoomInCheapestHotel(long startDate, long endDate)`

Keep your implementation simple and readable so it maps cleanly to your Sequence diagrams.

**Diagrams**

- Use **Visual Paradigm (Community Edition)** for all diagrams.
- Export your diagrams as instructed in the presentation.