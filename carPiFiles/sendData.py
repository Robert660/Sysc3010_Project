# Save as client.py 
# Message Sender
import os
from socket import *

def send(string):
    host = "192.168.43.160" # set to IP address of target computer
    port = 13000
    addr = (host, port)
    UDPSock = socket(AF_INET, SOCK_DGRAM)
    data = string
    bytes(data) 
    UDPSock.sendto(data, addr)
    UDPSock.close()

