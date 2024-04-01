# Possible Errors:
**Non-Compliant Input Formats**
- non-numerical characters
- improper separator characters which aren't spaces
- more or less than 3 fields per line
- negative numbers
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

# Implementation

Console:
> Enter name of starting room:
> Enter room connections. Type "done" or "finish" to end the inputs. Type "cancel" to stop the program.

For robustness:
- if an input or logical error is encountered, print a relevant error message to notify the user, and move to next input.  do not modify the map

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEzODQ2NTc3NzldfQ==
-->