# Campus Notice Board App - Staff Graded Assignment 2

- **Student ID**: `2024EB01570`
- **Course**: Programming in Mobile Devices
- **Assignment**: Staff Graded Assignment - 2
- **Topic**: Firebase Authentication, Shared Preferences, Firebase Realtime Database, Role-Based Access Control, and Android Notification Service.

---

## 📌 Deliverables Included

1. **Source Code**:
   - `RegisterActivity.kt` & `activity_register.xml` (User Registration with Role Selection & Firebase Auth)
   - `LoginActivity.kt` & `activity_login.xml` (User Login with SharedPreferences Session Storage)
   - `WelcomeActivity.kt` & `activity_welcome.xml` (Dashboard with Role-based notice creation & realtime feed)
   - `Notice.kt` (Data model for notice items)
   - `NoticeAdapter.kt` & `item_notice.xml` (RecyclerView adapter for displaying published notices)
   - `UserSessionManager.kt` (SharedPreferences wrapper for Email & Role persistence)
   - `NetworkUtils.kt` (Network connectivity check utility)
   - `NoticeNotificationService.kt` (Background Android Service for periodic system notifications)
   - `AndroidManifest.xml`, Gradle build configurations, and `google-services.json`.

2. **Screenshots** (All displaying Student ID `2024EB01570` prominently):
   - `Screenshot_1_RegisterActivity.png` - Registration screen with Email, Password, Role Spinner (Staff/Student), and Toast message.
   - `Screenshot_2_LoginActivity.png` - Login screen with Email, Password, Login button, and navigation links.
   - `Screenshot_3_WelcomeActivity.png` - Welcome screen displaying logged-in Email, Role, Student ID, and Logout button.
   - `Screenshot_4_NoticeCreationScreen.png` - Notice Creation Form (Staff view) with Title, Description, and Publish button.
   - `Screenshot_5_NoticesRetrievedScreen.png` - Realtime Notices Feed retrieved from Firebase Realtime Database.
   - `Screenshot_6_ServiceNotification.png` - Background Android Service notification popup ("Check the Campus Notice Board for new updates!").

---

## 🚀 Key Requirements & Implementation Details

### Question A: User Authentication with Shared Preferences (3 Marks)
- **RegisterActivity**:
  - Contains Email EditText, Password EditText, Spinner (Staff, Student), Register Button, and Login Link.
  - Checks network connectivity before registering via `createUserWithEmailAndPassword()`.
  - Saves role mapping into Firebase Realtime Database (`users/{uid}/role`).
  - Displays Toast: `"Account Created Successfully"`.
- **LoginActivity**:
  - Contains Email (TextView & EditText), Password (TextView & EditText), Login Button, and Register Link.
  - Authenticates user via `signInWithEmailAndPassword()`.
  - Saves Email and Role (`Staff`/`Student`) in `SharedPreferences` upon successful login.
- **WelcomeActivity**:
  - Displays `"Welcome, [Email]"` and `"Role: [Staff/Student]"`.
  - Displays `"Student ID: 2024EB01570"` in the header banner.
  - Contains a Logout button that clears `SharedPreferences`, signs out Firebase user, and returns to `RegisterActivity`.

### Question B: Publishing and Displaying Notices Using Firebase (7 Marks)
- **Network Validation**: Performs `NetworkUtils.isNetworkAvailable()` check before any Firebase operation.
- **Notice Publishing**:
  - Notice Form (Title, Description, Publish Button).
  - Saves notice data (`title`, `description`, `createdBy`, `timestamp`) to Firebase Realtime Database path `/notices`.
  - Displays Toast: `"Notice Published Successfully"`.
- **Role-Based Access Control**:
  - **Staff Users**: Can publish notices (Form active) and view all notices.
  - **Student Users**: Cannot publish notices (Notice Form disabled/hidden with read-only badge) and view all notices.
- **Display Notices**: Renders notice list using `RecyclerView` with custom `NoticeAdapter` showing Title, Description, Created By, and Timestamp.
- **Background Android Notification Service**:
  - Implements `NoticeNotificationService` running in background.
  - Periodically generates a notification: `"Check the Campus Notice Board for new updates!"`.
