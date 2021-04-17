package com.blackdev.thaparhelper.allutils;

public interface Constants {

    public static final int USER_ADMINISTRATION = 0;
    public static final int USER_STUDENT = 2;
    public static final int USER_FACULTY = 3;

    String NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";
    String CLOUD_SERVER_KEY = "AAAA5bF30vg:APA91bF4idUovFK_LPVc_gXXeYDO1zpnGSzdMcjkj2x6HPFibCMKalELVHrvFr6e41i4NF1Rpr4OhiNYDL0tJlzeTLpq6x0IMaNc83RnE-z15lrX84-xF07RSF22LL1V0v9IPEdPyR10";

    String CHANNEL_ID = "1000";

    int DATA_SHARED_PREF = 1;
    int TYPE_SHARED_PREF = 2;

    int LECTURE_TYPE = 101;
    int TUTORIAL_TYPE = 102;
    int LAB_TYPE = 103;

    int SAME_DAY_SEARCH = 0;
    int NEXT_DAY_SEARCH = 1;//created for checking if today there is another class

    String[] SUBJECTLIST = new String[]{"UCS619-Computer Graphics", "UCS619-Quantum Computing", "UCS701-Theory of computation", "UCS654-Predictive Analysis", "UCS654-Predictive Analysis", "UCS655-AI Applications", "UHU008-Corporate Finance"};

    String[] DAYLIST = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};





}
