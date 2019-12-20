package com.juefeng.android.framework.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import com.juefeng.android.framework.R;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:22
 * Description: show at Http requesting
 */
public class DialogUpdateVersion extends Dialog{


    private Context mContext;
    private boolean isCancelable;
    private OnDialogVersionListener onDialogVersionListener;
    private TextView titleTextView;
    private TextView cententTextView;
    private View lineView;
    private Button cancelBtn;
    private Button okBtn;
    private String content;

    public DialogUpdateVersion(Context context,String content,boolean isCancelable) {
        super(context,R.style.DialogTheme);
        this.mContext = context;
        this.content = content;
        this.isCancelable = isCancelable;
        initView();
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_version,null);
        setContentView(view);
        cententTextView = view.findViewById(R.id.message);
        titleTextView = view.findViewById(R.id.title);
        cancelBtn = view.findViewById(R.id.negativeButton);
        okBtn = view.findViewById(R.id.positiveButton);
        lineView = view.findViewById(R.id.line);
        cententTextView.setText(content);
        if (!isCancelable){
            cancelBtn.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }else{
            cancelBtn.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
        }

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wm = this.getWindow().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        lp.width = width - 100;
        dialogWindow.setAttributes(lp);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogVersionListener!=null){
                    onDialogVersionListener.onSure();
                }
                dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogVersionListener!=null){
                    onDialogVersionListener.onCancel();
                }
                dismiss();
            }
        });
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDialogVersionListener!=null){
                    onDialogVersionListener.onCancel();
                }
                dismiss();
            }
        });
    }

    public OnDialogVersionListener getOnDialogVersionListener() {
        return onDialogVersionListener;
    }

    public void setOnDialogVersionListener(OnDialogVersionListener onDialogVersionListener) {
        this.onDialogVersionListener = onDialogVersionListener;
    }

    public interface OnDialogVersionListener{
        void onSure();
        void onCancel();
    }


}
