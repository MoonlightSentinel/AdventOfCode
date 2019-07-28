
module day06;

void main() {
	import std.stdio;

	const res = File("day06.in", "r").byLine.solve();
	writefln!"A: %s\nB: %s"(res.a, res.b);
}

auto solve(R)(scope R lines) {
	static if(is(R == T[], T)) {
		const len = lines[0].length;
	}
	else {
		const len = lines.front.length;
	}
	
	scope counters = new uint[char][len];

	foreach(const line; lines) {
		foreach(const idx, const ch; line) {
			counters[idx][ch]++;
		}
	}

	struct Result {
		char[] a, b;
	}
	
	Result result;
	result.a = new char[len];
	result.b = new char[len];
	
	foreach(const idx, const counter; counters) {
		char best, worst;
		uint max = uint.min, min = uint.max;

		foreach(const ch, const count; counter) {
			if(count > max) {
				max = count;
				best = ch;
			}
			if(count < min) {
				min = count;
				worst = ch;
			}
		}

		result.a[idx] = best;
		result.b[idx] = worst;
	}

	return result;
}

unittest {
	enum example = [
		"eedadn",
		"drvtee",
		"eandsr",
		"raavrd",
		"atevrs",
		"tsrnev",
		"sdttsa",
		"rasrtv",
		"nssdts",
		"ntnada",
		"svetve",
		"tesnvt",
		"vntsnd",
		"vrdear",
		"dvrsen",
		"enarar"
	];
	
	const res = solve(example);
	assert("easter" == res.a);
	assert("advent" == res.b);
}