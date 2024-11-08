abstract class DashboardView implements IView{
    public void showNotifications(String[] notifications){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("NOTIFICATIONS: ");
        int count = 1;
        for(String notification : notifications){
            System.out.println("(" + count + ") " + notification);
            count++;
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showTutorial(String tutorial){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("TUTORIAL: ");
        System.out.println(tutorial);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showUserTips(String[] tips){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("TIPS: ");
        int count = 1;
        for(String tip : tips){
            System.out.println("(" + count + ") " + tip);
            count++;
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showSuccess(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showSuccess(){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]");
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showError(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[ERROR]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }
}