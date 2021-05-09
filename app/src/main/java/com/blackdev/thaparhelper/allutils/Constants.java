package com.blackdev.thaparhelper.allutils;

public interface Constants {

    int USER_ADMINISTRATION = 1;
    int USER_STUDENT = 2;
    int USER_FACULTY = 3;
    int DATA_SHARED_PREF = 1;
    int TYPE_SHARED_PREF = 2;
    int TOKEN_SHARED_PREF = 3;
    int LECTURE_TYPE = 101;
    int TUTORIAL_TYPE = 102;
    int LAB_TYPE = 103;
    int SAME_DAY_SEARCH = 0;
    int NEXT_DAY_SEARCH = 1;//created for checking if today there is another class
    int MAX_ALARM = 7;
    int ASSIGNMENT_REQUEST_CODE = 121;
    int IMAGE_REQUEST_CODE = 122;
    int GROUP_TYPE = 1;
    int ONE_TO_ONE_TYPE = 2;
    int NORMAL_MESSAGE = 1;
    int IMAGE_MESSAGE = 2;
    int DOC_MESSAGE = 3;
    int ASSIGNMENT_MESSAGE = 4;

    String APP_NAME = "Thapar Helper";
    String NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";
    String GROUP_NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/notification";
    String CLOUD_SERVER_KEY = "AAAA5bF30vg:APA91bF4idUovFK_LPVc_gXXeYDO1zpnGSzdMcjkj2x6HPFibCMKalELVHrvFr6e41i4NF1Rpr4OhiNYDL0tJlzeTLpq6x0IMaNc83RnE-z15lrX84-xF07RSF22LL1V0v9IPEdPyR10";
    String CHANNEL_ID = "1000";

    String[] SUBJECTLIST = new String[]{"UCS619-Computer Graphics", "UCS619-Quantum Computing", "UCS701-Theory of computation", "UCS654-Predictive Analysis", "UCS654-Predictive Analysis", "UCS655-AI Applications", "UHU008-Corporate Finance"};
    String[] DAYLIST = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    String[] TYPE_LIST = new String[]{"Lecture","Tutorial","Lab"};


    String SENDER_ID = "986524930808";


}
