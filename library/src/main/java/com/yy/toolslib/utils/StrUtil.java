package com.yy.toolslib.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

public class StrUtil {

	/**
	 * 给字符串加下划线
	 * 
	 * @param str
	 * @return
	 */
	public static SpannableString addLine(String str) {
		SpannableString msp = new SpannableString(str);
		// 设置下划线
		msp.setSpan(new UnderlineSpan(), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 给字符串指定字符加下划线
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static SpannableString addLine(String str, int start, int end) {
		SpannableString msp = new SpannableString(str);
		// 设置下划线
		msp.setSpan(new UnderlineSpan(), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 给字符串加下划线
	 * 
	 * @param msp
	 * @return
	 */
	public static SpannableString addLine(SpannableString msp) {
		// 设置下划线
		msp.setSpan(new UnderlineSpan(), 0, msp.toString().length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 给字符串指定字符串加下划线
	 * 
	 * @param msp
	 * @param start
	 * @param end
	 * @return
	 */
	public static SpannableString addLine(SpannableString msp, int start,
			int end) {
		// 设置下划线
		msp.setSpan(new UnderlineSpan(), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置字体前景色　
	 * 
	 * @param str
	 * @param color
	 *            　＝　0xffff00ff
	 * @return
	 */
	public static SpannableString addColor(String str, int color) {
		SpannableString msp = new SpannableString(str);
		// 设置字体前景色
		msp.setSpan(new ForegroundColorSpan(color), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置字体前景色
	 * 
	 * @param str
	 * @param color
	 *            = 0xff00ffff
	 * @param start
	 * @param end
	 * @return
	 */

	public static SpannableString addColor(String str, int color, int start,
			int end) {
		SpannableString msp = new SpannableString(str);
		// 设置字体前景色
		msp.setSpan(new ForegroundColorSpan(color), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置字体背景色
	 * 
	 * @param str
	 * @param color
	 * @return
	 */
	public static SpannableString addBagColor(String str, int color) {
		SpannableString msp = new SpannableString(str);
		msp.setSpan(new BackgroundColorSpan(color), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		return msp;
	}

	/**
	 * 设置字体背景色
	 * 
	 * @param str
	 * @param color
	 * @param start
	 * @param end
	 * @return
	 */
	public static SpannableString addBagColor(String str, int color, int start,
			int end) {
		SpannableString msp = new SpannableString(str);
		msp.setSpan(new BackgroundColorSpan(color), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		return msp;

	}

	/**
	 * 设置字体前景色　
	 * 
	 * @param msp
	 * @param color
	 *            　＝　0xffff00ff
	 * @return
	 */
	public static SpannableString addColor(SpannableString msp, int color) {
		// 设置字体前景色
		msp.setSpan(new ForegroundColorSpan(color), 0, msp.toString().length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置字体前景色
	 * 
	 * @param msp
	 * @param color
	 *            = 0xff00ffff
	 * @param start
	 * @param end
	 * @return
	 */

	public static SpannableString addColor(SpannableString msp, int color,
			int start, int end) {
		// 设置字体前景色
		msp.setSpan(new ForegroundColorSpan(color), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置字体背景色
	 * 
	 * @param msp
	 * @param color
	 * @return
	 */
	public static SpannableString addBagColor(SpannableString msp, int color) {
		msp.setSpan(new BackgroundColorSpan(color), 0, msp.toString().length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		return msp;
	}

	/**
	 * 设置字体背景色
	 * 
	 * @param msp
	 * @param color
	 * @param start
	 * @param end
	 * @return
	 */
	public static SpannableString addBagColor(SpannableString msp, int color,
			int start, int end) {
		msp.setSpan(new BackgroundColorSpan(color), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		return msp;

	}


	// 创建一个 SpannableString对象
	static SpannableString msp = new SpannableString(
			"字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot");

	public void score() {
		// 设置字体(default,default-bold,monospace,serif,sans-serif)
		msp.setSpan(new TypefaceSpan("monospace"), 0, 2,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new TypefaceSpan("serif"), 2, 4,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置字体大小（绝对值,单位：像素）
		msp.setSpan(new AbsoluteSizeSpan(20), 4, 6,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new AbsoluteSizeSpan(20, true), 6, 8,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
													// dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
		// 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
		msp.setSpan(new RelativeSizeSpan(0.5f), 8, 10,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 0.5f表示默认字体大小的一半
		msp.setSpan(new RelativeSizeSpan(2.0f), 10, 12,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍

		// 设置字体前景色
		msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		// 设置字体背景色
		msp.setSpan(new BackgroundColorSpan(Color.CYAN), 15, 18,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色

		// 设置字体样式正常，粗体，斜体，粗斜体
		msp.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
		msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 20, 22,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
		msp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 斜体
		msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24,
				27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗斜体
		// 设置下划线
		msp.setSpan(new UnderlineSpan(), 27, 30,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		// 设置删除线
		msp.setSpan(new StrikethroughSpan(), 30, 33,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		// 设置上下标
		msp.setSpan(new SubscriptSpan(), 34, 35,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 下标
		msp.setSpan(new SuperscriptSpan(), 36, 37,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标

		// 超级链接（需要添加setMovementMethod方法附加响应）
		msp.setSpan(new URLSpan("tel:4155551212"), 37, 39,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 电话
		msp.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 邮件
		msp.setSpan(new URLSpan("http://www.baidu.com"), 41, 43,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
		msp.setSpan(new URLSpan("sms:4155551212"), 43, 45,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 短信 使用sms:或者smsto:
		msp.setSpan(new URLSpan("mms:4155551212"), 45, 47,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 彩信 使用mms:或者mmsto:
		msp.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 地图

		// 设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
		msp.setSpan(new ScaleXSpan(2.0f), 49, 51,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变
		// 设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
		ColorStateList csllink = null;
		ColorStateList csl = null;
		// XmlResourceParser xppcolor = getResources().getXml(0);
		// try {
		// csl = ColorStateList.createFromXml(getResources(), xppcolor);
		// } catch (XmlPullParserException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// XmlResourceParser xpplinkcolor = getResources().getXml(0);
		// try {
		// csllink = ColorStateList
		// .createFromXml(getResources(), xpplinkcolor);
		// } catch (XmlPullParserException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		msp.setSpan(new TextAppearanceSpan("monospace",
				android.graphics.Typeface.BOLD_ITALIC, 30, csl, csllink), 51,
				53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置项目符号
		msp.setSpan(new BulletSpan(
				BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN),
				0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色
		// 设置图片
		/*
		 * Drawable drawable =
		 * getResources().getDrawable(R.drawable.ic_launcher);
		 * drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		 * drawable.getIntrinsicHeight()); msp.setSpan(new ImageSpan(drawable),
		 * 53, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 */
	}
}
