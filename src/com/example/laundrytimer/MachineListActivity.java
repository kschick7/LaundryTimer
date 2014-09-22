// Hosts the main list fragment

package com.example.laundrytimer;

import android.app.Fragment;

public class MachineListActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return new MachineListFragment();
	}

}
