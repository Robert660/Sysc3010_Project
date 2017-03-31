import os
from socket import *
import time


def send(case):
    host = "192.168.43.54"
    port = 13000
    addr = (host, port)
    UDPSock = socket(AF_INET, SOCK_DGRAM)
    duration = 5

    #1-5
    #1 - motor on for 3 seconds
    #2 - motor on for 5 seconds
    #3 - montor on for ~15
    #4 - motor always on - NOT RECOMMENDED
    #Please note: motor running will be terminated upon force detection
    data = case
    UDPSock.sendto(data, addr)
    UDPSock.close()

            

    
    

