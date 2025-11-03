# Lab 3 — Design Patterns: Singleton & Observer

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes

- Implement the **Singleton** pattern correctly (API, visibility, behavior).
- Implement the **Observer** pattern (Subject/Observer roles, notifications).
- Use Java’s **PropertyChange** mechanism to decouple events from handlers.
- How to handle concurrency (e.g. double-checked locking or holder pattern).
- Add at least one **integration test** and one **unit test**.

## Context

This lab is three small parts that build pattern fluency:

1) **Singleton** — an `IdGenerator` that returns monotonically increasing IDs; used by a simple `TicketService`.
2) **Observer** — an `Auction` that notifies observers on new bids; two observers react differently.
3) **Inventory (PropertyChange)** — a tiny inventory “DB” that is a thread-safe Singleton and fires change events to
   listeners.

## What You Have To Do

### Part A — Singleton (IdGenerator)

- Create a class `IdGenerator` as a **Singleton**:
- Use it inside `TicketService.create(String title)` to set the ID when creating a `Ticket`.

### Part B — Observer (Auction)

- Define the roles:
    - `Subject` interface with `addObserver(Observer)` and `removeObserver(Observer)`.
    - `Observer` interface with `void update(String event, Object payload)`.
- Implement `Auction` as a Subject:
    - Accept bids via `place(Bid)`; track the highest bid; **notify** observers on each accepted bid (e.g., event
      `"bidPlaced"` with the `Bid` as payload).
    - Provide a public getter (`highest()` or equivalent) to read the highest bid.
- Implement two observers:
    - `ConsoleAnnouncer` — prints a simple “new bid” message.
    - `MaxBidTracker` — tracks the maximum (expose a useful getter).
- In `Main`, wire an `Auction`, attach both observers, and place a few bids to see notifications.

### Part C — Inventory with PropertyChange (Thread-safe Singleton)

- Use an abstract `Database` base that wraps `PropertyChangeSupport`:
    - `addListener(..)`, `removeListener(..)`, and a protected `notifyObservers(..)` helper.
- Implement `InventoryDB` as a **thread-safe Singleton**
    - `setStock(String sku, int newQty)` updates stock and **fires** `"stockChanged"` with old/new values.
- Create listeners:
    - `AuditLogger` — logs “AUDIT:” with the new quantity when stock changes.
    - `ReorderService` — when quantity drops **below a threshold**, prints a “REORDER:” message.
- Provide a tiny controller and demo:
    - `Controller.adjust(String sku, int newQty)` updates the DB.
    - In `Main`, obtain the Singleton DB, register listeners, and perform a few adjustments (one above and one below the
      threshold) to see events.

### Deliverables
- You'll have to provide **at least one Unit test and one Integration test**.
- All code should be implemented.
- _Bonus_: provide UML diagrams


## Where to Code

The starter project is a standard Maven + IntelliJ Java project. The methods and classes you must implement are *
*already declared** and marked with `TODO` comments. Use one of these approaches to locate them:

- In IntelliJ: **View → Tool Windows → TODO** to list all TODOs in the project.
- Use **Navigate → Symbol** (⌘⌥O / Ctrl+Alt+Shift+N) and search for the method names below.

Keep your implementation simple and readable so it maps cleanly to the class diagrams.

## Tips

- Singleton: guard constructors; verify identity (`getInstance()` returns the **same** object) and monotonic IDs.
- Observer: keep `update(String, Object)` minimal and let observers down-cast payloads when needed.
- Inventory: prefer `ConcurrentHashMap` for stock; use clear event names (e.g., `"stockChanged"`).
