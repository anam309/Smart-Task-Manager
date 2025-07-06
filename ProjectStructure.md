## 🏗️ Project Architecture Overview

This Smart Task Manager app follows a modular and layered architecture, ensuring separation of concerns, testability, and maintainability.

### 1. UI Layer (View)

This layer is responsible for displaying information to the user and handling all user interactions.

* **Components:**
    * **Activities:** `MainActivity`, `LoginActivity`, `AddEditTaskActivity`, `AddEditCategoryActivity`
    * **Adapters:** `TaskAdapter`, `TaskDisplayItem` (likely a data class for display purposes)
    * **XML Layouts:** `activity_main.xml`, `activity_login.xml`, `item_task.xml`, etc.
* **Purpose:** To present data visually and capture user input.

### 2. ViewModel Layer

The ViewModel acts as a crucial link between your UI and the data layers, designed to hold and manage UI-related data in a lifecycle-conscious way.

* **Components:**
    * `TaskViewModel`
* **Purpose:** To provide data to the UI, handle UI logic, and survive configuration changes, preventing data loss.

### 3. Repository Layer

This layer serves as a single source of truth for data, abstracting the underlying data sources from the ViewModels.

* **Components:**
    * `TaskRepository`
* **Purpose:** To manage data operations, coordinating data flow from various sources like the local database (Room) and potentially remote sources (like Firebase, if expanded in the future).

### 4. Data Layer (Room Database)

This is where the local data persistence is managed using Android's Room Persistence Library.

* **Components:**
    * **Room Database:** `AppDatabase`
    * **DAOs (Data Access Objects):** `TaskDao`, `CategoryDao`
    * **Entities:** `Task`, `Category` (representing database tables)
    * **Type Converters:** For handling specific data types like `Date`
* **Purpose:** To provide an abstraction layer over the SQLite database for efficient local data storage and retrieval.

### 5. Firebase Integration

Firebase services are utilized for essential functionalities like user authentication and push notifications.

* **Services Used:**
    * **Firebase Authentication:** Specifically Email/Password authentication.
* **Purpose:** To manage user accounts securely and deliver timely task reminders and alerts.

### 6. Utils (Utility & Background Operations)

This section contains helper classes and components for background tasks and common utility functions.

* **Components:**
    * `DeadlineReceiver`
    * `NotificationUtils`
* **Purpose:** To handle the triggering and display of task deadline notifications and other general utility functions.

---
