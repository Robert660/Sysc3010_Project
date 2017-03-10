import socket, sys, time

def setup(port1,address1,port2,address2):
    s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address1 = (address1, port1)
    s1.bind(server_address1)

    #input("Press Enter when Client ready")

    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address2 = (address2, port2)
    return s1,s2,server_address2

def receive(s1):
    

    buf, address = s1.recvfrom(2048)
    print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))
    data = buf
    return buf
    
def send(sendData,s2,server_address2):
    data = sendData.strip()
    print ("Sending %s bytes %s: " % (len(data),data ))
    s2.sendto(data.encode('utf-8'), server_address2)
    return True
    
def teardown():
    s1.shutdown(1)
    s2.shutdown(1)

