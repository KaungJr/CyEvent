# CyEvent

Android app to centralize ISU campus events, enabling students to discover, track, and participate in activities with personalized recommendations. Features include an interactive calendar, real-time messaging, attendance tracking, and backend integration via Spring Boot.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
- [Roadmap](#roadmap)

---

## Features

- 📅 **Interactive Calendar:** Browse, filter, and save campus events.
- 💬 **Real-Time Messaging:** Chat with other attendees and event organizers.
- 🎓 **Attendance Tracking:** Check in to events and track your participation history.
- 🌟 **Personalized Recommendations:** Get event suggestions tailored to your interests.
- 🔗 **Backend Integration:** Powered by Spring Boot for secure and scalable data management.

---

## Tech Stack

- **Frontend:** Java (Android), HTML, JavaScript
- **Backend:** Spring Boot (Java)
- **Other:** [List any APIs, libraries, or frameworks used]
- **Version Control:** Git & GitHub

---

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- Java 8+
- [Gradle](https://gradle.org/) (if building manually)
- [Spring Boot](https://spring.io/projects/spring-boot) (for backend)

### Installation (Mobile App)

1. **Clone the repository:**
    ```sh
    git clone https://github.com/KaungJr/CyEvent.git
    cd CyEvent
    ```
2. **Open the project in Android Studio.**
3. **Build and run** the app on your emulator or Android device.

### Installation (Backend)

1. Navigate to the backend directory:
    ```sh
    cd backend
    ```
2. Build and run the Spring Boot application:
    ```sh
    ./gradlew bootRun
    ```
3. Configure your mobile app to connect to the backend API (see `app/src/main/res/values/strings.xml`).

---

## Usage

- **Browse Events:** Launch the app and view upcoming campus events.
- **Track Attendance:** Tap an event to join and mark your attendance.
- **Chat:** Use the messaging feature to communicate with other participants.
- **Personalize:** Set your preferences in the profile section for tailored recommendations.

---

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add your feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## Roadmap

- [ ] Push notifications for new events
- [ ] Event RSVP and reminders
- [ ] Admin dashboard for event organizers
- [ ] Integration with campus authentication

