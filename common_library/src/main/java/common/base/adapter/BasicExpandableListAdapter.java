package common.base.adapter;

import android.widget.BaseExpandableListAdapter;

/**
 * Created by cxw on 2016/1/21.
 */
public abstract class BasicExpandableListAdapter extends BaseExpandableListAdapter {
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
