# SecureSystem

[Description]
My SecureSystem begins by initializing mappings and creates the subjects "lyle" and "hal" and objects "lobj" and "hobj" with their respective security levels that cannot change. It then parses the provided file by line. For each line, an InstructionObject is created and uses a Scanner to parse through and break it into components using the following formats.
READ subject_name object_name
WRITE subject_name object_name value
If anything is incorrect or non-existent, a BadInstruction object is created and notifies the user, printing the current status of subjects and objects. If correct, it sends the InstructionObject to the ReferenceMonitor, which compares security labels, and if Simple Security and the *-property are satisfied, it executes the instruction, otherwise, it rejects it. If read is requested, the subject stores the object's value in it's mapping if successful, and 0 otherwise. If write is requested, the subject changes the object's current value to the new value. In either case, it prints out the action done and the new status. 

[Finish]
All of the required aspects of this assignment were implemented in my SecureSystem program. However, I did not have time to complete the extra credit this time around. I may still do it just to reinforce what I learned in the Spring in 439 if I have time this weekend for my own benefit.

[Test Cases]
[Input of test 1]
write hal hobj 
read hal 
write lyle lobj 10
read hal lobj 
write lyle hobj 20
write hal lobj 200
read hal hobj
read lyle lobj
read lyle hobj
foo lyle lobj
Hi lyle, This is hal
The missile launch code is 1234567

[Output of test 1]
Reading from file: instructionList

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

lyle writes value 10 to lobj
The current state is:
   lobj has value: 10
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

hal reads lobj
The current state is:
   lobj has value: 10
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 10

lyle writes value 20 to hobj
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 10

hal writes value 200 to lobj
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 10

hal reads hobj
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 20

lyle reads lobj
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 10
   hal has recently read: 20

lyle reads hobj
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 20

Bad Instruction
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 20

Bad Instruction
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 20

Bad Instruction
The current state is:
   lobj has value: 10
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 20

[Input of test 2]
write hal
write hobj hobj 10
read lobj lobj 
read hal hobj 
write lyle hobj 5

[Output of test 2]
Reading from file: instructionList

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

hal reads hobj
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

lyle writes value 5 to hobj
The current state is:
   lobj has value: 0
   hobj has value: 5
   lyle has recently read: 0
   hal has recently read: 0

[Input of test 3]
read hal hobj
write hal hobj 20
read sal hobj
write hal lobj 15
read lyle lobj
read lyle hobj

[Output of test 3]
Reading from file: instructionList

hal reads hobj
The current state is:
   lobj has value: 0
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

hal writes value 20 to hobj
The current state is:
   lobj has value: 0
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 0
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 0

hal writes value 15 to lobj
The current state is:
   lobj has value: 0
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 0

lyle reads lobj
The current state is:
   lobj has value: 0
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 0

lyle reads hobj
The current state is:
   lobj has value: 0
   hobj has value: 20
   lyle has recently read: 0
   hal has recently read: 0

[Input of test 4]
write lyle lobj 5
read lyle lobj
write lyle hobj 10
read lyle hobj
write hal lobj 20
read subject object
read lale lobj
write lyle lobj 10
write lyle lobj 20
write lyle lobj 30

[Output of test 4]
Reading from file: instructionList

lyle writes value 5 to lobj
The current state is:
   lobj has value: 5
   hobj has value: 0
   lyle has recently read: 0
   hal has recently read: 0

lyle reads lobj
The current state is:
   lobj has value: 5
   hobj has value: 0
   lyle has recently read: 5
   hal has recently read: 0

lyle writes value 10 to hobj
The current state is:
   lobj has value: 5
   hobj has value: 10
   lyle has recently read: 5
   hal has recently read: 0

lyle reads hobj
The current state is:
   lobj has value: 5
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

hal writes value 20 to lobj
The current state is:
   lobj has value: 5
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 5
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

Bad Instruction
The current state is:
   lobj has value: 5
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

lyle writes value 10 to lobj
The current state is:
   lobj has value: 10
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

lyle writes value 20 to lobj
The current state is:
   lobj has value: 20
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0

lyle writes value 30 to lobj
The current state is:
   lobj has value: 30
   hobj has value: 10
   lyle has recently read: 0
   hal has recently read: 0
