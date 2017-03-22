import UDPServer
x=UDPServer.setup(63546,'127.0.0.1',63545,'127.0.0.1')
while(1):
    t=input("type to client")
    UDPServer.send(t,x[1],x[3])
    print(UDPServer.receive(x[0]))
