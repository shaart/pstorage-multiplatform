| Branch | Build Status                                                                                                                                                                                                         | Code Quality |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| `main` | [![Build Release](https://github.com/shaart/pstorage-multiplatform/actions/workflows/build-release.yml/badge.svg?branch=main)](https://github.com/shaart/pstorage-multiplatform/actions/workflows/build-release.yml) |              |
| `dev`  | todo                                                                                                                                                                                                                 |              |

<h1 style="font-family: Alef,serif">
    <img src="static/icon64.svg" alt="logo" height="128" style="vertical-align: middle"/> pstorage
</h1>

**pstorage** - simple offline portable password storage application.

## Table of contents

- [How to use](#how-to-use)
    - [Auth](#auth)
    - [Registration](#registration)
    - [Main view](#main-view)
    - [Settings](#settings)
    - [System tray](#system-tray)
    - [More](#more)
- [How to install](#how-to-install)
- [How to build](#how-to-build)
- [How to run for local development](#how-to-run-for-local-development)
- [License](#license)

## How to use

### Auth

Once app is open you will see auth form:
![Auth view](docs/auth_view.png)

If you don't have an account you can follow "Create a new account" button.

In case of wrong credentials you will see an error.

### Registration

![Registration view](docs/registration_view.png)

You can create any number of accounts with unique 'Username'.

**Be careful:** you can't restore your password if you forget it. Be sure that you remember it or
write down in safe place.

### Main view

After successful log in you will see main view.

If it's your first log in - passwords table will be empty.

Else you will see information about your passwords as on screenshot below.
![Main view](docs/main_view_with_passwords.png)

On screenshot:

1. Column of passwords' aliases that you will see in System Tray passwords menu. On the right side
   of each record you see "Edit" button that can be used to change Alias value.
2. Placeholder of your password. Here also you can change your password's value (by "edit" button on
   the right side of column).
3. Action buttons:
    - "Copy" button - on press it copies password's value to Clipboard.
    - "Delete" button - delete password that you don't need anymore.
4. Form to add a new password.
5. Sidebar on the left side with navigation buttons and "Log out" button.

### Settings

![Settings view](docs/settings_view.png)

Here you can configure application.

- Notify about hiding to tray: if enabled - application will use Notification Center on window close
  ![Notification](docs/notification.png)

### System tray

On launch application creates an icon in system tray.

After successful log in you will see there your passwords' aliases.

![System tray menu](docs/tray_menu.png)

On screenshot above:

- Application name with version - use this to reopen the main window.
- Passwords menu - all your passwords' aliases. Click on specific element to copy it's password's
  value to clipboard. **Note:** It's blank if you don't have any password or in case if you are not
  authorized.
- Settings of current user.
- Quick 'log out' button.
- Exit button to shutdown application.

### More

- Design: [Figma](https://www.figma.com/file/k5UoSrgBohsrTmETQgDw4f/pstorage-multiplatform-(public)?type=design&node-id=0%3A1&mode=design&t=QEn5ReUuz1ehxoMI-1)

## How to install on Windows

1. Download release build for your system from "Releases" section.
2. Run `.msi` installation file.
3. Open `pstorage-multiplatform.exe` in the root of installed directory.

## How to install on MacOS

1. Download release build for your system from "Releases" section.
2. Run `.dmg` installation file. Drag `pstorage-multiplatform.app` into "Application" directory.
3. Open `pstorage-multiplatform.app`.

## How to build

#### Requirements:

- JDK 17

#### Steps

1. Package platform binary using command

```bash
./gradlew packageReleaseDistributionForCurrentOS
```

2. Find built artifact in `build\compose\binaries\main-release\`

## How to run for local development

If you need full application version:

```bash
./gradlew run
```

If you need to preview specific View (`@Composable`) in interactive way:

1. Go to `src/jvmMain/com/github/shaart/pstorage/multiplatform/PreviewMain.kt`
2. Edit view to needed
3. Run with flag `app.preview` = `true`

```bash
./gradlew -Dapp.preview=true run
```

## License

[Apache License 2.0](LICENSE)