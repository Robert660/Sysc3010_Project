import Tkinter as tk
import sendData
import time
import recieveData
import truncate
import retrieveData

root = tk.Tk()

def myfunction(*args):
    x = var.get()
    y = stringvar1.get()
    z = stringvar2.get()
    if x and y and z:
        button.config(state='normal')
    else:
        button.config(state='disabled')

def disable(*args):
    r1.config(state='disabled')
    r2.config(state='disabled')
    r3.config(state='disabled')

def enable(*args):
    r1.config(state='normal')
    r2.config(state='normal')
    r3.config(state='normal')

def sel(*args):
    textArea.delete(0.0,"end")
    textArea.insert(0.0,"Test " +str(buttonVar.get())+ " Selected\n")

def disableAll(*args):
    r1.config(state='disabled')
    r2.config(state='disabled')
    r3.config(state='disabled')
    button2.config(state='disabled')
    button1.config(state='disabled', text="running")
    textArea.insert('insert',"Test " +str(buttonVar.get())+ " Running\n")
    sendData.send(str(buttonVar.get()))
    run = 0
    while(run == 0):

        if recieveData.recieve()== 1:
            run = 1
            ran()
        

def ran(*args):
    textArea.insert('insert',"Recieved Test Signal\n")
    textArea.insert('insert',"Press Reset Upon Test completion\n")
    #decision.config(state = 'normal')
    finish.set(1)
    

def showDecision(*args):
    #output the scenario which occured
    #print event
    textArea.insert('insert',"Scenario Being Processed\n")
    test.set(1)
    event.set(retrieveData.decide())

def poll(*args):
    #print "hi"
    #print event.get()
    if(event.get() == 1):
        textArea.insert('insert',"No Injory Occured\n")
    elif(event.get() == 2):
        textArea.insert('insert',"Minor Incident Occured\n")
    elif(event.get() == 3):
        textArea.insert('insert',"Major Incident Occured\n")
    elif(event.get() == 0):
        event.set(retrieveData.decide())
    reset.config(state = 'normal')

def sleep(*args):
    time.sleep(5)

def testComplete(*args):
    textArea.delete(0.0,"end")
    r1.config(state='normal')
    r2.config(state='normal')
    r3.config(state='normal')
    button2.config(state='normal')
    button1.config(state='normal', text="submit")
    remove.set(1)
    ###########################
    #reset doesn't allow for multiple uses
    #event being set to 0
    reset.config(state = 'disabled')
    root.destroy()
    

def trunc(*args):
    truncate.trunk()

    
stringvar1 = tk.StringVar(root)
stringvar2 = tk.StringVar(root)
var = tk.StringVar(root)
buttonVar = tk.IntVar(root)
ack = tk.IntVar(root)
wait = tk.IntVar(root)
finish = tk.IntVar(root)
remove = tk.IntVar(root)
event = tk.IntVar(root)
test = tk.IntVar(root)


stringvar1.trace("w", myfunction)
stringvar2.trace("w", myfunction)
var.trace("w", myfunction)
buttonVar.trace("w",disable)
wait.trace("w",ran)
finish.trace("w",showDecision)
remove.trace("w", trunc)
event.trace("w",poll)
test.trace("w",sleep) 

label = tk.Label(root, text = "Please Select a Test Scenario:")
label.grid(row=1,column=1)

r1 = tk.Radiobutton(root, text ="Test 1",variable=buttonVar,value=1,command=sel)
r1.grid(row=2,column=1)

r2 =  tk.Radiobutton(root,text = "Test 2" ,variable=buttonVar,value=2,command=sel)
r2.grid(row=3,column=1)

r3 =  tk.Radiobutton(root,text = "Test 3" ,variable=buttonVar,value=3,command=sel)
r3.grid(row=4,column=1)

button1 = tk.Button(root,text="submit",command=disableAll)
button1.grid(row=5, column=1)
button2 = tk.Button(root,text="cancel",command = enable)
button2.grid(row=6, column=1)

textArea = tk.Text()
textArea.grid(column=1, row=7)

##decision = tk.Button(root, text = "Process", command = showDecision, state = 'disabled')
##decision.grid(column=1, row=8)

reset = tk.Button(root, text = "Finish", command=testComplete, state='disabled')
reset.grid(column=1,row=8)



root.title("Test Scenario Interface")
root.minsize(width=567,height=505)
root.maxsize(width=567,height=505)
root.mainloop()

