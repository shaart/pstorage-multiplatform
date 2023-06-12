| Branch | Build Status | Code Quality |
|--------|--------------|--------------|
| `main` | todo         |              |
| `dev`  | todo         |              |

## About

Simple offline portable Password Storage application.

## How to use

### Auth window

Once app is open you will see auth window:
![Auth view](docs/auth_view.png)

If you don't have an account you can enter "Login" and "Password" and press "Sign up" button. You
will be followed inside application.

**Be careful:** you can't restore your password if you forget it. Be sure that you remember it or
write down in safe place.

In case of wrong credentials you will see an error.

### Main window

After successful log in you will see main window.

If it's your first log in - table will be empty.

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

### System tray

On launch application creates an icon in system tray.

After successful log in you will see there your passwords' aliases.

![System tray menu](docs/tray_menu.png)

On screenshot:

- Application name with version - use this to reopen the main window.
- Passwords menu - all your passwords' aliases. Click on specific element to copy it's password's
  value to clipboard. **Note:** It's blank if you don't have any password or in case if you are not
  authorized.
- Exit button.

## How to install

1. Download release build for your system.
2. Run installation file.
3. Open `pstorage-multiplatform.exe` in the root of installed directory. Be free to move it to
   another place of to another PC as whole directory.

## How to build

#### Requirements:

- JDK 17

#### Steps

1. Package platform binary using command

```bash
./gradlew packageReleaseDistributionForCurrentOS
```

2. Find built artifact in `build\compose\binaries\main-release\`

## How to run for development

If you need full application version:

```bash
./gradlew run
```

If you need to preview specific View (@Composable) in intractable way:

1. Go to `src/jvmMain/com/github/shaart/pstorage/multiplatform/PreviewMain.kt`
2. Edit view to needed
3. Run with flag `app.preview` = `true`

```bash
./gradlew -Dapp.preview=true run
```