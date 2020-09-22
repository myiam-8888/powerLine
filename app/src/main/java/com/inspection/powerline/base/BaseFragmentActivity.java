package com.inspection.powerline.base;


import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;



public class BaseFragmentActivity extends FragmentActivity {
	
	AppManager appManager ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appManager = AppManager.getAppManager();
		// 把当前activity加入栈中
		appManager.addActivity(this);
		appManager.getSize("前堆栈activity数目");


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity关闭，从栈中移除
		appManager.finishActivity(this);
		appManager.getSize("后堆栈activity数目");
	}

	@Override
	protected void onResume() {
		super.onResume();

	}






}
