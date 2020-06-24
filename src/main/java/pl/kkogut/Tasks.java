package pl.kkogut;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tasks {
    static Map<String, Task> taskList = new HashMap<>();

    static public void  getAllRequests(){
        Map<String,Task> allRequests = new HashMap<>();

        JSONObject json = APIcaller.getJasonFromResponse(APIcaller.getAllRequests());
        JSONArray tasks = json.getJSONArray("requests");

        for (Object request: tasks){
            JSONObject jso = (JSONObject) request;
            String jss =  jso.getString("body");
            Task task = new Task(jss);
            if(!taskList.containsKey(task.ID)){
                taskList.put(task.ID, task);
            }
        }

    }
    private static void proceed() {
        for(Map.Entry<String, Task> entry : taskList.entrySet()){
            if(entry.getValue().proceeded==false){
                System.out.println("Proceed task ID = " + entry.getKey());
                boolean done = entry.getValue().proceed();
                if(done) System.out.println("Done task ID = " + entry.getKey());
            }
        }
    }
    static public void proceedWithTasks(){
        getAllRequests();
        proceed();

    }


}
