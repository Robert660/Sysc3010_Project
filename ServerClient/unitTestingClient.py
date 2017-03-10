import unittest
import UDPClient

def setUpClass(cls):
    print("")

class testClient(unittest.TestCase):
    def testFail(self):
        with self.assertRaises(TypeError):
            UDPClient.setup(None, None, None, None)

    def sendNull(self):        
        self.assertFalse(UDPClient.send(None),False)


    def testPass(self):
        self.assertTrue(UDPClient.setup(8080,8080, '127.0.0.1', '127.0.0.1'),True)     


    
  

if __name__== '__main__':
    setUpClass(testClient)
    unittest.main()
        
        
        
        




