package test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import recurse.Optimizer;

public class Tester {

	public static final BigInteger ZERO = BigInteger.ZERO;
	public static final BigInteger ONE = BigInteger.ONE;
	public static final BigInteger TWO = BigInteger.TWO;
	public static final BigInteger THREE = BigInteger.valueOf(3);

	public static void main(String[] args) {
		Function<Integer, Integer> fib = new Optimizer<>(f -> n -> (n <= 2) ? 1 : (f.apply(n - 1) + f.apply(n - 2)));

		Function<Integer, Integer> fac = new Optimizer<>(f -> n -> (n <= 1) ? 1 : (n * f.apply(n - 1)));

		Function<List<BigInteger>, BigInteger> ack = new Optimizer<>(f -> i -> {
			BigInteger m = i.get(0);
			BigInteger n = i.get(1);
			switch (m.intValue()) {
			case 0:
				return n.add(ONE);
			case 1:
				return n.add(TWO);
			case 2:
				return n.multiply(TWO).add(THREE);
			default:
				return f.apply(Arrays.asList(m.subtract(ONE),
						n.equals(ZERO) ? ONE : f.apply(Arrays.asList(m, n.subtract(ONE)))));
			}
		});

		System.out.println(fib.apply(10));
		System.out.println(fac.apply(10));
		System.out.println(ack.apply(Arrays.asList(BigInteger.valueOf(4), BigInteger.valueOf(2))));
	}
}
