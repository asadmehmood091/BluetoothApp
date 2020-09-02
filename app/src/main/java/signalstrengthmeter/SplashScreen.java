package signalstrengthmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        TextView textView = findViewById(R.id.appname);
        TextPaint paint = textView.getPaint();
        textView.setText("Bluetooth meter".toUpperCase());
        float width = paint.measureText("Bluetooth meter");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        final ImageView b = findViewById(R.id.icon2);
//        ImageView b2 = findViewById(R.id.icon3);
//        TextView sigmaz =findViewById(R.id.sigmax);
//       Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.rotate);
       Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(),
               R.anim.rotate);
//        Animation blinking2 = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.blinking3);
        b.startAnimation(rotate);
//        b2.startAnimation(blinking2);
//        sigmaz.startAnimation(blinking);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                b.clearAnimation();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 2500);
    }
}
