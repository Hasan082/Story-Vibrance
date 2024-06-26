# StoryVibrance

StoryVibrance is a social media application that allows users to connect with friends, chat, comment on posts, save posts, add friends, view friend lists, and manage friend requests. This README file provides an overview of the project, setup instructions, and details about the various features and components.

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Register and login securely.
- **Chat**: Real-time messaging with friends.
- **Post Management**: Create, edit, and delete posts.
- **Comments**: Add comments to posts.
- **Save Posts**: Save posts to view later.
- **Friend Management**: Add friends, view friend list, and manage friend requests.
- **Profile**: View and edit user profiles.

## Screenshots
<p>
<img src="https://github.com/Hasan082/Story-Vibrance/blob/main/screenshots/big-1.png" alt="Screenshot 1">
<img src="https://github.com/Hasan082/Story-Vibrance/blob/main/screenshots/big-2.png" alt="Screenshot 1">
<img src="https://github.com/Hasan082/Story-Vibrance/blob/main/screenshots/big-4.png" alt="Screenshot 1">
</p>

## Installation

### Prerequisites

- Android Studio
- A Firebase project set up with Firestore and Authentication enabled.

### Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/StoryVibrance.git
    cd StoryVibrance
    ```

2. **Open in Android Studio**:
    - Open Android Studio and select `Open an existing project`.
    - Navigate to the cloned repository and open it.

3. **Firebase Configuration**:
    - Add your `google-services.json` file to the `app` directory.
    - Ensure Firestore and Authentication are enabled in your Firebase project.

4. **Dependencies**:
    - The required dependencies are listed in the `build.gradle` file. Sync the project with Gradle files to download the dependencies.

## Usage

### User Authentication

- **Register**: New users can register with their email and password.
- **Login**: Registered users can log in with their credentials.
- **Forgot Password**: Registered users can request to change their password with their credentials.

### Chat

- **Messaging**: group chat facilities for now.


### Posts

- **Create Post**: Users can create new posts with text and images.
- **Edit Post**: Users can edit their own posts.
- **Delete Post**: Users can delete their own posts.
- **View Posts**: View posts from friends and other users.

### Comments

- **Add Comment**: Add comments to posts.
- **View Comments**: View comments on posts.

### Save Posts

- **Save Post**: Save interesting posts to view later.
- **View Saved Posts**: Access saved posts from the user profile.

### Friend Management

- **Add Friend**: Send friend requests to other users.
- **View Friend Requests**: View and manage incoming friend requests.
- **Friend List**: View the list of confirmed friends.

### Profile

- **View Profile**: View user profile information.
- **Edit Profile**: Edit personal information and profile picture.

## Contributing

We welcome contributions to StoryVibrance! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

Please ensure your code adheres to the coding standards and includes appropriate tests.

## License

This project is licensed under the MIT License. See the [LICENSE](https://opensource.org/license/mit) file for details.

