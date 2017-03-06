

import socket, sys, time

port1 = 1001
s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address1 = ('localhost', port1)

#input("Press Enter when Server ready")

port2 = 1000
s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address2 = ('localhost', port2)
s2.bind(server_address2)



while 1:
    print ("Enter data to transmit: ENTER to quit")
    data = sys.stdin.readline().strip()
    if not len(data):
        break
    s1.sendto(data.encode('utf-8'), server_address1)

    print ("Waiting to receive on port %d : press Ctrl-C or Ctrl-Break to stop " % port2)

    buf, address = s2.recvfrom(2048)
    if not len(buf):
        break
    print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))

s1.shutdown(1)
s2.shutdown(1)

