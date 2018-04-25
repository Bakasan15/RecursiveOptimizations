package recurse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Optimizer<T, R> implements Function<T, R> {

	private Deque<T> inputStack = new ArrayDeque<>();
	private Map<T, R> results = new HashMap<>();
	private Catcher<T, R> catcher = new Catcher<>(inputStack, results);
	private Function<T, R> function;
	
	public Optimizer(UnaryOperator<Function<T, R>> rawFunction) {
		function = rawFunction.apply(catcher);
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

}
