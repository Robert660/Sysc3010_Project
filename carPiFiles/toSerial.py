import serial
def run_test(test_number):
    ser = serial.Serial('/dev/ttyACM0',9600)
    string = ""+test_number
    ser.write(test_number)
