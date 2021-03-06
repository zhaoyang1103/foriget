package com.example.chartclient.zy_java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chartclient.R;
import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.dao.UserNameDao;
import com.example.chartclient.zy_java.util.ProDiaBar;
import com.example.chartclient.zy_java.util.Util;

import java.util.List;

public class RegeditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_qq;
    private EditText ed_passwrod;
    private EditText ed_sure_password;
    private Button regedit;
    private UserNameDao dao;
    private TextView tx_show;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Integer> allQQ = dao.getAllQQ();
            tx_show.setText("恭喜你获得QQ号" + allQQ.get(allQQ.size() - 1) + "");
            regedit.setText("注册完成");
            super.handleMessage(msg);


        }
    };
    private ProDiaBar ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        initView();
    }

    private void initView() {
        ed_qq = (EditText) findViewById(R.id.ed_qq);
        ed_passwrod = (EditText) findViewById(R.id.ed_passwrod);
        ed_sure_password = (EditText) findViewById(R.id.ed_sure_password);
        regedit = (Button) findViewById(R.id.regedit);
        dao = new UserNameDao();

        regedit.setOnClickListener(this);
        tx_show = (TextView) findViewById(R.id.tx_show);
        tx_show.setOnClickListener(this);
        ba = new ProDiaBar(RegeditActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regedit:
                if (regedit.getText().equals("注册")) {
                    submit();

                }
                break;
        }
    }

    private synchronized void submit() {
        // validate

        String qq = ed_qq.getText().toString().trim();
        if (TextUtils.isEmpty(qq) || qq.length() > 7) {
            Toast.makeText(this, "昵称太长或不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwrod = ed_passwrod.getText().toString().trim();
        if (TextUtils.isEmpty(passwrod)) {
            Toast.makeText(this, "密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String password1 = ed_sure_password.getText().toString().trim();
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwrod.equals(password1)) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ba.show();
            UserBean bean = new UserBean();
            bean.setGrade("100级");
            bean.setName(qq);
            bean.setPassword(passwrod);
            int i = dao.regedit(bean);
            if (i > 0) {

                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Toast.makeText(RegeditActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegeditActivity.this, MyQQAcitivity.class);
                startActivity(intent);
                List<Integer> allQQ = dao.getAllQQ();
                UserBean userNameDao = new UserBean(allQQ.get(allQQ.size() - 1), passwrod, "未设置", "100级");
                Util.saveUser(RegeditActivity.this, userNameDao);
                finish();
                ba.diss();
                handler.sendEmptyMessage(0);

            } else {
                ba.diss();
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
            }

        }

        // TODO validate success, do something


    }
}
