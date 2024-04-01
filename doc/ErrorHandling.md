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

# Implementation
> Enter name of starting room:
> 


For robustness:
- if an input error is encountered, print an error message to notify the user, and do not modify the map
- 
<!--stackedit_data:
eyJoaXN0b3J5IjpbODIwMzc3MDkzXX0=
-->