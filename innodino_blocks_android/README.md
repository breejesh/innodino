# InnoDino Blocks Android App

A playful, educational block-based programming app for children aged 6-14, featuring dinosaur-themed adventures with LED Matrix and DinoBot modules.

## 🦕 About InnoDino Blocks

InnoDino Blocks transforms learning programming into an exciting prehistoric adventure! Children embark on two epic journeys:

- **LED Crystal Chronicles**: Help Rex restore Digital Dinosaur Valley using LED Matrix programming
- **DinoBot Expedition**: Join Zara to save the Lost Dino City with robot programming

## 🚀 Features

- **Visual Block Programming**: Drag-and-drop interface for learning programming concepts
- **Story-Driven Missions**: Progressive adventures that build coding skills
- **Modern Material Design 3**: Beautiful, child-friendly interface
- **STEAM Education**: Science, Technology, Engineering, Arts, and Mathematics integration
- **Hardware Support**: LED Matrix and DinoBot with ultrasonic sensor

## 🎨 Design Theme

The app follows a vibrant dino/STEAM color palette:
- **Primary**: Dino Green (#6FCF97)
- **Secondary**: Tech Teal (#2D9CDB)
- **Accent**: Sun Yellow (#FFCE55)
- **Alert**: Soft Coral (#EB5757)
- **Background**: Cloud White (#FAFAFA)
- **Text**: Slate Gray (#4F4F4F)

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Design System**: Material Design 3
- **Build System**: Gradle with Kotlin DSL
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)

## 📱 Project Structure

```
app/
├── src/main/
│   ├── java/com/innodino/blocks/
│   │   ├── InodinoBlocksApplication.kt
│   │   └── ui/
│   │       ├── main/MainActivity.kt
│   │       ├── led/LEDCrystalActivity.kt
│   │       ├── dinobot/DinobotExpeditionActivity.kt
│   │       └── theme/
│   │           ├── Color.kt
│   │           ├── Theme.kt
│   │           └── Type.kt
│   ├── res/
│   │   └── values/
│   │       ├── colors.xml
│   │       ├── strings.xml
│   │       └── themes.xml
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or higher
- Android SDK with API 34

### Building the Project

1. **Clone or open the project in Android Studio**
2. **Sync Gradle files**
3. **Build the project**:
   ```bash
   ./gradlew build
   ```
4. **Run on device/emulator**:
   ```bash
   ./gradlew installDebug
   ```

### VS Code Tasks (Optional)

If using VS Code, you can use these tasks:
- **Build**: `Ctrl+Shift+P` → "Tasks: Run Task" → "Build Android App"
- **Clean**: `Ctrl+Shift+P` → "Tasks: Run Task" → "Clean Android Project"

## 🎮 Modules

### LED Crystal Chronicles
Programming concepts through LED Matrix control:
- **Digital Displays**: Basic LED patterns and text
- **Dino Animations**: Moving dinosaur sprites
- **Pattern Logic**: Complex light sequences
- **Time Control**: Timed animations and effects

### DinoBot Expedition
Robotics programming with sensor integration:
- **Movement Commands**: Forward, backward, turn controls
- **Sensor Programming**: Ultrasonic distance detection
- **Decision Making**: Conditional logic and responses
- **Advanced Navigation**: Complex path planning

## 🔧 Development Guidelines

### Code Style
- Use Kotlin for all new development
- Follow MVVM architecture pattern
- Implement Material Design 3 principles
- Use meaningful, dinosaur/adventure-themed variable names
- Add comprehensive comments for educational sections

### Block Programming System
- Create custom drag-and-drop visual blocks
- Each block represents a programming construct
- Colorful, child-friendly block designs
- Visual feedback for connections and execution

## 📚 Educational Goals

- **Computational Thinking**: Problem decomposition and pattern recognition
- **Sequential Logic**: Understanding step-by-step instructions
- **Conditional Logic**: Decision-making in programming
- **Loops and Iteration**: Repetitive actions and efficiency
- **Variables and Data**: Information storage and manipulation
- **Hardware Integration**: Understanding physical computing

## 🤝 Contributing

This project is part of the InnoDino educational platform. Follow the coding instructions in `.github/copilot-instructions.md` for consistent development practices.

## 📄 License

Educational project for Georgia Tech EdTech program.

---

**Ready to embark on a prehistoric programming adventure? Let's code with the dinosaurs! 🦕💻**
