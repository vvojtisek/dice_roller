# Dice Roller

[![CI](https://github.com/vvojtisek/dice_roller/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/vvojtisek/dice_roller/actions/workflows/ci.yml)
[![Security](https://github.com/vvojtisek/dice_roller/actions/workflows/security.yml/badge.svg?branch=main)](https://github.com/vvojtisek/dice_roller/actions/workflows/security.yml)
[![Latest release](https://img.shields.io/github/v/release/vvojtisek/dice_roller)](https://github.com/vvojtisek/dice_roller/releases/latest)

A free virtual dice app for Android, with no ads, no account and no data collection.
It is built with Kotlin and Jetpack Compose.

> **Language:** the app's interface is in Czech for now. English and other languages are planned; see [docs/IMPROVEMENT_PLAN.md](docs/IMPROVEMENT_PLAN.md).
>
> **Česky:** Dice Roller je bezplatná virtuální hrací kostka pro Android, bez reklam, bez účtu a bez sběru dat. Rozhraní aplikace je zatím v češtině. Hotovou aplikaci stáhnete v sekci [Releases](https://github.com/vvojtisek/dice_roller/releases/latest).

## Features

- **Dice types:** d4, d6 (pips or numerals), d8, d10 (optionally shows 10 as 0), d12 and d20.
- **Color dice:** six-sided dice with 2, 3 or 6 colors, plus optional shape symbols so color-blind players can read them too.
- **1 to 4 dice per roll**, with an animated throw and synthesized landing sounds that can be muted.
- **Profiles and presets:** up to 16 profiles with up to 8 quick presets each, such as "2× d6", saved on the device.
- **Dice colors:** seven mineral-inspired color themes, set per die.
- **Shake to roll** using the accelerometer (optional).
- **History** of the last 15 rolls, plus **per-die statistics** with a chi-square fairness check.
- Dark and light themes.

## Install

1. On your Android phone (Android 7.0 or newer), open the [latest release](https://github.com/vvojtisek/dice_roller/releases/latest).
2. Download `dice-roller-<version>.apk`. You can compare its checksum with `SHA256SUMS.txt` from the same release.
3. Open the file. Android asks you to allow installs from your browser ("Install unknown apps") the first time.
4. To get updates automatically, add `https://github.com/vvojtisek/dice_roller` to [Obtainium](https://github.com/ImranR98/Obtainium).

If a release only has a `-debug.apk`, it was built before a release signing key was set up. Uninstall that build before you install a later, signed release.

## Build from source

Requirements:

- JDK 17 or newer
- Android SDK with platform 36.1 (Android Studio downloads it on first sync), or a recent Android Studio version that supports Android Gradle Plugin 9.1

```bash
git clone https://github.com/vvojtisek/dice_roller.git
cd dice_roller
./gradlew assembleDebug          # APK in app/build/outputs/apk/debug/
./gradlew installDebug           # install on a connected device or emulator
```

No API keys or signing keys are needed. Without a keystore, debug builds use the Android SDK's default debug key and release builds are unsigned.

## Tests

| Command | What it runs | Where |
| --- | --- | --- |
| `./gradlew testDebugUnitTest` | Unit tests: pure dice logic (`DiceLogicTest`) and Compose UI tests on Robolectric | JVM, no device needed |
| `./gradlew connectedDebugAndroidTest` | End-to-end tests (`DiceRollerE2ETest`): launch the real app and roll, change settings, open statistics | Emulator or device |
| `./gradlew lintDebug` | Android Lint | JVM |

The dice logic that the unit tests cover (rolling, rotation snapping, preset mapping, profile validation and the fairness test) lives in [`DiceLogic.kt`](app/src/main/java/com/example/DiceLogic.kt). It has no Android dependencies.

## CI/CD

All workflows are in [`.github/workflows`](.github/workflows):

| Workflow | Trigger | What it does |
| --- | --- | --- |
| [`ci.yml`](.github/workflows/ci.yml) | Every pull request and push to `main` | Validates the Gradle wrapper, runs lint, unit tests and a debug build, and runs E2E tests on an Android 14 emulator. The debug APK and test reports are uploaded as artifacts. |
| [`pull-request-title.yml`](.github/workflows/pull-request-title.yml) | Pull requests | Checks that the title is a [Conventional Commit](https://www.conventionalcommits.org/) (`feat: …`, `fix: …`). |
| [`release.yml`](.github/workflows/release.yml) | Push to `main` | [release-please](https://github.com/googleapis/release-please) keeps a release pull request open. Merging it creates the `vX.Y.Z` tag and GitHub Release, then attaches the APK, AAB and checksums. |
| [`security.yml`](.github/workflows/security.yml) | Daily at 03:17 UTC, push to `main`, manual | CodeQL analysis of the Kotlin code and the workflows, Gradle dependency submission for Dependabot alerts, and a Gitleaks secret scan of the full history. |

Dependabot ([`.github/dependabot.yml`](.github/dependabot.yml)) proposes Gradle and GitHub Actions updates weekly.

### How releases work

1. Merge pull requests into `main` with Conventional Commit titles. `fix:` bumps the patch version, `feat:` bumps the minor version, and `feat!:` or a `BREAKING CHANGE:` footer bumps the major version.
2. release-please opens or updates a pull request named `chore(main): release X.Y.Z`. It bumps [`version.txt`](version.txt) and `CHANGELOG.md`.
3. Merging that pull request tags the commit and publishes the GitHub Release with the APK attached.

The app's `versionName` comes from `version.txt`, and `versionCode` is calculated as `MAJOR * 1_000_000 + MINOR * 1_000 + PATCH`, so every release can be installed over the previous one.

## Repository setup (one time, for the maintainer)

These are GitHub settings, so they cannot be stored in the repository:

1. **Protect `main`.** Go to Settings → Rules → Rulesets → New ruleset → Import a ruleset, and choose [`.github/rulesets/main.json`](.github/rulesets/main.json). After that, every change has to go through a pull request, and the Gradle wrapper validation, Unit tests, E2E tests and Conventional Commit title checks must pass before it can be squash-merged. The checks show up in the picker after they have run once.
2. **Allow release pull requests.** Go to Settings → Actions → General → Workflow permissions, and enable "Allow GitHub Actions to create and approve pull requests".
3. **Run CI on release pull requests.** Pull requests opened with the default `GITHUB_TOKEN` do not start other workflows, so the release pull request would never get its required checks. Create a fine-grained personal access token for this repository with *Contents* and *Pull requests* set to read and write, and save it as the repository secret `RELEASE_PLEASE_TOKEN`.
4. **Turn on security features.** Go to Settings → Code security, and enable the Dependency graph, Dependabot alerts, Dependabot security updates and Private vulnerability reporting. On a public repository, code scanning results from `security.yml` appear without any extra setup.
5. **Signing releases.** Create an upload keystore once and keep a backup somewhere safe. If you lose it, you cannot publish updates that install over existing ones.

   ```bash
   keytool -genkeypair -v -keystore upload.jks -alias upload -keyalg RSA -keysize 4096 -validity 10000
   base64 -w0 upload.jks   # paste the output into the ANDROID_KEYSTORE_BASE64 secret
   ```

   Add these repository secrets: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD` and `ANDROID_KEY_PASSWORD`. The key alias must be `upload`. Without these secrets, releases fall back to a debug-signed APK.

## Contributing

1. Fork the repository and create a branch.
2. Make your change. Add or update tests: unit tests in `app/src/test`, E2E tests in `app/src/androidTest`.
3. Run `./gradlew lintDebug testDebugUnitTest` locally, and `connectedDebugAndroidTest` if you have an emulator.
4. Open a pull request with a Conventional Commit title.

Bug reports and ideas are welcome in [Issues](https://github.com/vvojtisek/dice_roller/issues). Help with translations will be especially useful once the strings have been extracted (see the plan).

## Security

See [SECURITY.md](SECURITY.md) for how to report a vulnerability privately.

## Roadmap

See [docs/IMPROVEMENT_PLAN.md](docs/IMPROVEMENT_PLAN.md).

## License

No license has been chosen yet. Until one is added, the code is visible but not licensed for reuse. Choosing a license is the first item in the improvement plan.

Dice icons by [Delapouite](https://delapouite.com/) from [game-icons.net](https://game-icons.net), licensed under [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/).
