
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Optimizer<T, R> implements Function<T, R> {
	static final class StackInterrupter extends RuntimeException {
		private static final long serialVersionUID = -1369997288554338084L;
	}

	static final StackInterrupter interrupter = new StackInterrupter();

	Function<T, R> function;
	Deque<T> inputStack = new ArrayDeque<>();
	Map<T, R> results = new HashMap<>();

	public Optimizer(UnaryOperator<Function<T, R>> rawFunction) {
		function = rawFunction.apply(t -> {
			if (results.containsKey(t)) {
				return results.get(t);
			} else {
				inputStack.push(t);
				throw interrupter;
			}
		});
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
			}
		}
		return result;
	}
}
