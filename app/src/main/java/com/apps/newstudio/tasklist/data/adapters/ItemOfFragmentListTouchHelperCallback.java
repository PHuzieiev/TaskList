package com.apps.newstudio.tasklist.data.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemOfFragmentListTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemOfFragmentListTouchHelperAdapter mAdapter;

    /**
     * Constructor of ItemOfFragmentListTouchHelperCallback object
     *
     * @param adapter ItemOfFragmentListTouchHelperAdapter object
     */
    public ItemOfFragmentListTouchHelperCallback(ItemOfFragmentListTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Getter for value of movement flag
     *
     * @param recyclerView RecyclerView object
     * @param viewHolder   RecyclerView.ViewHolder object of list item
     * @return int value of movement flag
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.END | ItemTouchHelper.START);
    }

    /**
     * Realizes actions when list item was moved
     *
     * @param recyclerView RecyclerView object
     * @param viewHolder   RecyclerView.ViewHolder object of list item
     * @param target       RecyclerView.ViewHolder object of list item
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Realizes actions when list item was swiped
     *
     * @param viewHolder RecyclerView.ViewHolder viewHolder object
     * @param direction  direction of swipe process
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
        if (direction == ItemTouchHelper.END) {
            mAdapter.onItemDoneOrActive(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Changes item when item was moved or swiped
     *
     * @param c                 Canvas object witch will be changed
     * @param recyclerView      RecyclerView object of list
     * @param viewHolder        RecyclerView.ViewHolder of item
     * @param dX                float value
     * @param dY                float value
     * @param actionState       type of action
     * @param isCurrentlyActive type of currently action
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }
}
