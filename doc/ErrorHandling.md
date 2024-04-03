# Possible Errors:
**Non-Compliant Input Formats**
- non-integer characters
- improper separator characters which aren't spaces
- more or less than 3 fields per line
- any negative numbers
- for distances, < 0

**Logical Input Errors**
- unreachable connections
- the same room number repeated twice in one line
- at least one of the given rooms must already be in the building, to connect the new room to 
- no given starting room

**Other Errors**
- no given starting room
- ending input stream without entering any connections - (should this be an error?)
- exceeding a max number of rooms & connections (100?)

# Requirements

For robustness:
- input must be in form: int + space + int + space + int
- if an input or logical error is encountered, print a relevant error message to notify the user, and move to next input without modifying the map
- if an error is not encountered, parse the string to add the data to the building
- do not close the program until user specifies to

**TODO: Decision diagram here**

**TODO: Barricade diagram here**

**TODO: reference 8-2 and chp 8 checklist**


# Error Handling Strategy

**Barricade** 
- pros
	- centralized error handling -> consistency
	- reducing repeated code
	- methods can return the "sanitized" value
- cons
	- tight coupling
	- since it's in a separate class, the error handling may not be clear from the main method
	- methods will return the value, not a status variable

Global error handler
- pros
	- consistency
	- can return status variable
- cons
	- tight coupling
	- barricade might make the code more concise, by returning the correct values

Local error handler:
- pros
	- no coupling with other type
- cons
	- lots of repeated code

I will use one Barricade class. Backup plan will be a global error handler with methods that return status variables.

# Pseudocode

APP:
``
Building building;

print: Intro message
print: "Enter start room"

while start room is invalid:
	var start room <- entered starting room
	if (invalid): 
		print error message, and repeat loop
	else: 
		initialize building with start room
		exit loop

print: "Enter 

<!--stackedit_data:
eyJoaXN0b3J5IjpbMzMzMTg3MDkyLDEzNDU0MTI1MzJdfQ==
-->