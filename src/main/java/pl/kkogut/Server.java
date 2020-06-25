package pl.kkogut;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.kkogut.Task.TaskType;

import java.util.*;

import static pl.kkogut.Task.TaskType.*;

public class Server {
    static Map<String, Task> responsesList = new HashMap<>();
    //static List<Task> responsesList = new ArrayList<Task>();

    static void sendCommand(TaskType taskType, String cmd){
        Task task = new Task(taskType, cmd);
        task.sendToDo();

    }
    static public void  getAllResponses(){
        boolean wasResponse = false;

        JSONObject json = APIcaller.getJasonFromResponse(APIcaller.getAllRequests());
        JSONArray tasks = json.getJSONArray("requests");
        for (Object request: tasks){
            JSONObject jso = (JSONObject) request;
            String taskString =  jso.getString("body");
            Task task = new Task(taskString);
            if(task.proceeded){
                responsesList.put(task.ID,task);
                wasResponse = true;
            }
        }
        if (wasResponse){
            APIcaller.deleteAllCalls();
        }
        showResponseList();

    }
    public static void showResponseList(){
        for (Map.Entry<String, Task> entry : responsesList.entrySet()){
            Task task = entry.getValue();
            System.out.println("resp.ID = " + entry.getKey());
            if (task.response!=null) {
                if (task.type == VIEW_SITE) {
                    byte[] valueDecoded = Base64.getDecoder().decode(task.response);
                    String responseString = new String(valueDecoded);
                    FilesOperator.saveFile(responseString, System.currentTimeMillis() + "websiteFromResponse.html", false);
                } else if (task.type == HELLO) {
                    FilesOperator.saveFile(task.response, System.currentTimeMillis() + "Hello.txt", false);
                    System.out.println(task.response);

                } else if (task.type == CMD_RUN) {
                    FilesOperator.saveFile(task.response, System.currentTimeMillis() + "cmdResponse.txt", false);
                    System.out.println(task.response);
                }
            }
            else{
                System.out.println("There was no response content...");
            }
            responsesList.clear();
        }

    }
}
