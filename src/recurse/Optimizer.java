package recurse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Optimizer<T, R> implements Function<T, R> {
	private static final StackInterrupter interrupter = new StackInterrupter();

	private final Deque<T> inputStack = new ArrayDeque<>();
	private final Map<T, R> results = new HashMap<>();
	private final Function<T, R> function;

	public Optimizer(UnaryOperator<Function<T, R>> rawFunction) {
		function = rawFunction.apply(new Catcher());
	}

	@Override
	public R apply(T t) {
		inputStack.push(t);
		R result = null;
		while (!inputStack.isEmpty()) {
			try {
				result = function.apply(inputStack.peek());
				results.put(inputStack.pop(), result);
			} catch (StackInterrupter si) {
				continue;
			}
		}
		return result;
	}

	private class Catcher implements Function<T, R> {
		@Override
		public R apply(T t) {
			if (results.containsKey(t)) {
				return results.get(t);
			} else {
				inputStack.push(t);
				throw interrupter;
			}
		}
	}
}
