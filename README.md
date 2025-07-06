````markdown
# 📱 Smart Task Manager

Smart Task Manager is an Android app built using **Java** in **Android Studio**. It helps users manage daily tasks efficiently, with support for **user authentication**, **notifications**, and **offline data persistence**.

---

## 🚀 How to Run the Project (Android Studio - Java)

Follow these steps to run the project locally on your machine:

### 1. Clone the Repository

```bash
git clone [https://github.com/anam309/Smart-Task-Manager.git](https://github.com/anam309/Smart-Task-Manager.git)
````

### 2\. Open the Project in Android Studio

  * Launch **Android Studio**.
  * Click "**Open**" and select the cloned folder `Smart-Task-Manager`.
  * Allow **Gradle** to sync and install any required dependencies.

### 3\. Set Up Firebase

If the project isn’t already connected:

  * Create a **Firebase project** at [Firebase Console](https://console.firebase.google.com/).
  * Register your Android app and download the `google-services.json` file.
  * replace it in the `app/` directory with already exisiting one.
  * Authentication and notifications rely on Firebase. Ensure your Firebase project has **Email/Password auth** and **Firebase Cloud Messaging (FCM)** enabled.

### 4\. Run the App

  * Connect a real Android device or launch an emulator.
  * Click the green **Run button (▶)** or press `Shift + F10`.

-----

✨ Features

  * ✅ **User Authentication** (Firebase Email/Password)
  * 📝 **Add, edit, delete tasks**
  * 🗃️ **Room Database** for local offline storage
  * 🔔 **Task Notifications** using Firebase Cloud Messaging (FCM)
  * 📅 **Due date** and **task status tracking**
  * 📱 **Clean and responsive user interface**

-----

📂 Tech Stack

  * **Java**
  * **Android SDK**
  * **Firebase Authentication**
  * **Room Database**
  * **Gradle**

-----

🛠 Requirements

  * **Android Studio Bumblebee** or later
  * **Android SDK 24** or above
  * **Java 8** or newer
  * **Firebase account** (for auth)


```
```
