package com.blackdev.thaparhelper.dashboard.Chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.Utils;
import com.google.android.gms.common.util.AndroidUtilsLight;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.internal.UProgressionUtilKt;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;
import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
import static androidx.recyclerview.widget.ItemTouchHelper.UP;

class MessageSwipeController extends ItemTouchHelper.Callback {

    Context context;
    SwipeControllerActions swipeControllerActions;
    private View view;
    private Drawable timeView;
    private float dX;
    private boolean startTracking = false;
    private RecyclerView.ViewHolder currentItemViewHolder;
    private float replyButtonProgress = 0f;
    private float lastReplyButtonAnimationTime = 0L;
    private boolean isVibrate = false;
    private boolean swipeBack = false;
    private Drawable shareRound;


    public MessageSwipeController(Context context, SwipeControllerActions swipeControllerActions) {
        this.context = context;
        this.swipeControllerActions = swipeControllerActions;
    }



    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ACTION_STATE_SWIPE) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL || motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (Math.abs(view.getTranslationX()) >= convertTodp(100)) {
                            swipeControllerActions.showReplyUI(viewHolder.getAdapterPosition());
                        }
                    }
                    return false;
                }
            });
        }

        if(view.getTranslationX() <convertTodp(130) || dX <this.dX) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            this.dX = dX;
            startTracking = true;
        }
        currentItemViewHolder = viewHolder;
        showTimeText(c);
    }

    private void showTimeText(Canvas c) {
        if(currentItemViewHolder == null) {
            return;
        }
        float translationX = view.getTranslationX();
        float newTime = System.currentTimeMillis();
        float dt = Math.min(17,newTime - lastReplyButtonAnimationTime);
        lastReplyButtonAnimationTime = newTime;
        boolean showing = translationX>=convertTodp(30);
        if(showing) {
            if(replyButtonProgress < 1.0f) {
                replyButtonProgress += dt/ 180.0f;
                if(replyButtonProgress > 1.0f) {
                    replyButtonProgress = 1.0f;
                } else {
                    view.invalidate();
                }
            }
        } else if (translationX <= 0.0f) {
            replyButtonProgress = 0f;
            startTracking = false;
            isVibrate = false;
        } else {
            if (replyButtonProgress > 0.0f) {
                replyButtonProgress -= dt / 180.0f;
                if (replyButtonProgress < 0.1f) {
                    replyButtonProgress = 0f;
                } else {
                    view.invalidate();
                }
            }
        }
        int alpha;
        float scale;
        if(showing) {
            if (replyButtonProgress <= 0.8f) {
                scale = 1.2f * (replyButtonProgress / 0.8f);
            } else {
                scale = 1.2f - 0.2f * ((replyButtonProgress - 0.8f) / 0.2f);
            }
            alpha = (int) Math.min(255f, 255 * (replyButtonProgress / 0.8f));
        } else {
            scale = replyButtonProgress;
            alpha = (int) Math.min(255f, 255 * replyButtonProgress);
        }
        timeView.setAlpha(alpha);
        shareRound.setAlpha(alpha);

        if (startTracking) {
            if (!isVibrate && view.getTranslationX() >= convertTodp(100)) {
                view.performHapticFeedback(
                        HapticFeedbackConstants.KEYBOARD_TAP,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                isVibrate = true;
            }
        }
        int x = (int) view.getTranslationX()/2;
        if(view.getTranslationX() > convertTodp(130)) {
            x = convertTodp(130) /2;
        }

        float y = (float) (view.getTop() + view.getMeasuredHeight() / 2.0);
        shareRound.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        shareRound.setBounds((int)(x - convertTodp(18) * scale),
                (int)(y - convertTodp(18) * scale),
                (int)(x + convertTodp(18) * scale),
                (int)(y + convertTodp(18) * scale)
        );

        timeView.setBounds((int)(x - convertTodp(12) * scale),
                (int)(y - convertTodp(11) * scale),
                (int)(x + convertTodp(12) * scale),
                (int)(y + convertTodp(10) * scale)
        );
        shareRound.draw(c);
        timeView.draw(c);
        shareRound.setAlpha(255);
        timeView.setAlpha(255);

    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        view = viewHolder.itemView;
        timeView  = context.getDrawable(R.drawable.ic_baseline_send_24);
        shareRound = context.getDrawable(R.drawable.oval_button);
        return ItemTouchHelper.Callback.makeMovementFlags(ACTION_STATE_IDLE, UP);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if(swipeBack) {
            swipeBack  =false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private int convertTodp(int pixel) {
        return Utils.dp(pixel, context);
    }
}