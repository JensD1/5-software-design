# 5–Software Design — Student Starter

Welcome! This repository contains the **starter code and instructions** for the *5–Software Design* labs.
Each lab lives in `labs/labX` and has its **own README** with a clear explanation, the tasks, and the tips.

---

## Prerequisites

- **Java 21** (Temurin 21 recommended, e.g., via SDKMAN)
- **Git**
- **IntelliJ IDEA** (Community or Ultimate): highly recommended.
- *Windows users:* Preferably install WSL (Windows Subsystem for Linux).

---

## Initialization
> We recommend IntelliJ; it handles Maven import and run configs automatically.

### IntelliJ

1. **Open the project by the root `pom.xml`**  
   *File → Open…* → select the **root `pom.xml`** → *Open as Project*.
2. **Verify SDKs (Java 21 everywhere)**
    1) *File → Project Structure → Project* → SDK = **21**; Language level = **21**.
    2) *Project Structure → Modules* → ensure modules inherit the Project SDK.
    3) *Settings → Build Tools → Maven → Importing* → JDK for importer = **21**.
3. You should now be able to see the maven menu (click on the `m` icon on the right hand side of the screen).
    1) Within this maven menu, press the two circular arrows at the top. Then select **Reload All Maven Projects**
4. **Sanity check**
   In the Maven tool window: *Software Design – Student → Lifecycle → test* → **double‑click**.
   You should see `[INFO] BUILD SUCCESS`.

> If the Maven tab is greyed out, you most likely opened a folder instead of the **root `pom.xml`**. Close the project
> and re-open the `pom.xml`.

**Run something:** open a class with a `main` method and click the green ▶ gutter icon. For a demo, run `labs/grader/src/main/java/be/uantwerpen/sd/labs/grader/GraderMain.java` (this launches the autograder).

### VSCode
You can use VS Code, but **you** are responsible for a correct Java/Maven setup.

- **Build everything (skip tests)**
  ```bash
  ./mvnw clean package -DskipTests
  ```

- **Run all tests in all labs**
  ```bash
  ./mvnw clean verify
  ```

