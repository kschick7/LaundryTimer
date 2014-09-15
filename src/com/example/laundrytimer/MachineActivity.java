package com.example.laundrytimer;

import android.support.v4.app.Fragment;

public class MachineActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new MachineFragment();
	}

}
