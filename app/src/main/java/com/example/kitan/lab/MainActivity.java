package com.example.kitan.lab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Display;
import android.view.MotionEvent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import android.provider.MediaStore.Images.Media;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class MainActivity extends AppCompatActivity{

//-----------------------------------Class for Draw Array------------------------------->
    public class DrawItem {
        Path path;
        String text;
        String color;
        int chose;
        float textSize;
        float x;
        float y;

        public DrawItem(int ch, String col, Path p) {
            chose = ch;
            color = col;
            path = p;
        }

        public DrawItem(int ch, String col, String t, float x1, float y1, float sz) {
            chose = ch;
            color = col;
            text = t;
            x = x1;
            y = y1;
            textSize = sz;
        }
    }

//-----------------------------------Index and check variables ------------------------------->
    int index = 0;
    int check = 0;
    int checkFigure = 0;
    int imgCheck = 0;
    int imgSaveCheck = 0;

//-----------------------------------Variables for draw data------------------------------->
    float x;
    float y;
    float xOld;
    float yOld;
    float width = 10, height = 10;
    float radius;
    String color;
    String text;
    float textSize;

//-----------------------------------Variables for draw array------------------------------->
    int max = 1000;
    Path drawPath;
    DrawItem[] drawItemArr = new DrawItem[max];

//-----------------------------------Variables for image------------------------------->
    private static final int REQUEST = 1;
    Bitmap img;
    String fName;

//-----------------------------------CHANGE PATH ARRAY------------------------------->
//----------------------------------------------------------------->
//---------------------------------------------------->
    public void addPathColorArr(Path path, String color) {
        if(index == max){
            Toast.makeText(MainActivity.this, "Can`t draw more", Toast.LENGTH_SHORT).show();
        }else{
            drawItemArr[index] = new DrawItem(1, color, path);
            index ++;
        }
    }

    public void addTextColorArr(String text, String color, float x1, float y1, float sz) {
        if(index == max){
            Toast.makeText(MainActivity.this, "Can`t draw more", Toast.LENGTH_SHORT).show();
        }else{
            drawItemArr[index] = new DrawItem(2, color, text, x1, y1, sz);
            index ++;
        }
    }

    public void removePathColorArr() {
        if(index == 0){
            Toast.makeText(MainActivity.this, "No draw actions", Toast.LENGTH_SHORT).show();
        }else{
            if(drawItemArr[index-1].chose == 1){
                drawItemArr[index-1].path = new Path();
            }
            index --;
        }
        setContentView(new MainActivity.DrawArray(this));
    }

//-----------------------------------CHOOSE DRAW------------------------------->
//----------------------------------------------------------------->
//---------------------------------------------------->
    public void chooseDraw(int checkEvent) {

        switch (check){
            case 1:
                if(checkEvent == 2) removePathColorArr();
                if(checkEvent != 3)  drawFigure();
                break;
            case 2:
                if(checkEvent == 2) removePathColorArr();
                if(checkEvent != 3)  drawText();
                break;
            case 3:
                if(checkEvent == 1){
                    drawPath = new Path();
                    xOld = x - radius/2;
                    yOld = y - radius/2;
                    drawStart();
                }
                if(checkEvent == 3)  drawEnd();
                if(checkEvent == 2)  drawStart();
                break;
        }
    }

//-----------------------------------DRAW FIGURE-------------------------------->
//----------------------------------------------------------------->
//---------------------------------------------------->
    public void drawFigureRect() {
        Path path = new Path();
        RectF rectf;

        x = x - width/2;
        y = y - height/2;

        rectf = new RectF(x, y, x + width, y + height);
        path.addRect(rectf, Path.Direction.CW);

        addPathColorArr(path, color);
        setContentView(new MainActivity.DrawArray(this));

    }

    public void drawFigureTriangle() {
        Path path = new Path();

        x = x - width/2;
        y = y - height/2;

        float x1 = x;
        float y1 = y + height;

        float x2 = x + width;
        float y2 = y + height;

        float x3 = x + width/2;
        float y3 = y;

                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(x1, y1);
                path.lineTo(x2, y2);
                path.lineTo(x3, y3);
                path.lineTo(x1, y1);

        path.close();
        addPathColorArr(path, color);

        setContentView(new MainActivity.DrawArray(this));

    }

    public void drawFigureCircle() {
        Path path = new Path();
        RectF rectf;

        x = x - width/2;
        y = y - height/2;

        rectf = new RectF(x, y, x + width, y + height);
        path.addOval(rectf, Path.Direction.CW);

        addPathColorArr(path, color);
        setContentView(new MainActivity.DrawArray(this));
    }

    public void drawFigure() {
        switch (checkFigure){
            case 1:
                drawFigureRect();
                break;
            case 2:
                drawFigureTriangle();
                break;
            case 3:
                drawFigureCircle();
                break;
        }
    }

//-----------------------------------DRAW TEXT---------------------------------->
//---------------------------------------------------------->
//-------------------------------------------->
    public void drawText() {
        addTextColorArr(text, color, x, y, textSize);
        setContentView(new MainActivity.DrawArray(this));
    }

//-----------------------------------DRAW DRAW--------------------------------->
//---------------------------------------------------------->
//-------------------------------------------->
    public float abs(float x) {
        if(x < 0) x = x * -1;
        return x;
    }

    public void drawStart() {
        x = x - radius/2;
        y = y - radius/2;

        float a = (yOld - y + (float) 0.01)/(xOld - x + (float) 0.01);
        float b = y - x*a;
        float dx = 1;

        if( abs(y - yOld) > abs(x - xOld) ){
            if(y > yOld){
                for(float i = yOld; i < y; i += dx){
                    yOld += dx;
                    xOld = (yOld - b)/a;
                    drawPath.addCircle( xOld, yOld, radius,Path.Direction.CW);
                }
            }else{
                a = (y- yOld + (float) 0.01)/(x - xOld + (float) 0.01);
                b = yOld - xOld*a;
                for(float i = yOld; i > y; i -= dx){
                    yOld -= dx;
                    xOld = (yOld - b)/a;
                    drawPath.addCircle( xOld, yOld, radius,Path.Direction.CW);
                }
            }
        }else{
            if(x > xOld){
                for(float i = xOld; i < x; i += dx){
                    xOld += dx;
                    yOld = a*xOld + b;
                    drawPath.addCircle( xOld, yOld, radius,Path.Direction.CW);
                }
            }else{
                a = (y- yOld)/(x - xOld);
                b = yOld - xOld*a;
                for(float i = xOld; i > x; i -= dx){
                    xOld -= dx;
                    yOld = a*xOld + b;
                    drawPath.addCircle( xOld, yOld, radius,Path.Direction.CW);
                }
            }
        }

        xOld = x;
        yOld = y;
        drawPath.addCircle(x, y, radius,Path.Direction.CW);
        setContentView(new MainActivity.DrawDraw(this));
    }

    public void drawEnd() {
        addPathColorArr(drawPath, color);
        setContentView(new MainActivity.DrawArray(this));
    }

//-----------------------------------CREATE ACTIVITY---------------------------------->
//---------------------------------------------------------->
//-------------------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemBack:
                if(check == 0){
                    setContentView(new MainActivity.DrawArray(this));
                    check = -1;
                }else {
                    removePathColorArr();
                }
                return true;
            case R.id.itemFile:
                check = 0;
                setContentView(R.layout.activity_file);
                Toast.makeText(MainActivity.this, "File", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemFigure:
                check = 0;
                setContentView(R.layout.activity_figure);
                setBtnCheckFigureOnClick(1);
                Toast.makeText(MainActivity.this, "Figure", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemText:
                check = 0;
                setContentView(R.layout.activity_text);
                Toast.makeText(MainActivity.this, "Text", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemDraw:
                check = 0;
                setContentView(R.layout.activity_draw);
                Toast.makeText(MainActivity.this, "Draw", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//-----------------------------------DRAW CLASS----------------------->
//---------------------------------------------------------->
//-------------------------------------------->
    class DrawArray extends View {
        Paint p;
        Path path;

        public DrawArray(Context context) {
            super(context);
            p = new Paint();
            path = new Path();
        }

        @Override
        public void onDraw(Canvas canvas) {
            Display display = getWindowManager().getDefaultDisplay();
            int widthD = display.getWidth();
            int heightD = display.getHeight();

            if (imgCheck == 1) {

                Matrix m = new Matrix();
                m.setScale((float) widthD / img.getWidth(), (float) (heightD - 112) / img.getHeight());
                canvas.drawBitmap(img, m, p);
            }

            for(int i = 0; i < index; i++){
                p.setColor(Color.parseColor(drawItemArr[i].color));
                if(drawItemArr[i].chose == 1){
                    canvas.drawPath(drawItemArr[i].path, p);
                }else{
                    p.setTextSize(drawItemArr[i].textSize);
                    canvas.drawText(drawItemArr[i].text, drawItemArr[i].x, drawItemArr[i].y, p);
                }
            }

            if(imgSaveCheck == 1){
                callDrawView();
                imgSaveCheck = 0;
            }
        }

    }

    class DrawDraw extends View {

        Paint p;
        Path path;
        public DrawDraw(Context context) {
            super(context);
            p = new Paint();
            path = new Path();
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (imgCheck == 1) {
                Display display = getWindowManager().getDefaultDisplay();
                int widthD = display.getWidth();
                int heightD = display.getHeight();

                Matrix m = new Matrix();
                m.setScale((float) widthD / img.getWidth(), (float) (heightD - 112) / img.getHeight());
                canvas.drawBitmap(img, m, p);
            }

            for(int i = 0; i < index; i++){
                p.setColor(Color.parseColor(drawItemArr[i].color));
                if(drawItemArr[i].chose == 1){
                    canvas.drawPath(drawItemArr[i].path, p);
                }else{
                    p.setTextSize(drawItemArr[i].textSize);
                    canvas.drawText(drawItemArr[i].text, drawItemArr[i].x, drawItemArr[i].y, p);
                }
            }

            p.setColor(Color.parseColor(color));
            canvas.drawPath(drawPath, p);
        }
    }

    class SaveFile extends View {
        Paint paint;
        Bitmap bitmap;

        public SaveFile(Context context) {
            super(context);
            paint = new Paint();

            Display display = getWindowManager().getDefaultDisplay();
            int widthD = display.getWidth();
            int heightD = display.getHeight();

            bitmap = Bitmap.createBitmap(widthD, heightD - 112, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);

            if (imgCheck == 1) {
                Matrix m = new Matrix();
                m.setScale((float) widthD / img.getWidth(), (float) (heightD - 112) / img.getHeight());
                canvas.drawBitmap(img, m, paint);
            }else{
                Bitmap bmpIcon = BitmapFactory.decodeResource(getResources(), R.drawable.white);
                bmpIcon = Bitmap.createScaledBitmap(bmpIcon, widthD, heightD, true);
                canvas.drawBitmap(bmpIcon, 0,0, paint);
            }


            for(int i = 0; i < index; i++){
                paint.setColor(Color.parseColor(drawItemArr[i].color));
                if(drawItemArr[i].chose == 1){
                    canvas.drawPath(drawItemArr[i].path, paint);
                }else{
                    paint.setTextSize(drawItemArr[i].textSize);
                    canvas.drawText(drawItemArr[i].text, drawItemArr[i].x, drawItemArr[i].y, paint);
                }
            }

            try {

                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) , fName + ".jpg");
                if (!imgFile.exists()){
                    imgFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(imgFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Toast.makeText(MainActivity.this, "File saved", Toast.LENGTH_SHORT).show();

                fos.close();
            } catch (Exception e) {
            }


        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

    }

    void callDrawView() {
        setContentView(new SaveFile(this));
    }

//-----------------------------------TOUCH EVENT----------------------->
//---------------------------------------------------------->
//-------------------------------------------->

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        x = e.getX();
        y = e.getY() - 120;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                chooseDraw(1);
                break;
            case MotionEvent.ACTION_MOVE:
                chooseDraw(2);
                break;
            case MotionEvent.ACTION_UP:
                chooseDraw(3);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

//-----------------------------------BUTTONS----------------------->
//---------------------------------------------------------->
//-------------------------------------------->

//-----------------------------------FIGURE BUTTON----------------------->
//---------------------------------------------------------->

    public void btnFigureOnClick(View v) {
        try {
            width = Float.parseFloat( ((EditText) findViewById(R.id.editFigureWidth)).getText().toString() );
            height = Float.parseFloat( ((EditText) findViewById(R.id.editFigureHeigth)).getText().toString() );
            color = ((EditText) findViewById(R.id.editFigureRGB)).getText().toString();
            Color.parseColor(color);
            check = 1;
            setContentView(new MainActivity.DrawArray(this));
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Bad data", Toast.LENGTH_SHORT).show();
        }
    }

    public void setBtnCheckFigureOnClick(int i) {
        int rect = 255, triangle = 255, circle = 255;
        checkFigure = i;
        switch (i){
            case 1:
                rect = 255;
                triangle = 140;
                circle = 140;
                break;
            case 2:
                rect = 140;
                triangle = 255;
                circle = 140;
                break;
            case 3:
                rect = 140;
                triangle = 140;
                circle = 255;
                break;
        }

        Button btnRect, btnTriangle, btnCircle;
        btnRect = (Button) findViewById(R.id.buttonFigureCheckRect);
        btnTriangle = (Button) findViewById(R.id.buttonFigureCheckTriangle);
        btnCircle = (Button) findViewById(R.id.buttonFigureCheckCircle);

        btnRect.getBackground().setAlpha(rect);
        btnTriangle.getBackground().setAlpha(triangle);
        btnCircle.getBackground().setAlpha(circle);
    }

    public void btnCheckFigureRectOnClick(View v) {
        setBtnCheckFigureOnClick(1);
    }

    public void btnCheckFigureTriangleOnClick(View v) {
        setBtnCheckFigureOnClick(2);
    }

    public void btnCheckFigureCircleOnClick(View v) {
        setBtnCheckFigureOnClick(3);
    }

//-----------------------------------TEXT BUTTON----------------------->
//---------------------------------------------------------->
    public void btnTextOnClick(View v) {

        try {
            textSize = Float.parseFloat( ((EditText) findViewById(R.id.editTextSize)).getText().toString() );
            color = ((EditText) findViewById(R.id.editTextRGB)).getText().toString();
            text = ((EditText) findViewById(R.id.editTextString)).getText().toString();
            Color.parseColor(color);
            check = 2;
            setContentView(new MainActivity.DrawArray(this));
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Bad data", Toast.LENGTH_SHORT).show();
        }
    }

//-----------------------------------DRAW BUTTON----------------------->
//---------------------------------------------------------->
    public void btnDrawOnClick(View v) {
        try {
            color = ((EditText) findViewById(R.id.editDrawRGB)).getText().toString();
            radius = Float.parseFloat( ((EditText) findViewById(R.id.editDrawRadius)).getText().toString() );
            Color.parseColor(color);
            check = 3;
            setContentView(new MainActivity.DrawArray(this));
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Bad data", Toast.LENGTH_SHORT).show();
        }
    }

//-----------------------------------FILE BUTTONS----------------------->
//---------------------------------------------------------->
    public void btnOpenFileOnClick(View v) {
        try {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQUEST);
            setContentView(new MainActivity.DrawArray(this));
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Bad data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        img = null;

        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                img = Media.getBitmap(getContentResolver(), selectedImage);
                imgCheck = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setContentView(new MainActivity.DrawArray(this));
    }

    public void btnSaveFileOnClick(View v) {
        fName = ((EditText) findViewById(R.id.editTextFileName)).getText().toString();
        imgSaveCheck = 1;
        setContentView(new MainActivity.DrawArray(this));
    }

    public void btnChooseNameFileSave(View v) {
        setContentView(R.layout.activity_save);
    }

}


