package com.example.laundrytimer;

import java.util.UUID;

import android.app.Fragment;

public class MachineActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID machineID = (UUID)getIntent().getSerializableExtra(MachineFragment.EXTRA_MACHINE_ID);
		return MachineFragment.newInstance(machineID);
	}

}
