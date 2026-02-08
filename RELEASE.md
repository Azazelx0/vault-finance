# Release Guide 🚀

This guide explains how to version your app, build a release package (APK/AAB), and publish it to GitHub.

## 1. Versioning Types

Android uses two version settings in `app/build.gradle.kts`:

*   **`versionCode`**: An integer (e.g., `1`). Must increase with every update.
*   **`versionName`**: A user-visible string (e.g., `1.0.0`).

### How to Update:
1.  Open `app/build.gradle.kts`.
2.  Find the `defaultConfig` block.
3.  Increment `versionCode` and update `versionName`.

```kotlin
defaultConfig {
    versionCode = 2  // Increment this!
    versionName = "1.1.0" // Update this!
}
```

## 2. Building a Signed Release (Android Studio)

To distribute your app, you need a **Signed APK** or **AAB**.

1.  Go to **Build** > **Generate Signed Bundle / APK...**.
2.  Select **APK** (for direct install) or **Android App Bundle** (for Play Store).
3.  Click **Next**.
4.  **Key Store Path**: Click "Create new..." if you don't have one.
    *   **Keep this file and password private & safe!** Losing it prevents updates.
5.  Fill in the passwords and alias.
6.  Click **Next**, select **release** build variant.
7.  Click **Create**.

Android Studio will generate the file in `app/release/`.

## 3. Creating a GitHub Release

Once you have your code committed and your APK built:

1.  **Commit & Push**:
    ```bash
    git add .
    git commit -m "Release: v1.1.0"
    git push origin main
    ```
2.  **Tag the Release**:
    ```bash
    git tag v1.1.0
    git push origin v1.1.0
    ```
3.  **Go to GitHub**:
    *   Open your repository.
    *   Click **Releases** > **Draft a new release**.
    *   Choose the tag (`v1.1.0`).
    *   **Attach binary**: Upload your signed `.apk` file here.
    *   Publish release.
