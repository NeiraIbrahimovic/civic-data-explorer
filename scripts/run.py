"""Compile/run the original Java package without requiring an IDE.

Use --test PATH_TO_JUNIT_CONSOLE_JAR for the portable regression selection.
The legacy dataset-dependent tests still require their original local inputs.
"""
from pathlib import Path
import os
import re
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[1]
BUILD = ROOT / "build/classes"
DEPENDENCY = ROOT / "json-simple-1.1.1.jar"


def run():
    args = sys.argv[1:]
    testing = bool(args and args[0] == "--test")
    if testing and len(args) != 2:
        raise SystemExit("Usage: python scripts/run.py --test PATH_TO_JUNIT_CONSOLE_JAR")
    main = next((ROOT / "src").rglob("Main.java"))
    # Read the existing package declaration; do not rewrite the original API.
    package = re.search(r"^package\s+([\w.]+);", main.read_text(), re.MULTILINE).group(1)
    sources = sorted((ROOT / "src").rglob("*.java"))
    if not testing:
        sources = [path for path in sources if "testing" not in path.parts]
    classpath = str(DEPENDENCY)
    junit = None
    if testing:
        junit = Path(args[1]).resolve()
        if not junit.is_file():
            raise SystemExit(f"JUnit console jar not found: {junit}")
        classpath += os.pathsep + str(junit)
    BUILD.mkdir(parents=True, exist_ok=True)
    subprocess.run(["javac", "-encoding", "UTF-8", "-cp", classpath, "-d", str(BUILD),
                    *map(str, sources)], check=True, cwd=ROOT)
    runtime = str(BUILD) + os.pathsep + str(DEPENDENCY)
    if testing:
        command = ["java", "-jar", str(junit), "execute", "--class-path", runtime,
                   "--select-class", package + ".testing.PortfolioRegressionTest",
                   "--select-class", package + ".testing.ProcessorVaccinationStatsTest",
                   "--disable-banner", "--details=summary"]
    else:
        command = ["java", "-cp", runtime, package + ".Main", *args]
    subprocess.run(command, check=True, cwd=ROOT)


if __name__ == "__main__":
    run()
