package recurse;



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
	int count;

	public Optimizer(UnaryOperator<Function<T, R>> rawFunction, int depth, boolean dynamic) {
		count = depth;
		function = rawFunction.apply(t -> {
			if (results.containsKey(t)) {
				return results.get(t);
			} else if (count > 0) {
				count--;
				R result = function.apply(t);
				if (dynamic) {
					results.put(t, result);
				}
				return result;
			} else {
				count = depth;
				inputStack.push(t);
				throw interrupter;
			}
		});
	}
	
	public Optimizer(UnaryOperator<Function<T, R>> rawFunction) { 
		this(rawFunction, 1000, true);
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
