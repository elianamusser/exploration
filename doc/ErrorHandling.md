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

**TODO: remove error checking from inner classes**

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

## App:
```
main method {

	Building building;

	print: Intro message
	print: "Enter start room"

	while start room is invalid:
		var start room <- entered starting room
		if invalid: 
			print error message, and repeat loop
		else: 
			initialize building with start room
			exit loop

	print: "Enter room connections"

	while exit message has not been inputted:
		var input <- user input
		if input is exit message: 
			exit loop
		try {
			int[] data <- Barricade::validInputData
			Building::addRoom with data
		} catch IllegalArgumentException {
			skip to next user input (without modifying building)
		}
		
	other operations here?
	print: farewell message

}
```

## Barricade:

```
static void validInputData (String userInput) {
	1) check for input errors
	int[] inputData <- Barricade::dataWithoutInputErrors(userInput) 
	
	2) check for logical errors
	switch(inputData): 
		either room # < 0: exception
		distance <= 0: exception
		room #s the same: exception
		either room isn't in the building: exception
		the rooms already have a corridor: exception

	return inputData
}
		

static int[] dataWithoutInputErrors (String userInput) {
	int[] inputData <- size 3
	iterator iterator <- iterator over userInput as a char array
	int arrIndex <- 0	//the index of inputData to update
	while iterator has next AND arrIndex < 3:	//add data from userInput to inputData array
		char c <- next in iterator
		if c is a space: continue
		try {
			inputData[index] <- parsed int from c
		} catch (exception thrown if c is not an int) {
			throw exception: "inputs must be integers"
		} catch (exception thrown if index out of bounds) {		//checked in while loop, but just in case
			throw exception: "only 3 integers can be inputted"
		}
		arrIndex++
}


```

# Defensive Programming Checklist

CHECKLIST: Defensive Programming 
## General 
❑ Does the routine protect itself from bad input data? 
❑ Have you used assertions to document assumptions, including preconditions and postconditions? 
❑ Have assertions been used only to document conditions that should never occur? 
❑ Does the architecture or high-level design specify a specific set of errorhandling techniques? 
❑ Does the architecture or high-level design specify whether error handling should favor robustness or correctness? 
❑ Have barricades been created to contain the damaging effect of errors and reduce the amount of code that has to be concerned about error processing? 
❑ Have debugging aids been used in the code? 
❑ Have debugging aids been installed in such a way that they can be activated or deactivated without a great deal of fuss? 
❑ Is the amount of defensive programming code appropriate—neither too much nor too little? 
❑ Have you used offensive-programming techniques to make errors difficult to overlook during development? 

## Exceptions 
❑ Has your project defined a standardized approach to exception handling? 
❑ Have you considered alternatives to using an exception? 
❑ Is the error handled locally rather than throwing a nonlocal exception, if possible? 
❑ Does the code avoid throwing exceptions in constructors and destructors? 
❑ Are all exceptions at the appropriate levels of abstraction for the routines that throw them? 
❑ Does each exception include all relevant exception background information? 
❑ Is the code free of empty catch blocks? (Or if an empty catch block truly is appropriate, is it documented?) 
## Security Issues 
❑ Does the code that checks for bad input data check for attempted buffer overflows, SQL injection, HTML injection, integer overflows, and other malicious inputs? 
❑ Are all error-return codes checked?
❑ Are all exceptions caught? 
❑ Do error messages avoid providing information that would help an attacker break into the system?
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE5MDA1MzEzMzEsLTUxNTk4NzI2NSwxOD
Q2NTQzODQzLDIwNzc1NzIyNDQsLTE3MTg0OTkwMDUsMTM0NTQx
MjUzMl19
-->