# Security policy

## Supported versions

Security fixes go into the latest release only. Please update to the [latest release](https://github.com/vvojtisek/dice_roller/releases/latest) before you report a problem.

## Reporting a vulnerability

Do not open a public issue for security problems. Report them privately through GitHub instead: go to **Security → Report a vulnerability** in this repository ([direct link](https://github.com/vvojtisek/dice_roller/security/advisories/new)).

Please include:

- the app version (Settings → Apps → Dice Roller) and your Android version
- the steps to reproduce the problem, and what impact you expect it to have

You should get a reply within 7 days.

## Automated checks

Every day, [`security.yml`](.github/workflows/security.yml) runs CodeQL, submits the Gradle dependencies for Dependabot alerts, and runs a Gitleaks secret scan. Dependabot also proposes dependency updates every week.
