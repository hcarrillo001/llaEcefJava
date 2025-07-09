
# LlaEcef

## Authors 
    Hanns Carrillo 

Tasks
Read the 

    • CSV file
    • Convert LLA to ECEF
    • Calculate the ECEF velocity at the time points given in the input file
    • Interpolate ECEF velocity for any requested time
  
NOTE: A LOT OF DEBUG COMMENTS/PRINT STATEMENTS WERE LEFT
TO SHOW DEVELOPMENT PROCESS, USING MOCK DATA TO TEST WHILE
PROGRAMMING

Sources 


Java SDK
    
     Oracle OpenJDK 1.8.0

Java Version

    java version "1.8.0_101"
    Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
    Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13,mixed mode)    


How to run on linux machine

    NOTE: This program was developed in a Windows machine. 
    Sending this as a zip file

    Setup Java in a linux (UBUNTU) 
      1. Open terminal run the following command 
           java -version
        Output 
            java version "1.8.0_xxx" 
        Note: Newer Version of java should work
      2. Update Java (if needed) run the following commands 
            sudo apt update
            sudo apt install openjdk-8-jdk
      3. If you want to default to Java 8 you might need to 
      your JAVA_HOME to the correct path 
            1. Take note of your Java 8 path
            eg. /usr/lib/jvm/java-8-open-jdk-amd64
            2. Edit running
                   vi ~/.bashrc file
            3. At the very bottom add the following text
                 1. To edit esc then if i (option)
                 2. export JAVA_HOME=/usr/lib/jvm/java-8-open-jdk-amd64
                 3. PATH=$JAVA_HOME/bin:$PATH
                 4. Save file 
                      esc (click) :wq!   (then enter)
            4. Confirmed it worked run the command in the terminal
                    Command: java -version 
                    Output : java version "1.8.0_xxx"
                    Command: echo $JAVA_HOME
                    Output: /usr/lib/jvm/java-8-open-jdk-amd64

Run the java .zip file
      
    1. Place the .zip file in your home directory
        scp llaecef.zip user@linux_host:/home/user
    2. Unzip file 
        unzip llaecef.zip -d llaecef
    3. Change into the program directory
        cd llaecef
    4. List files
        ls -ltr
    5. Look for 
        1. A .jar file (example: llaecf.jar)
        2. A main class (Coordinate Controller) 0r package lder 
        (com/example/Main.class, this does not have this dir)
        3. A lib with dependencies  
    6. Multiple ways to run 
        1. If there is a .jar file
           java -jar llaecef.jar
        2. From Main class (CoordinateController)
           java Coordinate Controller
        3. From the package
           java com.example.llaecef.CoordinateController

    If there is dependencies in a lib/ dir
      run: java -cp "/lib/*" com.example.llaecef.CoordinateController
                
            