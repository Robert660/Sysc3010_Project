import unittest
import UDPServer
def setUpClass(cls):
    UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1')
class TestServer(unittest.TestCase):
    
     
    #testing setup
    def test_valid(self):
        self.assertTrue(UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1'),False)
        
    def test_invalid(self):
        with self.assertRaises(TypeError):
            UDPServer.setup(None,None,None,None)
    #testing send
    def test_sendNull(self):
        x=UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1')
        self.assertTrue(UDPServer.send("hello",x[1],x[2]))
        
    def test_sendValid(self):
        x=UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1')
        with self.assertRaises(AttributeError):
            UDPServer.send(None,x[1],x[2])
    def test_receiveNull(self):
        x=UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1')
        self.assertFalse(UDPServer.receive(x[0]))

    def test_receiveNull(self):
        x=UDPServer.setup(0,'127.0.0.1',0,'127.0.0.1')
        UDPServer.send("hi",x[1],x[2])
        self.assertTrue(UDPServer.receive(x[0])=="hi")

if __name__ == '__main__':
    setUpClass(TestServer)
    unittest.main()
