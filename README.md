# Nature Posts Application (mobile)
The **Nature Posts** is a mobile Android application developed in **Kotlin**, utilizing **Firebase**, designed to allow users to create, save, comment on, and view nature-related posts. All posts and related data are securely stored in **Firebase** databases and cloud storage.

## Features
- **User Authentication**: Secure registration and login with data validation, including hashed password storage in **Firebase Authentication**.
- **Firebase Integration**:
   - **Firebase Authentication** for user credentials.
   - **Firestore Database** for storing shared data like posts and comments.
   - **Firebase Storage** for cloud-based photo storage.
- **Camera Integration**:
   - Supports both front and rear cameras of the mobile device.
   - Preview captured photos on the screen.
   - Convert photos to URI format and upload them to **Firebase Storage**.
- **Geolocation**:
   - Retrieves the current coordinates (latitude, longitude) of the device.
   - Attaches geolocation data to photos for location tagging.
- **OpenStreetMap Integration**:
   - Displays a map with markers indicating the photo capture location.
   - Shows the exact coordinates (latitude, longitude) on the map.
- **Subsampling Scale Image View**:
   - Enables zooming and panning of images using gestures like pinch-to-zoom.
- **Post Management**:
   - Create, view, and manage nature-related posts with photos, titles, descriptions, and geolocation data.
   - Comment on posts and view comments from other users.
- **User Profile**:
   - Manage user settings, including name, email, password, and profile picture.
   - View personal posts and associated comments.

## Requirements
The application is built using the following technologies:
- **Android SDK**: 34
- **Kotlin**: 1.9.25
- **Firebase**:
   - **Firebase Authentication** for user management.
   - **Firestore Database** for storing posts and comments.
   - **Firebase Storage** for storing photos.
- **Camera**: For capturing photos using the device's camera.
- **Geolocation**: For retrieving and tagging photos with location data.
- **OpenStreetMap**: For displaying photo locations on a map.
- **Subsampling Scale Image View**: For interactive image zooming.
- **Gradle**: For building the application and managing dependencies.
  structured
## Code Overview
The application is structured into several key components:
- **MainActivity**: Handles user registration (name, email, password, and profile photo). Saves user data to **Firebase Authentication** and the profile photo to **Firebase Storage**.
- **LoginActivity**: Manages user login by retrieving user data from **Firebase Authentication**.
- **PostsActivity**: Displays all nature posts created by users, with photos, titles and descriptions, stored in **Firestore Database**.
- **CreatePostActivity**: Allows users to create a nature post using the device's camera and geolocation, including photo preview functionality. Posts include a photo, title, description, and geolocation data. The post is saved to **Firestore Database**, and the photo is uploaded to **Firebase Storage**.
- **ProfilePostsActivity**: Displays nature posts created by the logged-in user, along with associated comments stored in **Firestore Database**.
- **PostDetailsActivity**: Shows detailed information about a specific nature post, including:
   - The post's photo with zoom functionality.
   - Author information and post description.
   - A list of comments from other users.
   - Buttons to navigate to the photo's capture location and add a new comment.
- **PostMapActivity**: Displays a map with a marker indicating the photo's capture location and its coordinates (latitude, longitude).
- **AddCommentActivity**: Allows users to add comments to a post, which are saved in **Firestore Database**.
- **SettingsActivity**: Enables users to update their profile information, including name, email, password, and profile photo. The new photo is uploaded to **Firebase Storage**.

## Getting Started
To execute the application, you are required to configure your own Firebase account, establish a database within Firebase, and provide the corresponding configuration file.
