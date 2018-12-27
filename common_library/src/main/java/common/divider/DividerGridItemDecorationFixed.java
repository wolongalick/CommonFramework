package common.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class DividerGridItemDecorationFixed extends RecyclerView.ItemDecoration {

    //    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };
    private Drawable mDivider;
    private boolean isNotDrawVerticalDivider;   //是否不绘制竖直的分割线
    private boolean isNotDrawPenultDivider;     //是否不绘制加载更多footer与其上面item之间的分割线(也就是不绘制倒数第二个分割线)

    public DividerGridItemDecorationFixed(Context context, int drawableResId) {
        this(context, drawableResId, false, false);
    }

    public DividerGridItemDecorationFixed(Context context, int drawableResId, boolean isNotDrawVerticalDivider, boolean isNotDrawFooterDivider) {
        mDivider = context.getResources().getDrawable(drawableResId);
        this.isNotDrawVerticalDivider = isNotDrawVerticalDivider;
        this.isNotDrawPenultDivider = isNotDrawFooterDivider;
    }

    /*public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        outRect.set(0, 0, 0, 9);
    }*/

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);

        int childCount = state.getItemCount();
        if (childCount == 1) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        int spanCount = getSpanCount(parent);
        boolean isLastRow = false;
        boolean isLastColum = false;
        if (isLastRow(parent, itemPosition, spanCount, childCount)) {// 如果是最后一行，则不需要绘制底部
            isLastRow = true;
        }
        if (isLastColum(parent, itemPosition, spanCount, childCount)) {// 如果是最后一列，则不需要绘制右边
            isLastColum = true;
        }
        int left = 0;
        int top = 0;
        int right = isLastColum ? 0 : mDivider.getIntrinsicWidth();
        int bottom = isLastRow ? 0 : mDivider.getIntrinsicHeight();
//        BLog.e("设置偏移量,索引:" + itemPosition + ",左上右下分别是为:" + left + "," + top + "," + right + "," + bottom);

        outRect.set(0, 0,right,bottom);
    }

    //第二执行的方法
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!isNotDrawVerticalDivider) {
            drawVertical(c, parent);
        }
        drawHorizontal(c, parent);
    }


    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();


//            BLog.i("绘制水平分割线,索引:" + i + "左上右下分别为:" + left + "," + top + "," + right + "," + bottom);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

//            BLog.i("绘制垂直分割线,索引:" + i + "左上右下分别为:" + left + "," + top + "," + right + "," + bottom);
            if (i == 2 || i == 5 || i == 8) {
//                mDivider.setBounds(left, top, left, bottom);
            } else {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }


        }
    }

    private boolean isLastColum(RecyclerView parent, int position, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            int currentSpan = (position + 1) % spanCount == 0 ? spanCount : (position + 1) % spanCount;

//            BLog.i("总列数:" + spanCount + ",索引:" + position + ",所在第" + currentSpan + "列,是否为最后一列:" + (currentSpan == spanCount));


            return currentSpan == spanCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((position + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (position >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRow(RecyclerView parent, int position, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (childCount <= spanCount) {
                //当总数不超过一行的数量时,肯定是最后一行(也是第一行)
                return true;
            }

            //计算出总行数
            final int rowCount = childCount % spanCount == 0 ? childCount / spanCount : (childCount / spanCount + 1);

            //计算出第几行(第一行从1开始开始)
            int currentRow = 1;
            if (position + 1 <= spanCount) {
                currentRow = 1;
            } else {
                if ((position + 1) % spanCount == 0) {
                    currentRow = (position + 1) / spanCount;
                } else {
                    currentRow = (position + 1) / spanCount + 1;
                }
            }

//            BLog.i("总行数:"+rowCount+",索引:"+position+",所在第"+currentRow+"行,是否为最后一行:"+(currentRow==rowCount));
            //如果当前行数等于总行数,则视为最后一行
            return currentRow == rowCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (position >= childCount)
                    return true;
            } else {
                // StaggeredGridLayoutManager 且横向滚动

                // 如果是最后一行，则不需要绘制底部
                if ((position + 1) % spanCount == 0) {
                    return true;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (position >= childCount - 1) {
                // 如果是最后一行，则不需要绘制底部
                return true;
            }
            if (isNotDrawPenultDivider) {
                if (position >= childCount - 2) {
                    // 如果是最后二行，则不需要绘制底部
                    return true;
                }
            }
        }
        return false;
    }


    public void setNotDrawVerticalDivider(boolean notDrawVerticalDivider) {
        isNotDrawVerticalDivider = notDrawVerticalDivider;
    }

    public void setNotDrawPenultDivider(boolean notDrawPenultDivider) {
        isNotDrawPenultDivider = notDrawPenultDivider;
    }
}