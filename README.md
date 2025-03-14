# Jellyfin Android TV - Custom Builds with Online Subtitle Support

This repository provides custom builds of the Jellyfin Android TV app with additional features that were not merged into the official repository. My primary focus is enhancing the subtitle experience by integrating online subtitle providers and adding better synchronization options.

## Features

### 1. **Client-Side Support for Online Subtitles**
- Added support for fetching subtitles from:
  - **OpenSubtitles.com**
  - **SUBDL**
- Users can now download and use subtitles from these services directly within the app.

### 2. **Subtitle Delay Adjustment**
- Implemented a **subtitle delay feature** to allow fine-tuning the timing of external subtitles.
- Users can manually adjust subtitle synchronization to match the audio.

## Screenshots
- Automatic Subtitle Fetching:

![Screenshot](scr/cc.png)

- Subtitle Delay:
  
![Screenshot](scr/cctiming.png)

- Preferred Language Settings
  
![Screenshot](scr/onlinesubs.png)
![Screenshot](scr/onlinesubsprefs.png)

## Why Was This Not Merged into Official Jellyfin?
The original Pull Request ([#4502](https://github.com/jellyfin/jellyfin-androidtv/pull/4502)) was rejected due to the following reason:
- The maintainers preferred to keep online subtitle fetching outside the client app and rely on server-side solutions.

## Monthly Releases
Since these features are useful for many users, I will continue maintaining them in this repository. I will provide **monthly releases** with:
- Bug fixes and improvements
- Possible new features based on community feedback

## How to Install
0. Remove the original release first, since signatures are different.
1. Download the latest release from the [Releases](https://github.com/ahmethascelik/jellyfin-androidtv/releases) page.
2. Install the APK on your Android TV device.
3. Enable external subtitle fetching in the settings.

## Feedback & Contributions
- If you encounter any issues, feel free to open an issue in this repository.
- Suggestions and feature requests are welcome!

---
This project is based on the official [Jellyfin Android TV](https://github.com/jellyfin/jellyfin-androidtv) client but includes additional enhancements not found in the main branch.
