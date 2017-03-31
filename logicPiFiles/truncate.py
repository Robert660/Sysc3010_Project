import MySQLdb as mdb
import sys
import time

def trunk():
    connection = mdb.connect ('192.168.43.54','fooUser','1234','projectdb');
    # prepare a cursor object using cursor() method
    cursor = connection.cursor ()

    # truncate query

    cursor.execute('truncate sensordata')

    connection.commit()
    connection.close()
    #print "table truncated"
