package com.teamwan.wander.db;

import android.content.Context;

/*
* Class that contains a context, and can be edited to contain more variables to be passed onto the DBUpload task.
*/
public class DBpars {

        private Context context;

        public DBpars(Context c) {
            this.context = c;
        }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
