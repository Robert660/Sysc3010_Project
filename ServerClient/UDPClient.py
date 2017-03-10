import socket, sys, time , unittest


def setup(port1,port2,address1,address2):
    s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address1 = (address1, port1)
    #input("Press Enter when Server ready")
    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address2 = (address2, port2)
    s2.bind(server_address2)

    return s1,s2,server_address1
    

def send(sendData):
    data = sendData.strip()
    s1.sendto(data.encode('utf-8'), server_address1)
    return True

def receive(receiveData):
    print ("Waiting to receive on port %d : press Ctrl-C or Ctrl-Break to stop " % port2)
    buf, address = s2.recvfrom(2048)
    print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))
    return True

def shutdown():
    s1.shutdown(1)
    s2.shutdown(1)

