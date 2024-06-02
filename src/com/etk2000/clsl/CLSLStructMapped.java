package com.etk2000.clsl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CLSLStructMapped extends CLSLStruct {
	private final Map<String, CLSLValue> members;

	public CLSLStructMapped(Map<String, CLSLValue> members) {
		this.members = Collections.unmodifiableMap(members);
	}

	@Override
	public CLSLValue dot(String member) {
		return members.get(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLStructConstMapped copy() {
		Map<String, CLSLValue> constMap = new HashMap<>(members.size());
		for (Entry<String, CLSLValue> e : members.entrySet())
			constMap.put(e.getKey(), e.getValue().copy());
		return new CLSLStructConstMapped(constMap);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CLSLValue) {
			CLSLValue other = (CLSLValue) obj;
			switch (other.type) {
				case ARRAY:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case VOID:
					break;
				case STRUCT:
					return other instanceof CLSLStructMapped ? members.equals(((CLSLStructMapped) other).members) : false;
			}
			throw new UnsupportedOperationException(
					"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((CLSLValue) obj).typeName());
		}
		return false;
	}

	@Override
	public CLSLIntConst sizeof() {
		int res = 0;
		for (CLSLValue v : members.values())
			res += v.sizeof().val;
		return new CLSLIntConst(res);
	}

	@Override
	public String typeName() {
		return "struct";
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("struct { ");
			for (Entry<String, CLSLValue> e : members.entrySet())
				sb.append(e.getKey()).append(" = ").append(e.getValue()).append(", ");
			return sb.deleteLast(members.size() > 0 ? 2 : 1).append('}').toString();
		}
	}

	@Override
	public Map<String, CLSLValue> toJava() {
		return members;
	}
}