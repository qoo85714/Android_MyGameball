package tw.jason.app.helloworld.mygame;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {

    private  int[] ballRes =
            {R.drawable.ball1,R.drawable.ball2,
                    R.drawable.ball3,R.drawable.ball4};
    private Bitmap[] bmpBall = new Bitmap[ballRes.length];
    private Resources res;
    private Paint paintBall;
    private boolean isInit;
    private int viewW, viewH;
    private float ballW, ballH,dx,dy;

    private Matrix matrix;
    private LinkedList<BallTask> balls;

    private GestureDetector gd;
    private Timer timer;

    public GameView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.welcome);
        res = context.getResources();



        paintBall = new Paint();
        paintBall.setAlpha(255);

        matrix = new Matrix();
        timer = new Timer();
        balls = new LinkedList<>();
        gd = new GestureDetector(context,new MyGDlistener());
    }
    private void init(){
        isInit = true;
        viewW = getWidth(); viewH = getHeight();
        ballW = viewW / 10f; ballH = ballW;
        dx=viewW/256f ;
        dy=viewH/256f;
        for(int i=0;i<ballRes.length;i++) {
            bmpBall[i] = BitmapFactory.decodeResource(res, ballRes[i]);

        matrix.reset();
        matrix.postScale(ballW / bmpBall[i].getWidth(), ballH / bmpBall[i].getHeight());
        bmpBall[i] = Bitmap.createBitmap(bmpBall[i],0,0,
                bmpBall[i].getWidth(),bmpBall[i].getHeight(),
                matrix, false);
        }
        timer.schedule(new RefreshTask(),0,60);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float ex = event.getX(), ey = event.getY();
//        BallTask ballTask = new BallTask(ex-ballW/2f,ey-ballH/2f,dx,dy);
//        timer.schedule(ballTask,500,30);
//        balls.add(ballTask);
        return gd.onTouchEvent(event);//false;//.onTouchEvent(event);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) init();
        for(BallTask ballTask:balls) {

            canvas.drawBitmap(bmpBall[ballTask.intBmp],
                    ballTask.ballX, ballTask.ballY, paintBall);
        }

    }

    private  class MyGDlistener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Log.i("brad","onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float vX, float vY) {
            if (Math.abs(vX) > Math.abs(vY) + 1000){
                if (vX > 0){
                    Log.i("brad", "Right");
                }else{
                    Log.i("brad", "Left");
                }
            }else if (Math.abs(vY) > Math.abs(vX) + 1000){
                if (vY > 0){
                    Log.i("brad", "Down");
                }else{
                    Log.i("brad", "Up");
                }
            }
            return super.onFling(e1, e2, vX, vY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //Log.i("brad","onDown");
            return true ;//super.onDown(e);
        }
    }

    private  class  BallTask extends TimerTask{
        float ballX,ballY,dx,dy;
        int intBmp;
        BallTask(float ballX ,float ballY ,float dx, float dy ){
            this.ballX = ballX;
            this.ballY = ballY;
            this.dx = dx;
            this.dy = dy;
            intBmp = (int)(Math.random()*4);

        }


        @Override
        public void run() {
            if(ballX<0 || ballX+ballW>viewW){
                dx *= -1;
            }
            if(ballY<0 || ballY+ballH>viewH){
                dy *= -1;
            }
            ballX += dx;
            ballY += dy;


        }
    }
    private  class RefreshTask extends TimerTask{
        @Override
        public void run() {
            postInvalidate();
        }
    }
}
