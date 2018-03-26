package box.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import com.squareup.picasso.Transformation;

public class RoundCornerTransform implements Transformation {
	
	int width, height;
	public RoundCornerTransform(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public RoundCornerTransform() {}
	

	@Override
	public Bitmap transform(Bitmap bitmap) {
		Bitmap src;
		if(width == 0) {
			src = bitmap;
		} else {
			src = createScaledBitmap(bitmap, width, height);
		}
		
		Bitmap output = Bitmap.createBitmap(src.getWidth(),
				src.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 15;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);
		src.recycle();
		return output;
		
	}
	
	@Override
	public String key() {
		return "square()";
	}
	
	public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) {
			src.recycle();
		}
		return dst;
	}

}
