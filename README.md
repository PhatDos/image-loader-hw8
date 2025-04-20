# image-loader-hw8

This Android app demonstrates various features like loading images from a URL, handling network connectivity, and managing background tasks using services, notifications, and asynchronous components. The app also demonstrates handling network status changes and shows notifications to the user when an image loading service is running.

## Features

- **AsyncTask**: Loads images in the background without blocking the UI.
- **AsyncTaskLoader**: Optimized for long-running background tasks that need to be restarted.
- **Broadcasts**: Listens to changes in network connectivity using `BroadcastReceiver`.
- **Service**: Runs the image loading service in the background and shows notifications.
- **Notifications**: Notifies users when the service is running and provides feedback on image loading status.
- **App includes Hilt, Kotlin Coroutines

## Changes

- I have shortened time for notification reannouce from 5 mins to 1 mins
- AsyncTask is deprecated. I used Kotlin Coroutines with viewModelScope (for ViewModel-related tasks) or Dispatchers.IO instead
- AsyncTaskLoader is deprecated so i used Migrate to WorkManager for background tasks that need to be persisted across app restarts or LiveData/StateFlow for asynchronous data loading tied to the UI lifecycle.
- Besides, i used Broadcast receivers to listen connectivity change, Service and Notifications
