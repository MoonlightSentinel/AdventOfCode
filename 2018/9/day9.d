import std.algorithm;

void main() {
	version(unittest){} else {
		import std.stdio;

		uint players, lastValue;

		File("day9.in").readf!"%d players; last marble is worth %d points"(players, lastValue);

		writefln("1: %s", part1(players, lastValue));

		import std.datetime.stopwatch : StopWatch;
		
		StopWatch timer;
		timer.start();
		const res2 =  part2(players, lastValue);
		timer.stop();

		const d = timer.peek().split!("hours", "minutes", "seconds");

		writefln!"2: %s (after %d:%d:%d)"(res2, d.hours, d.minutes, d.seconds);
	}
}

auto part1(const uint players, const uint lastMarble) {
	auto marbles = new uint[](lastMarble + 1);
	
	auto scores = new ulong[players];
	uint curPos = 1,
		curPlayer = 1,
		length = 2;

	marbles[0 .. 2] = [0,1];

	foreach(const curMarble; 2 .. lastMarble + 1) {
		if(curMarble % 23 != 0) {
			curPos += 2;
		
			if(curPos > length) curPos %= length;

			length++;

			foreach_reverse(const i; curPos .. length - 1) {
				marbles[i + 1] = marbles[i];
			}

			marbles[curPos] = curMarble;
		}
		else {
			const removeIdx = curPos < 7
				? curPos + length - 7
				: curPos - 7;

			scores[curPlayer] += curMarble + marbles[removeIdx];

			foreach(const i; removeIdx .. length - 1) {
				marbles[i] = marbles[i + 1];
			}

			length--;
			curPos = removeIdx;
		}

		curPlayer = (curPlayer + 1) % players;
	}

	return scores.maxElement;
}

auto part2(const uint players, const uint lastMarble) {
	return part1(players, lastMarble * 100);
}

unittest {
	assertEquals(part1(	9,	32),	32);
	assertEquals(part1(	10,	1618),	8317);
	assertEquals(part1(	13,	7999),	146373);
	assertEquals(part1(	17,	1104),	2764);
	assertEquals(part1(	21,	6111),	54718);
	assertEquals(part1(	30,	5807),	37305);
}

version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}