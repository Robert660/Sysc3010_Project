import UDPClient
y=UDPClient.setup(63546,'127.0.0.1',63545,'127.0.0.1')
while(1):
    t=UDPClient.receive(y[1])
    if(t=="1"):
        print("run test 1")
        UDPClient.send("received type 1",y[0],y[2])
    elif(t=="2"):
        print("run test 2")
        UDPClient.send("received type 2",y[0],y[2])
    elif(t=="3"):
        print("run test 3")
        UDPClient.send("received type 3",y[0],y[2])
    elif(t=="4"):
        print("run test 4")
        UDPClient.send("received type 4",y[0],y[2])
