# DSA III (SCS2201) - Assignment 1 (String matching)

## Question 1 - Wildcard matching

Matches given pattern with multiple single character(```_```) wild cards using naive string matching.

### Explanation

Naive algorithm is used because of simplicity of the problem. Thus algorithms such as Karp/Rabin algorithm adds unnecessary complexity and needs rehashing for already hashed letters in order to match wildcards.



### How to run

Move to ```question1``` directory, compile and run Wildcard.java with arguments

```bash
cd question1
javac Wildcard.java
java Wildcard <text_file> <pattern_file> <output_file>
# <text_file> - REQUIRED Text which contains the pattern
# <pattern_file> - REQUIRED Pattern that will be searched in the given text
# <output_file> - OPTIONAL Output file to write the output

# TEST - java Wildcard text.txt pattern.txt output.txt
```

---

## Question 2 - DNA matching

Matches given multiple DNA queries with multiple DNA database using Karp/Rabin string matching algorithm.

## Explanation

DNA sequences can get very long and needs more efficent algorithm than naive bruteforce algorithm. Therefore solution for this question is implemented using Karp/Rabin algorithm(A depreciated naive algorithm function is also implemented alongside to check the correctness of the algorithm). Rolling hash was calculated using the RollingHash class.

### How to run

Move to ```question2``` directory, compile and run Wildcard.java with arguments

```bash
cd question2
javac DNA.java
java DNA <database_file> <query_file> <output_file>
# <database_file> - REQUIRED File which contains the DNA database
# <query_file> - REQUIRED File that contains queries to be matched with the database
# <output_file> - OPTIONAL Output file to write the output

# TEST - java DNA database.txt query.txt output.txt
```

---

## Team 

### Number - 16

* U U Kumarasinghe - 17000912
* M I Lakshan - 17000939
* O Lelum - 17000955

