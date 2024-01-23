#!/bin/bash

# Run a command within ADB shell to check if the device is rooted
adb_root_status=$(adb shell whoami)

# Check the result
if [ "$adb_root_status" != "root" ]; then
    echo "Device is not rooted, trying to get root"
    adb root
    sleep 2
fi

echo "Device is rooted. Changing time to 11:00"
adb shell date 112510592020.58 ; adb shell am broadcast -a android.intent.action.TIME_SET
