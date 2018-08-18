package com.spa.ui.diy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.event.DataMessage;
import com.spa.tools.Fragments;
import com.spa.ui.MainFr;
import com.spa.ui.bottom.OrderFr;
import com.spa.ui.bottom.YouhuiFr;
import com.spa.ui.bottom.help.HelpFr;
import com.spa.ui.bottom.liuwei.LiuweiActivity;
import com.spa.ui.intro.IntroFr;
import com.spa.ui.jishi.JishiStyleFr;
import com.spa.views.BtmDialog;

import java.net.URLEncoder;

public class Bottom extends LinearLayout implements View.OnClickListener {
    View view;

    App app;
    Context context;

    public Bottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.bottom, this);
        app = (App) context.getApplicationContext();
        this.context = context;
        find();
    }

    ImageView bottom_menu1, bottom_menu2, bottom_menu3, bottom_menu4, bottom_menu5, bottom_menu6, bottom_menu7, bottom_menu8, bottom_menu9, bottom_menu10;

    private void find() {
        bottom_menu1 = findViewById(R.id.bottom_menu1);
        bottom_menu2 = findViewById(R.id.bottom_menu2);
        bottom_menu3 = findViewById(R.id.bottom_menu3);
        bottom_menu4 = findViewById(R.id.bottom_menu4);
        bottom_menu5 = findViewById(R.id.bottom_menu5);
        bottom_menu6 = findViewById(R.id.bottom_menu6);
        bottom_menu7 = findViewById(R.id.bottom_menu7);
        bottom_menu8 = findViewById(R.id.bottom_menu8);
        bottom_menu9 = findViewById(R.id.bottom_menu9);
        bottom_menu10 = findViewById(R.id.bottom_menu10);

        bottom_menu1.setOnClickListener(this);
        bottom_menu2.setOnClickListener(this);
        bottom_menu3.setOnClickListener(this);
        bottom_menu4.setOnClickListener(this);
        bottom_menu5.setOnClickListener(this);
        bottom_menu6.setOnClickListener(this);
        bottom_menu7.setOnClickListener(this);
        bottom_menu8.setOnClickListener(this);
        bottom_menu9.setOnClickListener(this);
        bottom_menu10.setOnClickListener(this);
    }

    private AlertDialog dialog_call;
    private Button ok;
    private Button cancle;

    private void showCall() {

        dialog_call = new AlertDialog.Builder(context).create();
        if (dialog_call.isShowing()) {
            dialog_call.dismiss();
        } else {
            dialog_call.show();
        }
        dialog_call.setContentView(R.layout.dialog_call);
        ok = dialog_call.findViewById(R.id.ok);
        cancle = dialog_call.findViewById(R.id.cancle);
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Req.get(Req.notice + "&notifyNews=" + URLEncoder.encode("呼叫"));
                dialog_call.dismiss();
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_call.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == bottom_menu1) {//主页
//            if (!context.getClass().getSimpleName().equals("MainActivity")) {
//                context.startActivity(new Intent(context, MainActivity.class));
//            }
//            context.startActivity(new Intent(context, MainActivity.class)
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            Fragments.Replace(((Activity) context).getFragmentManager(), new MainFr());
        } else if (v == bottom_menu2) {//介绍
            Fragments.Replace(((Activity) context).getFragmentManager(), new IntroFr());
        } else if (v == bottom_menu3) {//优惠
            Fragments.Replace(((Activity) context).getFragmentManager(), new YouhuiFr());
        } else if (v == bottom_menu4) {//技师
            Fragments.Replace(((Activity) context).getFragmentManager(), new JishiStyleFr());
        } else if (v == bottom_menu5) {//消费
            Fragments.Replace(((Activity) context).getFragmentManager(), new OrderFr());
        } else if (v == bottom_menu6) {//呼叫
            showCall();
        } else if (v == bottom_menu7) {//留位
            showLiuwei();
        } else if (v == bottom_menu8) {//控制
            showCtrl();
        } else if (v == bottom_menu9) {//帮助
            Fragments.Replace(((Activity) context).getFragmentManager(), new HelpFr());
        } else if (v == bottom_menu10) {//返回
            if (((Activity) context).getFragmentManager().getBackStackEntryCount() > 1) {
                ((Activity) context).getFragmentManager().popBackStack();
            } else {
                exit();
            }
        }
    }

    private long exitTime = 0;

    private void exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toas toas = new Toas();
                    toas.setMsg(context.getString(R.string.exit));
                    toas.show(context);
                    toas = null;
                }
            });
        } else {
            ((Activity) context).finish();
            System.exit(0);
        }
    }

    private Handler handler = new Handler();

    public void onEvent(final DataMessage event) {
        try {
            if (event.getApi().equals(Req.notice + "&notifyNews=" + URLEncoder.encode("呼叫"))) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toas toas = new Toas();
                        toas.setMsg(context.getString(R.string.call_success));
                        toas.show(context);
                        toas = null;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog dialog_ctrl;
    private Button up_z, down_z, jie_z, jia_z;

    private void showCtrl() {
        dialog_ctrl = new AlertDialog.Builder(context).create();
        if (dialog_ctrl.isShowing()) {
            dialog_ctrl.dismiss();
        } else {
            dialog_ctrl.show();
        }
        dialog_ctrl.setContentView(R.layout.dialog_ctrl);
        up_z = dialog_ctrl.findViewById(R.id.up_z);
        up_z.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_ctrl.dismiss();
                showsp();
            }
        });
        down_z = dialog_ctrl.findViewById(R.id.down_z);
        down_z.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_ctrl.dismiss();
                showsp();
            }
        });
        jie_z = dialog_ctrl.findViewById(R.id.jie_z);
        jie_z.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_ctrl.dismiss();
                showsp();
            }
        });
        jia_z = dialog_ctrl.findViewById(R.id.jia_z);
        jia_z.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_ctrl.dismiss();
                showsp();
            }
        });
    }

    private void showsp() {
        dialog_liuwei = new AlertDialog.Builder(context).create();
        if (dialog_liuwei.isShowing()) {
            dialog_liuwei.dismiss();
        } else {
            dialog_liuwei.show();
        }
        dialog_liuwei.setContentView(R.layout.dialog_liuwei);
        cancle = dialog_liuwei.findViewById(R.id.cancle);
        key = (EditText) dialog_liuwei.findViewById(R.id.key);
        dialog_liuwei.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                        key_temp += keyCode - 7;
                        handler2.removeMessages(2);
                        handler2.sendEmptyMessageDelayed(2, 100);
                    }
                }
                return false;
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_liuwei.dismiss();
            }
        });
    }

    private AlertDialog dialog_liuwei;
    private EditText key;
    private String key_temp = "";

    private void showLiuwei() {
        dialog_liuwei = new AlertDialog.Builder(context).create();
        if (dialog_liuwei.isShowing()) {
            dialog_liuwei.dismiss();
        } else {
            dialog_liuwei.show();
        }
        dialog_liuwei.setContentView(R.layout.dialog_liuwei);
        cancle = dialog_liuwei.findViewById(R.id.cancle);
        key = (EditText) dialog_liuwei.findViewById(R.id.key);
        dialog_liuwei.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                        key_temp += keyCode - 7;
                        handler2.removeMessages(0);
                        handler2.sendEmptyMessageDelayed(0, 100);
                    }
                }
                return false;
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                context.startActivity(new Intent(context, LiuweiActivity.class));
                dialog_liuwei.dismiss();
            }
        });
    }

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    try {
                        app.setKey(key_temp.substring(1, 8).trim());
                        handler2.sendEmptyMessageDelayed(1, 500);

                    } catch (Exception e) {
                    }

                    break;
                case 1:
                    key_temp = "";
                    if (dialog_liuwei != null && dialog_liuwei.isShowing()) {
                        dialog_liuwei.dismiss();
                    }
                    context.startActivity(new Intent(context, LiuweiActivity.class));

                    break;
                case 2:
                    try {
                        // key.setText(key_temp.trim());
                        app.setKey(key_temp.substring(1, 8).trim());
                        handler2.sendEmptyMessageDelayed(3, 500);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }

                    break;
                case 3:
                    key_temp = "";
                    if (dialog_liuwei != null && dialog_liuwei.isShowing()) {
                        dialog_liuwei.dismiss();
                    }
                    Toas toas = new Toas();
                    toas.setMsg(context.getString(R.string.success));
                    toas.show(context);
                    toas = null;
                    break;


                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 是否取消呼叫服务员
     */
    private void showDialogStyle12() {
        BtmDialog dialog = new BtmDialog(context, R.layout.dialog_style12, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
