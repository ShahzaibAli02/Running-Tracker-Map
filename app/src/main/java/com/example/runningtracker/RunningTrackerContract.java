package com.example.runningtracker;

import android.net.Uri;

public class RunningTrackerContract {



    // RunningTrackerContract contains some static variables
    // which are used in MyContentProvider
    public static final String AUTHORITY = "com.example.runningtracker.provider.MyContentProvider";
    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/exercise.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/exercise.data.text";

}
 