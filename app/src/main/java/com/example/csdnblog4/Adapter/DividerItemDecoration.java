package com.example.csdnblog4.Adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * RecyclerView的自定义divider
 * 
 * @author orange
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
	private int mVerticalSpaceHeight;
	private Drawable mDrawable;
	private int color= Color.parseColor("#00ff00");
	private int status;

	public DividerItemDecoration(int mVerticalSpaceHeight) {
		this.mVerticalSpaceHeight = mVerticalSpaceHeight;
		this.status = 0;
	}

	public DividerItemDecoration(Drawable drawable) {
		this.mDrawable = drawable;
		this.status = 1;
	}

	public DividerItemDecoration(int color, int spaceHeight) {
		this.color = color;
		this.mVerticalSpaceHeight = spaceHeight;
		this.status = 2;
	}

	@Override
	public void onDraw(Canvas canvas, RecyclerView parent, State state) {
		if (status == 1 || status == 2) {
			Paint paint = null;
			if (status == 2) {
				paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(color);
				paint.setStyle(Paint.Style.FILL);
			}

			final int left = parent.getPaddingLeft();
			final int right = parent.getWidth() - parent.getPaddingRight();

			final int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View child = parent.getChildAt(i);
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int top = child.getBottom() + params.bottomMargin;

				if (status == 2) {
					final int bottom = top + mVerticalSpaceHeight;
					canvas.drawRect(left, top, right, bottom, paint);
				} else {
					final int bottom = top + mDrawable.getIntrinsicHeight();
					mDrawable.setBounds(left, top, right, bottom);
					mDrawable.draw(canvas);
				}

			}
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
			if (status == 0 || status == 2) {
				outRect.bottom = mVerticalSpaceHeight;
			} else if (status == 1) {
				outRect.bottom = mDrawable.getIntrinsicWidth();
			}
		}
	}
}
