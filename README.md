# PROG5121 POE - Chat Application (Part 1)

A Java console application built for the PROG5121 Portfolio of Evidence, IIE.

## Overview
This is the first part of a chat application project. It implements user registration, user login, and a message object that tracks its delivery status.

## Classes

### Registration
Validates a new user's details before registering them:
- **Username** – must contain an underscore (`_`) and be no more than 5 characters long.
- **Password** – must be at least 8 characters long, and contain at least one capital letter, one number, and one special character.
- **Cellphone number** – must be in the South African international format (e.g. `+27821234567`), validated using regex.

The `registerUser` method runs all three checks and returns a specific error message if any check fails, or a success message if the user is registered correctly.

### Login
Verifies a user's login attempt against their registered details:
- `loginUser` checks that the entered username and password match the registered ones.
- `returnLoginStatus` returns a welcome message on success, or an error message on failure.

### Message
Represents a single chat message:
- Stores the message payload (text).
- Tracks three status flags: `messageSent`, `messageReceived`, `messageRead`.
- Includes methods to update each flag as the message moves through the chat process.

## Testing
JUnit 4 unit tests were written for both the `Registration` and `Login` classes, covering valid and invalid cases:
- **RegistrationTest** – 9 tests covering username, password, and cellphone validation (pass and fail cases).
- **LoginTest** – 4 tests covering successful and unsuccessful login attempts, including exact message verification.

## How to Run
1. Open the project in NetBeans (Java with Ant).
2. Run `PROG5121POE.java` to see registration, login, and message functionality demonstrated in the console output.
3. Right-click `RegistrationTest.java` or `LoginTest.java` → Test File to run the unit tests.

## Author
Tebogo Baloyi – ST10490267
