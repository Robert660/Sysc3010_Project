import os
import toSerial
from socket import *
import sendData
import time


host = ""
port = 13000
buf = 1024
addr = (host, port)
UDPSock = socket(AF_INET, SOCK_DGRAM)
UDPSock.bind(addr)
while True:
    (data, addr) = UDPSock.recvfrom(buf)
    toSerial.run_test(""+data.decode("utf-8"))
    sendData.send("1")
##    if(data == 1):
##        time.sleep(3)
##        sendData.send("0")
##    elif(data == 2):
##        time.sleep(5)
##        sendData.send("0")
##    elif(data == 3):
##        time.sleep(15)
##        sendData.send("0")
UDPSock.close()

