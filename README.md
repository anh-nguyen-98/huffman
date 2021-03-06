# Data Compression Software

There are conceptually two programs: one to compress ("huff") and the other to uncompress ("unhuff") files that are compressed by the first program.

## Installation

## Usage 
  **a. Compress file:** 
1. Run ***main()** method from ***Huff.java*** file.
2. Enter the name the name of the file to be huffed (*.txt*)
3. Get coded file 

For standard version: there are 2 output files (*.code* & *.huff* )
For advanced version: there is only 1 output binary file (*.huff*)

**b. Decompress file:**

1. Run ***main()*** method from ***Unhuff*** file
2. Enter the name the name of the file to be unhuffed (*.huff*)
3. Get the decoded file (*.unhuff*).



The Huff program calls the ***write()*** method inside ***HuffmanTree*** to print the generated
encoding to a ***.code*** file. This ***.code*** file is used by ***Unhuff*** when uncompressing a file. This
is unwieldy when you use a utility like zip, it doesn't produce two output files (a ***.code*** file
and the compressed binary file). It just produces one. For the **advanced** version, the ***.code*** file generation is eliminated. 


## Authors:

Stuart Reges, Raghu, Nguyen Hoang Nam Anh 

## License: 
