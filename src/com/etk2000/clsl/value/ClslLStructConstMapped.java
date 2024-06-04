package com.etk2000.clsl.value;

import java.util.Map;

public class ClslLStructConstMapped extends ClslLStructMapped implements ClslConst {
	public ClslLStructConstMapped(Map<String, ClslValue> members) {
		super(members);
	}

	@Override
	public ClslLStructConstMapped copy() {
		return this;
	}

	@Override
	public String toString() {
		return "const " + super.toString();
	}
}