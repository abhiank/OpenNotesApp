# OpenNotes App
An app to create and save notes

A simple app with two main functionalities 

* Add Notes with images
* See List of Notes 

The notes can be deleted and edited.

The app provides both offline and online storage. Offline storage is backed by [Realm](https://blog.realm.io/realm-for-android/) and online storage using [Firebase](https://firebase.google.com/).

All data is global as the functionality of users using sign in has not been added yet. So, all notes are visible and accecible to all users of the app. 

When offline, all the notes are stored to Realm. When internet returns, the data is synced to Firebase. While the app is used, all data is stored first to the local store and then to the remote store. Retrival of data only happens from the remote store when no items are returned from the local store. 

The app uses the following libraries

* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Parceler](https://github.com/johncarl81/parceler) - For ease of using the Parcelable interface to pass objects in between activies
* [Dexter](https://github.com/Karumi/Dexter) - For easy permissions requesting for API 24+
* [Firebase Database](https://firebase.google.com/docs/database/android/start/) - To store the notes online
* [Firebase Storage](https://firebase.google.com/docs/storage/android/start) - To store the images
* [Glide](https://github.com/bumptech/glide) - For awesome image downloading and maneging
* RecyclerView, CardView, DesignSupportLib

For architecture, this app uses MVP for the presentation layer, inspired from the code from this project - [Android MVP](https://github.com/antoniolg/androidmvp).  
For the data layer, used the patter as used in this project - [Google Architecture Blueprints](https://github.com/googlesamples/android-architecture/tree/todo-mvp).

