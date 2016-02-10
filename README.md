# class-dumper-agent
dumps class files to disk
then you can use jad to decompile

# usage
add the agent parameter as a jvm argument
-javaagent:class-dumper-agent-0.1-SNAPSHOT.jar="(.\*)MyClass(.\*),"C:/Temp/classes/"

paramater 1 is a regexp that matches the full name of the class as obtained with Class.getName()
like : (.\*)MyClass(.\*)

parameter 2 is the path where to put classes 
like : "C:/Temp/classes/"
