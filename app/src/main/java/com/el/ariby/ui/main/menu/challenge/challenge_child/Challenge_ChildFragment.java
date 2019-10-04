package com.el.ariby.ui.main.menu.challenge.challenge_child;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.el.ariby.R;

public class Challenge_ChildFragment extends Fragment {
    private ProgressBar progressBar;
    private Dialog epicDialog;
    private TextView titleTv, messageTv;
    private ImageView closePopupNegativeImg;
    private TextView percent_text, value_text;
    private Button btnGiveup, btnAccept, btnReward;
    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;
    private double maxProgress = 40000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.challenge_childfragment, container,false);
        btnGiveup = (Button)v.findViewById(R.id.btnGiveup);
        btnReward = (Button)v.findViewById(R.id.btnreward);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        percent_text = (TextView)v.findViewById(R.id.chall_textView);
        value_text = (TextView)v.findViewById(R.id.challViewProgress) ;
        epicDialog = new Dialog(getContext());
        btnGiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNegativePopup();
            }
        });
        dosomething();
        return v;
    }
    public void dosomething(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                double persentage = 0.0;
                final int[] i = {0};
                    while (persentage < maxProgress){
                        persentage = persentage + 100;
                        final double finalPersentage = persentage;
                        mProgressStatus = (int) (finalPersentage / 400);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(mProgressStatus);
                                value_text.setText(""+ finalPersentage/1000+"km / "+maxProgress/1000+"km");
                                percent_text.setText(""+mProgressStatus+"%");
                                if (finalPersentage==maxProgress){
                                    i[0] += 1;
                                    if(i[0] == 1){
                                        btnGiveup.setVisibility(View.INVISIBLE);
                                        btnReward.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

            }
        }).start();
    }
    public void ShowNegativePopup(){
        epicDialog.setContentView(R.layout.dialog_negative_challenge);
        closePopupNegativeImg = (ImageView)epicDialog.findViewById(R.id.closePopupNegativeImg);
        btnAccept = (Button)epicDialog.findViewById(R.id.btnAccept);
        titleTv = (TextView)epicDialog.findViewById(R.id.titleTv);
        messageTv = (TextView)epicDialog.findViewById(R.id.messageTv);

        closePopupNegativeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }
}
