# Save as server.py 
# Message Receiver
import os
from socket import *

def recieve(*args):
    
    host = ""
    port = 13000
    buf = 1024
    addr = (host, port)
    UDPSock = socket(AF_INET, SOCK_DGRAM)
    UDPSock.bind(addr)
    #print "Waiting to receive messages..."
    while True:
        (data, addr) = UDPSock.recvfrom(buf)
        #print "Received message: " + data
        if data == "1":
            ack = 1
            break
        if data == "0":
            ack = 0
            break
    UDPSock.close()

    
    return ack

    
