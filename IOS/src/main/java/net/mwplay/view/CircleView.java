package net.mwplay.view;

import org.robovm.apple.coreanimation.CABasicAnimation;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIImageView;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.Method;
import org.robovm.rt.bro.annotation.ByVal;
import org.robovm.rt.bro.annotation.Pointer;

@CustomClass("CircleView")
public class CircleView extends UIImageView {

	public void rotation() {
		// 旋转
		CABasicAnimation animation = new CABasicAnimation();
		animation.setKeyPath("transform.rotation");
		animation.setFromValue(NSNumber.valueOf(0));
		animation.setToValue(NSNumber.valueOf(180 * 2));
		animation.setDuration(200);
		animation.setRepeatCount(10000);

		getLayer().addAnimation(animation, null);
	}

	public void useCircle() {
		// 边框
		getLayer().setBorderWidth(4);
		getLayer().setBorderColor(UIColor.fromRGBA(1, 1, 1, 0.7).getCGColor());

		// 设置圆角
		setClipsToBounds(true);
		getLayer().setCornerRadius(getFrame().getWidth() / 2);
	}
}
