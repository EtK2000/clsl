package com.etk2000.clsl;

import com.etk2000.clsl.exception.ClslStackUnderflowException;
import com.etk2000.clsl.exception.variable.ClslVariableRedefinitionException;
import com.etk2000.clsl.value.ClslValue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClslRuntimeStack {
	public enum ClslStackType {
		array_deque
	}

	public interface StackFrame {
		void defineVar(String name, ClslValue value);

		ClslValue getVar(String name);

		Map<String, ClslValue> getVars();
	}

	private static class DequeStack extends ClslRuntimeStack {
		private static class StackFrameImpl implements StackFrame {
			private final Map<String, ClslValue> frame = new HashMap<>();
			private final Deque<List<String>> semiFrames = new ArrayDeque<>();
			private List<String> currentSemi;

			// LOW: maybe don't have push defined for outermost semi?
			StackFrameImpl() {
				semiFrames.add(currentSemi = new ArrayList<>());
			}

			@Override
			public void defineVar(String name, ClslValue value) {
				if (frame.put(name, value) != null)
					throw new ClslVariableRedefinitionException(name);
				currentSemi.add(name);
			}

			@Override
			public ClslValue getVar(String name) {
				return frame.get(name);
			}

			@Override
			public Map<String, ClslValue> getVars() {
				return frame;
			}

			void semiPop() {
				if (frame.isEmpty())
					throw new ClslStackUnderflowException(true);
				semiFrames.pop().forEach(frame::remove);
				currentSemi = semiFrames.peek();
			}

			void semiPush() {
				semiFrames.push(new ArrayList<>());
			}
		}

		private final Deque<StackFrameImpl> stack = new ArrayDeque<>();
		private StackFrameImpl currentFrame;

		DequeStack() {
		}

		@Override
		public void defineVar(String name, ClslValue value) {
			currentFrame.defineVar(name, value);
		}

		@Override
		public Collection<? extends StackFrame> frames() {
			return stack;
		}

		// FIXME: don't double lookup if stack.size() == 1
		// FIXME: allow optimization to dictate whether to look in stackFrame or
		// stack.get(0) (global)
		@Override
		public ClslValue getVar(String name) {
			ClslValue res = currentFrame.getVar(name);
			return res != null ? res : stack.getLast().getVar(name);
		}

		@Override
		public void pop(boolean full) {
			if (full) {
				if (stack.size() == 1)
					throw new ClslStackUnderflowException(false);
				stack.pop();
				currentFrame = stack.peek();
			}
			else
				currentFrame.semiPop();
		}

		@Override
		public void push(boolean full) {
			if (full)
				stack.push(currentFrame = new StackFrameImpl());
			else
				currentFrame.semiPush();
		}

		@Override
		public void wipe() {
			// LOW: maybe reuse global stackframe after clearing it..?
			stack.clear();
			push(true);
		}
	}

	public static ClslRuntimeStack create(ClslStackType type) {
		switch (type) {
			case array_deque:
				return new DequeStack();
		}

		throw new IllegalArgumentException("invalid ClslRuntimeStack type");
	}

	public abstract void defineVar(String name, ClslValue value);

	public abstract Collection<? extends StackFrame> frames();

	public abstract ClslValue getVar(String name);

	public abstract void pop(boolean full);

	public abstract void push(boolean full);

	public abstract void wipe();
}