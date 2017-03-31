#!/bin/sh
#launcher.sh
#navigate to home directory, then to this directory, then execute the pyhton script, then go back home

cd /
cd home/pi/Desktop/projectfolder/
sudo fuser -k 13000/udp
sudo python ./recieveData.py
cd /
