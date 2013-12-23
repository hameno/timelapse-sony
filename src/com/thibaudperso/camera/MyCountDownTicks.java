package com.thibaudperso.camera;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * Countdown based on android.os.CountDownTimer
 * 
 * @author Thibaud Michel
 *
 */
public abstract class MyCountDownTicks {

	/**
	 * The number of ticks in the future 
	 */
	private int mRemainingTicks;

	/**
	 * The interval in millis that the user receives callbacks
	 */
	private final long mCountdownInterval;


	/**
	 * @param numberOfTicks The number of ticks in the future from the call
	 *   to {@link #start()} until the countdown is done and {@link #onFinish()}
	 *   is called.
	 * @param countDownInterval The interval along the way to receive
	 *   {@link #onTick(long)} callbacks.
	 */
	public MyCountDownTicks(int numberOfTicks, long countDownInterval) {
		mRemainingTicks = numberOfTicks;
		mCountdownInterval = countDownInterval;
	}

	/**
	 * Cancel the countdown.
	 */
	public final void cancel() {
		mHandler.removeMessages(MSG);
	}

	/**
	 * Start the countdown.
	 */
	public synchronized final MyCountDownTicks start() {
		if (mRemainingTicks <= 0) {
			onFinish();
			return this;
		}
		mHandler.sendMessage(mHandler.obtainMessage(MSG));
		return this;
	}


	/**
	 * Callback fired on regular interval.
	 * @param remainingTicks The amount of ticks until finished.
	 */
	public abstract void onTick(int remainingTicks);

	/**
	 * Callback fired when the time is up.
	 */
	public abstract void onFinish();


	private static final int MSG = 1;


	// handles counting down
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			synchronized (MyCountDownTicks.this) {
				
				mRemainingTicks--;

				if (mRemainingTicks == 0) {
					onTick(mRemainingTicks);
					onFinish();
				} else {
					onTick(mRemainingTicks);
					sendMessageDelayed(obtainMessage(MSG), mCountdownInterval);
				}
			}
		}
	};
}