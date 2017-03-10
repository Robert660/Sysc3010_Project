import unittest
import UDPClient

class testSetup(unittest.TestCase):
    
    def testFail(self):
        with self.assertRaises(TypeError):
            UDPClient.setup(None, None, None, None)


    def testPass(self):
        self.assertTrue(UDPClient.setup(1101,1021, 'localhost', 'localhost') != None, None, None, None)

        
class testSend(unittest.TestCase):

    def sendNull():
        
        self.assertFalse(UDPClient.send(None))
        UPDClient.shutdown()       


   # def sendValid():


#class testReceive(unittest.TestCase):

   # def receiveNull():

   # def receiveValid():

        

      
    #def testPass(self):

if __name__== '__main__':
   unittest.main()
        
        
        
        




