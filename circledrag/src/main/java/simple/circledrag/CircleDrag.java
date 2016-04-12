package simple.circledrag;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

public class CircleDrag {

    private float dX, dY, defaultX, defaultY;

    public void init(final View view, final int radius) {
        defaultX = view.getX();
        defaultY = view.getY();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = defaultX - event.getRawX();
                        dY = defaultY - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        float valueX = event.getRawX() + dX;
                        float valueY = event.getRawY() + dY;

                        final float distanceBetweenCenterAndTouch = calculateDistance(new Point(defaultX, defaultY), new Point(valueX, valueY));
                        if (distanceBetweenCenterAndTouch > radius) {

                            final Point circleLineIntersectionPoint = getIntersectionPoints(
                                    new Point(valueX, valueY),
                                    new Point(defaultX, defaultY),
                                    radius);

                            valueX = circleLineIntersectionPoint.getFloatX();
                            valueY = circleLineIntersectionPoint.getFloatY();
                        }

                        view.animate()
                                .x(valueX)
                                .y(valueY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:

                        final AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(
                                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.getTranslationX(), 0f),
                                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTranslationY(), 0f));
                        animatorSet.setDuration(1000);
                        animatorSet.setInterpolator(new AttenuatedFluctuation());
                        animatorSet.start();

                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.clearAnimation();
                                animatorSet.setInterpolator(null);
                            }
                        });

                        break;
                }
                return true;
            }
        });

    }

    public static Point getIntersectionPoints(Point touchPoint, Point center, double radius) {
        double angle = Math.atan2(touchPoint.getY() - center.getY(), touchPoint.getX() - center.getX());
        return new Point(center.getX() + (radius * Math.cos(angle)), center.getY() + (radius * Math.sin(angle)));
    }

    public float calculateDistance(Point a, Point b) {
        float squareDifference = (float) Math.pow((a.x - b.x), 2);
        float squareDifference2 = (float) Math.pow((a.y - b.y), 2);
        return (float) Math.sqrt(squareDifference + squareDifference2);
    }

    public class AttenuatedFluctuation implements Interpolator {

        public float getInterpolation(float t) {
            return (float) ((-Math.cos(5 * Math.PI * t) / Math.exp(5 * t)) + 1);
        }
    }

    public static class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public float getFloatX() {
            return (float) x;
        }

        public float getFloatY() {
            return (float) y;
        }

        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }
}
