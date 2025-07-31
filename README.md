MatchMate - Modern Android Matrimonial App

MatchMate is a modern, and fully offline-capable matrimonial app built using the MVVM architecture.
It leverages powerful Android Jetpack libraries such as Room, Retrofit, LiveData, ViewModel, and WorkManager.
The project is designed to demonstrate clean architecture, robust offline support, pagination, error handling, and a user-friendly UI/UX.

- Features

- Modern MVVM Architecture with Repository Pattern
- Paginated User Listing with RecyclerView
- Offline Caching via Room Database
- Background Sync with WorkManager (Auto-syncs when online)
- Accept/Decline Functionality with Status Persistence
- Seamless Offline Mode (Interact without internet)
- Error Handling with Snackbars
- Modern Material Design UI (Toolbar, Buttons, CardView)

- Tech Stack

Language: Kotlin
Architecture: MVVM (Model-View-ViewModel)
Networking: Retrofit + Coroutines
Local Storage: Room Persistence Library
Background Work: WorkManager
UI Components: RecyclerView, Material Components
Image Loading: Glide

- How It Works

1. Initial Launch - 
Fetches users from the API and caches them in Room.
UI displays user cards with "Accept" and "Decline" buttons.

2. Offline Mode -
Displays cached user data.
Accept/Decline actions are stored locally with isSynced = false.

3. Sync on Reconnect - 
When the network is restored, WorkManager syncs the local changes to the server (mocked here).

4. Pagination -
New pages load as the user scrolls (up to a defined max page).
Footer spinner indicates loading.
When the user tries to scroll past the max page, then appropriate message is shown.

6. UI Enhancements -
Material You style buttons & layout.
Bold toolbar title.
Profile card shows name, nationality, age, and current status.

7. Error handling - 
Appropriate Error messages are shown when there is no internet or when API fecth fails.
