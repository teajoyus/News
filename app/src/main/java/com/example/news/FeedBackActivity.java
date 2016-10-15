package com.example.news;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myview.NewAlertDialog;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class FeedBackActivity extends Activity {
    private Button bt_commit_feedback;
    private EditText et_feedback_feedback;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        initView();
        initListener();
    }

    private void initView() {
        bt_commit_feedback = (Button) findViewById(R.id.bt_commit_feedback);
        back = (ImageView) findViewById(R.id.iv_back_feedback);
        et_feedback_feedback = (EditText) findViewById(R.id.et_feedback_feedback);
    }

    private void initListener() {
        bt_commit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_feedback_feedback.getText().toString();
                if (content.length() < 6) {
                    Toast.makeText(FeedBackActivity.this, "您输入的反馈字数太少了", Toast.LENGTH_SHORT).show();

                } else {
                    NewAlertDialog.showAlert(FeedBackActivity.this, "确定", "您确定好提交您的反馈了吗", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FeedBackActivity.this, "您的反馈已经提交", Toast.LENGTH_SHORT).show();
                            FeedBackActivity.this.finish();
                        }
                            });


                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.this.finish();
            }
        });

    }
}
