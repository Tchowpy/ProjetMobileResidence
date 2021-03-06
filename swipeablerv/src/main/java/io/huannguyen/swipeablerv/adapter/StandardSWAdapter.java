package io.huannguyen.swipeablerv.adapter;

import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.List;

import io.huannguyen.swipeablerv.R;
import io.huannguyen.swipeablerv.SWItemRemovalListener;
import io.huannguyen.swipeablerv.SWSnackBarDataProvider;
import io.huannguyen.swipeablerv.utils.ResourceUtils;

/**
 * Created by huannguyen
 */

/**
 * This is a default standard implementation of the {@link SWAdapter} interface. This class
 * requires to hold a reference to and directly manipulate (add and remove) the list of items.
 * <p>
 * Subclass this class if that suits your implementation. Otherwise consider using {@link
 * DelegateSWAdapter} which does not directly manipulate the items if you need more flexibility.
 */
public abstract class StandardSWAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements
                                                                                     io.huannguyen.swipeablerv.adapter.SWAdapter<T> {
    protected List<T> mItems;
    protected SWItemRemovalListener<T> mItemRemovalListener;
    protected SWSnackBarDataProvider mSnackBarDataProvider;

    protected StandardSWAdapter(List<T> items) {
        mItems = items;
    }

    @Override
    public void onItemCleared(final ViewHolder viewHolder, final int direction) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final T item = mItems.get(adapterPosition);

        displaySnackBarIfNeeded(viewHolder, item, adapterPosition, direction);

        if (mItemRemovalListener != null) {
            mItemRemovalListener.onItemTemporarilyRemoved(item, adapterPosition);
        }
        mItems.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    private void displaySnackBarIfNeeded(ViewHolder viewHolder, final T item, final int adapterPosition, int direction) {
        if (mSnackBarDataProvider != null && mSnackBarDataProvider.isUndoEnabled()) {
            final Snackbar snackbar = Snackbar
                    .make(mSnackBarDataProvider.getView(), getSnackBarMessage(viewHolder, direction),
                          Snackbar.LENGTH_LONG)
                    .setAction(getUndoActionText(viewHolder, direction),
                               new OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       mItems.add(adapterPosition, item);
                                       notifyItemInserted(adapterPosition);
                                       if (mItemRemovalListener != null) {
                                           mItemRemovalListener.onItemAddedBack(item, adapterPosition);
                                       }
                                   }
                               });

            snackbar.setCallback(new Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event != DISMISS_EVENT_ACTION && mItemRemovalListener != null) {
                        mItemRemovalListener.onItemPermanentlyRemoved(item);
                    }
                }
            });

            // set colors
            View snackBarView = snackbar.getView();

            // background color
            int snackBarBackgroundColor =
                    mSnackBarDataProvider.getSnackBarBackgroundColor(direction);
            if (snackBarBackgroundColor != ResourceUtils.NO_COLOR) {
                snackBarView.setBackgroundColor(snackBarBackgroundColor);
            }

            // undo action text color
            int undoActionTextColor = mSnackBarDataProvider.getUndoActionTextColor(direction);
            if (undoActionTextColor != ResourceUtils.NO_COLOR) {
                snackbar.setActionTextColor(undoActionTextColor);
            }

            // info message color
            int infoMessageColor = mSnackBarDataProvider.getSnackBarMessageColor(direction);
            if (infoMessageColor != ResourceUtils.NO_COLOR) {
                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                textView.setTextColor(infoMessageColor);
            }

            snackbar.show();
            snackbar.getView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }
    }

    @Override
    public String getSnackBarMessage(ViewHolder viewHolder, int direction) {
        if(mSnackBarDataProvider != null) {
            return mSnackBarDataProvider.getSnackBarMessage(direction);
        }

        return null;
    }

    @Override
    public String getUndoActionText(ViewHolder viewHolder, int direction) {
        if(mSnackBarDataProvider != null) {
            return mSnackBarDataProvider.getUndoActionText(direction);
        }

        return null;
    }

    @Override
    public int getSwipeDirs(ViewHolder viewHolder) {
        return -1;
    }

    public SWItemRemovalListener getItemRemovalListener() {
        return mItemRemovalListener;
    }

    public void setItemRemovalListener(SWItemRemovalListener<T> itemRemovalListener) {
        mItemRemovalListener = itemRemovalListener;
    }

    public SWSnackBarDataProvider getSnackBarDataProvider() {
        return mSnackBarDataProvider;
    }

    @Override
    public void setSnackBarDataProvider(SWSnackBarDataProvider snackBarDataProvider) {
        mSnackBarDataProvider = snackBarDataProvider;
    }
}