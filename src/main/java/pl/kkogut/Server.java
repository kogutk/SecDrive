package pl.kkogut;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Server {
    static Map<String, Task> responsesList = new HashMap<>();
    //static List<Task> responsesList = new ArrayList<Task>();

    static void sendCommand(Task.TaskType taskType, String cmd){
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
        showResponseList();
        if (wasResponse){
            APIcaller.deleteAllCalls();
        }
    }
    public static void showResponseList(){
        for (Map.Entry<String, Task> entry : responsesList.entrySet()){
            System.out.println("resp.ID = " + entry.getKey());
            byte[] valueDecoded = Base64.getDecoder().decode(entry.getValue().response);
            String responseString = new String(valueDecoded);
            FilesOperator.saveFile(responseString, System.currentTimeMillis() +"websiteFromResponse.html", false);
        }

    }
}
