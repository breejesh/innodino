@echo off
echo.
echo ===========================================
echo   InnoDino Blocks Android - Build Check
echo ===========================================
echo.

echo Checking project structure...
if exist "app\src\main\java\com\innodino\blocks" (
    echo ✓ Source directory exists
) else (
    echo ✗ Source directory missing
    goto error
)

if exist "app\src\main\res\values" (
    echo ✓ Resources directory exists
) else (
    echo ✗ Resources directory missing
    goto error
)

if exist "app\build.gradle" (
    echo ✓ App build.gradle exists
) else (
    echo ✗ App build.gradle missing
    goto error
)

if exist "gradle\wrapper\gradle-wrapper.properties" (
    echo ✓ Gradle wrapper exists
) else (
    echo ✗ Gradle wrapper missing
    goto error
)

echo.
echo Checking Kotlin source files...
if exist "app\src\main\java\com\innodino\blocks\InodinoBlocksApplication.kt" (
    echo ✓ Application class exists
) else (
    echo ✗ Application class missing
    goto error
)

if exist "app\src\main\java\com\innodino\blocks\ui\main\MainActivity.kt" (
    echo ✓ MainActivity exists
) else (
    echo ✗ MainActivity missing
    goto error
)

if exist "app\src\main\java\com\innodino\blocks\ui\led\LEDCrystalActivity.kt" (
    echo ✓ LED module activity exists
) else (
    echo ✗ LED module activity missing
    goto error
)

if exist "app\src\main\java\com\innodino\blocks\ui\dinobot\DinobotExpeditionActivity.kt" (
    echo ✓ DinoBot module activity exists
) else (
    echo ✗ DinoBot module activity missing
    goto error
)

echo.
echo ===========================================
echo   ✓ All checks passed! Project is ready.
echo ===========================================
echo.
echo Next steps:
echo 1. Open in Android Studio
echo 2. Sync with Gradle files
echo 3. Run on device/emulator
echo.
goto end

:error
echo.
echo ===========================================
echo   ✗ Project setup incomplete!
echo ===========================================
echo.
echo Please check missing files and try again.
echo.

:end
pause
