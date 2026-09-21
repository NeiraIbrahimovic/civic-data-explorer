# Portfolio maintenance notes

Original algorithms, source layout, contributor records, and commit history are preserved.

## Verified corrections

- Reset loaded readers when `main` is called again, preventing stale datasets from a previous invocation.
- Require both population and vaccination data before offering or dispatching per-capita vaccination calculations.
- Treat end-of-input at the main menu as a normal exit.
- Report missing required COVID CSV columns as an input error.
- Skip malformed or negative vaccination-count rows while retaining valid rows; blank counts retain the original zero default.
- Preserve a final empty CSV field when the last row has no newline.
- Exclude nonfinite property measurements from numeric aggregates.
- Close the JSON input reader after parsing.

## Documentation and tooling

- Added a recruiter-facing README, architecture explanation, data contracts, and a portable launcher.
- Added clearly labeled synthetic inputs; original course datasets are not redistributed.
- Added eight independent regression checks. All eight pass, together with the five existing self-contained vaccination tests.
- Clarified that the legacy health-equity-named calculation uses fully vaccinated counts and does not establish a clinical or policy measure.
- Corrected misleading parser and allocation comments. Detailed new comments explain failure cases, dependency requirements, and cache assumptions.

Other legacy integration tests reference absent files and machine-specific paths. They were not reported as passing. No complete production-readiness claim is made.
