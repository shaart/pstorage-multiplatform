## Changelog
#### v1.1.0
- Fix immediate crash on MacOS: now database and logs are stored under user's home 
directory: `~/pstorage-multiplatform` 
- MacOS build info enriched with app category, BundleID specified explicitly.

**Note:** if you already used `v1.0.1` - you should move a database file from `<app_dir>/pstorage.data.db`
to `<user_home>/pstorage-multiplatform/pstorage.data.db` 

#### v1.0.1
- Application MVP:
  - authorization
  - system tray with way to reopen app, copy password or exit
  - hide to tray on close
  - main view with passwords table
  - copy decrypted password to clipboard from table and from tray
  - add a new password
  - edit existing password: alias or password's value
  - delete existing password