- **Work on a single lab** (build & run that lab's tests only)
  ```bash
  ./mvnw -pl labs/labX -am test
  ```

    - Note, you need to replace X with the actual lab. For example:
      ```bash
      ./mvnw -pl labs/lab1 -am test
      ```


---

## Repository Layout (Student)

```
.
├─ labs/
│  ├─ lab1/
│  ├─ lab2/
│  └─ ...
│  └─ grader/
│  └─ autograder.sh
├─ .mvn/wrapper/        # Maven wrapper files
├─ mvnw, mvnw.cmd       # Maven wrapper launchers
├─ pom.xml              # Root parent POM (Java 21, shared plugins/deps)
└─ README.md            # This file
```

---

## How To Get Started With Each Lab

- Download all files of the lab from BB.
    - If Visual Paradigm is used, open the provided `.xml`.
- Review the presentation for each lab.
- Read the lab’s README in `labs/labX/README.md`.
- Code where TODOs indicate (use IntelliJ’s **TODO** window to find them).

---

## Autograder

The autograder checks your public methods via reflection and prints a pass/fail report with hints.

### How to run it (IntelliJ)

This is the *Fastest* option, but requires the usage of IntelliJ with Maven import.

1. Open the project and let IntelliJ import Maven (should already be done if you followed this readme correctly).
2. Open `labs/grader/src/main/java/be/uantwerpen/sd/labs/grader/GraderMain.java`.
3. Right-click **GraderMain** → **Run 'GraderMain.main()'** to grade **all** labs.
4. To grade a **single** lab:  
   Run dropdown ▸ **Edit Configurations…** ▸ **Program arguments** → `--lab lab1` ▸ **Run**.

> You can also press the green “play” icon next to the `main` method in `GraderMain`.

### How to run it (command line, VSCode)

This option is a bit slower due to the necessity of building the `./m2-autograder` package from scratch at runtime.
However, this option doesn't require a Maven setup (already done for you) nor does it require IntelliJ.

#### Mac, Linux, or WSL

*Run all labs (default):*

```bash
bash labs/autograder.sh
```

*Run a single lab (example: lab1):*

```bash
bash ./labs/autograder.sh lab=lab1
```

or

```bash
bash ./labs/autograder.sh lab lab1
```

#### Windows

*Run all labs (default):*

```powershell
.\labs\autograder.ps1
```

*Run a single lab (example: lab1):*

```powershell
.\labs\autograder.ps1 lab=lab1
```

### Reading the output

- Each **suite** prints a header and numbered tests:
    - `✅` = passed, `❌` = failed
    - Failed tests show a short hint
- A final **summary** line shows how many tests passed.

### Expectations for your code

* Use the correct **package** per lab, e.g. `be.uantwerpen.sd.labs.lab1`.
* Match the required **class names** and **method signatures** from the lab README.

### Common issues & quick fixes

- **Everything that should pass returns `-1`:**  
  Your lab classes likely weren’t built or the autograder can’t load them.
    - Try a full build first:
      ```bash
      ./mvnw -q -pl labs -am package
      ```
    - Then re-run the autograder (commands above).
    - In IntelliJ, if things still look odd, open the **Maven** tool window and click **Reload All Maven Projects**.

- **“Unknown lab 'labX'” when using `--lab`:**  
  Check the lab key you typed (e.g. `lab1`, `lab2`, …).

That’s it—run, read the hints, adjust your code, and re-run until it’s all green. ✅

> Note that you **should not** adjust the autograder itself. During grading all autograders will automatically be replaced with the teacher's version to ensure correctness.

---

## Deliverables (Portfolio)

Read the presentations and verify how to deliver your solution (and what should be included)!

In short:

_Portfolio:_

- **One zip** containing:
    - **Code**: this entire repository (***Remove '.IDEA' for submission***).
    - **UML exports**:
        - **One folder for *each* lab** when applicable (Visual Paradigm project exported as defined in the
          presentations).
    - **AI usage Document** (if AI is used, which is highly discouraged)
        - A document (type of choice: word, txt, or markdown) explaining how, why, and for what tasks AI is used.
        - ***ENSURE YOU UNDERSTAND WHAT YOU DELIVER***
- **Naming convention:** `5SD_Portfolio_FirstnameLastname.zip` (replace with your name).
- **Deadlines & submission** are announced on Blackboard — follow any instructions there if they differ.

_Project:_

- **One zip** containing:
    - **Code**: the entire repository of your project (***Remove '.IDEA' for submission***).
    - **UML exports**.
    - **Slides**
    - **AI usage Document** (if AI is used)
        - A document (type of choice: word, txt, or markdown) explaining how, why, and for what tasks AI is used.
        - ***ENSURE YOU UNDERSTAND WHAT YOU DELIVER***
- **Naming convention:** `5SD_Project_FirstnameLastname.zip` (replace with your name).
- **Deadlines & submission** are announced on Blackboard — follow any instructions there if they differ.

---

## Assessment methods

**_Always verify on SISA for the latest updates!_**

> The practical part is evaluated in three ways:
>
> First, presence and cooperation in practica sessions is mandatory and will be assessed during the semester. Next, the
> created portfolio is delivered before the 7th lab session at a specific date and time mentioned during the class.
> During
> the 7th lab session all students will be questioned individually to assess their understanding of the provided code
> and
> UML diagrams. Finally, the students deliver, present and defend their project during a time slot in the examination
> period.
> For the first practica session, the students can submit their assignment in order to receive feedback and improve
> themselves towards the later practical sessions. This submission and feedback is not assessed in the final
> evaluations.
> However, they have to submit their assignments into their portfolio during the examination period. Hence, the students
> can improve their submissions based on the feedback they received. Students are allowed and encouraged to ask
> questions
> during the next lab sessions when the requirements are unclear.
> Note that the evaluations of the presence, portfolio, project and presentation are transferred to the second
> assessment
> period. This means the practical part of the course cannot be redone in the second assessment period.

> **AI**
>
> The students are allowed to use generative AI. However, they must keep track of a logbook mentioning why, when and
> where they used AI. This use is only permitted to deepen their understanding of design patterns and assist in the
> debugging process. It is therefore essential that students fully understand the entire code base they submit.
>
> WARNING: AI can hallucinate and be wrong. Always be critical of its output and do not blindly believe, copy, or use
> its output.

---

## Common Issues & Tips

- **Maven tab greyed out in IntelliJ** → open the **root `pom.xml`**, not the folder.
- **JUnit 5 only** (`org.junit.jupiter.*`); avoid JUnit 4 imports.
- **Java 21** everywhere (Project SDK, module SDK, Maven importer JDK).
- If you see benign **Byte Buddy/Mockito agent** warnings on Java 21 during tests, you can ignore them.

Good luck, and have fun building clean, tested designs!

---