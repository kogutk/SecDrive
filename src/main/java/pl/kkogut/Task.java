package pl.kkogut;

import org.json.JSONObject;
import com.google.gson.Gson;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.UUID;
import java.util.Base64;


public class Task {
    String ID = null;
    TaskType type;
    String cmd; //command
    String response;
    boolean proceeded;
    enum TaskType{VIEW_SITE, GET_IP, SEND_FILES};

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
        try {
            if (type == TaskType.VIEW_SITE) {
                File webPageFile = getWebPage(cmd);
            } else {
                response = "nothing done";
            }
            proceeded = true;
            sendResponse();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    void sendResponse(){
        APIcaller.post(this.toJson());
    }
    File getWebPage(String websiteAdress){
        try {
            URL url = new URL(websiteAdress);
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
