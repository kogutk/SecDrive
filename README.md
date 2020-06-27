# SecDrive
Program works as an example of malware and presenting how botnet application could work. It is a student project and should be use only for Information Networks Security learning purposes.
## How it works
Program runs as "bot" or as "server".
At start Bot copy itself to Windows system autostart location and some archive location.
If drive is mounted it copy txt and log files to specified in code archive location.

Then Bot is getting commands from server, processing it and sending responses. Server is sending commands and getting responses from bots.

Commands and responses:
* say - send a message and waiting for response
    * hello - get a user name, IP address, and Device name from bot.

* viewSite - bot open a web page and send back a page content
    * default - open "https://www.myip.com/"
    * websiteAddress - open given web page

* cmdRun - bot run a cmd command on its Windows system
    * command - command to run
    
Program uses https://rbaskets.in/ basket to send commands and responses.

## How to run it
Simple run Jar file with argument "bot".
After that it is replicating itselft so only run.bat file needs to be run.

On the server site run jar file with parameters:
```
java -jar JarFile.jar server_send say message

java -jar JarFile.jar server_send viewSite default

java -jar JarFile.jar server_send viewSite webAddress

java -jar JarFile.jar server_send cmdRun command
```
### Examples:
```
java -jar JarFile.jar server_send say hello

java -jar JarFile.jar server_send viewSite default

java -jar JarFile.jar server_send viewSite "https://amazon.com"

java -jar JarFile.jar server_send cmdRun "cd C:\ & dir"
```
