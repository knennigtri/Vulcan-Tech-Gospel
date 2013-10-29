package com.nennig.vtglibrary.custobjs;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Kevin on 6/8/13.
 */
public class VTGToast extends Toast {
    private Context c;

    public VTGToast(Context context) {
        super(context);
        c = context;
    }

    public void comingSoonFeature(){
        makeText(c, "This feature will be added soon!", this.LENGTH_LONG).show();
    }

    public void comingSoonProFeature(){
        makeText(c, "This pro feature will be added in a later update", this.LENGTH_LONG).show();
    }

    public void proOnlyFeature(){
        makeText(c, "This is a pro only feature", this.LENGTH_LONG).show();
    }

}
