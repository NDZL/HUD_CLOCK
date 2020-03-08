@echo off
setlocal

set OUTPUT=%1
set HOME=%cd%
set SCRIPTHOME=%~dp0

set OUTPUTZIP=%OUTPUT%\DemoClock.zip
set OUTPUTAPK=%OUTPUT%\DemoClock.apk
set APKRELEASE=.\app\build\outputs\apk\release\app-release.apk
set APKDEBUG=.\app\build\outputs\apk\debug\app-debug.apk

REM if output dir exists then build RELEASE version
if not ""=="%OUTPUT%" (
  if not exist "%OUTPUT%" goto _NEEDOUTPUTDIR
  
  cd %SCRIPTHOME%

  REM clean and zip up the demo code
  @echo call gradlew clean
  call gradlew clean
  
  @echo call "%JAVA_HOME%\bin\jar" -cfvM %OUTPUTZIP% @manifest.txt
  call "%JAVA_HOME%\bin\jar" -cfvM %OUTPUTZIP% @manifest.txt
  
  @echo call gradlew assembleRelease
  call gradlew assembleRelease

  if not exist %APKRELEASE% goto ERROR_FAILED_TO_BUILD
  
  @echo copy /y %APKRELEASE% %OUTPUTAPK%
  copy /y %APKRELEASE% %OUTPUTAPK%
) else (
  @echo call gradlew assembleDebug
  call gradlew assembleDebug

  if not exist %APKDEBUG% goto ERROR_FAILED_TO_BUILD
)

goto SUCCESS

:ERROR_FAILED_TO_BUILD
cd %HOME%
@echo Build failed
endlocal
exit /B 1

:_NEEDOUTPUTDIR
@echo ERROR: output directory not found: %OUTPUT%
endlocal
exit /B 1

:SUCCESS
cd %HOME%
@echo Build complete
endlocal
