# Source: https://pymotw.com/2/socket/udp.html

import socket, sys, time
port = 1001

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

server_address = ('localhost', port)

while 1:
    print ("Enter data to transmit: ENTER to quit")
    data = sys.stdin.readline().strip()
    if not len(data):
        break
#    s.sendall(data.encode('utf-8'))
    s.sendto(data.encode('utf-8'), server_address)

s.shutdown(1)

