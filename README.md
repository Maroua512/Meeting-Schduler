Meeting Scheduler App

Table of Contents
Description
Features
Technologies
Design Patterns
Installation
Usage
Contributing
Description
The Meeting Scheduler app is designed to facilitate the planning of meetings and events between teachers and students within a university faculty.
It provides a user-friendly and intuitive interface, allowing users to create, manage, 
and participate in meetings while integrating communication and event management features.

Features
Push Notifications: Receive notifications for upcoming events.

Calendar: View and manage your events in an interactive calendar.

Current Events: Easily access the list of ongoing events.

Chat: Communicate with participants before and after meetings.

Settings: Customize your user experience.

Create an Event: Create events by specifying type, date, details, and adding supporting files.

User Profiles List: View and manage user profiles (teachers and students).

Discussion List: Access ongoing discussions with participants.

Firebase: Utilize Firebase for user and discussion management.

Glide: Optimized image loading with Glide.

Koin: Simplified dependency injection with Koin.

PostgreSQL: Connect to an external database for existence verification during registration.

Forgot Password Management: Easily recover forgotten passwords.

Account Deletion: Allow users to delete their accounts.

Password Update: Easily update passwords.

Technologies
Kotlin: Primary language for app development.
Firebase: For authentication and data management.
PostgreSQL: External database for user verification.
Glide: For image loading.
Koin: For dependency injection.
Design Patterns
The application utilizes several design patterns to improve code structure and maintainability:

Strategy: Defines a family of algorithms, encapsulates them, and makes them interchangeable, facilitating the extension of application functionality without modifying existing code.
Observer: Allows objects to subscribe and be notified of state changes in other objects, making it easier to manage real-time updates, such as notifications of new messages in chat.
SOLID: Applies SOLID principles to enhance code readability and maintainability, promoting separation of concerns and extensibility.
MVVM (Model-View-ViewModel): An architecture that separates presentation and business logic concerns, facilitating unit testing and application evolution.
Installation
Clone the repository:

bash
Copier le code
git clone https://github.com/your-username/meeting-scheduler-app.git
Open the project in Android Studio.

Sync the Gradle dependencies.

Configure Firebase settings in the google-services.json file.

Set up the connection to your PostgreSQL database.

Usage
Launch the application on an Android device or emulator.
Sign up or log in as a teacher or student.
Start creating events and chatting with other users.
Contributing
Contributions are welcome! To contribute:

Fork the project.
Create a new branch for your changes:
bash
Copier le code
git checkout -b my-new-feature
Commit your changes:
bash
Copier le code
git commit -m "Add a new feature"
Push the branch:
bash
Copier le code
git push origin my-new-feature
