import os
import subprocess
from socket import *

def receive(* args):
    host=""
    port= 13001
    buf=1024
    count = 1
    addr=(host,port)
    sock=socket(AF_INET, SOCK_DGRAM)
    sock.bind(addr)

    while True:
        (data,sender)=sock.recvfrom(buf)
        data=data.decode('utf-8')
        print("receiving: "+data)
        if(data > 1):
            if (data=="enable GUI"):
                if(count == 1):
                    proc=subprocess.call(['java', '-jar', 'DesktopGUIView.jar'])
                    count = 0
        else:
            count = 1
                                
    sock.close()
    return data

print(receive())

