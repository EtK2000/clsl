package com.etk2000.clsl.value;

import java.util.Map;

public class ClslStructConstMapped extends ClslStructMapped implements ClslConst {
	public ClslStructConstMapped(Map<String, ClslValue> members) {
		super(members);
	}

	@Override
	public ClslStructConstMapped copy() {
		return this;
	}

	@Override
	public String toString() {
		return "const " + super.toString();
	}
}