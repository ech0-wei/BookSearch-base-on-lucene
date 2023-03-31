Running Environment: Java Runtime Environment
Development Tool: IntelliJ IDEA

How to compile and run our programs:
First the user needs to change the file path to get the data folder and Index folder.
For example, in IntelliJ the path would look something like
C:\\Users\\usernamehere\\IdeaProjects\\BookSearchNew\\data
C:\\Users\\usernamehere\\IdeaProjects\\BookSearchNew\\Index
To do this in IntelliJ, the user can click on Run on the top left, then click on Edit Configurations..
From there there is a textbox that where the user can edit the file paths to match the location of the data and Index folder in their system.
Click Apply then OK for this to go through. This will make sure the program runs without error.

To get started with our program, the user can open our program through IntelliJ IDEA or their preferred IDE and open our BookSearchNew folder project.
The implementation of this program works through our Main.java class. The program starts by first deleting all the index files and clearing our index folder through deleteFile.
Then the program reads the stopwords.txt file to get a list of the stop words to increase the efficiency of our program. 
Then it starts building the index from scratch with the help of Indexer.java class.

From here the user is prompted with the following text
============Search the books============

"Enter the book id: "
The user is first prompted to enter the book id of the topic. In our case, the ID entered can be the number 401 - 420.

"Enter the keywords: "
The user is then prompted to enter keywords. To get the keywords from our program we look at the topics and enter the description.
For example for topic number 401, the keywords would be the following:
What language and cultural differences impede the integration of foreign minorities in Germany?

"Enter the number of results: "
The user is finally prompted to enter the number of results, this can be any number great or equal to 1. 
For example if we were to generate 5 results for topic 401, we will get the following output:
============Search the books============
Enter the book id: 401
Enter the keywords: What language and cultural differences impede the integration of foreign minorities in Germany?
Enter the number of results: 5
KEYWORDS:what KEYWORDS:language KEYWORDS:cultural KEYWORDS:differences KEYWORDS:impede KEYWORDS:integration KEYWORDS:foreign KEYWORDS:minorities KEYWORDS:germany
401 Q0 WT02-B12-220 1 15.707073211669922 GRP3
401 Q0 WT02-B12-219 2 15.535935401916504 GRP3
401 Q0 WT02-B12-218 3 11.447311401367188 GRP3
401 Q0 WT02-B20-84  4 11.058934211730957 GRP3
401 Q0 WT02-B13-45  5 10.728760719299316 GRP3

