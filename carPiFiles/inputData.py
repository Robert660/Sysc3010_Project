import serial
import time
import MySQLdb as mdb
import warnings
warnings.filterwarnings("ignore")

#create connection to the serial port of the arduino uno
arduino = serial.Serial("/dev/ttyACM0")
arduino.baudrate = 9600
go = 1

while (go == 1):
    #retrieve data from the arduino serial connection
    data = arduino.readline()
    time.sleep(0.2)
    data = arduino.readline()
    #deliminiter of tab
    pieces = data.split("\t")
    print pieces
    #data is sent over as [acceleration(G's), force(newtons), rps]
    if len(pieces)==4:
        acceleration = pieces[0]
        force = pieces[1]
        rps = pieces[2]


        #connect to the database 
        con = mdb.connect('localhost','root','1234','projectdb');

        with con:
            cursor = con.cursor()

            #insert values into the sensordata of the database
            #  %s denotes that a value will be printed into the tables column
            # first column does not get anyhing as the mySQL database auto-increments that column
            cursor.execute("""INSERT INTO sensordata VALUES('',%s,%s,%s)""",(acceleration,force,rps))
            #commit the data much like git
            con.commit()
            #exit the database
            cursor.close()

    
