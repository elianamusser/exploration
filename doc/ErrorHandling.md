# Possible Errors:
**Non-Compliant Input Formats**
- non-integer characters
- improper separator characters which aren't spaces
- more or less than 3 fields per line
- any negative numbers
- for distances, < 0
 
**Logical Input Errors**
- the same room number repeated twice in one line
- at least one of the given rooms must already be in the building, to connect the new room to 
- building already contains a corridor between the rooms
- no given starting room

**Other Errors**
- no given starting room
- ending input stream without entering any connections - (should this be an error?)
- exceeding a max number of rooms & connections (100?)

# Error Handling Strategy

Robustness will be favored over correctness. 
For robustness:
- if an input or logical error is encountered, print a relevant error message to notify the user, and move to next input without modifying the map
- do not close the program until user specifies to
- if an error is not encountered, parse the string to add the data to the building

## Barricade

I will use a barricade, in between the user input and Building class, for global error handling.
It will throw `IllegalArgumentExceptions` with relevant messages. The error messages will be displayed to the user. They will not shut down the program, but will skip to the next user input without modifying the data.

APP (untrusted) -> BARRICADE -> BUILDING (partially trusted)

The barricade will sanitize data involved in adding rooms and corridors to a building. It will not sanitize data for other operations, such as sorting. 

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

	final int SAFETY_COUNTER <- 100
	int iterations <- 0
	while true:
		var input <- user input
		if input is exit message: 
			print: exit message
			exit loop 	//exit condition 1
		if SAFETY_COUNTER == iterations:
			print: safety counter message
			exit loop	//exit condition 2
		try {
			int[] data <- Barricade::validInputData
			Building::addRoom with data
		} catch (IllegalArgumentException e) {
			print: e message
			skip to next user input (without modifying building)
		}
		
	other operations here?
	print: farewell message

}
```

## Barricade:

```
check user input is valid:
for as many ints as should be in the input:
	if it's a room #, ensure it's a positive integer or 0
	if it's a distance, ensure it's a positive integer
	for start room, i only want one int



protected static int[] validInputData (String userInput) {
	1) check for input errors
	int[] inputData <- Barricade::dataWithoutInputErrors(userInput) 
	
	2) check for logical errors
	boolean 
		either room # < 0: exception
		distance <= 0: exception
		room #s the same: exception
		either room isn't in the building: exception
		the rooms already have a corridor: exception
	reduce code repetition: i'm checking a condition and doing the same if statement
	- make a bunch of intermediate bools?
	- helper?

	assert postconditions:
		every index of inputData is initialized with a valid int
		
	return inputData
}
		

private static int[] dataWithoutInputErrors (String userInput) {
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
- X ❑ Does the routine protect itself from bad input data? 
- X ❑ Have you used assertions to document assumptions, including preconditions and postconditions? 
- X ❑ Have assertions been used only to document conditions that should never occur? 
- X ❑ Does the architecture or high-level design specify a specific set of error handling techniques? 
- X ❑ Does the architecture or high-level design specify whether error handling should favor robustness or correctness? 
- X ❑ Have barricades been created to contain the damaging effect of errors and reduce the amount of code that has to be concerned about error processing? 
- X ❑ Have debugging aids been used in the code? 
- X ❑ Have debugging aids been installed in such a way that they can be activated or deactivated without a great deal of fuss? 
- X ❑ Is the amount of defensive programming code appropriate—neither too much nor too little? 
- X ❑ Have you used offensive-programming techniques to make errors difficult to overlook during development? 

## Exceptions 
- X ❑ Has your project defined a standardized approach to exception handling? 
- X ❑ Have you considered alternatives to using an exception? 
- X ❑ Is the error handled locally rather than throwing a nonlocal exception, if possible? 
- X ❑ Does the code avoid throwing exceptions in constructors and destructors? 
- X ❑ Are all exceptions at the appropriate levels of abstraction for the routines that throw them? 
- X ❑ Does each exception include all relevant exception background information? 
- X ❑ Is the code free of empty catch blocks? (Or if an empty catch block truly is appropriate, is it documented?) 

## Security Issues 
- ❑ Does the code that checks for bad input data check for attempted buffer overflows, SQL injection, HTML injection, integer overflows, and other malicious inputs? 
- ❑ Are all error-return codes checked?
- X ❑ Are all exceptions caught? 
- X ❑ Do error messages avoid providing information that would help an attacker break into the system?
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjAyNDQ4NTkyNiw5NDkyMDcwNzIsNDc0Nz
I2NjQsMTM5NjIyMjk4NywtMzc5MDEzODMzLDEzMjc5NjQ2OTEs
NjkxNjYxNDYzLC02NTIwOTY1NDksLTY1Njc5MjkyNiwtNDMxOT
g2NCwtNTE1OTg3MjY1LDE4NDY1NDM4NDMsMjA3NzU3MjI0NCwt
MTcxODQ5OTAwNSwxMzQ1NDEyNTMyXX0=
-->