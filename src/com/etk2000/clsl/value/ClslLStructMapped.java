package com.etk2000.clsl.value;

import com.etk2000.clsl.StringBuilderPoolable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ClslLStructMapped extends ClslLStruct {
	private final Map<String, ClslValue> members;

	public ClslLStructMapped(Map<String, ClslValue> members) {
		this.members = Collections.unmodifiableMap(members);
	}

	@Override
	public ClslValue dot(String member) {
		return members.get(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslLStructConstMapped copy() {
		Map<String, ClslValue> constMap = new HashMap<>(members.size());
		for (Entry<String, ClslValue> e : members.entrySet())
			constMap.put(e.getKey(), e.getValue().copy());
		return new ClslLStructConstMapped(constMap);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClslValue) {
			ClslValue other = (ClslValue) obj;
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
					return other instanceof ClslLStructMapped ? members.equals(((ClslLStructMapped) other).members) : false;
			}
			throw new UnsupportedOperationException(
					"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((ClslValue) obj).typeName());
		}
		return false;
	}

	@Override
	public ClslIntConst sizeof() {
		int res = 0;
		for (ClslValue v : members.values())
			res += v.sizeof().val;
		return new ClslIntConst(res);
	}

	@Override
	public String typeName() {
		return "struct";
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("struct { ");
			for (Entry<String, ClslValue> e : members.entrySet())
				sb.append(e.getKey()).append(" = ").append(e.getValue()).append(", ");
			return sb.deleteLast(members.size() > 0 ? 2 : 1).append('}').toString();
		}
	}

	@Override
	public Map<String, ClslValue> toJava() {
		return members;
	}
}