package com.etk2000.clsl.value;

import com.etk2000.clsl.StringBuilderPoolable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ClslStructMapped extends ClslStruct {
	private final Map<String, ClslValue> members;

	public ClslStructMapped(Map<String, ClslValue> members) {
		this.members = Collections.unmodifiableMap(members);
	}

	@Override
	public ClslValue dot(String member) {
		return members.get(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslStructConstMapped copy() {
		Map<String, ClslValue> constMap = new HashMap<>(members.size());
		for (Entry<String, ClslValue> e : members.entrySet())
			constMap.put(e.getKey(), e.getValue().copy());
		return new ClslStructConstMapped(constMap);
	}

	@Override
	public boolean eq(ClslValue other) {
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
				return other instanceof ClslStructMapped && members.equals(((ClslStructMapped) other).members);
		}
		throw new UnsupportedOperationException(
				"The operator == is undefined for the argument type(s) " + typeName() + ", " + other.typeName()
		);
	}

	@Override
	public ClslIntConst sizeof() {
		int res = 0;
		for (ClslValue v : members.values())
			res += v.sizeof().val;
		return ClslIntConst.of(res);
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
			return sb.deleteLast(members.isEmpty() ? 1 : 2).append('}').toString();
		}
	}

	@Override
	public Map<String, ClslValue> toJava() {
		return members;
	}
}