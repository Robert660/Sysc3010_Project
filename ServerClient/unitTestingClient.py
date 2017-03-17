import unittest
import UDPClient, UDPServer



class testClient(unittest.TestCase):
    #test invalid setup semantics
    def test_SetupInvalid(self):
        with self.assertRaises(TypeError):
            UDPClient.setup(None, None, None, None)

    def test_SetupValid(self):
        self.assertTrue(UDPClient.setup(8080, '127.0.0.1',8081, '127.0.0.1'),True)     

    def test_SendValid(self):
        x= UDPClient.setup(8082, '127.0.0.1',8083, '127.0.0.1')
        self.assertTrue(UDPClient.send("hello",x[0],x[2]))

    def test_SendInvalid(self):
        x= UDPClient.setup(8084, '127.0.0.1',8085, '127.0.0.1')
        with self.assertRaises(AttributeError):
            UDPClient.send(None,x[0],x[2])

    def test_RecieveValid(self):
        x= UDPClient.setup(8086, '127.0.0.1',8087, '127.0.0.1')
        self.assertFalse(UDPClient.receive(x[1]))

    def test_ReceiveValid(self):
        x= UDPClient.setup(8088, '127.0.0.1',8089, '127.0.0.1')
        y= UDPServer.setup(8088, '127.0.0.1',8089, '127.0.0.1')
        UDPServer.send("hi", y[1],y[3])
        msg=UDPClient.receive(x[1])
        print(msg)
        self.assertTrue(msg=="hi")
        
if __name__== '__main__':
    unittest.main()
        
        
        
        




