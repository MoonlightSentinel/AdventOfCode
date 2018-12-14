import std.algorithm;

enum MyInput = 846601;

void main() {
	version(unittest){} else {
		import std.stdio : writefln;

		writefln!"1: %d"(part1(MyInput));
		writefln!"2: %d"(part2(MyInput));
	}
}

unittest {
	assertEquals(part1(MyInput), 3811491411);
	assertEquals(part2(MyInput), 20408083);
}

auto part1(const uint input) {
	auto state = State(State.Default);

	while(state.scores.length < input + 10) {
		state.step();
	}

	ulong res = 0;

	foreach(const score; state.scores[input .. input + 10]) {
		res *= 10;
		res += score;
	}

	return res;
}

unittest {
	immutable expectedValues = [
		5: 124515891,
		9: 5158916779,
		18: 9251071085,
		2018: 5941429882
	];

	foreach(const limit, const expectedValue; expectedValues) {
		assertEquals(part1(limit), expectedValue);
	}
}

auto part2(const uint input) {

	const digits = digits(input);
	auto state = State(State.Default);

	while(true) {

		if(state.scores.endsWith(digits)) {
			return state.scores.length - digits.length;
		}
		else if(state.scores[0 .. $-1].endsWith(digits)) {
			return state.scores.length - digits.length - 1;
		}

		state.step();
	}
}

unittest {
	immutable expectedValues = [
		51589: 9,
		1245: 6,	// Removed the 0
		92510: 18,
		59414: 2018
	];

	foreach(const input, const expectedValue; expectedValues) {
		assertEquals(part2(input), expectedValue);
	}
}

auto digits(const uint input) {
	State.Score[] digits;

	for(uint cur = input; cur != 0; ) {
		digits = (cur % 10) ~ digits;
		cur /= 10;
	}

	return cast(const) digits;
}

unittest {
	assertEquals(digits(1234), [1,2,3,4]);
	assertEquals(digits(526312345), [5,2,6,3,1,2,3,4,5]);
}

struct State {
	static immutable Default = cast(immutable) State(0, 1, [3, 7]);

	import std.array : Appender;
	static import std.stdint;

	alias Score = std.stdint.uint_fast8_t;

	size_t elve1, elve2;
	Appender!(Score[]) _scores;

	pure nothrow @safe:
	
	pure this(const size_t e1, const size_t e2, Score[] sc) {
		this.elve1 = e1;
		this.elve2 = e2;
		this._scores = typeof(_scores)(sc);
	}

	pure this(ref const State other) {
		this(other.elve1, other.elve2, other.scores.dup);
	}

	void step() {
		const sum = scores[elve1] + scores[elve2];

		if(sum >= 10) _scores ~= cast(Score)(sum / 10);

		_scores ~= cast(Score)(sum % 10);

		progress(elve1);
		progress(elve2);
	}

	private void progress(ref size_t elve) const {
		elve = (elve + scores[elve] + 1) % scores.length;
	}

	const(Score)[] scores() const {
		return _scores.data;
	}

	bool opEquals(ref const State other) const {
		return this.elve1 == other.elve1
			&& this.elve2 == other.elve2
			&& this.scores == other.scores;
	}
}

unittest {
	static immutable State[] states = cast(immutable) [
		State(0, 1,		[3, 7]),
		State(0, 1,		[3, 7, 1,  0]),
		State(4, 3,		[3, 7, 1, 0, 1, 0 ]),
		State(6, 4,		[3, 7, 1, 0, 1, 0, 1]),
		State(0, 6,		[3, 7, 1, 0, 1, 0, 1, 2]),
		State(4, 8,		[3, 7, 1, 0, 1, 0, 1, 2, 4]),
		State(6, 3,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5 ]),
		State(8, 4,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1 ]),
		State(1, 6,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5 ]),
		State(9, 8,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8 ]),
		State(1, 13,	[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9]),
		State(9, 7,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6 ]),
		State(15, 10,	[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6, 7 ]),
		State(4, 12,	[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6, 7, 7 ]),
		State(6, 2,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6, 7, 7, 9 ]),
		State(8, 4,		[3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6, 7, 7, 9, 2 ])

	];

	auto state = State(states[0]);

	foreach(ref const expectedState; states[1 .. $]) {
		state.step();
		assertEquals(state, expectedState);
	}
}

version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Missmatch!\nExpected: %s\nGot     : %s!"(expected, got));
}