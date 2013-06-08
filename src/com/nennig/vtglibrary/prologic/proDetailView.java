package com.nennig.vtglibrary.prologic;

import android.app.Activity;
import android.content.Context;

import com.nennig.constants.AppConstants;
import com.nennig.vtglibrary.activities.BaseActivity;
import com.nennig.vtglibrary.activities.DetailViewActivity;
import com.nennig.vtglibrary.activities.VideoActivity;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.VTGToast;
import com.nennig.vtglibrary.draw.VTGMove;
import com.nennig.vtglibrary.managers.VideoManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kevin on 6/8/13.
 */
public class proDetailView {

    public static void touchIcon(Activity a, AppConstants.Set set, MatrixID mID){
        int propID = a.getSharedPreferences(AppConstants.VTG_PREFS, BaseActivity.MODE_PRIVATE).getInt(AppConstants.MOVE_PROP,0);
        //TODO Change when making more videos
        if(propID == 0)//lock for only showing the poi videos that are made
            new VideoManager(a,VideoActivity.class, set,
                    mID, AppConstants.PropType.getPropType(propID)).execute();
        else
            new VTGToast(a).comingSoonProFeature();
    }
//
//    public static VTGMove setUpIconAndPins(DetailViewActivity detailViewActivity, MatrixID curMatrixID, AppConstants.Set curSet, VTGMove drawnMove) {
//        drawnMove.removeDefaultIcon();
//        InputStream iStream;
//        try {
//            if(curSet.equals(AppConstants.Set.ONEFIVE))//TODO coming soon
//            {
//                new VTGToast(this).comingSoonFeature();
//                iStream = getAssets().open(AppConstants.LOGO_FOLDER + "/" + AppConstants.COMING_SOON_IMAGE);
//                drawnMove.addDefaultIcon(iStream);
//            }
//            else
//            {
//                iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + pMove.getImageFileName(curSet.toSetID()));
//                drawnMove.addPinsAndIcon(pMovePins, iStream);
//            }
//        } catch (IOException e) {
//            drawnMove.addPins(pMovePins);
//        }
//        return drawnMove;
//    }
}
