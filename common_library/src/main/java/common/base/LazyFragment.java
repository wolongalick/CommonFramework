package common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class LazyFragment extends BaseFragment {
    private boolean isDataLoaded =false;
    private boolean isCreated=false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isCreated)
            isCreated=true;
        if (getUserVisibleHint()&&!isDataLoaded){
            isDataLoaded =true;
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&isCreated&&!isDataLoaded){
            isDataLoaded =true;
            lazyLoad();
        }
    }


    public abstract void lazyLoad();
}