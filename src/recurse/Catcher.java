package recurse;

import java.util.Deque;
import java.util.Map;
import java.util.function.Function;

public class Catcher<T, R> implements Function<T, R> {
	
	
	private Deque<T> inputStack;
	private Map<T, R> results;
	
	public Catcher( Deque<T> inputStack, Map<T, R> results) {
		this.inputStack = inputStack;
		this.results = results;
	}

	@Override
	public R apply(T t) {
		if (results.containsKey(t)) {
			return results.get(t);
		} else {
			inputStack.push(t);
			throw new StackInterrupter();
		}
	}

}
