

import socket, sys, time

s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port1 = 1001
server_address1 = ('localhost', port1)
s1.bind(server_address1)

input("Press Enter when Client ready")

s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port2 = 1000
server_address2 = ('localhost', port2)


while True:

    print ("Waiting to receive on port %d : press Ctrl-C or Ctrl-Break to stop " % port1)

    buf, address = s1.recvfrom(2048)
    if not len(buf):
        break
    print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))

    print ("Enter data to transmit: ENTER to quit")
    data = sys.stdin.readline().strip()
    if not len(data):
        break
    s2.sendto(data.encode('utf-8'), server_address2)

s1.shutdown(1)
s2.shutdown(1)
