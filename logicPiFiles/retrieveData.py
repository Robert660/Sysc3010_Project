
# view_rows.py - Fetch and display the rows from a MySQL database query # import the MySQLdb and sys modules
import MySQLdb as mdb
import sys
import time
import warnings
import sendToServer as server
warnings.filterwarnings("ignore")

def decide():
    go = True
    #store the current row
    current_row = 1
    hi_reading = [0,0,0,0]
    closed = False
    count = 0
    event = 0
    # open a database connection
    # although this is creating a connection its main purpose it to get the table which is why this is within the while loop
    connection = mdb.connect ('192.168.43.54','fooUser','1234','projectdb');
    # prepare a cursor object using cursor() method
    cursor = connection.cursor ()
    
    prev = 0
    runs = 0

    while(go):
        
        
        # execute the SQL query using execute() method.
        cursor.execute ("select * FROM sensordata WHERE id = %s",current_row)
        # fetch all of the rows from the query
        data = cursor.fetchone ()
        #check if the database has new entry at the next row
        row_count = cursor.rowcount

        # print the rows
        if row_count > 0:
            #print ("Highest: G force:"+repr(hi_reading[1])+", Force:"+ repr(hi_reading[2])+", RPS:"+ repr(hi_reading[3]))
            #print ("Current: G force:"+repr(data[1])+", Force:"+ repr(data[2])+", RPS:"+ repr(data[3]))
            #go to the next row
            current_row += 1
            
            #store highest reading
            if(data[1]>hi_reading[1]):
                hi_reading[1]=data[1]
            if(data[2]>hi_reading[2]):
                hi_reading[2]=data[2]
            if(data[3]>hi_reading[3]):
                hi_reading[3]=data[3]
                
            #decision making and sending server scenarios
            if(data[1]>1.0 or (data[2]>1.0)):
                #print("G: minor")
                prev = event
                event = 2
            elif(data[1]>=3.0 or data[2]>=3.0):
                #print("G: major")
                prev = event
                event = 3
            else:
                #print("G: no injury")
                prev = event
                event = 1


            if(event != prev and event > prev and event != 0):
               
                cursor = connection.cursor ()
                # truncate query
                cursor.execute('truncate events')
                #input new query
                cursor.execute("""INSERT INTO events VALUES('',%s)""",(event))
                connection.commit()
              
            
            
            
        elif runs == 0:
            # close the cursor object
            cursor.close ()
            # close the connection
            connection.close ()
            # open a database connection
            # although this is creating a connection its main purpose it to get the table which is why this is within the while loop
            connection = mdb.connect ('192.168.43.54','fooUser','1234','projectdb');
            # prepare a cursor object using cursor() method
            cursor = connection.cursor ()
            runs = 1

        else:
            break
    server.send(str(event))
    return event

        
    # exit the program

