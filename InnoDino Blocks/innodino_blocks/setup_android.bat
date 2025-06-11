@echo off
echo Setting up Android environment variables...

setx ANDROID_HOME "%LOCALAPPDATA%\Android\Sdk"
setx PATH "%PATH%;%ANDROID_HOME%\platform-tools"
setx PATH "%PATH%;%ANDROID_HOME%\cmdline-tools\latest\bin"

echo Environment variables set successfully!
echo Please restart your command prompt and run 'flutter doctor' again.
pause 