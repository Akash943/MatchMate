MatchMate - Modern Android Matrimonial App

MatchMate is a modern, and fully offline-capable matrimonial app built using the MVVM architecture.
It leverages powerful Android Jetpack libraries such as Room, Retrofit, LiveData, ViewModel, and WorkManager.
The project is designed to demonstrate clean architecture, robust offline support, pagination, error handling, and a user-friendly UI/UX.

- Features

1. Modern MVVM Architecture with Repository Pattern
2. Paginated User Listing with RecyclerView
3. Offline Caching via Room Database
4. Background Sync with WorkManager (Auto-syncs when online)
5. Accept/Decline Functionality with Status Persistence
6. Seamless Offline Mode (Interact without internet)
7. Error Handling with Snackbars
8. Modern Material Design UI (Toolbar, Buttons, CardView)

- Tech Stack

1. Language: Kotlin
2. Architecture: MVVM (Model-View-ViewModel)
3. Networking: Retrofit + Coroutines
4. Local Storage: Room Persistence Library
5. Background Work: WorkManager
6. UI Components: RecyclerView, Material Components
7. Image Loading: Glide

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
