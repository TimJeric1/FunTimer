# <h1 align="center">⏱️ FunTimer (In Development)</h1>

<p align="center">FunTimer offers a practical solution for the time-conscious environment of water parks. This app serves as a user-friendly Alarm/Timer tool, specifically crafted to manage entry times efficiently. In the context of water parks, visitors receive armbands with unique numbers upon entry and  FunTimer empowers park staff to set timers for each visitor based on their armband number, streamlining the entire process.</>

# <h1 align="center">🖼 Preview </h1>

# <h1 align="center">Timer Setup Screen 📝</h1>
<div align="center">

| Add  | Change Layout| Pick Custom Duration & Extra Time  |
|------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/add.gif?raw=true"> | <img src="https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/change_layout.gif?raw=true"> | <img src="https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/pick_custom_duration_and_extra_time.gif?raw=true"> |    

</div>

# <h1 align="center">Active Timers Screen ⏳</h1>
<div align="center">

| Delete                                                                                |
|---------------------------------------------------------------------------------------| 
| <img src="https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/delete.gif?raw=true"> | |

</div>

# <h1 align="center">Past Timers Screen ⌛</h1>
<div align="center">

| View                                                                                        |
|---------------------------------------------------------------------------------------------| 
| <p align="center"><img src="https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/past.jpg?raw=true" style="display: block; margin: auto;"></p> |

</div>

# <h1 align="center">Notifications 📳</h1>
<div align="center">

| Standard                                                                                    | Fullscreen                                                                                             |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| ![](https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/notification.gif?raw=true) | ![](https://github.com/TimJeric1/FunTimer/blob/master/ScreenGifs/fullscreen_notification.gif?raw=true) |
</div>

<h2 align="center">Features⭐</h2>

- Add and delete timers/alarms.
- When screen is on, fire a notification alarm.
- When screen is off, fire a fullscreen alarm.
- Mute the alarm without dismissing it
- View current timers/alarms
- View past timers/alarms

<h2 align="center">Architecture ☁</h2>

This app follows the MVVM (Model-View-ViewModel) architecture pattern combined with clean
architecture. The components of the app are organized as follows:

- Data Layer:
    - Model: The data source for the app is the TimerDatabase (Room database), which provides
      information about set timers triggerTime and its corresponding numbers. The app uses Room to
      store the data, TypeConverters for seamless integration with DateTime objects. Data is exposed
      through TimerRepositoryImpl. Also the app uses DataStore for storing user preferences like
      different layouts, custom timer durations and extra time, Which are exposed as a flow of
      UserPreferences object.

- Domain Layer:

    - Interfaces: Contains TimerRepository and UserPreferencesRepository interfaces which are
      implemented in the Data Layer and used in ViewModels. They decouple the data layer from the
      presentation layer using an abstraction (interface).

    - Data Models: Contains Data classes that both the presentation and data layer use. Those are
      TimerItem for representing timers/alarms and UserPreferences for representing customizable
      components of the app (different layouts, custom timer durations and extra time)

    - Use Cases: Currently an empty folder waiting for... a use case

- Presentation Layer :

    - View: Using Jetpack Compose, the main activity (MainActivity) with Bottom Navigation Bar
      orchestrates smooth transitions between the Timer Setup and Active Timers screens. Users
      create timers in Timer Setup by selecting numbers and durations. The Active Timers screen
      offers an intuitive timer management experience.

    - ViewModel: ActiveTimersViewModel and TimerSetupViewModel, acting as intermediaries in Jetpack
      Compose, manage data flow between UI and repositories, updating views dynamically. Channels
      handle one-time dialog events. Both view models access TimerItems through a repository
      interface in a Room database. TimerSetupViewModel connects to the datastore via
      UserPreferencesRepository, enabling users to create timers with number and duration selection.
      ActiveTimersViewModel facilitates timer management, handling long clicks for item deletion.

<h2 align="center">Getting Started 🚀</h2>

To run this app, you'll need to have Android Studio installed. Follow these steps to get started:

- Clone this repository: git clone https://github.com/TimJeric1/FunTimer.git
- Open the project in Android Studio.
- Build and run the app.

# <h1 align="center">📚 Libraries and Tools Used </h1>

<p align="center">

- Jetpack Compose
- Room
- Flows
- Jetpack Datastore
- Dagger Hilt
- Coroutines

</p>
