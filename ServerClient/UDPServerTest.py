import unittest
import UDPServer
import UDPClient
class TestServer(unittest.TestCase):
    
     
    #testing setup
    def test_valid(self):
        self.assertTrue(UDPServer.setup(64014,'127.0.0.1',64105,'127.0.0.1'),False)
        
    def test_invalid(self):
        with self.assertRaises(TypeError):
            UDPServer.setup(None,None,None,None)
    #testing send
    def test_sendValid(self):
        x=UDPServer.setup(64106,'127.0.0.1',64107,'127.0.0.1')
        self.assertTrue(UDPServer.send("hello",x[1],x[3]))
        
    def test_sendNull(self):
        x=UDPServer.setup(64108,'127.0.0.1',64109,'127.0.0.1')
        with self.assertRaises(AttributeError):
            UDPServer.send(None,x[1],x[3])
            
    def test_receiveNull(self):
        x=UDPServer.setup(64110,'127.0.0.1',64111,'127.0.0.1')
        self.assertFalse(UDPServer.receive(x[0]))

    def test_receiveValid(self):
        x=UDPServer.setup(64112,'127.0.0.1',64113,'127.0.0.1')
        y=UDPClient.setup(64112,'127.0.0.1',64113,'127.0.0.1')
        UDPClient.send("hi",y[0],y[2])
        msg=UDPServer.receive(x[0])
        print(msg)
        self.assertTrue(msg=="hi")

if __name__ == '__main__':
    unittest.main()
