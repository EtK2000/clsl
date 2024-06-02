package com.etk2000.clsl;

import java.util.Map;

public class CLSLStructConstMapped extends CLSLStructMapped implements CLSLConst {
	public CLSLStructConstMapped(Map<String, CLSLValue> members) {
		super(members);
	}

	@Override
	public CLSLStructConstMapped copy() {
		return this;
	}

	@Override
	public String toString() {
		return "const " + super.toString();
	}
}