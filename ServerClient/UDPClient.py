import socket, sys, time, select


def setup(port1,address1,port2,address2):
    s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s1.setblocking(0)
    server_address1 = (address1, port1)
    #input("Press Enter when Server ready")
    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s2.setblocking(0)
    server_address2 = (address2, port2)
    s2.bind(server_address2)
    print(s2.getsockname())

    return s1,s2,server_address1,server_address2
    

def send(sendData,s1,server_address1):
    data = sendData.strip()
    s1.sendto(data.encode('utf-8'), server_address1)
    return True

def receive(s2):
    msg=select.select([s2],[],[],5)
    if msg[0]:
        buf, address = s2.recvfrom(2048)
        print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))
        data=buf.decode('utf-8')
    else:
        print("Receive Timeout")
        data=False
    return data

def shutdown(s1,s2):
    s1.shutdown(1)
    s2.shutdown(1)
