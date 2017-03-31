import os
from socket import *

def send(msg):
    host="192.168.43.9"
    port=13005
    addr=(host, port)
    sock=socket(AF_INET, SOCK_DGRAM)
    msg= msg.encode('utf-8')
    sock.sendto(msg,addr)
    sock.close()


